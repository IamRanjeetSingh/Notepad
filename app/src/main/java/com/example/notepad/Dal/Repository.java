package com.example.notepad.Dal;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.notepad.model.Note;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import java_async.Task;
import java_async.TaskImpl;

public class Repository {

    private MutableLiveData<List<Note>> notes = new MutableLiveData<>(null);
    private Database db;
    private Executor executor;
    private GoogleSignInAccount gAccount;

    public Repository(Context context) {
        db = Database.getInstance(context);
        executor = Executors.newSingleThreadExecutor();
        this.gAccount = GoogleSignIn.getLastSignedInAccount(context);
    }

    public Task<Long> insertNote(Note note) {
        if(note == null)
            throw new IllegalArgumentException("Note cannot be null");

        if(note.getTitle() == null || note.getTitle().trim().equals("") || note.getBody() == null || note.getBody().trim().equals(""))
            throw new IllegalArgumentException("Note's title and body cannot be empty");

        note.setAccountId(gAccount.getId());

        TaskImpl<Long> task = new TaskImpl<>();

        executor.execute(() -> {
            long rowId = db.noteDao().insert(note);
            refreshNotes();
            if(rowId < 1) {
                executeOnMainThread(() -> task.setResult(rowId));
            }
            else {
                executeOnMainThread(() -> task.setFailure(new RoomInvalidOperationException("Error occurred while inserting note to database"), rowId));
            }
        });

        return task;
    }

    @NonNull
    public Task<Note> getNote(long id) {
        if(id < 1)
            throw new IllegalArgumentException("Id cannot be less than 1");

        TaskImpl<Note> task = new TaskImpl<>();

        executor.execute(() -> {
            Note note = db.noteDao().get(gAccount.getId(), id);
            if(note != null)
                executeOnMainThread(() -> task.setResult(note));
            else
                executeOnMainThread(() -> task.setFailure(new RoomInvalidOperationException("Error occurred while inserting note to database"), null));
        });

        return task;
    }

    public Task<Integer> updateNote(@NonNull Note note) {
        if(note == null)
            throw new IllegalArgumentException("Note cannot be null");

        if(note.getTitle() == null || note.getTitle().trim().equals("") || note.getBody() == null || note.getBody().trim().equals(""))
            throw new IllegalArgumentException("Note's title and body cannot be empty");

        TaskImpl<Integer> task = new TaskImpl<>();

        executor.execute(() -> {
            int rowsAffected = db.noteDao().update(note);
            refreshNotes();
            if(rowsAffected == 1) {
                executeOnMainThread(() -> task.setResult(rowsAffected));
            }
            else {
                executeOnMainThread(() -> task.setFailure(new RoomInvalidOperationException(rowsAffected<0 ? "No note got updated" : "More than one note got updated"), rowsAffected));
            }
        });

        return task;
    }

    public Task<Boolean> insertOrUpdateNote(@NonNull Note note) {
        if(note == null)
            throw new IllegalArgumentException("Note cannot be null");

        TaskImpl<Boolean> task = new TaskImpl<>();

        if(note.getAccountId() == null)
            note.setAccountId(gAccount.getId());

        executor.execute(() -> {
            db.noteDao().insertOrUpdate(note);
            refreshNotes();
            // TODO: 22-02-2021 check for valid insert/update
            task.setResult(true);
        });

        return task;
    }

    public Task<Integer> deleteNote(@NonNull Note note) {
        if(note == null)
            throw new IllegalArgumentException("Note cannot be null");

        TaskImpl<Integer> task = new TaskImpl<>();

        executor.execute(() -> {
            int rowsDeleted = db.noteDao().delete(note);
            refreshNotes();
            if(rowsDeleted == 1) {
                executeOnMainThread(() -> task.setResult(rowsDeleted));
            }
            else {
                executeOnMainThread(() -> task.setFailure(new RoomInvalidOperationException(rowsDeleted<0 ? "No note got deleted" : "More than one note got deleted"), rowsDeleted));
            }
        });

        return task;
    }

    public Task<Integer> deleteNote(int id) {
        if(id < 1)
            throw new IllegalArgumentException("Note's id cannot be less than 1");

        TaskImpl<Integer> task = new TaskImpl<>();

        executor.execute(() -> {
            int rowsDeleted = db.noteDao().delete(gAccount.getId(), id);
            refreshNotes();
            if(rowsDeleted == 1) {
                executeOnMainThread(() -> task.setResult(rowsDeleted));
            }
            else {
                executeOnMainThread(() -> task.setFailure(new RoomInvalidOperationException(rowsDeleted<0 ? "No note got deleted" : "More than one note got deleted"), rowsDeleted));
            }
        });

        return task;
    }

    public Task<Integer> deleteNotes(@NonNull List<Long> ids) {
        if(ids == null)
            throw new IllegalArgumentException("Note's list cannot be null");

        TaskImpl<Integer> task = new TaskImpl<>();

        executor.execute(() -> {
            int rowsDeleted = db.noteDao().delete(gAccount.getId(), ids);
            refreshNotes();
            executeOnMainThread(() -> task.setResult(rowsDeleted));
        });

        return task;
    }

    @NonNull
    public Task<LiveData<List<Note>>> getAllNotes() {
        TaskImpl<LiveData<List<Note>>> task = new TaskImpl<>();

        executor.execute(() -> {
            List<Note> noteList = db.noteDao().getAll(gAccount.getId());
            // TODO: 22-02-2021 Check the returned noteList value
            executeOnMainThread(() -> {
                notes.setValue(noteList);
                task.setResult(notes);
            });
        });

        return task;
    }

    public Task<Integer> deleteAllNotes() {
        TaskImpl<Integer> task = new TaskImpl<>();

        executor.execute(() -> {
            int rowsDeleted = db.noteDao().deleteAll(gAccount.getId());
            refreshNotes();
            executeOnMainThread(() -> task.setResult(rowsDeleted));
        });

        return task;
    }

    private void refreshNotes() {
        executor.execute(() -> {
            List<Note> noteList = db.noteDao().getAll(gAccount.getId());
            // TODO: 22-02-2021 Check the returned noteList value
            executeOnMainThread(() -> notes.setValue(noteList));
        });
    }

    private void executeOnMainThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
