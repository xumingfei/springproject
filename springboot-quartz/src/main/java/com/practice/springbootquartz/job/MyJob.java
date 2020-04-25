package com.practice.springbootquartz.job;

import com.practice.springbootquartz.quartz.BaseTaskJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MyJob implements BaseTaskJob {
    private static final Logger log = LoggerFactory.getLogger(MyJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("MyJob  is start ..................");

        log.info("Hello quzrtz  {}",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));

        log.info("MyJob  is end .....................");

    }
}
