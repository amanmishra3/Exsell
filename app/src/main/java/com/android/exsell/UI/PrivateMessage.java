package com.android.exsell.UI;

import android.content.Intent;
import android.media.Image;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PrivateMessage extends AppCompatActivity {
    private static final String TAG = "PrivateMessage";

    private ArrayList<Message> messageArrayList;
    MessageAdapter adapter;
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

        getMessages();

        setAdapter();
    }

    private void setAdapter() {
        Log.i(TAG, "setAdapter");
        adapter = new MessageAdapter(messageArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void getMessages() {
        Log.i(TAG, "getMessages");
        messageArrayList = new ArrayList<>();
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
                                message.setSender(doc.getString("sender"));

                                Calendar calendar = Calendar.getInstance();
                                Map<String, Object> map = (Map<String, Object>) doc.get("timeStamp");
                                calendar.setTime(((Timestamp) map.get("time")).toDate());
                                message.setTimeStamp(calendar);

                                boolean found = false;
                                for(Message m : messageArrayList) {
                                    if(m.isSame(message)) {
                                        found = true;
                                        break;
                                    }
                                }
                                if(!found) {
                                    messageArrayList.add(message);
                                    adapter.notifyItemInserted(messageArrayList.size() - 1);
                                    recyclerView.scrollToPosition(messageArrayList.size() - 1);
                                }
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
        Log.i(TAG, "createMessage " + m);
        mAuth = FirebaseAuth.getInstance();
//      Add message to database
        Message message = new Message();
        message.setMessage(m);
        message.setSender(mAuth.getCurrentUser().getUid());
        message.setTimeStamp(Calendar.getInstance());

        FirebaseFirestore.getInstance().collection("messages").document(messageId)
                .collection("messages").document().set(message);

//      Update the most recent message in database
        Map<String, Object> preview = new HashMap<>();
        preview.put("previewMessage", m);
        preview.put("previewTimeStamp", Calendar.getInstance());
        preview.put("messageId", messageId);

        FirebaseFirestore.getInstance().collection("messages").document(messageId).set(preview);

//      Scroll to the bottom of the chat
        recyclerView.scrollToPosition(messageArrayList.size() - 1);
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, MessagePreviews.class);
        startActivity(intent);
    }
}