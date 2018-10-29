package me.aurora.service;

import com.aliyuncs.exceptions.ClientException;
import me.aurora.domain.ResponseEntity;
import me.aurora.domain.utils.AliDayuConfig;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author 郑杰
 * @date 2018/10/29 16:59:00
 */
@CacheConfig(cacheNames = "aLiDaYu")
public interface AliDayuService {

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    AliDayuConfig findById(long id);

    @CachePut(key = "#p0.getId()")
    AliDayuConfig updateConfig(AliDayuConfig aliDayuConfig, AliDayuConfig old);

    ResponseEntity send(AliDayuConfig byId, String phone, String code) throws Exception;
}
