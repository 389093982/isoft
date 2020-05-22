package com.linkknown.ilearning.common;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public abstract class OnLoadMoreListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;
    private int itemCount, lastPosition, lastItemCount;

    public abstract void onLoadMore();

    private boolean isUpPull = false;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
        isUpPull = dy > 0;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            itemCount = layoutManager.getItemCount();
            lastPosition = layoutManager.findLastCompletelyVisibleItemPosition();
        } else {
            Log.e("OnLoadMoreListener", "The OnLoadMoreListener only support LinearLayoutManager");
            return;
        }

        if (lastItemCount != itemCount && lastPosition == itemCount - 1 && isUpPull) {
            lastItemCount = itemCount;
            this.onLoadMore();
        }
    }
}
