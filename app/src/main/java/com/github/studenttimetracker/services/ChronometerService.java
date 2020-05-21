package com.github.studenttimetracker.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.github.studenttimetracker.MainActivity;
import com.github.studenttimetracker.R;

import java.util.Timer;
import java.util.TimerTask;

import static com.github.studenttimetracker.notifications.App.CHANNEL_ID;

public class ChronometerService extends Service {
    public static final String ELAPSED_TIME = "elapsed_time";
    public static final String ACTION_CHRONOMETER_BROADCAST = ChronometerService.class.getName() + "TimeBroadcast";
    public static final String ACTIVITY_NAME = "activity_name";
    public static boolean active = false;
    Timer timer = new Timer();
    long startTime = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Time Measurement
        active = true;
        startTime = (startTime!=0)?startTime:System.currentTimeMillis();
        timer.scheduleAtFixedRate( new sendBroadcastMessageClass(), 0, 1000);

        // Notification
        String activityName = intent.getStringExtra(ACTIVITY_NAME);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,notificationIntent,0);

        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Student Time Tracker")
                .setContentText(activityName)
                .setSmallIcon(R.drawable.ic_timer)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);

        return START_NOT_STICKY;
    }

    private class sendBroadcastMessageClass extends TimerTask
    {
        public void run()
        {
            sendBroadcastMessage(System.currentTimeMillis() - startTime);
        }
    }

    private void sendBroadcastMessage(long elapsedTime) {
        // Sending Data back to Fragment
        Intent intent = new Intent(ACTION_CHRONOMETER_BROADCAST);
        intent.putExtra(ELAPSED_TIME, elapsedTime);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        active = false;
        startTime = 0;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
