package com.example.notepad.views.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notepad.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);

        if(gAccount != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("accountId", gAccount.getId());
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        }

        finish();
    }
}
