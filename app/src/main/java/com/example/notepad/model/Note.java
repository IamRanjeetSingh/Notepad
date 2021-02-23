package com.example.notepad.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String accountId;
    @NonNull
    private String title = "";
    @NonNull
    private String body = "";

    public Note() {

    }

    public Note(@NonNull String title,@NonNull String body) {
        this.title = title;
        this.body = body;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(@NonNull String accountId) {
        this.accountId = accountId;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getBody() {
        return body;
    }

    public void setBody(@NonNull String body) {
        this.body = body;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(!(obj instanceof Note))
            return false;
        Note n = (Note)obj;

        if(id != 0 && n.id != 0 && id != n.id)
            return false;
        else
            return accountId.equals(n.accountId) && title.equals(n.title) && body.equals(n.body);
    }

    @NonNull
    @Override
    public String toString() {
        return id+" "+accountId+" "+title+" "+body;
    }
}
