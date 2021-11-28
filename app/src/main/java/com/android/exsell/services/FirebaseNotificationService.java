package com.android.exsell.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.exsell.R;
import com.android.exsell.UI.Home;
import com.android.exsell.UI.MainActivity;
import com.android.exsell.db.UserDb;
import com.android.exsell.models.Notifications;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Date;

public class FirebaseNotificationService extends FirebaseMessagingService {
    private UserDb userDb;
    private FirebaseAuth mAuth;
    @Override
    public void onCreate() {
        super.onCreate();
        userDb = UserDb.newInstance();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i("MessageRecieved", "message" + remoteMessage.getNotification());
        if (remoteMessage.getNotification() != null) {
            Log.i("MessageRecieved", "title: "+remoteMessage.getNotification().getTitle()+" "+"message" + remoteMessage.getNotification().getBody());
            // Since the notification is received directly from
            // FCM, the title and the body can be fetched
            // directly as below.
            JSONObject notification = new JSONObject();
            try {
                notification.put("title", (String) remoteMessage.getNotification().getTitle());
                notification.put("message", remoteMessage.getNotification().getBody());
                notification.put("time", new Date());
            } catch(Exception e) {

            }
            mAuth = FirebaseAuth.getInstance();
            if(notification != null && mAuth.getCurrentUser() != null) {
                userDb.addToNotifications(mAuth.getCurrentUser().getUid(),notification.toString());
            }
            Notifications.addNotification(notification);
            showNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.i("RegisterationToken", "token: "+s);
        UserDb.setMyRegisterationToken(s);
    }

    //Set View for Notification
    private RemoteViews getCustomDesign(String title,
                                        String message) {
        RemoteViews remoteViews = new RemoteViews(
                getApplicationContext().getPackageName(),
                R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon,
                R.drawable.ic_notifications);
        return remoteViews;
    }
    public void showNotification(String title,
                                 String message) {
        // Pass the intent to switch to the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message).setAutoCancel(true).setContentIntent(pendingIntent);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        manager.notify(NotificationCompat.PRIORITY_HIGH, builder.build());
    }
}
