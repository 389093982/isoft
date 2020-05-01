package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CourseListActivity;
import com.linkknown.ilearning.activity.LoginActivity;
import com.linkknown.ilearning.util.NetUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private Context mContext;

    @BindView(R.id.btnToLogin)
    public Button btnToLogin;
    @BindView(R.id.btnToCourseList)
    public Button btnToCourseList;

    private View rootView;

    // 下拉刷新的layout
    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.handleData();

        btnToLogin.setOnClickListener(this);
        btnToCourseList.setOnClickListener(this);

        refreshLayout.setOnRefreshListener(() -> ToastUtil.showText(mContext, "onRefresh..."));
    }

    private void handleData () {
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

    @Override
    public void onClick(View v) {
        ToastUtil.showText(mContext, "点击了按钮~");
        switch (v.getId()) {
            case R.id.btnToLogin:
                UIUtils.gotoActivity(mContext, LoginActivity.class);
                break;
            case R.id.btnToCourseList:
                UIUtils.gotoActivity(mContext, CourseListActivity.class);
                break;
            default:
                break;
        }
    }
}
