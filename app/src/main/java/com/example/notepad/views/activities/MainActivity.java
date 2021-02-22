package com.example.notepad.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.notepad.Dal.Repository;
import com.example.notepad.R;
import com.example.notepad.databinding.MainActivityBinding;
import com.example.notepad.models.Note;
import com.example.notepad.viewmodels.MainViewModel;
import com.example.notepad.views.fragments.NoteFragment;
import com.example.notepad.views.fragments.NoteListFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

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
                binding.addNote.setIconResource(R.drawable.ic_save);
            else
                binding.addNote.setIconResource(R.drawable.ic_add);
        });

        binding.addNote.setOnClickListener(view -> {
            NoteFragment noteFragment = (NoteFragment) getSupportFragmentManager().findFragmentByTag(NoteFragment.TAG);
            if(noteFragment == null) {
                mainViewModel.setCurrentNote(null);
                openNoteFragment();
            } else {
                noteFragment.saveNote();
            }
        });

        binding.toolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.account) {
                GoogleSignIn
                        .getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestId().build())
                        .signOut()
                        .addOnCompleteListener(task -> {
                            startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        });
                return true;
            }
            else
                return false;
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

    @Override
    public void showContextualActionBar() {

    }

    @Override
    public void hideContextualActionBar() {

    }

    private void feedDummyData() {
        //only for debugging purposes
        deleteDatabase("NotepadDb");
        for(int i = 0; i < 10; i++) {
            Repository repo = new Repository(this, GoogleSignIn.getLastSignedInAccount(this));
            repo.insertNote(new Note("Note "+(i+1), "This is the body of the note. This is the body of a standard note. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
        }
    }
}
