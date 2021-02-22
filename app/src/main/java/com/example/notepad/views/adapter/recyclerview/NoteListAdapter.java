package com.example.notepad.views.adapter.recyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.example.notepad.databinding.NoteListItemBinding;
import com.example.notepad.models.Note;

import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {

    private List<Note> notes;
    private OnVhClickListener<NoteListAdapter.ViewHolder> vhClickListener;

    public NoteListAdapter(LifecycleOwner lifecycleOwner, LiveData<List<Note>> liveNotes, OnVhClickListener<NoteListAdapter.ViewHolder> vhClickListener) {
        this.notes = liveNotes.getValue();
        liveNotes.observe(lifecycleOwner, this::setNotes);
        this.vhClickListener = vhClickListener;
    }

    public void setNotes(List<Note> notes) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtilCallback<>(this.notes, notes,
                ((oldItemPosition, newItemPosition) -> this.notes.get(oldItemPosition).getId() == notes.get(newItemPosition).getId()),
                ((oldItemPosition, newItemPosition) -> this.notes.get(oldItemPosition).equals(notes.get(newItemPosition)))));

        this.notes = notes;

        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteListAdapter.ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.note_list_item, parent, false), vhClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes != null ? notes.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private NoteListItemBinding binding;
        private Note note;

        private ViewHolder(NoteListItemBinding binding, OnVhClickListener<NoteListAdapter.ViewHolder> vhClickListener) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(view -> vhClickListener.onVHClick(this));
        }

        private void bind(Note note) {
            this.note = note;
            binding.title.setText(note.getTitle());
            binding.body.setText(note.getBody());
        }

        public Note getNote() {
            return note;
        }
    }
}
