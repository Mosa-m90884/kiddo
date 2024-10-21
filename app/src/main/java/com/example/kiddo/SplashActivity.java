package com.example.kiddo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kiddo.Controller.FirebaseManager;
import com.example.kiddo.Controller.SharedPreferencesHelper;
import com.example.kiddo.Login.LoginActivity;
import com.example.kiddo.Login.SignUpActivity;
import com.example.kiddo.child.HomeActivity;
import com.example.kiddo.parent.parentDashboard;
import com.google.firebase.auth.FirebaseUser;

import org.tensorflow.lite.Interpreter;

public class SplashActivity extends AppCompatActivity {
    Button sign, log;
    FirebaseManager firebaseManager;
    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseManager = new FirebaseManager();

        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        String type = sharedPreferencesHelper.getString("username", "لا يوجد اسم محفوظ");
        FirebaseUser currentUser = firebaseManager.getCurrentUser();
        if (currentUser != null) {
            // المستخدم مسجل الدخول، انتقل إلى الشاشة الرئيسية
            if(type.equals("children")){
                Intent intent = new Intent(this, HomeActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return;
            }

            if (type.equals("parent")){
                Intent intent = new Intent(this, parentDashboard.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return;
            }


        }
        setContentView(R.layout.activity_splash);
        sign = findViewById(R.id.sign_in_splash);
        log = findViewById(R.id.log_in_splash);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, SignUpActivity.class));
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));

            }
        });


    }


}