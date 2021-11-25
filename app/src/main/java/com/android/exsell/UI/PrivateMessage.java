package com.android.exsell.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.exsell.R;
import com.android.exsell.adapters.MessageAdapter;
import com.android.exsell.models.Message;

import java.util.ArrayList;
import java.util.Calendar;

public class PrivateMessage extends AppCompatActivity {
    private static final String TAG = "PrivateMessage";

    private ArrayList<Message> messageArrayList;
    private RecyclerView recyclerView;

    TextView contactName;

    EditText newMessage;

    String contactUid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_message);

        recyclerView = findViewById(R.id.private_message);

        messageArrayList = new ArrayList<>();

        setMessageInfo(); // TODO replace with getMessages() when implemented

        setAdapter();

        newMessage = findViewById(R.id.new_chat_message);

        Bundle extras = getIntent().getExtras();
        contactUid = extras.get("uid").toString();
        String name = extras.get("name").toString();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        contactName = findViewById(R.id.contact_name);
        contactName.setText(name);
        Log.i(TAG, "contactUid: " + contactUid);

        recyclerView.scrollToPosition(messageArrayList.size() - 1);
    }

    private void setAdapter() {
        Log.i(TAG, "setAdapter");
        MessageAdapter adapter = new MessageAdapter(messageArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void setMessageInfo() {
        Log.i(TAG, "setPreviewInfo");
        Calendar timeStamp = Calendar.getInstance();
        messageArrayList.add(new Message("Hello there!!!", 0, timeStamp));
        messageArrayList.add(new Message("How are you doing??", 1, timeStamp));
        messageArrayList.add(new Message("Is that item still available?", 0, timeStamp));
        messageArrayList.add(new Message("Are you still looking to buy that gabagoo", 1, timeStamp));
        messageArrayList.add(new Message("YES!!!\n How much are you looking for $$$$?", 0, timeStamp));
        messageArrayList.add(new Message("I was looking for $300", 1, timeStamp));
        messageArrayList.add(new Message("WOOWOWOWOWOWOW", 0, timeStamp));
        messageArrayList.add(new Message("So kind!!!!", 0, timeStamp));
        messageArrayList.add(new Message("Could you come down to $150?", 0, timeStamp));
        messageArrayList.add(new Message("You got yourself a deal!!", 1, timeStamp));
        messageArrayList.add(new Message("When will you come and get it", 1, timeStamp));
    }

    public void getMessages() {
        Log.i(TAG, "getMessagePreviews");
        /*
        TODO
        For each message
            Message message = new Message()
            message.message = get message
            message.sender = get sender (0 if other, 1 if self)
            message.timeStamp = get message timestamp

            messageArrayList.append(message)

         Sort list in descending order by message.timeStamp
         */
    }

    public void sendMessage(View view) {
        Log.i(TAG, "sendMessage");
        String message = newMessage.getText().toString();
        if (!message.isEmpty()) {
            createMessage(message);
            newMessage.setText("");
        }
    }

    public void createMessage(String message) {
        Log.i(TAG, "createMessage");
        // To Do
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, MessagePreviews.class);
        startActivity(intent);
    }
}