package com.example.notepad.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notepad.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DURATION = 1000;
    private long startTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent;
        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);

        if(gAccount != null) {
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("accountId", gAccount.getId());
        } else {
            intent = new Intent(this, SignInActivity.class);
        }

        long elapsedTime = System.currentTimeMillis() - startTime;

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(intent);
            finish();
        }, Math.max(SPLASH_SCREEN_DURATION - elapsedTime, 0));
    }
}
