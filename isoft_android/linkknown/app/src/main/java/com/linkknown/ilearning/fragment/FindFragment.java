package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.view.View;

import com.linkknown.ilearning.R;

import butterknife.ButterKnife;

public class FindFragment extends BaseLazyLoadFragment {
    private Context mContext;

    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_find;
    }
}
