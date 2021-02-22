package com.example.notepad.views.fragments;

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
import com.example.notepad.viewmodels.MainViewModel;
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

        binding.noteList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.noteList.setAdapter(new NoteListAdapter(this, mainViewModel.getAllNotes(), vh -> {
            mainViewModel.setCurrentNote(vh.getNote());
            if(getActivity() != null)
                ((Commands) getActivity()).openNoteFragment();
        }));

        return binding.getRoot();
    }

    public interface Commands {
        void openNoteFragment();
    }
}
