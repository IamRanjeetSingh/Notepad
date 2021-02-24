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
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.Task;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";

    private static final int RC_GOOGLE_SIGN_IN = 100;

    private SigninActivityBinding binding;
    private GoogleSignInClient gClient;
    private GoogleSignInAccount gAccount;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.signin_activity);

        GoogleSignInOptions gSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestProfile()
                .requestEmail()
                .build();
        gClient = GoogleSignIn.getClient(this, gSignInOptions);

        binding.signInBtn.setOnClickListener(v -> startActivityForResult(gClient.getSignInIntent(), RC_GOOGLE_SIGN_IN));

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
        if(requestCode == RC_GOOGLE_SIGN_IN) {
            signIn(GoogleSignIn.getSignedInAccountFromIntent(data));
        }
    }

    private void signIn(Task<GoogleSignInAccount> signInTask) {
        Log.d(TAG, "signIn() called with: signInTask = [" + signInTask + "]");
        if(signInTask == null)
            return;
        try {
            gAccount = signInTask.getResult(ApiException.class);
            onSignIn();
        } catch (ApiException e) {
            String errorMessage;
            if(e.getStatusCode() == CommonStatusCodes.NETWORK_ERROR)
                errorMessage = getString(R.string.GoogleSignIn_Network_Error);
            else if(e.getStatusCode() == CommonStatusCodes.INVALID_ACCOUNT)
                errorMessage = getString(R.string.GoogleSignIn_InvalidAccount_Error);
            else if(e.getStatusCode() == GoogleSignInStatusCodes.SIGN_IN_CANCELLED)
                errorMessage = getString(R.string.GoogleSignIn_SignInCancelled_Error);
            else
                errorMessage = getString(R.string.GoogleSignIn_Other_Error);

            Log.e(TAG, "signIn: " + GoogleSignInStatusCodes.getStatusCodeString(e.getStatusCode()), e);

            binding.signInError.setText(errorMessage);
        }
    }

    private void onSignIn() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
