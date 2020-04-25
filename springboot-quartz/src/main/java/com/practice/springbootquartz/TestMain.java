package com.practice.springbootquartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMain {
    private static final Logger logger = LoggerFactory.getLogger(TestMain.class);

    public static void main(String[] args) {
        String str = "123";
        logger.info("输出值:{},这是一个测试{}...{}" , args, str,str);
    }

    public static void startSchedule() {
        try {
//            JobDetail jobDetail = JobBuilder.newJob();
        } catch (Exception e) {

        }
    }
}
