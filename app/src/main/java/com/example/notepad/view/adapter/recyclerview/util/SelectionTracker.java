package com.example.notepad.view.adapter.recyclerview.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectionTracker<T> {
    private Set<T> selectedKeys;

    public SelectionTracker() {
        this.selectedKeys = new HashSet<>();
    }

    public void setIsSelected(T key, boolean isSelected) {
        if(isSelected)
            selectedKeys.add(key);
        else
            selectedKeys.remove(key);
    }

    public boolean isSelected(T key) {
        return selectedKeys.contains(key);
    }

    public int getSelectionCount() {
        return selectedKeys.size();
    }

    public List<T> getSelectionList() {
        return new ArrayList<>(selectedKeys);
    }

    public void clearSelections() {
        selectedKeys.clear();
    }
}
