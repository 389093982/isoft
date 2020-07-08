package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jakewharton.rxbinding4.view.RxView;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.InvitationAdapter;
import com.linkknown.ilearning.adapter.LinkknownWithMeAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.helper.SwipeRefreshLayoutHelper;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.Paginator;
import com.linkknown.ilearning.model.UserLinkAgentResponse;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.KeyBoardUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InvitationFragment extends BaseLazyLoadFragment{

    private Context mContext;

    //邀请他人
    @BindView(R.id.addCustomerTextView)
    public EditText addCustomerTextView;
    @BindView(R.id.addCustomerEditTextClear)
    public ImageView addCustomerEditTextClear;
    @BindView(R.id.addCustomerBtn)
    public ImageView addCustomerBtn;

    //正在邀请我
    private InvitationAdapter baseQuickAdapter;
    private Handler handler = new Handler();
    private Paginator paginator;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayoutHelper swipeRefreshLayoutHelper = new SwipeRefreshLayoutHelper();

    //获取邀请我的数据
    private List<UserLinkAgentResponse.UserLinkAgent> userLinkAgentList = new ArrayList<>();



    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_invitation;
    }

    @Override
    protected void initView(View mRootView) {
        ButterKnife.bind(this, mRootView);
        this.mContext = getActivity();

        swipeRefreshLayoutHelper.bind(getContext(), refreshLayout);
        swipeRefreshLayoutHelper.initStyle();
        swipeRefreshLayoutHelper.registerListener(() -> initData());

        baseQuickAdapter = new InvitationAdapter(mContext,userLinkAgentList);
        baseQuickAdapter.setClickAcceptInvite(new InvitationAdapter.ClickAcceptInvite() {
            @Override
            public void acceptInvite(String agent_user_name) {
                AgreeUserLinkAgent(agent_user_name);
            }
        });

        // 是否自动加载下一页（默认为true）
        baseQuickAdapter.getLoadMoreModule().setAutoLoadMore(true);
        // 设置加载更多监听事件
        baseQuickAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            if (paginator != null && paginator.getCurrpage() < paginator.getTotalpages()) {
                loadPageData(paginator.getCurrpage() + 1, Constants.DEFAULT_PAGE_SIZE);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(baseQuickAdapter);

    }


    @Override
    protected void initData() {

        // 有内容显示 clear 图标,没有则隐藏
        RxTextView.textChanges(addCustomerTextView).map(CharSequence::toString).subscribe(s -> {
            if (!TextUtils.isEmpty(s)) {
                addCustomerEditTextClear.setVisibility(View.VISIBLE);
            } else {
                addCustomerEditTextClear.setVisibility(View.GONE);
                //关闭软键盘
                KeyBoardUtil.closeKeybord(addCustomerTextView, mContext);
            }
        });

        // 清空输入框
        RxView.clicks(addCustomerEditTextClear).subscribe(aVoid -> {
            addCustomerTextView.setText("");
            //关闭软键盘
            KeyBoardUtil.closeKeybord(addCustomerTextView, mContext);
        });

        //点击邀请
        addCustomerBtn.setOnClickListener(v -> {
            if (StringUtils.isNotEmpty(addCustomerTextView.getText())){
                //邀请
                AddUserLinkAgent(addCustomerTextView.getText().toString().trim());
            }else{
                ToastUtil.showText(mContext,"请输入受邀者账号！");
            }
            //关闭软键盘
            KeyBoardUtil.closeKeybord(addCustomerTextView, mContext);
        });


        //查询邀请我的人
        //网络请求
        loadPageData(1, Constants.DEFAULT_PAGE_SIZE);
    }


    private void loadPageData(int current_page, int pageSize) {
        // 第一页不延时执行
        if (current_page == 1) {
            executeLoadPageData(current_page, pageSize);
        } else {
            // 后续页面，延迟执行，让加载效果更好
            handler.postDelayed(() -> executeLoadPageData(current_page, pageSize), 1000);
        }
    };


    //加载数据-- 查询邀请我的人
    private void executeLoadPageData(int current_page, int pageSize) {
        LinkKnownApiFactory.getLinkKnownApi().QueryTodayInviteMe(current_page,pageSize)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<UserLinkAgentResponse>() {
                    @Override
                    public void onNext(UserLinkAgentResponse o) {
                        if (o.isSuccess()){
                            if (CollectionUtils.isNotEmpty(o.getUserLinkAgentList())){
                                if (current_page == 1) {
                                    // 先清空再添加
                                    userLinkAgentList.clear();
                                }
                                userLinkAgentList.addAll(o.getUserLinkAgentList());
                                baseQuickAdapter.setList(userLinkAgentList);

                                // 当前这次数据加载完毕，调用此方法
                                baseQuickAdapter.getLoadMoreModule().loadMoreComplete();
                            }else{
                                if (current_page == 1) {
                                    userLinkAgentList.clear();
                                    baseQuickAdapter.setList(userLinkAgentList);
                                    baseQuickAdapter.setEmptyView(R.layout.layout_region_recommend_empty);
                                    TextView emptyTipText = baseQuickAdapter.getEmptyLayout().findViewById(R.id.emptyTipText);
                                    emptyTipText.setText("暂无邀请者");
                                }
                                baseQuickAdapter.getLoadMoreModule().loadMoreComplete();
                                baseQuickAdapter.getLoadMoreModule().loadMoreEnd();
                            }

                            paginator = o.getPaginator();
                            // 最后一页
                            if (CommonUtil.isLastPage(paginator)) {
                                baseQuickAdapter.getLoadMoreModule().loadMoreEnd();
                            }
                        }else{
                            baseQuickAdapter.getLoadMoreModule().loadMoreFail();
                        }
                        swipeRefreshLayoutHelper.finishRefreshing();
                    };
                    @Override
                    public void onError(Throwable e) {
                        Log.e("QueryTodayInviteMe:", e.getMessage());
                        ToastUtil.showText(mContext,"查询失败！");
                        swipeRefreshLayoutHelper.finishRefreshing();
                        // 当前这次数据加载错误，调用此方法
                        baseQuickAdapter.getLoadMoreModule().loadMoreFail();
                    }
                });
    }


    //邀请其他人
    private void AddUserLinkAgent(String user_name) {
        if (user_name.equals(LoginUtil.getLoginUserName(mContext))){
            ToastUtil.showText(mContext,"不能邀请自己^_^");
            return;
        }
        LinkKnownApiFactory.getLinkKnownApi().AddUserLinkAgent(user_name)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse o) {
                        if (o.isSuccess()){
                            ToastUtil.showText(mContext,"邀请成功！");
                        }else{
                            ToastUtil.showText(mContext,o.getErrorMsg());
                        }
                    };
                    @Override
                    public void onError(Throwable e) {
                        Log.e("AddUserLinkAgent:", e.getMessage());
                        ToastUtil.showText(mContext,"添加失败！");
                    }
                });
    }


    //接收邀请
    private void AgreeUserLinkAgent(String agent_user_name){
        LinkKnownApiFactory.getLinkKnownApi().AgreeUserLinkAgent(agent_user_name)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse o) {
                        if (o.isSuccess()){
                            ToastUtil.showText(mContext,"关联成功！");
                            //刷新界面
                            //网络请求
                            loadPageData(1, Constants.DEFAULT_PAGE_SIZE);
                        }else{
                            ToastUtil.showText(mContext,o.getErrorMsg());
                        }
                    };
                    @Override
                    public void onError(Throwable e) {
                        Log.e("AgreeUserLinkAgent:", e.getMessage());
                        ToastUtil.showText(mContext,"关联失败！");
                    }
                });
    };


}
