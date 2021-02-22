package com.example.notepad.views.adapter.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

public interface OnVhClickListener<T extends RecyclerView.ViewHolder> {
    void onVHClick(T vh);
}
