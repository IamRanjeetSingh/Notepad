package com.example.notepad.Dal;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notepad.model.Note;

import java.util.List;

@Dao
public abstract class NoteDao {

    @Insert
    public abstract long insert(Note note);

    @Update
    public abstract int update(Note note);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertOrUpdate(Note note);

    @Delete
    public abstract int delete(Note note);

    @Query("DELETE FROM Notes WHERE accountId = :accountId AND id = :id")
    public abstract int delete(String accountId, long id);

    @Query("DELETE FROM Notes WHERE accountId = :accountId AND id IN (:ids)")
    public abstract int delete(String accountId, List<Long> ids);

    @Query("SELECT * FROM Notes WHERE accountId = :accountId AND id = :id")
    public abstract Note get(String accountId, long id);

    @Query("SELECT * FROM Notes WHERE accountId = :accountId")
    public abstract List<Note> getAll(String accountId);

    @Query("DELETE FROM Notes WHERE accountId = :accountId")
    public abstract int deleteAll(String accountId);
}
