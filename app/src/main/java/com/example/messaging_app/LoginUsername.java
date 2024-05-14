package com.example.messaging_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.messaging_app.model.userModel;
import com.example.messaging_app.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginUsername extends AppCompatActivity {


    EditText username_input;
    Button finish_btn;
    ProgressBar login_progress_bar;
    String phoneNumber;
    userModel userModel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_username);

        username_input = findViewById(R.id.username_input);
        finish_btn = findViewById(R.id.finish_btn);
        login_progress_bar = findViewById(R.id.login_progress_bar);

        phoneNumber = getIntent().getExtras().getString("phone");
        getUsername();

        finish_btn.setOnClickListener(v -> {
            setUsername();


        });


    }

    void getUsername(){
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    userModel = task.getResult().toObject(userModel.class);
                    if(userModel != null){
                        username_input.setText(userModel.getUsername());
                    }

                }
            }
        });

    }
    void setUsername(){
        String username = username_input.getText().toString();
        if(username.isEmpty() || username.length()<3){
            username_input.setError("Kullanıcı adı en az 3 karakter olmalı");
            return;
        }
        setInProgress(false);
       if(userModel != null){
           userModel.setUsername(username);
       }else{
           userModel = new userModel(phoneNumber,username, Timestamp.now(),FirebaseUtil.currentUserId());
       }

       FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               setInProgress(false);
               if(task.isSuccessful()){
                   Intent intent = new Intent(LoginUsername.this, MainActivity.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   startActivity(intent);
               }

           }
       });
    }

    void setInProgress(boolean inProgress){
        if(inProgress){
            login_progress_bar.setVisibility(View.VISIBLE);
            finish_btn.setVisibility(View.GONE);
        }else{
            login_progress_bar.setVisibility(View.GONE);
            finish_btn.setVisibility(View.VISIBLE);

        }
    }
}