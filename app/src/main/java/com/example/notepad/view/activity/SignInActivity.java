package com.example.notepad.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.notepad.R;
import com.example.notepad.databinding.SigninActivityBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class SignInActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;

    private SigninActivityBinding binding;
    private GoogleSignInClient gClient;
    private GoogleSignInAccount gAccount;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.signin_activity);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestId().build();
        gClient = GoogleSignIn.getClient(this, gso);

        binding.signInBtn.setOnClickListener(v -> startActivityForResult(gClient.getSignInIntent(), RC_SIGN_IN));
    }

    @Override
    protected void onStart() {
        super.onStart();

        gAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(gAccount != null)
            onSignIn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            signIn(GoogleSignIn.getSignedInAccountFromIntent(data));
        }
    }

    private void signIn(Task<GoogleSignInAccount> signInTask) {
        try {
            gAccount = signInTask.getResult(ApiException.class);
            onSignIn();
        } catch (ApiException e) {
            Log.d("MyTad", "signIn: sign in failed: "+e);
            binding.signInError.setText(getString(R.string.GoogleSignIn_Error));
        }
    }

    private void onSignIn() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
