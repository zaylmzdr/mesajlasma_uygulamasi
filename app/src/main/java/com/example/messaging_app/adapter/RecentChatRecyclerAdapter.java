package com.example.messaging_app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messaging_app.ChatActivity;
import com.example.messaging_app.R;
import com.example.messaging_app.model.ChatRoomModel;
import com.example.messaging_app.model.ChatRoomModel;
import com.example.messaging_app.model.userModel;
import com.example.messaging_app.utils.AndroidUtil;
import com.example.messaging_app.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatRoomModel, RecentChatRecyclerAdapter.ChatroomModelviewHolder> {

    Context context;
    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options,Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull ChatroomModelviewHolder holder, int position, @NonNull ChatRoomModel model) {
     FirebaseUtil.getOtherUserFromChatroom(model.getUserIds())
             .get().addOnCompleteListener(task -> {
                 if (task.isSuccessful()) {
                     boolean lastMessageSentByMe = model.getLastMessageSenderId().equals(FirebaseUtil.currentUserId());
                     userModel otherUserMOdel = task.getResult().toObject(userModel.class);
                     holder.usernameText.setText(otherUserMOdel.getUsername());

                     if(lastMessageSentByMe){
                         holder.lastMessageText.setText("Sen : "+model.getLastMessage());
                     }else
                         holder.lastMessageText.setText(model.getLastMessage());
                     holder.lastMessageTime.setText(FirebaseUtil.timestampToString(model.getLastMessageTimestamp()));



                     holder.itemView.setOnClickListener(v -> {
                         Intent intent = new Intent(context, ChatActivity.class);
                         AndroidUtil.passUserModelAsIntent(intent,otherUserMOdel);
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         context.startActivity(intent);

                     });
                 }
             });


    }


    @NonNull
    @Override
    public ChatroomModelviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.resend_chat_recycler_row,parent,false);
        return new ChatroomModelviewHolder(view);
    }

    class ChatroomModelviewHolder extends RecyclerView.ViewHolder {


        TextView usernameText;
        TextView lastMessageText;
        TextView lastMessageTime;
        ImageView profilePic;
        public ChatroomModelviewHolder(@NonNull View itemView) {
            super(itemView);

            usernameText = itemView.findViewById(R.id.user_name_text);
            lastMessageText = itemView.findViewById(R.id.last_message_text);
            lastMessageTime = itemView.findViewById(R.id.last_message_time_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);

        }


    }



}
