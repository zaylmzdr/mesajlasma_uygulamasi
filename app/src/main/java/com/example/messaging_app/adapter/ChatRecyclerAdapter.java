package com.example.messaging_app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messaging_app.ChatActivity;
import com.example.messaging_app.R;
import com.example.messaging_app.model.ChatMessageModel
;
import com.example.messaging_app.utils.AndroidUtil;
import com.example.messaging_app.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessageModel
, ChatRecyclerAdapter.ChatModelviewHolder> {

    Context context;
    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel
> options,Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull ChatModelviewHolder holder, int position, @NonNull ChatMessageModel
 model) {

        if(model.getSenderId().equals(FirebaseUtil.currentUserId())){
            holder.right_chat_textview.setText(model.getMessage());
            holder.right_chat_layout.setVisibility(View.VISIBLE);
            holder.left_chat_layout.setVisibility(View.GONE);
        }else{
            holder.right_chat_layout.setVisibility(View.GONE);
            holder.left_chat_layout.setVisibility(View.VISIBLE);
            holder.left_chat_textview.setText(model.getMessage());
        }

      

    }


    @NonNull
    @Override
    public ChatModelviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recyler_row,parent,false);
        return new ChatModelviewHolder(view);
    }

    class ChatModelviewHolder extends RecyclerView.ViewHolder {


        LinearLayout left_chat_layout,right_chat_layout;
        TextView left_chat_textview,right_chat_textview;

        public ChatModelviewHolder(@NonNull View itemView) {
            super(itemView);
            left_chat_layout = itemView.findViewById(R.id.left_chat_layout);
            right_chat_layout = itemView.findViewById(R.id.right_chat_layout);
            left_chat_textview = itemView.findViewById(R.id.left_chat_textview);
            right_chat_textview = itemView.findViewById(R.id.right_chat_textview);
        }

    }



}
