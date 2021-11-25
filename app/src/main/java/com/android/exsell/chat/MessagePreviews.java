package com.android.exsell.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.exsell.R;

import java.util.ArrayList;
import java.util.Calendar;

public class MessagePreviews extends AppCompatActivity {
    private static final String TAG = "MessagePreviewList";

    private ArrayList<Preview> previewArrayList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_preview_list);

        recyclerView = findViewById(R.id.message_preview_list);

        previewArrayList = new ArrayList<>();

        setPreviewInfo(); // replace with getMessagePreviews() when implemented

        setAdapter();
    }

    private void setAdapter() {
        Log.i(TAG, "setAdapter");
        MessagePreviewAdapter adapter = new MessagePreviewAdapter(previewArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void setPreviewInfo() {
        Log.i(TAG, "setPreviewInfo");
        Calendar timeStamp = Calendar.getInstance();
        previewArrayList.add(new Preview("andrew", "hello there", timeStamp, null));
        previewArrayList.add(new Preview("jack", "my name is jack", timeStamp, null));
        previewArrayList.add(new Preview("paul", "wheres the gabagoo", timeStamp, null));
        previewArrayList.add(new Preview("sam", "how much?????", timeStamp, null));
    }

    public void getMessagePreviews() {
        Log.i(TAG, "getMessagePreviews");
        /*
        For each message thread that user has
            Preview preview = new Preview()
            preview.profilePic = get contacts profile pic
            preview.name = get contacts name
            preview.message = get most recent message
            preview.timeStamp = get timestamp for most recent message

            previewArrayList.append(previews)

         Sort list in descending order by preview.time
         */
    }

    public void moveToMessage(View view) {
//        Log.i(TAG, "moveToMessage");
//        Intent intent = new Intent(this, PrivateMessage.class);
//        String uid = ""; // Get selected chat user uid
//        intent.putExtra("contact", uid);
//        startActivity(intent);
    }
}
