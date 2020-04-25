package com.practice.springbootquartz.schedule;

import com.practice.springbootquartz.quartz.BaseTaskJob;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * task任务创建工具类
 */
@Component
public class QuartzJobManager {
    private static final Logger logger = LoggerFactory.getLogger(QuartzJobManager.class);

    private static QuartzJobManager jobUtil;

    @Autowired
    private Scheduler scheduler;

    public QuartzJobManager() {
        logger.info("init jobUtil");
        jobUtil = this;
    }

    public static QuartzJobManager getInstance() {
        logger.info("return JobCreateUtil");
        return QuartzJobManager.jobUtil;
    }

    /**
     * 创建job
     *
     * @param clazz          任务类
     * @param jobName        任务名称
     * @param jobGroupName   任务所在组名称
     * @param cronExpression cron 表达式
     * @throws SchedulerException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void addJob(Class clazz, String jobName, String jobGroupName, String cronExpression) throws SchedulerException, IllegalAccessException, InstantiationException {
        //启动调度器
        scheduler.start();

        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(((BaseTaskJob) clazz.newInstance()).getClass()).withIdentity(jobName, jobGroupName).build();

        //表达式调度构建器(任务执行时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName).withSchedule(scheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 新增job 可传参
     *
     * @param clazz          类名
     * @param jobName        定时任务名称
     * @param jobGroupName   定时任务所在组名称
     * @param cronExpression cron表达式
     * @param argMap         参数
     * @throws SchedulerException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void addJob(Class clazz, String jobName, String jobGroupName, String cronExpression, Map<String, Object> argMap) throws SchedulerException, IllegalAccessException, InstantiationException {
        //启动调度器
        scheduler.start();

        JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) clazz.newInstance().getClass()).withIdentity(jobName, jobGroupName).build();

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName).withSchedule(scheduleBuilder).build();
        //获得datamap参数写入数据
        trigger.getJobDataMap().putAll(argMap);
        scheduler.scheduleJob(jobDetail, trigger);
    }

    //暂停定时任务
    public void pauseJob(String jobName, String jobGroupName) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(jobName, jobGroupName));
    }

    public void resumeJob(String jobName, String jobGroupName) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(jobName, jobGroupName));
    }

    public void updateJob(String jobName, String jobGroupName, String cronExpression) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        //表达调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        //按新的cronExpression表达式重新构建trigger
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

        //按新的trigger重新设置job执行
        Date date = scheduler.rescheduleJob(triggerKey, trigger);
        logger.info("重设{}定时任务时间:{}", jobName, date.toString());
    }

    public void updateJob(String jobName, String jobGroupName, String cronExpression, Map<String, Object> argMap) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        cronTrigger.getJobDataMap().putAll(argMap);
        scheduler.rescheduleJob(triggerKey, cronTrigger);
    }

    public void deleteJob(String jobName, String jobGroupName) throws SchedulerException {
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroupName));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroupName));
        scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
    }

    /**
     * 开启所有定时任务
     */
    public void startAllJobs() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭所有定时任务
     */
    public void shutdownAllJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Map<String, Object>> getAllJob() throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<Map<String, Object>> jobList = new ArrayList<>();
        for (JobKey jobKey :
                jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger :
                    triggers) {
                Map<String, Object> job = new HashMap<>();
                job.put("jobName", jobKey.getName());
                job.put("jobGroupName", jobKey.getGroup());
                job.put("trigger", trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.put("jobStatus", triggerState.name());
                if(trigger instanceof CronTrigger){
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.put("cronException", cronExpression);
                }
                jobList.add(job);
            }
        }
        return jobList;
    }


}
