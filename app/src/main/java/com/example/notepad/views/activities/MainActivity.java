package com.example.notepad.views.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.notepad.R;
import com.example.notepad.databinding.MainActivityBinding;
import com.example.notepad.models.Note;
import com.example.notepad.viewmodels.MainViewModel;
import com.example.notepad.views.fragments.NoteFragment;
import com.example.notepad.views.fragments.NoteListFragment;

public class MainActivity extends AppCompatActivity implements NoteListFragment.Commands {

    private MainActivityBinding binding;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MainViewModel.class);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(binding.fragmentContainer.getId(), new NoteListFragment(), NoteListFragment.TAG)
                    .commit();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if(getSupportFragmentManager().findFragmentByTag(NoteFragment.TAG) != null)
                binding.addNote.setVisibility(View.GONE);
            else
                binding.addNote.setVisibility(View.VISIBLE);
        });

        binding.addNote.setOnClickListener(view -> {
            mainViewModel.setCurrentNote(null);
            openNoteFragment();
        });
    }

    @Override
    public void openNoteFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.fragmentContainer.getId(), new NoteFragment(), NoteFragment.TAG)
                .addToBackStack("AddEditNote")
                .commit();
    }
}
