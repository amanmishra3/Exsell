package com.android.exsell.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.exsell.R;
import com.android.exsell.cloudStorage.MyFirebaseStorage;
import com.android.exsell.db.AppSettingsDb;
import com.android.exsell.db.ItemDb;
import com.android.exsell.listeners.TopBottomNavigationListener;
import com.android.exsell.listeners.navigationListener;
import com.android.exsell.models.Category;
import com.android.exsell.models.Product;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewListing extends AppCompatActivity {
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawerlist;
    NavigationView navigationView;
    Product addProduct;
    private Uri uri;
    private AppSettingsDb appSettingsDb;
    private String TAG = "NewListing ";
    private ItemDb itemDb;
    private MyFirebaseStorage myStorage;
    private ImageView search, wishlist, addListing, message, addImage;
    private Button addItem;
    private TextView title, description, tags, price;
    private Spinner s;
    private static final int galleryPick = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_new_listing);
        itemDb = ItemDb.newInstance();
        myStorage = new MyFirebaseStorage();
        addProduct = new Product();
        appSettingsDb = AppSettingsDb.newInstance();
        layoutTop = findViewById(R.id.layoutTopBar);
        layoutBottom = findViewById(R.id.layoutBottomBar);
        drawerlist = (DrawerLayout) findViewById(R.id.drawerLayoutItem);
        navigationView = findViewById(R.id.navigationMenuItem);
        navigationView.setNavigationItemSelectedListener(new navigationListener(getApplicationContext()));
        search = (ImageView) layoutTop.findViewById(R.id.searchButton);
        search.setOnClickListener(new TopBottomNavigationListener(R.id.searchButton, getApplicationContext()));
        wishlist = (ImageView) layoutBottom.findViewById(R.id.wishlistButton);
        wishlist.setOnClickListener(new TopBottomNavigationListener(R.id.wishlistButton, getApplicationContext()));
        addListing = (ImageView) layoutBottom.findViewById(R.id.addItemButton);
        addListing.setOnClickListener(new TopBottomNavigationListener(R.id.addItemButton, getApplicationContext()));
        message = (ImageView) findViewById(R.id.chatButton);
        s = (Spinner) findViewById(R.id.category);
        message.setOnClickListener(new TopBottomNavigationListener(R.id.chatButton, getApplicationContext()));
        layoutTop.findViewById(R.id.leftNavigationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerlist.openDrawer(GravityCompat.START);

            }
        });
        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);
        tags = (TextView) findViewById(R.id.tags);
        price = (TextView) findViewById(R.id.price);
        addImage = (ImageView) findViewById(R.id.imageAdd);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        addItem = (Button) findViewById(R.id.addListing);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewItem(new uploadCompleteCallback() {
                    @Override
                    public void onCallback(boolean uploaded) {
                        startActivity(new Intent(getApplicationContext(), MyListings.class));
                    }
                });
            }
        });
        //dummy items in the category
        List<String> arraySpinner = Arrays.asList("Select an item", "Books", "Electronics", "Furniture", "Sports", "Stationery");
        appSettingsDb.getCategories(new AppSettingsDb.getCategoryCallback() {
            @Override
            public void onCallback(List<String> categories) {
                Log.i("TAG", "my Categories"+categories);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(adapter);
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
//                addProduct.setImageUri(imageUri.toString());
                uri = imageUri;
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                addImage.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void addNewItem(uploadCompleteCallback callback) {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String itemTitle = title.getText().toString();
        String itemDescription = description.getText().toString();
        String sTags = tags.getText().toString();
        List<String> itemTags = new ArrayList<>();
        int itemPrice = Integer.parseInt(price.getText().toString());
        String itemSeller = currentFirebaseUser.getUid();

        addProduct.setTitle(itemTitle);
        addProduct.setCategories(Arrays.asList(s.getSelectedItem().toString()));
        addProduct.setDescription(itemDescription);
        addProduct.setSeller(itemSeller);
        for(String tags: sTags.split(",")) {
            itemTags.add(tags.trim().toLowerCase());
        }
        addProduct.setTags(itemTags);
        addProduct.setPrice(itemPrice);
        itemDb.createItem(addProduct, new ItemDb.createItemsCallback() {
            @Override
            public void onCallback(boolean ok, String id) {
                Log.i(TAG,ok + " : id : "+id);
                myStorage.uploadImage(uri, id, new MyFirebaseStorage.downloadUrlCallback() {
                    @Override
                    public void onCallback(String url) {
                        Log.i(TAG," My URI "+url);
                        itemDb.getItemCollectionReference().document(id).update("imageUri", url);
                        callback.onCallback(true);
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        drawerlist.closeDrawer(Gravity.LEFT, false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        title.setText("");
        description.setText("");
        tags.setText("");
        price.setText("");
    }
    public interface uploadCompleteCallback {
        void onCallback(boolean uploaded);
    }
}