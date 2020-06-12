package com.github.studenttimetracker.notifications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationChannels extends Application {
    public static final String SERVICE_CHANNEL_ID = "timeTrackingChannel";
    public static final String POMODORO_CHANNEL_ID = "pomodoroTrackingChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    SERVICE_CHANNEL_ID,
                    "Time Tracking Channel",
                    NotificationManager.IMPORTANCE_MIN
            );
            serviceChannel.setDescription("Service Channel");

            NotificationChannel pomodoroChannel = new NotificationChannel(
                    POMODORO_CHANNEL_ID,
                    "Time Tracking Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            pomodoroChannel.setDescription("Pomodoro Channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(serviceChannel);
            manager.createNotificationChannel(pomodoroChannel);
        }
    }
}
