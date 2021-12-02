package com.android.exsell.models;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.exsell.db.UserDb;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Notifications {
    private static List<JSONObject> myNotifications;
    public static Notifications myNotificationObj;
    String title;
    String message;
    String intent;
    Date time;
    public static void setMyNotifications(List<JSONObject> myNotifications) {
        Notifications.myNotifications = myNotifications;
    }

    public static Notifications newInstance() {
        if(myNotificationObj == null) {
            myNotifications = new ArrayList<>();
            myNotificationObj = new Notifications();
        }
        return myNotificationObj;
    }
    public static List<JSONObject> getMyNotifications() {
        return myNotifications;
    }

    public Notifications () {}
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
        myNotifications.add(notify);
        for(JSONObject x: myNotifications) {
            try {
                Log.i("Notifications ", x.get("message").toString() + x.get("title").toString());
            } catch(Exception e) {}
        }
    }

    public static void addSingleNotification(JSONObject notify) {
        myNotifications.add(0,notify);
    }
    public static void removeSingleNotification(JSONObject notify) {
        myNotifications.remove(notify);
    }
    public static void updateNotifications(notificationUpdateCallback callback) {
//        myNotifications = new ArrayList<>();
        UserDb userDb = UserDb.newInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null)
            return;
        userDb.getNotifications(mAuth.getCurrentUser().getUid(), new UserDb.getNotificationsCallback() {
            @Override
            public void onCallback(List<String> notifications) {
                if(notifications != null && notifications.size() > 0 ) {
                    Log.i("Notifications ", " Recieved: "+notifications.size());
                    List<JSONObject> notificationObj = new ArrayList<>();
                    boolean newNotification = false;
                    for(String obj: notifications) {
                        JSONObject tmp;
                        try {
                            tmp = new JSONObject(obj);
                            if(tmp.has("new")) {
                                myNotifications.add(tmp);
                                notificationObj.add(0, tmp);
                                newNotification = true;
                            }else {
                                if(!myNotifications.contains(tmp))
                                    myNotifications.add(tmp);
                                notificationObj.add(tmp);
                            }
                        } catch (Exception e) {

                        }
                    }
                    if(newNotification) {
//                        Toast.makeText(context, "New Notification received",Toast.LENGTH_SHORT).show();
                    }
                    Collections.sort(myNotifications, new Notifications().new MyJSONComparator());
                    callback.onCallback(notificationObj, newNotification);
                }
            }
        });
    }

    public interface notificationUpdateCallback {
        void onCallback(List<JSONObject> notifications, boolean newNotification);
    }
    class MyJSONComparator implements Comparator<JSONObject> {

        @Override
        public int compare(JSONObject n1, JSONObject n2) {
            try {
                Date v1 = (Date) (n1.get("time"));
                Date v3 = (Date) (n2.get("time"));
                return v1.compareTo(v3);
            } catch (Exception e) {
                Log.e("Error comaprator ", Log.getStackTraceString(e));
            }
            return 0;
        }
    }
}
