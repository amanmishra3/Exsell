package com.android.exsell.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.exsell.models.Product;
import com.android.exsell.models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserDb {
    private String TAG = "UserDb";
    private static UserDb userDb;
    private FirebaseFirestore db;
    private CollectionReference userCollectionReference;
    private FirebaseAuth mAuth;
    public static UserDb newInstance() {
        if(userDb == null)
            userDb = new UserDb();
        return userDb;
    }
    private UserDb() {
        db = FirebaseFirestore.getInstance();
        userCollectionReference = db.collection("Users");
    }
    public void createDocument(Users user) {
        Log.i(TAG, "createDocument");
        userCollectionReference.document(user.getUserId())
                .set(user.userAttributes()).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void a) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + user.getUserId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding document", e);
                    }
                });
    }

    public void createUser(Users user, createUserCallback callback) {
        //fetch mAuth userId and add to user then create the user
        userCollectionReference.add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i(TAG, "Succefully inserted");
                        callback.onCallback(true, documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onCallback(false, null);
                    }
                });
    }

    public void getUser(String userId, getUserCallback callback) {
        userCollectionReference.document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            callback.onCallback(documentSnapshot.toObject(Users.class));
                        }else {
                            callback.onCallback(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    public void updateUserDetails(Users user, String productId) {
        Log.i(TAG, "Item Id "+user.getUserId());
        DocumentReference documentReference = userCollectionReference.document(user.getUserId());
//        documentReference.update("wishlist", FieldValue.arrayUnion(productId));
        //TO DO: add update fields
    }
    public void addToSellList(String userId, String itemId) {
        Log.i(TAG, "Item Id "+userId);
        DocumentReference documentReference = userCollectionReference.document(userId);
        documentReference.update("selllist", FieldValue.arrayUnion(itemId));
    }
    public void addToOrders(String userId, String orderId) {
        Log.i(TAG, "Item Id "+userId);
        DocumentReference documentReference = userCollectionReference.document(userId);
        documentReference.update("orders", FieldValue.arrayUnion(orderId));
    }

    public void addToWishList(String userId, String productId) {
        Log.i(TAG, "Item Id "+userId);
        DocumentReference documentReference = userCollectionReference.document(userId);
        documentReference.update("wishlist", FieldValue.arrayUnion(productId));
    }
    public void removeFromWishList(String userId, String productId) {
        Log.i(TAG, "Item Id "+userId);
        DocumentReference documentReference = userCollectionReference.document(userId);
        documentReference.update("wishlist",FieldValue.arrayRemove(productId));
    }

    public interface createUserCallback {
        void onCallback(boolean ok, String id);
    }
    public interface getUserCallback {
        void onCallback(Users user);
    }
}
