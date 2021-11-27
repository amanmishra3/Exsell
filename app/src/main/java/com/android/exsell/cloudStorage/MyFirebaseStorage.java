package com.android.exsell.cloudStorage;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.android.exsell.db.ItemDb;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MyFirebaseStorage {
    private static MyFirebaseStorage myStorage;
    private StorageReference storageRef;
    private ItemDb itemDb;

    public static MyFirebaseStorage newInstance() {
        if(myStorage != null)
            myStorage = new MyFirebaseStorage();
        return myStorage;
    }

    public MyFirebaseStorage() {
        this.storageRef = FirebaseStorage.getInstance().getReference();
    }

    public void uploadImage(Uri filepath, String itemId, int type, downloadUrlCallback callback) {
        String path = new String();
        if(filepath == null || itemId == null || itemId.length() <= 0 || type > 2)
            callback.onCallback(null);
        if(type == 0) {
            path = "products/"+itemId;
        } else if(type == 1) {
            path = "users/"+itemId;
        }
        StorageReference upload = this.storageRef.child(path);
        upload.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.i("MFS", " download URL "+uri.toString());
                                callback.onCallback(uri.toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    }
                });
    }
    public interface downloadUrlCallback {
        void onCallback(String url);
    }
}
