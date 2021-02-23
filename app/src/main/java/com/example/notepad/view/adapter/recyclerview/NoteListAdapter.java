package com.example.notepad.view.adapter.recyclerview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.example.notepad.model.Note;
import com.example.notepad.view.adapter.recyclerview.util.SelectionTracker;
import com.example.notepad.view.adapter.recyclerview.viewHolder.NoteListItem;

import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListItem> {

    private List<Note> notes;
    private SelectionTracker<Long> selectionTracker;
    private VhInteractionListener<NoteListItem> vhClickListener;

    public NoteListAdapter(LifecycleOwner lifecycleOwner, LiveData<List<Note>> liveNotes, SelectionTracker<Long> selectionTracker, VhInteractionListener<NoteListItem> vhClickListener) {
        setHasStableIds(true);
        this.notes = liveNotes.getValue();
        liveNotes.observe(lifecycleOwner, this::setNotes);
        this.selectionTracker = selectionTracker;
        this.vhClickListener = vhClickListener;
    }

    public void setNotes(List<Note> notes) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtilCallback<>(this.notes, notes,
                ((oldItemPosition, newItemPosition) -> this.notes.get(oldItemPosition).getId() == notes.get(newItemPosition).getId()),
                ((oldItemPosition, newItemPosition) -> this.notes.get(oldItemPosition).equals(notes.get(newItemPosition)))));

        diffResult.dispatchUpdatesTo(this);

        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteListItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteListItem(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.note_list_item, parent, false), selectionTracker,vhClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListItem holder, int position) {
        holder.setNote(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes != null ? notes.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return notes.get(position).getId();
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    //    public static class ViewHolder extends RecyclerView.ViewHolder{
//
//        private NoteListItemBinding binding;
//        private Note note;
//
//        private ViewHolder(NoteListItemBinding binding, VhInteractionListener<ViewHolder> vhClickListener) {
//            super(binding.getRoot());
//            this.binding = binding;
//
//            binding.getRoot().setOnClickListener(view -> vhClickListener.onVhClick(this));
//            binding.getRoot().setOnLongClickListener(view -> vhClickListener.onVhLongClick(this));
//        }
//
//        private void bind(Note note) {
//            this.note = note;
//            binding.title.setText(note.getTitle());
//            binding.body.setText(note.getBody());
//        }
//
//        public Note getNote() {
//            return note;
//        }
//    }
}
