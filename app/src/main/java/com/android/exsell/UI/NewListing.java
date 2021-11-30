package com.android.exsell.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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
import com.android.exsell.adapters.NotificationAdapter;
import com.android.exsell.cloudStorage.MyFirebaseStorage;
import com.android.exsell.db.AppSettingsDb;
import com.android.exsell.db.ItemDb;
import com.android.exsell.fragments.FragmentSearchBar;
import com.android.exsell.fragments.FragmentTopBar;
import com.android.exsell.listeners.TopBottomNavigationListener;
import com.android.exsell.listeners.navigationListener;
import com.android.exsell.models.Category;
import com.android.exsell.models.Notifications;
import com.android.exsell.models.Product;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewListing extends AppCompatActivity implements FragmentTopBar.navbarHamburgerOnClickCallback, FragmentSearchBar.SearchBarOnSearch, FragmentTopBar.NotificationBellClickCallback, FragmentSearchBar.SearchBarBack {
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawerlist;
    RecyclerView notificationRecycler;
    private RecyclerView.LayoutManager layoutManager;
    public static RecyclerView.Adapter adapter;
    NavigationView navigationView;
    Product addProduct;
    private Uri uri;
    private AppSettingsDb appSettingsDb;
    private String TAG = "NewListing ";
    private ItemDb itemDb;
    private MyFirebaseStorage myStorage;
    private ImageView search, wishlist, addListing, message, addImage, notification;
    private Button addItem;
    private TextView title, description, tags, price;
    private LinearLayout progressLayout;
    private Spinner s;
    private AlertDialog.Builder builder;
    private static final int galleryPick = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_new_listing);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameTopBar, new FragmentTopBar());
        fragmentTransaction.commit();

        itemDb = ItemDb.newInstance();
        myStorage = new MyFirebaseStorage();
        addProduct = new Product();
        appSettingsDb = AppSettingsDb.newInstance();

        layoutBottom = findViewById(R.id.layoutBottomBar);
        drawerlist = (DrawerLayout) findViewById(R.id.drawerLayoutItem);
        navigationView = findViewById(R.id.navigationMenuItem);
        navigationView.setNavigationItemSelectedListener(new navigationListener(getApplicationContext()));

        wishlist = (ImageView) layoutBottom.findViewById(R.id.wishlistButton);
        wishlist.setOnClickListener(new TopBottomNavigationListener(R.id.wishlistButton, getApplicationContext()));
        addListing = (ImageView) layoutBottom.findViewById(R.id.addItemButton);
        addListing.setOnClickListener(new TopBottomNavigationListener(R.id.addItemButton, getApplicationContext()));
        message = (ImageView) layoutBottom.findViewById(R.id.chatButton);
        message.setOnClickListener(new TopBottomNavigationListener(R.id.chatButton, getApplicationContext()));

        notificationRecycler = (RecyclerView) findViewById(R.id.right_drawer);
        notificationRecycler.setNestedScrollingEnabled(true);
        loadNotificationsRecycler(notificationRecycler, Notifications.getMyNotifications(), 1);

        s = (Spinner) findViewById(R.id.category);
        builder = new AlertDialog.Builder(this);

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
                        finish();
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

    public void loadNotificationsRecycler(RecyclerView thisRecycler, List<JSONObject> products, int columns) {
        layoutManager = new GridLayoutManager(this, columns);
        thisRecycler.setHasFixedSize(true); // set has fixed size
        thisRecycler.setLayoutManager(layoutManager); // set layout manager

        // create and set adapter
        adapter = new NotificationAdapter(products, this);
        thisRecycler.setAdapter(adapter);
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
        setDialog(true);
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
                if(!ok) {
                    setDialog(false);
                    return;
                }
                Log.i(TAG,ok + " : id : "+id);
                myStorage.uploadImage(uri, id, 0, new MyFirebaseStorage.downloadUrlCallback() {
                    @Override
                    public void onCallback(String url) {
                        Log.i(TAG," My URI "+url);
                        itemDb.getItemCollectionReference().document(id).update("imageUri", url);
                        setDialog(false);
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
    private void setDialog(boolean show) {
        Log.i(TAG, "Progress bar is up");
        builder.setView(R.layout.progressalert);
        builder.setCancelable(false);
        Dialog dialog = builder.create();
        if (show) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            dialog.show();
        }
        else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            dialog.dismiss();
        }
    }

    @Override
    public void onHamburgerClickCallback() {
        Log.i(TAG,"onHamburgerClickCallback");
        drawerlist.closeDrawer(GravityCompat.END, false);
        drawerlist.openDrawer(GravityCompat.START);
    }

    @Override
    public void onNotificationBellClick() {
        Log.i(TAG,"onNotificationBellClick");
        drawerlist.closeDrawer(GravityCompat.START, false);
        drawerlist.openDrawer(GravityCompat.END);
    }

    public void setNavigationHeader() {

    }

    @Override
    public void onSearch(String search) {
        Log.i(TAG,"onSearch received "+search);
        //startActivity(new Intent(getApplicationContext(), Home.class));
    }

    @Override
    public void onSearchBack() {
        Log.i("onSearchBack", "searchBack");
    }

}