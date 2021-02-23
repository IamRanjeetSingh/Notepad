package com.example.notepad.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.notepad.R;
import com.example.notepad.databinding.MainActivityBinding;
import com.example.notepad.viewmodel.MainViewModel;
import com.example.notepad.view.fragment.NoteFragment;
import com.example.notepad.view.fragment.NoteListFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class MainActivity extends AppCompatActivity implements NoteListFragment.Commands {

    private MainActivityBinding binding;
    private MainViewModel mainViewModel;
    private ActionMode actionMode;

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
                        .addOnCompleteListener(task -> startActivity(new Intent(MainActivity.this, SignInActivity.class)));
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

    private void deleteSelectedNotes() {
        NoteListFragment noteListFragment = (NoteListFragment) getSupportFragmentManager().findFragmentByTag(NoteListFragment.TAG);
        if(noteListFragment != null)
            noteListFragment.deleteSelectedNotesIfConfirmed();
    }

    @Override
    public void openActionBar() {
        if(!isActionBarOpen())
            actionMode = startSupportActionMode(actionModeCallback);
    }

    @Override
    public void updateActionBar(int selectionCount) {
        if(isActionBarOpen())
            actionMode.setTitle(getString(R.string.selected_template, selectionCount));
    }

    @Override
    public void closeActionBar() {
        if(isActionBarOpen()) {
            actionMode.finish();
            actionMode = null;

            NoteListFragment noteListFragment = (NoteListFragment) getSupportFragmentManager().findFragmentByTag(NoteListFragment.TAG);
            if(noteListFragment != null)
                noteListFragment.clearNoteSelections();
        }
    }

    @Override
    public boolean isActionBarOpen() {
        return actionMode != null;
    }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.action_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.delete) {
                deleteSelectedNotes();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            closeActionBar();
        }
    };
}