package com.example.jec;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isConnected(context)) {
            // Internet is connected, schedule your background task with WorkManager
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class).build();
            WorkManager.getInstance(context).enqueue(workRequest);
        } else {
            // Internet is not connected
        }
    }

    private boolean isConnected(Context context) {
        return NetworkUtils.isInternetAvailable(context.getApplicationContext());
    }
}
