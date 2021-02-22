package com.example.notepad.Dal;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.notepad.models.Note;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import java_async.Task;
import java_async.TaskImpl;

public class Repository {

    private MutableLiveData<List<Note>> notes = new MutableLiveData<>(null);
    private Database db;
    private Executor executor;

    public Repository(Context context) {
        db = Database.getInstance(context);
        executor = Executors.newSingleThreadExecutor();
    }

    public Task<Long> insertNote(Note note) {
        if(note == null)
            throw new IllegalArgumentException("Note cannot be null");

        if(note.getTitle() == null || note.getTitle().trim().equals("") || note.getBody() == null || note.getBody().trim().equals(""))
            throw new IllegalArgumentException("Note's title and body cannot be empty");

        TaskImpl<Long> task = new TaskImpl<>();

        executor.execute(() -> {
            long rowId = db.noteDao().insert(note);
            if(rowId < 1)
                executeOnMainThread(() -> task.setResult(rowId));
            else
                executeOnMainThread(() -> task.setFailure(new RoomInvalidOperationException("Error occurred while inserting note to database"), rowId));
        });

        return task;
    }

    @NonNull
    public Task<Note> getNote(long id) {
        if(id < 1)
            throw new IllegalArgumentException("Id cannot be less than 1");

        TaskImpl<Note> task = new TaskImpl<>();

        executor.execute(() -> {
            Note note = db.noteDao().get(id);
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
            if(rowsAffected == 1)
                executeOnMainThread(() -> task.setResult(rowsAffected));
            else
                executeOnMainThread(() -> task.setFailure(new RoomInvalidOperationException(rowsAffected<0 ? "No note got updated" : "More than one note got updated"), rowsAffected));
        });

        return task;
    }

    public Task<Integer> deleteNote(@NonNull Note note) {
        if(note == null)
            throw new IllegalArgumentException("Note cannot be null");

        TaskImpl<Integer> task = new TaskImpl<>();

        executor.execute(() -> {
            int rowsDeleted = db.noteDao().delete(note);
            if(rowsDeleted == 1)
                executeOnMainThread(() -> task.setResult(rowsDeleted));
            else
                executeOnMainThread(() -> task.setFailure(new RoomInvalidOperationException(rowsDeleted<0 ? "No note got deleted" : "More than one note got deleted"), rowsDeleted));
        });

        return task;
    }

    public Task<Integer> deleteNote(int id) {
        if(id < 1)
            throw new IllegalArgumentException("Note's id cannot be less than 1");

        TaskImpl<Integer> task = new TaskImpl<>();

        executor.execute(() -> {
            int rowsDeleted = db.noteDao().delete(id);
            if(rowsDeleted == 1)
                executeOnMainThread(() -> task.setResult(rowsDeleted));
            else
                executeOnMainThread(() -> task.setFailure(new RoomInvalidOperationException(rowsDeleted<0 ? "No note got deleted" : "More than one note got deleted"), rowsDeleted));
        });

        return task;
    }

    @NonNull
    public Task<LiveData<List<Note>>> getAllNotes() {
        TaskImpl<LiveData<List<Note>>> task = new TaskImpl<>();

        executor.execute(() -> {
            List<Note> noteList = db.noteDao().getAll();
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
            int rowsDeleted = db.noteDao().deleteAll();
            executeOnMainThread(() -> task.setResult(rowsDeleted));
        });

        return task;
    }

    private void executeOnMainThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
