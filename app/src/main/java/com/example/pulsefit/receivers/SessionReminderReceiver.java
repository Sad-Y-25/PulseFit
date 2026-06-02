package com.example.pulsefit.receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.pulsefit.R;

public class SessionReminderReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "pulsefit_reminder_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String sessionName = intent.getStringExtra("SESSION_NAME");
        if (sessionName == null) {
            sessionName = "Séance";
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Rappels de séance";
            String description = "Canal pour les rappels de séances PulseFit";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Rappel PulseFit")
                .setContentText("PulseFit Reminder: Your " + sessionName + " starts soon!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
