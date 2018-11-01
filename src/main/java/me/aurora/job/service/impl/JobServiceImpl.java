package me.aurora.job.service.impl;

import cn.hutool.http.HttpStatus;
import me.aurora.common.config.exception.AuroraException;
import me.aurora.job.domain.Job;
import me.aurora.job.repository.JobRepo;
import me.aurora.job.repository.spec.JobSpce;
import me.aurora.job.service.JobService;
import me.aurora.common.utils.PageUtil;
import me.aurora.common.utils.ValidationUtil;
import me.aurora.job.utils.ScheduleUtils;
import org.quartz.CronExpression;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

/**
 * @author 郑杰
 * @date 2018/10/05 19:18:05
 */
@Service(value = "jobService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepo jobRepo;

    @Resource(name = "scheduler")
    private Scheduler scheduler;

    @Override
    public Map getJobsInfo(JobSpce jobSpce, Pageable pageable) {
        Page<Job> jobs = jobRepo.findAll(jobSpce,pageable);
        return PageUtil.buildPage(jobs.getContent(),jobs.getTotalElements());
    }

    @Override
    public void insert(Job job) {
        if (!CronExpression.isValidExpression(job.getCronExpression())){
            throw new AuroraException(HttpStatus.HTTP_BAD_REQUEST,"cron表达式格式错误");
        }
        jobRepo.save(job);
        ScheduleUtils.createScheduleJob(scheduler, job);
    }

    @Override
    public Job findById(Long id) {
        Optional<Job> job = jobRepo.findById(id);
        ValidationUtil.isNull(job,"id :" +id +" is not find");
        return job.get();
    }

    @Override
    public void updateStatus(Job job) {
        if (job.getStatus().equals(Job.ScheduleStatus.PAUSE.getValue())) {
            ScheduleUtils.resumeJob(scheduler, job.getId());
            job.setStatus("0");
        } else {
            ScheduleUtils.pauseJob(scheduler,job.getId());
            job.setStatus("1");
        }
        jobRepo.save(job);
    }

    @Override
    public void update(Job old, Job job) {
        if (!CronExpression.isValidExpression(job.getCronExpression())){
            throw new AuroraException(HttpStatus.HTTP_BAD_REQUEST,"cron表达式格式错误");
        }
        job.setStatus(old.getStatus());
        jobRepo.save(job);
        ScheduleUtils.updateScheduleJob(scheduler,job);
    }

    @Override
    public void delete(Job job) {
        ScheduleUtils.deleteScheduleJob(scheduler,job.getId());
        jobRepo.delete(job);
    }
}
