package com.example.notepad.view.adapter.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

public interface VhInteractionListener<T extends RecyclerView.ViewHolder> {
    void onVhClick(T vh);
    boolean onVhLongClick(T vh);
}
