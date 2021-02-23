package com.example.notepad.view.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.notepad.R;
import com.example.notepad.databinding.NoteListFragmentBinding;
import com.example.notepad.view.adapter.recyclerview.util.SelectionTracker;
import com.example.notepad.view.adapter.recyclerview.viewHolder.NoteListItem;
import com.example.notepad.viewmodel.MainViewModel;
import com.example.notepad.view.adapter.recyclerview.NoteListAdapter;
import com.example.notepad.view.adapter.recyclerview.VhInteractionListener;

public class NoteListFragment extends Fragment {
    public static final String TAG = NoteListFragment.class.getSimpleName();

    private NoteListFragmentBinding binding;
    private MainViewModel mainViewModel;
    private Commands parentActivity;

    private SelectionTracker<Long> selectionTracker;
    private AlertDialog.Builder deleteConfirmationDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.note_list_fragment, container, false);
        if(getActivity() != null) {
            mainViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
            parentActivity = ((Commands) getActivity());
        }

        deleteConfirmationDialog = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.Delete))
                .setCancelable(false)
                .setPositiveButton(R.string.Delete, (dialog, which) -> deleteSelectedNotes())
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

        selectionTracker = new SelectionTracker<>();

        binding.noteListSwipeRefreshLayout.setRefreshing(true);
        mainViewModel.getAllNotes().addOnCompleteListener(taskGetAllNotes -> {
            binding.noteListSwipeRefreshLayout.setRefreshing(false);
            if(taskGetAllNotes.isSuccessful()){
                binding.noteList.setAdapter(new NoteListAdapter(NoteListFragment.this, taskGetAllNotes.getResult(), selectionTracker, vhInteractionListener));
            }
        });
        binding.noteList.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.noteListSwipeRefreshLayout.setOnRefreshListener(() ->
                mainViewModel.getAllNotes().addOnCompleteListener(taskGetAllNotes ->
                        binding.noteListSwipeRefreshLayout.setRefreshing(false)));

        return binding.getRoot();
    }

    public void clearNoteSelections() {
        selectionTracker.clearSelections();
        if(binding.noteList.getAdapter() != null)
            binding.noteList.getAdapter().notifyItemRangeChanged(0, binding.noteList.getAdapter().getItemCount());
    }

    public void deleteSelectedNotesIfConfirmed() {
        deleteConfirmationDialog
                .setMessage(getResources().getQuantityString(R.plurals.Delete_Confirmation_Template, selectionTracker.getSelectionCount(), selectionTracker.getSelectionCount()))
                .show();
    }

    private void deleteSelectedNotes() {
        mainViewModel.deleteNotes(selectionTracker.getSelectionList());
        parentActivity.closeActionBar();
    }

    private VhInteractionListener<NoteListItem> vhInteractionListener = new VhInteractionListener<NoteListItem>() {

        @Override
        public void onVhClick(NoteListItem vh) {
            if(!parentActivity.isActionBarOpen()) {
                mainViewModel.setCurrentNote(vh.getNote());
                parentActivity.openNoteFragment();
            } else {
                onVhLongClick(vh);
            }
        }

        @Override
        public boolean onVhLongClick(NoteListItem vh) {
            vh.setIsSelected(!vh.isSelected());

            if(!parentActivity.isActionBarOpen())
                parentActivity.openActionBar();

            int selectionCount = selectionTracker.getSelectionCount();
            if(selectionCount > 0)
                parentActivity.updateActionBar(selectionCount);
            else
                parentActivity.closeActionBar();

            return true;
        }
    };

    public interface Commands {
        void openNoteFragment();
        void openActionBar();
        void updateActionBar(int selectionCount);
        void closeActionBar();
        boolean isActionBarOpen();
    }
}