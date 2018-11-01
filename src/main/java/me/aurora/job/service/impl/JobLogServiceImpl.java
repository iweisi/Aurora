package me.aurora.job.service.impl;

import me.aurora.job.domain.JobLog;
import me.aurora.job.repository.JobLogRepo;
import me.aurora.job.repository.spec.JobLogSpce;
import me.aurora.job.service.JobLogService;
import me.aurora.common.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author 郑杰
 * @date 2018/10/05 19:51:22
 */
@Service(value = "jobLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JobLogServiceImpl implements JobLogService {

    @Autowired
    private JobLogRepo jobLogRepo;

    @Override
    public void insert(JobLog log) {
        jobLogRepo.save(log);
    }

    @Override
    public Map getJobLogsInfo(JobLogSpce jobLogSpce, Pageable pageable) {
        Page<JobLog> jobLogs = jobLogRepo.findAll(jobLogSpce,pageable);
        return PageUtil.buildPage(jobLogs.getContent(),jobLogs.getTotalElements());
    }
}
