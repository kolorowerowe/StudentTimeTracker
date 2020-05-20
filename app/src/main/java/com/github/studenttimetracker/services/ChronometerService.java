package com.github.studenttimetracker.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Timer;
import java.util.TimerTask;

public class ChronometerService extends Service {
    public static final String ELAPSED_TIME = "elapsed_time";
    public static final String ACTION_CHRONOMETER_BROADCAST = ChronometerService.class.getName() + "TimeBroadcast";
    Timer timer = new Timer();
    long startTime;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTime = System.currentTimeMillis();
        timer.scheduleAtFixedRate( new sendBroadcastMessageClass(), 0, 1000);
        return START_STICKY;
    }

    private class sendBroadcastMessageClass extends TimerTask
    {
        public void run()
        {
            sendBroadcastMessage(System.currentTimeMillis() - startTime);
        }
    }

    private void sendBroadcastMessage(long elapsedTime) {
        Intent intent = new Intent(ACTION_CHRONOMETER_BROADCAST);
        intent.putExtra(ELAPSED_TIME, elapsedTime);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        startTime = 0;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }
}
