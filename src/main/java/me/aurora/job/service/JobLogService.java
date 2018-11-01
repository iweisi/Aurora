package me.aurora.job.service;

import me.aurora.job.domain.JobLog;
import me.aurora.job.repository.spec.JobLogSpce;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * @author 郑杰
 * @date 2018/10/05 19:50:21
 */
public interface JobLogService {

    /**
     * 保存日志
     * @param log
     */
    void insert(JobLog log);

    /**
     * 查询全部日志
     * @param jobLogSpce
     * @param pageable
     * @return
     */
    Map getJobLogsInfo(JobLogSpce jobLogSpce, Pageable pageable);
}
