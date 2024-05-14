package com.example.messaging_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messaging_app.utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {


    String phoneNumber;
    Long timeoutSeconds = 60L;
    EditText otp_code;
    Button next_btn;
    ProgressBar login_progress_bar;
    TextView otp_code_again;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);
        otp_code = findViewById(R.id.otp_code);
        next_btn = findViewById(R.id.next_btn);
        login_progress_bar= findViewById(R.id.login_progress_bar);
        otp_code_again = findViewById(R.id.otp_code_again);


        phoneNumber = getIntent().getExtras().getString("phone");


       /* Map<String,String> data = new HashMap<>();
        FirebaseFirestore.getInstance().collection("test").add(data);* test edildi ve silindi /
        */

        sendOtp(phoneNumber,false);

        next_btn.setOnClickListener(v -> {
            String enteredOtp = otp_code.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,enteredOtp);
            signIn(credential);
            setInProgress(true);


        });

        otp_code_again.setOnClickListener(v -> {
            sendOtp(phoneNumber,true);
        });

    }
    void sendOtp(String phoneNumber,boolean isResend){
        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder =
        PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signIn(phoneAuthCredential);
                        setInProgress(false);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        AndroidUtil.showToast(getApplicationContext(),"Doğrulama başarısız");
                        setInProgress(false);

                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationCode = s;
                        resendingToken = forceResendingToken;
                        AndroidUtil.showToast(getApplicationContext(),"Doğrulama kodu gönderildi");
                        setInProgress(false);
                    }
                });
    if(isResend){
        PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
    }else{
        PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }

    }

    void setInProgress(boolean inProgress){
        if(inProgress){
            login_progress_bar.setVisibility(View.VISIBLE);
            next_btn.setVisibility(View.GONE);
        }else{
            login_progress_bar.setVisibility(View.GONE);
            next_btn.setVisibility(View.VISIBLE);

        }
    }
    void signIn(PhoneAuthCredential phoneAuthCredential){
        //login part and next activity
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginOtpActivity.this, LoginUsername.class);
                    intent.putExtra("phone",phoneNumber);
                    startActivity(intent);

                }else{
                    AndroidUtil.showToast(getApplicationContext(),"Doğrulama kodu başarısız");
                }

            }
        });

    }
    void startResendTimer(){
        otp_code_again.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                otp_code_again.setText("Doğrulama kodunu"+timeoutSeconds+"içinde gönder");
                if(timeoutSeconds <=0){
                    timeoutSeconds = 60l;
                    timer.cancel();
                    runOnUiThread(() -> {
                        otp_code_again.setEnabled(true);

                    });
                }

            }
        },0,1000);
    }
}