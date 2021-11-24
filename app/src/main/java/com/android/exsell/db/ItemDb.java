package com.android.exsell.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.exsell.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemDb {
    private String TAG = "ItemDB";
    private static ItemDb itemDb;
    private FirebaseFirestore db;
    private CollectionReference itemCollectionReference;

    public static ItemDb newInstance() {
        if(itemDb == null)
            itemDb = new ItemDb();
        return itemDb;
    }
    private ItemDb() {
        db = FirebaseFirestore.getInstance();
        itemCollectionReference = db.collection("Items");
    }
    public void createItem(Product item, createItemsCallback callback) {
        List<String> searchKeywords = searchKeywords(item);
        item.setSearch(searchKeywords);
        itemCollectionReference.add(item)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i(TAG, "Succefully inserted");
                        item.setProductId(documentReference.getId());
                        // add the itemId to ItemId body
                        updateItem(item);
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
    public void getAllItems(getItemsCallback callback) {
        itemCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    List<Product> itemsList = querySnapshot.toObjects(Product.class);
                    for(Product it: itemsList) {
                        Log.i(TAG," data recieved "+ it.getBuyer());
                    }
                    callback.onCallback(itemsList);
                } else {
                    Log.i(TAG, "no documents ", task.getException());
//                    callback.onCallback(null);
                }
            }
        });
    }

    // this fucktion to be used by search nav bar
    public void searchProducts(String searchString, getItemsCallback callback) {
        if(searchString == null || searchString.length() <= 0) {
            callback.onCallback(null);
            return;
        }
        List<String> searchKeys = new ArrayList<>();
        for ( String x: searchString.split(" "))
            searchKeys.add(x.toLowerCase());
        Query query = itemCollectionReference.whereArrayContainsAny("search", searchKeys);
        Log.i(TAG," search keywords "+ searchKeys);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    List<Product> itemsList = querySnapshot.toObjects(Product.class);
                    int i = 0;
                    callback.onCallback(itemsList);
                } else {
                    Log.i(TAG, "no documents ", task.getException());
                    callback.onCallback(null);
                }
            }
        });
    }
    public void searchItems(Product params, getItemsCallback callback) {
        Query query = itemCollectionReference;
        if(params.getBuyer() != null)
            query = itemCollectionReference.whereEqualTo("buyer", params.getBuyer());
        if(params.getStatus() != null)
            query = itemCollectionReference.whereEqualTo("status", params.getStatus());
        if(params.getSeller() != null)
            query = itemCollectionReference.whereEqualTo("seller", params.getSeller());
        if(params.getCategories() != null)
            query = itemCollectionReference.whereIn("categories", params.getCategories());
        if(params.getProductId() != null)
            query = itemCollectionReference.whereEqualTo("itemId", params.getProductId());
        if(params.getTags() != null)
            query = itemCollectionReference.whereArrayContains("tags", params.getTags());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    List<Product> itemsList = querySnapshot.toObjects(Product.class);
                    callback.onCallback(itemsList);
                } else {
                    Log.i(TAG, "no documents ", task.getException());
                    callback.onCallback(null);
                }
            }
        });
    }
    public void getItem(String docId, getItemCallback callback) {
        itemCollectionReference.document(docId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            callback.onCallback(documentSnapshot.toObject(Product.class));
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

    public void updateItem(Product item) {
        String itemId = item.getProductId();
        Log.i(TAG, "Item Id "+itemId);
        DocumentReference documentReference = itemCollectionReference.document(itemId);
        documentReference.set(item);
    }
    public interface getItemsCallback {
        void onCallback(List<Product> itemsList);
    }
    public interface createItemsCallback {
        void onCallback(boolean ok, String id);
    }
    public interface getItemCallback {
        void onCallback(Product item);
    }

    public List<String> searchKeywords(Product items) {
        Set<String> searchList = new HashSet<>();
        for(String x: items.getTags())
            searchList.add(x.toLowerCase());
        for(String x: items.getTitle().split(" "))
            searchList.add(x.toLowerCase());
        for(String x: items.getCategories())
            searchList.add(x.toLowerCase());
        return new ArrayList<>(searchList);
    }
}
