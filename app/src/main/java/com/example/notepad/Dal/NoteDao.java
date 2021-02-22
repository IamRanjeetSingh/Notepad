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

    @Query("DELETE FROM Notes WHERE accountId = :accountId AND id = :id")
    public abstract int delete(String accountId, long id);

    @Query("SELECT * FROM Notes WHERE accountId = :accountId AND id = :id")
    public abstract Note get(String accountId, long id);

    @Query("SELECT * FROM Notes WHERE accountId = :accountId")
    public abstract List<Note> getAll(String accountId);

    @Query("DELETE FROM Notes WHERE accountId = :accountId")
    public abstract int deleteAll(String accountId);
}
