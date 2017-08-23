package xyz.dongaboomin.greet.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class GreetJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SetSchedule setSchedule = new SetSchedule();
        setSchedule.setJedisHomeText();
    }
}