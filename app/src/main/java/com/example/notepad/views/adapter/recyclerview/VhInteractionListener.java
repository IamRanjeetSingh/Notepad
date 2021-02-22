package com.example.notepad.views.adapter.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

public interface VhInteractionListener<T extends RecyclerView.ViewHolder> {
    void onVhClick(T vh);
    void onVhLongClick(T vh);
}
