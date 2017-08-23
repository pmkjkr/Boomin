package xyz.dongaboomin.greet.controller;
//
//import org.quartz.JobDetail;
//import org.quartz.Scheduler;
//import org.quartz.SchedulerException;
//import org.quartz.Trigger;
//import org.quartz.impl.StdSchedulerFactory;
//import xyz.dongaboomin.etc.db.JedisUse;
//import xyz.dongaboomin.greet.dto.GreetDTO;
//import xyz.dongaboomin.greet.dto.GreetResultDTO;
//import xyz.dongaboomin.greet.job.GreetJob;
//
//import static org.quartz.CronScheduleBuilder.cronSchedule;
//import static org.quartz.JobBuilder.newJob;
//import static org.quartz.TriggerBuilder.newTrigger;
//
//public class GreetController {
//
//    private JedisUse jedisUse;
//
//    public GreetController(JedisUse jedisUse){
//        this.jedisUse = jedisUse;
//    }
//
//    public boolean setText(String text){
//        return jedisUse.setContent("homeText",text);
//    }
//
//
//    public GreetResultDTO getGreetMessage(){
//        String homeText = jedisUse.getContent("homeText");
//        GreetDTO greetDTO = new GreetDTO(homeText);
//        return new GreetResultDTO(200, greetDTO);
//    }
//
//
//    public void setScheduler(){
//        try {
//            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//            scheduler.start();
//            JobDetail job = newJob(GreetJob.class)
//                    .withIdentity("job1", "group1")
//                    .build();
//            Trigger trigger = newTrigger()
//                    .withIdentity("trigger1", "group1")
////                    .withSchedule(cronSchedule("0 0 0 * * ?"))
//                    .withSchedule(cronSchedule("0/5 * * * * ?"))
//                    .build();
//            scheduler.scheduleJob(job, trigger);
////            scheduler.shutdown();
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }
//    }
//}

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import xyz.dongaboomin.etc.db.JedisUse;
import xyz.dongaboomin.greet.dto.GreetDTO;
import xyz.dongaboomin.greet.job.GreetJob;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class GreetController {
    private JedisUse jedisUse;

    public GreetController(JedisUse jedisUse){
        this.jedisUse = jedisUse;
    }

    public boolean setText(String text){
        return jedisUse.setContent("homeText",text);
    }


    public GreetDTO getGreetMessage(){
        return new GreetDTO(jedisUse.getContent("homeText"));
    }


    public void setScheduler(){
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(GreetJob.class)
                    .withIdentity("job1", "group1")
                    .build();
            Trigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
//                    .withSchedule(cronSchedule("0/5 * * * * ?"))
                    .withSchedule(cronSchedule("0 0 0 * * ?"))
                    .build();
            scheduler.scheduleJob(job, trigger);
//            Thread.sleep(60000);
//            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}