package com.linkknown.ilearning.helper;

import android.content.Context;
import android.util.TypedValue;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class SwipeRefreshLayoutHelper {

    private SwipeRefreshLayout refreshLayout;
    private boolean mIsRefreshing = false;
    private Context mContext;

    public void bind (Context mContext, SwipeRefreshLayout refreshLayout){
        this.mContext = mContext;
        this.refreshLayout = refreshLayout;
    }

    public void initStyle (){
        refreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        refreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, mContext.getResources().getDisplayMetrics()));
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

    }

    public void registerListener (OnRefreshListener listener) {
        // refreshLayout 设置刷新监听
        refreshLayout.setOnRefreshListener(() -> {
            if (canRefresh()) {
                refreshLayout.setRefreshing(true);
                mIsRefreshing = true;
                listener.refreshData();
            }
        });
    }

    public interface OnRefreshListener {
        void refreshData();
    }

    private boolean canRefresh () {
        return !mIsRefreshing;
    }

    public void finishRefreshing() {
        mIsRefreshing = false;
        refreshLayout.setRefreshing(false);
    }
}
