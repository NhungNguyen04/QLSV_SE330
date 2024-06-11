package com.example.qlsv;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

class MyFirebaseMessagingService extends FirebaseMessagingService {
    String channelId = "notification_channel";
    String channelName = "com.example.qlsv";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (message.getNotification() != null) {
            generateNotification(message.getNotification().getTitle(), message.getNotification().getBody());
        }
    }

    public void generateNotification(String title, String message){
        Intent intent = new Intent(this, LoginActivity.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        long[] list = new long[]{1000,1000,1000,1000};
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.bi__briefcase_fill)
                .setAutoCancel(true)
                .setVibrate(list)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);
        builder = builder.setContent(getRemoteView(title, message));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(0, builder.build());
    }

    public RemoteViews getRemoteView(String title, String message) {
        RemoteViews remoteView = new RemoteViews("com.example.qlsv", R.layout.notification);
        remoteView.setTextViewText(R.id.notification_title, title);
        remoteView.setTextViewText(R.id.notification_description, message);
        remoteView.setImageViewResource(R.id.logo, R.drawable.bi__briefcase_fill);
        return remoteView;
    }
}
