package com.example.notepad.Dal;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.notepad.models.Note;

@androidx.room.Database(entities = { Note.class }, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public static Database getInstance(Context context) {
        if(instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "NotepadDb").build();
        return instance;
    }

    public abstract NoteDao noteDao();
}
