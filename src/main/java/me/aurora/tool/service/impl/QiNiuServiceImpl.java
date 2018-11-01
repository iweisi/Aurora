package me.aurora.tool.service.impl;

import cn.hutool.http.HttpStatus;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import me.aurora.common.config.exception.AuroraException;
import me.aurora.common.config.api.ResponseEntity;
import me.aurora.tool.domain.QiniuConfig;
import me.aurora.tool.domain.QiniuContent;
import me.aurora.tool.repository.QiNiuConfigRepo;
import me.aurora.tool.repository.QiniuContentRepo;
import me.aurora.tool.repository.spec.QiNiuContentSpec;
import me.aurora.tool.service.QiNiuService;
import me.aurora.common.utils.FileUtil;
import me.aurora.common.utils.PageUtil;
import me.aurora.tool.utils.QiNiuUtil;
import me.aurora.common.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;

/**
 * @author 郑杰
 * @date 2018/10/02
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class QiNiuServiceImpl implements QiNiuService {

    @Autowired
    private QiNiuConfigRepo qiNiuConfigRepo;

    @Autowired
    private QiniuContentRepo qiniuContentRepo;

    private final String TYPE = "公开";

    @Override
    public QiniuConfig findById(Long id) {
        if(id == null){
            throw new AuroraException(HttpStatus.HTTP_NOT_FOUND,"id not exist");
        }
        Optional<QiniuConfig> qiniuConfig = qiNiuConfigRepo.findById(id);
        if(qiniuConfig.isPresent()){
            return qiniuConfig.get();
        } else {
            return null;
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QiniuConfig updateConfig(QiniuConfig qiniuConfig) {
        if (!(qiniuConfig.getHost().toLowerCase().startsWith("http://")||qiniuConfig.getHost().toLowerCase().startsWith("https://"))) {
            throw new AuroraException(HttpStatus.HTTP_NOT_FOUND,"外链域名必须以http://或者https://开头");
        }
        qiNiuConfigRepo.saveAndFlush(qiniuConfig);
        return qiniuConfig;
    }

    @Override
    public Map getContentInfo(QiNiuContentSpec qiNiuContentSpec, Pageable pageable) {
        Page<QiniuContent> qiniuContents = qiniuContentRepo.findAll(qiNiuContentSpec,pageable);
        return PageUtil.buildPage(qiniuContents.getContent(),qiniuContents.getTotalElements());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upload(MultipartFile file,QiniuConfig qiniuConfig) {

        if(qiniuConfig == null){
            throw new AuroraException(HttpStatus.HTTP_NOT_FOUND,"请先添加相应配置，再操作");
        }
        /**
         * 构造一个带指定Zone对象的配置类
         */
        Configuration cfg = QiNiuUtil.getConfiguration(qiniuConfig.getZone());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        String upToken = auth.uploadToken(qiniuConfig.getBucket());
        try {
            Response response = uploadManager.put(file.getBytes(), QiNiuUtil.getKey(file.getOriginalFilename()), upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            //存入数据库
            QiniuContent qiniuContent = new QiniuContent();
            qiniuContent.setBucket(qiniuConfig.getBucket());
            qiniuContent.setType(qiniuConfig.getType());
            qiniuContent.setKey(putRet.key);
            qiniuContent.setUrl(qiniuConfig.getHost()+"/"+putRet.key);
            qiniuContent.setSize(FileUtil.getSize(Integer.parseInt(file.getSize()+"")));
            qiniuContentRepo.save(qiniuContent);
        } catch (Exception e) {
           throw new AuroraException(HttpStatus.HTTP_INTERNAL_ERROR,e.getMessage());
        }
    }

    @Override
    public QiniuContent findByContentId(Long id) {
        Optional<QiniuContent> qiniuContent = qiniuContentRepo.findById(id);
        ValidationUtil.isNull(qiniuContent,"文件不存在");
        return qiniuContent.get();
    }

    @Override
    public ResponseEntity download(QiniuContent content,QiniuConfig config) throws UnsupportedEncodingException {
        String finalUrl = null;
        if(TYPE.equals(content.getType())){
            finalUrl  = content.getUrl();
        } else {
            Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
            /**
             * 1小时，可以自定义链接过期时间
             */
            long expireInSeconds = 3600;
            finalUrl = auth.privateDownloadUrl(content.getUrl(), expireInSeconds);
        }
        return ResponseEntity.ok(finalUrl);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(QiniuContent content, QiniuConfig config) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = QiNiuUtil.getConfiguration(config.getZone());
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(content.getBucket(), content.getKey());
            qiniuContentRepo.delete(content);
        } catch (QiniuException ex) {
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void synchronize(QiniuConfig config) {
        if(config == null){
            throw new AuroraException(HttpStatus.HTTP_NOT_FOUND,"请先添加相应配置，再操作");
        }
        //构造一个带指定Zone对象的配置类
        Configuration cfg = QiNiuUtil.getConfiguration(config.getZone());
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        //文件名前缀
        String prefix = "";
        //每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(config.getBucket(), prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            QiniuContent qiniuContent = null;
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                if(qiniuContentRepo.findByKey(item.key) == null){
                    qiniuContent = new QiniuContent();
                    qiniuContent.setSize(FileUtil.getSize(Integer.parseInt(item.fsize+"")));
                    qiniuContent.setKey(item.key);
                    qiniuContent.setType(config.getType());
                    qiniuContent.setBucket(config.getBucket());
                    qiniuContent.setUrl(config.getHost()+"/"+item.key);
                    qiniuContentRepo.save(qiniuContent);
                }
            }
        }

    }
}
