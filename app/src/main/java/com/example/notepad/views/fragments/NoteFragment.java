package com.example.notepad.views.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notepad.R;
import com.example.notepad.databinding.NoteFragmentBinding;
import com.example.notepad.models.Note;
import com.example.notepad.viewmodels.MainViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class NoteFragment extends Fragment {
    public static final String TAG = NoteFragment.class.getSimpleName();

    private NoteFragmentBinding binding;
    private MainViewModel mainViewModel;
    private Note currentNote;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.note_fragment, container, false);
        if(getActivity() != null)
            mainViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(MainViewModel.class);

        currentNote = mainViewModel.getCurrentNote();
        setNoteContent(currentNote);

        binding.titleEditText.addTextChangedListener(new TextInputLayoutErrorRemover(binding.titleTextInputLayout));
        binding.bodyEditText.addTextChangedListener(new TextInputLayoutErrorRemover(binding.bodyTextInputLayout));

//        binding.saveNote.setOnClickListener(view -> saveNote());

        return binding.getRoot();
    }

    public void saveNote() {
        boolean canBeSaved = true;
        if(binding.titleEditText.getText() == null || binding.titleEditText.getText().toString().trim().equals("")) {
            binding.titleTextInputLayout.setError(getContext() != null ? getContext().getString(R.string.Required_Field_Empty_Error) : "");
            canBeSaved = false;
        }
        if(binding.bodyEditText.getText() == null || binding.bodyEditText.getText().toString().trim().equals("")) {
            binding.bodyTextInputLayout.setError(getContext() != null ? getContext().getString(R.string.Required_Field_Empty_Error) : "");
            canBeSaved = false;
        }

        if(canBeSaved) {
            String title = binding.titleEditText.getText().toString().trim();
            String body = binding.bodyEditText.getText().toString().trim();

            currentNote.setTitle(title);
            currentNote.setBody(body);

            mainViewModel.addOrUpdateNote(currentNote);

            if(getActivity() != null)
                getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainViewModel.setCurrentNote(null);
    }

    private void setNoteContent(Note note) {
        if(note != null) {
            binding.titleEditText.setText(note.getTitle());
            binding.bodyEditText.setText(note.getBody());
        }
    }

    private static class TextInputLayoutErrorRemover implements TextWatcher {
        private TextInputLayout textInputLayout;

        private TextInputLayoutErrorRemover(TextInputLayout textInputLayout) {
            this.textInputLayout = textInputLayout;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            textInputLayout.setError(null);
        }
    }
}
