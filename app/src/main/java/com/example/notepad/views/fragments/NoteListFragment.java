package com.example.notepad.views.fragments;

import android.os.Bundle;
import android.os.Looper;
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
import com.example.notepad.models.Note;
import com.example.notepad.viewmodels.MainViewModel;
import com.example.notepad.views.activities.MainActivity;
import com.example.notepad.views.adapter.recyclerview.NoteListAdapter;

public class NoteListFragment extends Fragment {
    public static final String TAG = NoteListFragment.class.getSimpleName();

    private MainViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NoteListFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.note_list_fragment, container, false);
        if(getActivity() != null)
            mainViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);

        binding.noteListSwipeRefreshLayout.setRefreshing(true);
        mainViewModel.getAllNotes().addOnCompleteListener(taskGetAllNotes -> {
            binding.noteListSwipeRefreshLayout.setRefreshing(false);
            if(taskGetAllNotes.isSuccessful()){
                binding.noteList.setAdapter(new NoteListAdapter(NoteListFragment.this, taskGetAllNotes.getResult(), vh -> {
                    mainViewModel.setCurrentNote(vh.getNote());
                    if(getActivity() != null)
                        ((Commands)(getActivity())).openNoteFragment();
                }));
            }
        });
        binding.noteList.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.noteListSwipeRefreshLayout.setOnRefreshListener(() ->
                mainViewModel.getAllNotes().addOnCompleteListener(taskGetAllNotes ->
                        binding.noteListSwipeRefreshLayout.setRefreshing(false)));

        return binding.getRoot();
    }

    public interface Commands {
        void openNoteFragment();
    }
}




/*
        binding.noteList.setAdapter(
                new NoteListAdapter(
                        this,

                mainViewModel.getAllNotes(),
                vh -> {

                    mainViewModel.setCurrentNote(vh.getNote());
                    if(getActivity() != null)
                        ((Commands) getActivity()).openNoteFragment();
                }));
* */