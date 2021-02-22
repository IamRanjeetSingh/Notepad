package com.example.notepad.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notepad.Dal.Repository;
import com.example.notepad.models.Note;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private Note currentNote;
    private Repository repo;

    public MainViewModel(Application app) {
        super(app);
        repo = new Repository(app.getApplicationContext());
    }

    public Note getCurrentNote() {
        return currentNote;
    }

    public void setCurrentNote(Note note) {
        this.currentNote = note;
    }

    public LiveData<List<Note>> getAllNotes() {
        return repo.getAllNotes();
    }

    public void addNote(Note note) {
//        repo.addNote(note);
    }

    public void addOrUpdateNote(Note note) {
//        repo.addOrUpdateNote(note);
    }
}
