package com.practice.springbootquartz.controller;

import com.practice.springbootquartz.job.MyJob;
import com.practice.springbootquartz.schedule.QuartzJobManager;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/task")
public class TaskController {

    @Autowired
    QuartzJobManager manager;
    @RequestMapping("/add")
    public void task() throws IllegalAccessException, SchedulerException, InstantiationException {
        String name = UUID.randomUUID().toString().replaceAll("-","");
        manager.addJob(MyJob.class, "myJob","groupName", "*/1 * * * * ?");
    }

    @RequestMapping("/list")
    @ResponseBody
    public List listTask() throws SchedulerException {
        return manager.getAllJob();
    }

    @RequestMapping("/pause")
    public void pauseJob() throws SchedulerException {
        manager.pauseJob("myJob","groupName");
    }

    @RequestMapping("/index")
    public String index(){
        return "index.html";
    }

    @RequestMapping("/hello")
    public String hello(HttpServletRequest request, @RequestParam(value = "name", defaultValue = "springboot-thymeleaf") String name){
        request.setAttribute("name",name);
        return "hello";
    }
}
