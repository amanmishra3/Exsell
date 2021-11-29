package com.android.exsell.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.exsell.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppSettingsDb {
    private static AppSettingsDb appSettingsDb;
    private CollectionReference appSettingsReference;
    private FirebaseFirestore db;
    private String TAG = "App Settings Db";

    public static AppSettingsDb newInstance() {
        if(appSettingsDb == null)
            appSettingsDb = new AppSettingsDb();
        return appSettingsDb;
    }

    public AppSettingsDb() {
        db = FirebaseFirestore.getInstance();
        appSettingsReference = db.collection("appSettings");
    }
    public void getCategories(getCategoryCallback callback) {
        appSettingsReference.document("categories")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    Log.i(TAG , "wefwef "+task.getResult().getData().get("categoryList"));
                    callback.onCallback((ArrayList<String>)(task.getResult().getData().get("categoryList")));
                } else {
                    Log.i(TAG, "no documents ", task.getException());
                    callback.onCallback(null);
                }
            }
        });
    }

    public interface getCategoryCallback {
        public void onCallback(List<String> categories);
    }
}
