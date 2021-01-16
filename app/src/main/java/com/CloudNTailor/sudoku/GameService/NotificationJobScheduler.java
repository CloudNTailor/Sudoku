package com.CloudNTailor.sudoku.GameService;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class NotificationJobScheduler {

    private final static long fiveMins=300000;
    public  static int NotificationPeriodCounter=1;

    public  static final int JOB_ID  = 1026;

    public static void scheduleJob(Context context)
    {
        long nextLatency=getNextScheduleTime();
        ComponentName serviceComponent = new ComponentName(context, NotificationJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceComponent);
        builder.setMinimumLatency(nextLatency); // wait at least
        builder.setOverrideDeadline(nextLatency+fiveMins); // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
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
        c1.add(Calendar.DAY_OF_MONTH,NotificationPeriodCounter);
        c1.set(Calendar.HOUR_OF_DAY,19);
        c1.set(Calendar.MINUTE,30);

        Date nextDay = c1.getTime();
        Log.e("NOTE",nextDay.toString());

       return nextDay.getTime()-now.getTime();
        // return 600000;
    }

    public static boolean isJobServiceOn( Context context ) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;

        boolean hasBeenScheduled = false ;

        for ( JobInfo jobInfo : scheduler.getAllPendingJobs() ) {
            if ( jobInfo.getId() == JOB_ID ) {
                hasBeenScheduled = true ;
                Log.e("NOTEJ",Long.toString(jobInfo.getMaxExecutionDelayMillis()));
                break ;
            }
        }

        return hasBeenScheduled ;
    }
}
