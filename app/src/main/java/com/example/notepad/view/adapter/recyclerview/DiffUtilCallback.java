package com.example.notepad.view.adapter.recyclerview;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class DiffUtilCallback<T> extends DiffUtil.Callback {

    private List<T> oldList, newList;
    private ItemSimilarityChecker itemSimilarityChecker;
    private ContentSimilarityChecker contentSimilarityChecker;

    public DiffUtilCallback(List<T> oldList, List<T> newList, ItemSimilarityChecker itemSimilarityChecker, ContentSimilarityChecker contentSimilarityChecker) {
        this.oldList = oldList;
        this.newList = newList;
        this.itemSimilarityChecker = itemSimilarityChecker;
        this.contentSimilarityChecker = contentSimilarityChecker;
    }


    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        if(oldList == null || newList == null)
            return false;
        return itemSimilarityChecker.areItemsTheSame(oldItemPosition, newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if(oldList == null || newList == null)
            return false;
        return contentSimilarityChecker.areContentsTheSame(oldItemPosition, newItemPosition);
    }

    public interface ItemSimilarityChecker {
        boolean areItemsTheSame(int oldItemPosition, int newItemPosition);
    }

    public interface ContentSimilarityChecker {
        boolean areContentsTheSame(int oldItemPosition, int newItemPosition);
    }
}
