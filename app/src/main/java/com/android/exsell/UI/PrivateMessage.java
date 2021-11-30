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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class PrivateMessage extends AppCompatActivity {
    private static final String TAG = "PrivateMessage";

    private ArrayList<Message> messageArrayList;
    private RecyclerView recyclerView;

    FirebaseAuth mAuth;

    TextView contactName;

    EditText newMessage;

    String messageId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_message);

        recyclerView = findViewById(R.id.private_message);

        newMessage = findViewById(R.id.new_chat_message);

        Bundle extras = getIntent().getExtras();
        messageId = extras.get("messageId").toString();
        String name = extras.get("name").toString();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        contactName = findViewById(R.id.contact_name);
        contactName.setText(name);
        Log.i(TAG, "messageId: " + messageId);

//        setMessageInfo(); // use sample data
        getMessages(); // use firebase data

        setAdapter();

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
        Log.i(TAG, "getMessages");
        /*
        TODO  fix ui to update when messageArrayList<messages> changes
         */
        messageArrayList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        String uidSelf = mAuth.getCurrentUser().getUid();
        Log.i(TAG, messageId + " ");
        FirebaseFirestore.getInstance().collection("messages").document(messageId)
                .collection("messages").orderBy("timeStamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        for (QueryDocumentSnapshot doc : value) {
                            if(doc.exists()) {

                                Message message = new Message();
                                message.setMessage(doc.getString("message"));

                                if(doc.getString("sender") == uidSelf)
                                    message.setSender(0);
                                else
                                    message.setSender(1);

                                Calendar calendar = Calendar.getInstance();
                                Map<String, Object> map = (Map<String, Object>) doc.get("timeStamp");
                                calendar.setTime(((Timestamp) map.get("time")).toDate());
                                message.setTimeStamp(calendar);

                                messageArrayList.add(message);
                            }
                        }
                    }
                });
    }

    public void sendMessage(View view) {
        Log.i(TAG, "sendMessage");
        String message = newMessage.getText().toString();
        if (!message.isEmpty()) {
            createMessage(message);
            newMessage.setText("");
        }
    }

    public void createMessage(String m) {
        Log.i(TAG, "createMessage");
        Map<String, Object> message = new HashMap<>();
        message.put("message", m);
        message.put("timeStamp", Calendar.getInstance());
        message.put("sender", mAuth.getCurrentUser().getUid());

        Map<String, Object> preview = new HashMap<>();
        preview.put("previewMessage", m);
        preview.put("previewTimeStamp", Calendar.getInstance());

        FirebaseFirestore.getInstance().collection("messages").document(messageId)
                .collection("messages").document().set(message);

        FirebaseFirestore.getInstance().collection("messages").document(messageId).set(preview);
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, MessagePreviews.class);
        startActivity(intent);
    }
}