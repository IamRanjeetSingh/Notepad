package com.example.notepad.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notepad.Dal.Repository;
import com.example.notepad.models.Note;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

import java.util.List;

import java_async.Task;

public class MainViewModel extends AndroidViewModel {

    private Note currentNote;
    private Repository repo;

    public MainViewModel(Application app) {
        super(app);
        repo = new Repository(app.getApplicationContext(), GoogleSignIn.getLastSignedInAccount(app));
    }

    public Note getCurrentNote() {
        return currentNote;
    }

    public void setCurrentNote(Note note) {
        this.currentNote = note;
    }

    public Task<LiveData<List<Note>>> getAllNotes() {
        return repo.getAllNotes();
    }

    public void insertOrUpdateNote(Note note) {
        repo.insertOrUpdateNote(note);
    }
}
