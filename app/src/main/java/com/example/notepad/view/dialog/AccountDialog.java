package com.example.notepad.view.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.notepad.R;
import com.example.notepad.databinding.AccountDialogBinding;

public class AccountDialog extends DialogFragment {
    public static final String TAG = "AccountDialog";

    public static final String ARG_NAME = "Account_Name";
    public static final String ARG_EMAIL = "Account_Email";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AccountDialogBinding binding = DataBindingUtil.inflate(inflater, R.layout.account_dialog, container, false);

        if(getArguments() != null) {
            String name = getArguments().getString(ARG_NAME);
            String email = getArguments().getString(ARG_EMAIL);
            if(name != null)
                binding.name.setText(name);
            if(email != null)
                binding.email.setText(email);
        }

        binding.cancel.setOnClickListener(v -> dismiss());
        binding.signOut.setOnClickListener(v -> {
            if(getActivity() != null)
                ((DialogEventListener)getActivity()).onDialogEvent(DialogEvent.SIGN_OUT);
        });

        return binding.getRoot();
    }
}
