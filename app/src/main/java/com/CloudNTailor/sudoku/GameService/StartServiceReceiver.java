package com.CloudNTailor.sudoku.GameService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationJobScheduler.scheduleJob(context);
    }
}
