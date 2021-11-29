package com.android.exsell.services;

import android.util.Log;

import com.android.exsell.db.UserDb;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class SendMessage {
    private static String serverKey = "AAAAUNRgr6g:APA91bFhKP-1SArpphNucmgVa1_UldAUWF7O9Xy16AoyC--19rSKSIF69eBOV9qmZoEnx4CiehvOJJntBskS36x-MYRJRyG30iD_Ut7Z1RwL25rHr11XAzDKwVpK-vlTRkKdeLsNyw69";
    private static String apiurl = "https://fcm.googleapis.com/fcm/send";

    public SendMessage () {};
    public static void sendMessage(String token, String title, String message, String type, Date time) {
        try{
            Log.i("SendMessage"," "+token);
            JSONObject msg = new JSONObject();
            JSONObject notification = new JSONObject();
            msg.put("to", token);
            notification.put("title", title);
            notification.put("body", message);
            msg.put("notification", notification);
            JSONObject payloadJson = new JSONObject();

            URL url = new URL(apiurl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "key=" + serverKey);
            connection.setRequestProperty("Content-Type","application/json");
            String payload = msg.toString();// This should be your json body i.e. {"Name" : "Mohsin"}
            byte[] out = payload.getBytes(StandardCharsets.UTF_8);
            OutputStream stream = connection.getOutputStream();
            stream.write(out);
            Log.i("SendMessage",connection.getResponseCode() + " " + connection.getResponseMessage()); // THis is optional
            connection.disconnect();
        }catch (Exception e){
            System.out.println(e);
            Log.i("SendMessage","Failed successfully "+ Log.getStackTraceString(e));
        }
    }
}
//curl -X POST -H "Authorization: key=AAAAUNRgr6g:APA91bFhKP-1SArpphNucmgVa1_UldAUWF7O9Xy16AoyC--19rSKSIF69eBOV9qmZoEnx4CiehvOJJntBskS36x-MYRJRyG30iD_Ut7Z1RwL25rHr11XAzDKwVpK-vlTRkKdeLsNyw69" -H "Content-Type: application/json" -d '{
//        "to":"fmivfZG7QAm-Ib3buKFVsO:APA91bF39No-wNynSGhGcK39anyRkFTTP5mR9epl5prPhn0Pjd6MBy3See4Kx69HPv45PhFpBumc2Qp8iJ6woNZ9VHj_3rsEoSwluNcbjBHY6toejlLDlk06-Od4cLTWvaIr7Cv5tAUU"
//        "notification":{
//        "title":"FCM Message",
//        "body":"This is an FCM Message"
//        },
//        }' https://fcm.googleapis.com/fcm/send