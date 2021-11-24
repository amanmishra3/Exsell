package com.android.exsell.UI;

import android.util.Log;

import com.android.exsell.db.ItemDb;
import com.android.exsell.models.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExampleFunctions {
    private ItemDb itemDb;
    private String TAG = "Example";
    public ExampleFunctions() {
        itemDb = ItemDb.newInstance();
    }
    public void DoNothing() {
        //to add Product;
        itemDb.getAllItems(new ItemDb.getItemsCallback() {
            @Override
            public void onCallback(List<Product> itemsList) {
            }
        });

        // creating a Product obj for filtered searching, populate only the fields to filter leave the rest as it is
        Product search = new Product();
        search.setTitle("bat");
        search.setCategories(Arrays.asList("bat"));
        search.setTags(Arrays.asList("bat"));

//        List<String> st = new ArrayList<>();
//        st.add("sports");
//        st.add("cricket");
//        st.add("bat");
//        search.setTags(st);
//        search.setSeller("uuid");
//        search.setDescription("This is a year old cricket bat");
//        search.setStatus("new");
//        search.setBargain(true);

        // create an item
        itemDb.createItem(search, new ItemDb.createItemsCallback() {
            @Override
            public void onCallback(boolean ok, String id) {
                if(ok) // on create callback insert views or star intents over here
                    Log.i(TAG,"Document created with id "+id);
            }
        });

        // search items based on Product object
        itemDb.searchItems(search, new ItemDb.getItemsCallback() {
            @Override
            public void onCallback(List<Product> itemsList) {
                if(itemsList == null || itemsList.size() == 0)
                    Log.i(TAG," My data is null");
                else {
                    Log.i(TAG, " My data " + itemsList.size());
                    Product myItem = itemsList.get(0);
                    List<String> st = new ArrayList<>();
                    st.add("sports");
                    st.add("cricket");
                    st.add("notABat");
                    myItem.setTags(st);
                    itemDb.getItem(myItem.getProductId(), new ItemDb.getItemCallback() {
                        @Override
                        public void onCallback(Product item) {
                            Log.i(TAG, " My data " + item.getTags());
                        }
                    });
                }
            }
        });

    }

}
