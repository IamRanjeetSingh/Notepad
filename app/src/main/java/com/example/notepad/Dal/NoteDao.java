package com.example.notepad.Dal;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notepad.models.Note;

import java.util.List;

@Dao
public abstract class NoteDao {

    @Insert
    public abstract long insert(@NonNull Note note);

    @Update
    public abstract int update(@NonNull Note note);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertOrUpdate(@NonNull Note note);

    @Delete
    public abstract int delete(@NonNull Note note);

    @Query("DELETE FROM Notes WHERE id = :id")
    public abstract int delete(long id);

    @Query("SELECT * FROM Notes WHERE id = :id")
    public abstract Note get(long id);

    @Query("SELECT * FROM Notes")
    public abstract List<Note> getAll();

    @Query("DELETE FROM Notes")
    public abstract int deleteAll();

    public enum OrderBy{
        Id,
        Title,
        Body
    }
}
