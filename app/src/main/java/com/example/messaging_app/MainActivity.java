package com.example.messaging_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {



    BottomNavigationView bottomNavigationView;
    ImageButton searchButton;
    chatFragment chatFragment;
    profileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatFragment = new chatFragment();
        profileFragment = new profileFragment();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        searchButton = findViewById(R.id.main_search);

        bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.my_secondary));
        bottomNavigationView.setItemTextColor(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.my_secondary)));


        searchButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this,SearchUserActivity.class));

        });


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                if(item.getItemId()==R.id.menu_chats){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,chatFragment).commit();
                }if(item.getItemId()==R.id.menu_profile){
                 /*   getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,profileFragment).commit();*/
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this, LoginPhoneNumber.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.menu_chats);


    }

}