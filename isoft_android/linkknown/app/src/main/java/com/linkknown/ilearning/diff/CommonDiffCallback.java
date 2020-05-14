package com.linkknown.ilearning.diff;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class CommonDiffCallback<T> extends DiffUtil.Callback {


    private List<T> newList;
    private List<T> oldList;

    public CommonDiffCallback(List<T> oldList, List<T> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getClass().equals(newList.get(newItemPosition).getClass());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T oldStr = oldList.get(oldItemPosition);
        T newStr = newList.get(newItemPosition);
        return oldStr.equals(newStr);
    }
}
