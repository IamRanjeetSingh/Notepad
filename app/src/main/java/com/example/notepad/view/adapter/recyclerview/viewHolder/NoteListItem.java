package com.example.notepad.view.adapter.recyclerview.viewHolder;

import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.example.notepad.databinding.NoteListItemBinding;
import com.example.notepad.model.Note;
import com.example.notepad.view.adapter.recyclerview.VhInteractionListener;
import com.example.notepad.view.adapter.recyclerview.util.SelectableViewHolder;
import com.example.notepad.view.adapter.recyclerview.util.SelectionTracker;

public class NoteListItem extends RecyclerView.ViewHolder implements SelectableViewHolder {

    private NoteListItemBinding binding;
    private Note note;
    private SelectionTracker<Long> selectionTracker;

    public NoteListItem(NoteListItemBinding binding, SelectionTracker<Long> selectionTracker, VhInteractionListener<NoteListItem> interactionListener) {
        super(binding.getRoot());
        this.binding = binding;
        this.selectionTracker = selectionTracker;

        binding.getRoot().setOnClickListener(v -> interactionListener.onVhClick(this));
        binding.getRoot().setOnLongClickListener(v -> interactionListener.onVhLongClick(this));
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;

        if(note != null) {
            binding.title.setText(note.getTitle());
            binding.body.setText(note.getBody());
        } else {
            binding.title.setText("");
            binding.body.setText("");
        }

        setContextualColor();
    }

    private void setContextualColor() {
        if(isSelected())
            binding.bgLayout.setBackgroundResource(R.drawable.shp_selected_note);
        else
            binding.bgLayout.setBackgroundResource(0);
    }

    @Override
    public void setIsSelected(boolean selection) {
        selectionTracker.setIsSelected(getItemId(), selection);
        setContextualColor();
    }

    @Override
    public boolean isSelected() {
        return selectionTracker.isSelected(getItemId());
    }
}
