package com.example.messaging_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.hbb20.CountryCodePicker;

public class LoginPhoneNumber extends AppCompatActivity {



    CountryCodePicker countryCodePicker;
    EditText phone_number;
    Button otp_btn;
    ProgressBar login_progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone_number);
        countryCodePicker = findViewById(R.id.country_code);
        phone_number =findViewById(R.id.phone_number);
        otp_btn = findViewById(R.id.otp_btn);
        login_progress_bar = findViewById(R.id.login_progress_bar);

        login_progress_bar.setVisibility(View.GONE);

        countryCodePicker.registerCarrierNumberEditText(phone_number);

        otp_btn.setOnClickListener((v) -> {
            if(!countryCodePicker.isValidFullNumber()){
                phone_number.setError("Geçersiz telefon numarası!");
                return;
            }
            Intent intent = new Intent(LoginPhoneNumber.this,LoginOtpActivity.class);
            intent.putExtra("phone",countryCodePicker.getFullNumberWithPlus());
            startActivity(intent);
        });

    }
}