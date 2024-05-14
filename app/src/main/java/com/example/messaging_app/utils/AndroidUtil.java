package com.example.messaging_app.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.messaging_app.model.userModel;

import java.net.ContentHandler;

public class AndroidUtil {

    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();

    }

    public static void passUserModelAsIntent(Intent intent, userModel model){
        intent.putExtra("username",model.getUsername());
        intent.putExtra("phone",model.getPhone());
        intent.putExtra("userId",model.getUserId());
    }

    @NonNull
    public static userModel getuserModelFromIntent(Intent intent){
        userModel userModel = new userModel();
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setPhone(intent.getStringExtra("phone"));
        userModel.setUserId(intent.getStringExtra("userId"));
        return userModel;
    }
}
