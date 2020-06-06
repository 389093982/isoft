package com.linkknown.ilearning.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.linkknown.ilearning.util.NetUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

public abstract class BaseLazyLoadFragment extends Fragment {

    private View mRootView;
    private boolean misVisibleToUser;
    private boolean mIsfirstgetdata = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView==null){
            mRootView = inflater.inflate(providelayoutId(),container,false);
            initView(mRootView);

            //判断网络状态，并初始化数据
            if (!NetUtil.isNetworkAvailable(this.getContext())) {
                ToastUtil.showText(getContext(), "亲，网络异常，无法获取数据请检查网络！");
            }
        }
        return mRootView;
    }

    protected abstract void initView(View mRootView);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lazyloadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        misVisibleToUser = isVisibleToUser;
        lazyloadData();
    }


    public void lazyloadData() {
        if ((mIsfirstgetdata || setIsRealTimeRefresh())&&misVisibleToUser && mRootView!=null){
            mIsfirstgetdata=false;
            initData();
        }

    }

    protected abstract void initData();

    //fragment是否需要实时刷新，如果需要返回true，不需要返回false
    protected abstract boolean setIsRealTimeRefresh();

    protected abstract int providelayoutId();


}
