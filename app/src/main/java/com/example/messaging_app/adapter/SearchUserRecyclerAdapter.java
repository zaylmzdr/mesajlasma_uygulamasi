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
import com.example.messaging_app.model.userModel;
import com.example.messaging_app.utils.AndroidUtil;
import com.example.messaging_app.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<userModel, SearchUserRecyclerAdapter.userModelviewHolder> {

    Context context;
    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<userModel> options,Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull userModelviewHolder holder, int position, @NonNull userModel model) {
        holder.usernameText.setText(model.getUsername());
        holder.phoneText.setText(model.getPhone());
        if(model.getUserId().equals(FirebaseUtil.currentUserId())){
            holder.usernameText.setText(model.getUsername()+" (Ben) ");

        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            AndroidUtil.passUserModelAsIntent(intent,model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        });


    }


    @NonNull
    @Override
    public userModelviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row,parent,false);
        return new userModelviewHolder(view);
    }

    class userModelviewHolder extends RecyclerView.ViewHolder {


        TextView usernameText;
        TextView phoneText;
        ImageView profilePic;
        public userModelviewHolder(@NonNull View itemView) {
            super(itemView);

            usernameText = itemView.findViewById(R.id.user_name_text);
            phoneText = itemView.findViewById(R.id.phone_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);

        }


    }



}
