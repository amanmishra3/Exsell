package com.android.exsell.models;
import android.util.Log;

import com.android.exsell.db.UserDb;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Notifications {
    private static List<JSONObject> myNotifications;
    String title;
    String message;
    String intent;
    Date time;
    public static void setMyNotifications(List<JSONObject> myNotifications) {
        Notifications.myNotifications = myNotifications;
    }

    public static List<JSONObject> getMyNotifications() {
        return myNotifications;
    }

    public Notifications(String title, String message, String intent, Date date) {
        this.time = date;
        this.title = title;
        this.message = message;
        this.intent = intent;
    }

    public static void clearData() {
        myNotifications = null;
    }

    public static void addNotification(JSONObject notify) {
        if(myNotifications == null)
            myNotifications = new ArrayList<>();
        myNotifications.add(0,notify);
        for(JSONObject x: myNotifications) {
            try {
                Log.i("Notifications ", x.get("message").toString() + x.get("title").toString());
            } catch(Exception e) {}
        }
    }
    public static void updateNotifications() {
        myNotifications = new ArrayList<>();
        UserDb userDb = UserDb.newInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null)
            return;
        userDb.getNotifications(mAuth.getCurrentUser().getUid(), new UserDb.getNotificationsCallback() {
            @Override
            public void onCallback(List<String> notifications) {
                if(notifications != null && notifications.size() > 0 ) {
                    Log.i("Notifications ", " Recieved: "+notifications.size());
                    for(String obj: notifications) {
                        JSONObject tmp;
                        try {
                            tmp = new JSONObject(obj);
                            myNotifications.add(tmp);
                        } catch (Exception e) {

                        }
                    }
                }
            }
        });
    }
}
