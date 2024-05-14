package com.example.messaging_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.messaging_app.adapter.ChatRecyclerAdapter;
import com.example.messaging_app.adapter.SearchUserRecyclerAdapter;
import com.example.messaging_app.model.ChatMessageModel
;
import com.example.messaging_app.model.ChatRoomModel;
import com.example.messaging_app.model.userModel;
import com.example.messaging_app.utils.AndroidUtil;
import com.example.messaging_app.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {

    ChatRecyclerAdapter adapter;
    userModel otherUser;
    String chatroomId;
    ChatRoomModel chatRoomModel;

    EditText chatMessage;
    ImageButton sendButton;
    ImageButton backButton;
    RecyclerView recyclerView;
    TextView otherUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        otherUser = AndroidUtil.getuserModelFromIntent(getIntent());
        chatroomId = FirebaseUtil.getChatroomId(FirebaseUtil.currentUserId(),otherUser.getUserId());

        chatMessage = findViewById(R.id.chat_message);
        sendButton= findViewById(R.id.send_btn);
        backButton= findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.chat_recyclerView);
        otherUsername = findViewById(R.id.other_username);


        backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher();
        });

        otherUsername.setText(otherUser.getUsername());

        sendButton.setOnClickListener(v -> {
            String message = chatMessage.getText().toString().trim();
            if (message.isEmpty())
                return;
            sendMessageToUser(message);
            });

        getOrCreateChatroomModel();
        setupChatRecyclerView();

    }
    
    void setupChatRecyclerView(){

        Query query = FirebaseUtil.getChatroomMessageReference(chatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query,ChatMessageModel.class).build();

        adapter = new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager= new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    void sendMessageToUser(String message){

        chatRoomModel.setLastMessageTimestamp(Timestamp.now());
        chatRoomModel.setLastMessageSenderId(FirebaseUtil.currentUserId());
        chatRoomModel.setLastMessage(message);
        FirebaseUtil.getChatroomReference(chatroomId).set(chatRoomModel);

        ChatMessageModel
 ChatMessageModel
 = new ChatMessageModel
(message,FirebaseUtil.currentUserId(),Timestamp.now());
        FirebaseUtil.getChatroomMessageReference(chatroomId).add(ChatMessageModel
)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            chatMessage.setText("");
                        }
                    }
                });
    }

    void getOrCreateChatroomModel(){
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                chatRoomModel = task.getResult().toObject(ChatRoomModel.class);
                if(chatRoomModel==null){
                    chatRoomModel = new ChatRoomModel(
                            chatroomId,
                            Arrays.asList(FirebaseUtil.currentUserId(),otherUser.getUserId()),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtil.getChatroomReference(chatroomId).set(chatRoomModel);
                }
            }
        });
    }

}