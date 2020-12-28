package com.CloudNTailor.sudoku.GameService;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class NotificationJobScheduler {

    private final static long fiveMins=300000;
    public  static int NotificationPeriodCounter=1;

    public static void scheduleJob(Context context)
    {
        long nextLatency=getNextScheduleTime();
        ComponentName serviceComponent = new ComponentName(context, NotificationJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(nextLatency); // wait at least
        builder.setOverrideDeadline(nextLatency+fiveMins); // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
        NotificationPeriodCounter++;
    }

    private static long getNextScheduleTime()
    {
        Date now = new Date();

        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DATE,NotificationPeriodCounter);
        c1.set(Calendar.HOUR,13);
        c1.set(Calendar.MINUTE,0);

        Date nextDay = c1.getTime();

        return nextDay.getTime()-now.getTime();

    }
}
