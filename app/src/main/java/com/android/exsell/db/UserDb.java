package com.android.exsell.db;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.exsell.cloudStorage.MyFirebaseStorage;
import com.android.exsell.models.Notifications;
import com.android.exsell.models.Product;
import com.android.exsell.models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.json.JSONObject;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDb {
    private String TAG = "UserDb";
    private static UserDb userDb;
    private static String myRegisterationToken;
    private MyFirebaseStorage myStorage;
    private FirebaseFirestore db;
    private CollectionReference userCollectionReference;
    private FirebaseAuth mAuth;
    public static Map<String, Object> myUser;
    public static UserDb newInstance() {
        if(userDb == null) {
            userDb = new UserDb();
            myUser = new HashMap<>();
            setMyUser();
        }
        return userDb;
    }

    public static void setMyRegisterationToken(String token) {
        myRegisterationToken = token;
        if(FirebaseAuth.getInstance().getCurrentUser() != null && token != null && token.length() > 0) {
            userDb.updateRegisterationToken(FirebaseAuth.getInstance().getCurrentUser().getUid(), token);
        }
    }

    public static String getMyRegisterationToken() {
        return myRegisterationToken;
    }

    public static void clearData() {
        myUser = null;
        userDb = null;
    }

    public static void setMyUser() {
        if(userDb == null || FirebaseAuth.getInstance().getCurrentUser() == null)
            return;
        myUser = new HashMap<>();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(myRegisterationToken != null && myRegisterationToken.length() > 0)
            userDb.updateRegisterationToken(userId, myRegisterationToken);
        userDb.getUser(userId, new getUserCallback() {
            @Override
            public void onCallback(Users user) {
                Log.i("setMyUser"," Name: "+user.getFname());
                if(user == null) {
                    Log.i("UserDb",userId);
                    return;
                }
                myUser.put("name", user.getFname());
                myUser.put("fname", user.getFname());
                myUser.put("userId", user.getUserId());
                myUser.put("wishlist",user.getWishlist());
                myUser.put("email", user.getEmail());
                myRegisterationToken = user.getRegisterationToken();
                if(user.getDob() != null)
                    myUser.put("dob", user.getDob());
                if(user.getContact() != null)
                    myUser.put("contact", user.getContact());
                if(user.getImageUri() != null)
                    myUser.put("imageUri", user.getImageUri());
                if(user.getChatIds() != null)
                    myUser.put("chatIds", user.getChatIds());
            }
        });
    }

    public static void imageUriUpdate(String uri) {
        myUser.put("imageUri", uri);
    }
    private UserDb() {
        db = FirebaseFirestore.getInstance();
        userCollectionReference = db.collection("Users");
        mAuth = FirebaseAuth.getInstance();
        myStorage = new MyFirebaseStorage();
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
        user.setCreatedOn(new Date());
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
    public void updateUserDetails(Map<String,Object> user, Uri uri, updateUserCallback callback) {
        if(user.get("userId") == null) {
            callback.onCallback(false);
            return;
        }
        String id = (String) user.get("userId");
        Log.i(TAG, "Item Id "+user.get("userId"));
        DocumentReference documentReference = userCollectionReference.document(id);
        documentReference.update(user);

        if(uri != null) {
            myStorage.uploadImage(uri, id, 1, new MyFirebaseStorage.downloadUrlCallback() {
                @Override
                public void onCallback(String url) {
                    Log.i(TAG," My URI "+url);
                    userDb.userCollectionReference.document(id).update("imageUri", url);
                    userDb.myUser.put("imageUri", url);
//                    setDialog(false);
                    callback.onCallback(true);
                }
            });
        } else {
            callback.onCallback(true);
        }
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
    public void updateRegisterationToken(String userId, String token) {
        Log.i(TAG, "Item Id "+userId);
        DocumentReference documentReference = userCollectionReference.document(userId);
        documentReference.update("registerationToken",token);
    }
    public void addToNotifications(String userId, String notification) {
        Log.i(TAG, "Item Id "+userId);
        DocumentReference documentReference = userCollectionReference.document(userId);
        documentReference.update("notification", FieldValue.arrayUnion(notification));
//        Notifications.updateNotifications();
    }
    public void updateNotifications(String userId, JSONObject notification) {
        Log.i(TAG, "Item Id update  "+ notification);
        DocumentReference documentReference = userCollectionReference.document(userId);
        documentReference.update("notification", FieldValue.arrayRemove(notification.toString()));
        notification.remove("new");
        Log.i(TAG, "Item Id again  "+ notification);
        documentReference = userCollectionReference.document(userId);
        documentReference.update("notification", FieldValue.arrayUnion(notification.toString()));
//        Notifications.updateNotifications();
    }

    public void setupChatId(String userId, String sellerId, String buyer, String seller, String imageSeller, String imageBuyer, getChatCallback callback) {
        String chatId = userId + sellerId;
        Map<String, Object> mp = new HashMap<>();
        mp.put("messageId", chatId);
        mp.put("otherNames", seller);
        mp.put("otherPic", imageSeller);
        DocumentReference documentReference = userCollectionReference.document(userId).collection("messages").document(chatId);
        documentReference.set(mp)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference documentReference2 = userCollectionReference.document(sellerId).collection("messages").document(chatId);
                        mp.put("otherNames", buyer);
                        mp.put("otherPic", imageBuyer);
                        documentReference2.set(mp)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        callback.onCallback(true);
                                    }
                                });
                    }
                });

    }

    public void getNotifications(String userId, getNotificationsCallback callback) {
        userCollectionReference.document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot.exists() && documentSnapshot.get("notification") != null) {
                    Log.i("Notifications ", documentSnapshot.get("notification").toString());
                    List<String> notifications = (List<String>)documentSnapshot.get("notification");
//                    Collections.sort(notifications);
                    notifications = notifications.subList(0, notifications.size() > 15 ? 15 : notifications.size());
                    callback.onCallback(notifications);
                }else {
                    callback.onCallback(null);
                }
            }
        });
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if(documentSnapshot.exists() && documentSnapshot.get("notification") != null) {
//                            Log.i("Notifications ", documentSnapshot.get("notification").toString());
//                            List<String> notifications = (List<String>)documentSnapshot.get("notification");
//                            Collections.sort(notifications);
//                            notifications = notifications.subList(0, 15);
//                            callback.onCallback(notifications);
//                        }else {
//                            callback.onCallback(null);
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
    }

    public interface createUserCallback {
        void onCallback(boolean ok, String id);
    }
    public interface getUserCallback {
        void onCallback(Users user);
    }
    public interface updateUserCallback {
        void onCallback(boolean updated);
    }
    public interface getNotificationsCallback {
        void onCallback(List<String> notifications);
    }
    public interface getChatCallback {
        void onCallback(boolean done);
    }
}
