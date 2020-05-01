package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.NetUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    private Context mContext;

    // 下拉刷新的layout
    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, view);

        this.initData();

        refreshLayout.setOnRefreshListener(() -> ToastUtil.showText(mContext, "onRefresh..."));

        return view;
    }

    private void initData () {
        //判断网络状态，并初始化数据
        if (!NetUtil.isNetworkAvailable(this.getContext())) {
            ToastUtil.showText(mContext, "亲，网络异常，无法获取数据请检查网络！");
            return;
        }
            //获取分类
//            initCategoryClassification();
//            //加载轮播图
//            initBanner();
//            //获取热评商品
//            initCommodityList(Constants.HOME_PAGE_LIMIT, 0, true);
    }
}
