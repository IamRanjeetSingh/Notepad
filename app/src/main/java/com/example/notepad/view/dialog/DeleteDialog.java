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
import com.example.notepad.databinding.DeleteDialogBinding;

public class DeleteDialog extends DialogFragment {
    public static final String TAG = "DeleteDialog";

    public static final String ARG_SELECTION_COUNT = "NoteCount";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DeleteDialogBinding binding = DataBindingUtil.inflate(inflater, R.layout.delete_dialog, container, false);
        setCancelable(false);

        if(getArguments() != null) {
            int noteCount = getArguments().getInt(ARG_SELECTION_COUNT);
            binding.message.setText(getResources().getQuantityString(R.plurals.Delete_Confirmation_Template, noteCount, noteCount));
        }

        binding.cancel.setOnClickListener(v -> dismiss());
        binding.delete.setOnClickListener(v -> {
            if(getActivity() != null)
                ((DialogEventListener)getActivity()).onDialogEvent(DialogEvent.DELETE_NOTES);
        });

        return binding.getRoot();
    }
}