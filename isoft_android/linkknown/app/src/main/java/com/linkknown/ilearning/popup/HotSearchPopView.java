package com.linkknown.ilearning.popup;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.widget.CommonTagView;
import com.linkknown.ilearning.widget.ShowAndCloseMoreView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HotSearchPopView extends BottomPopupView {

    List<String> showTagList = new ArrayList<>();
    List<String> hideTagList = new ArrayList<>();

    private CallbackListener listener;

    private Context mContext;
    public HotSearchPopView(@NonNull Context context, CallbackListener listener) {
        super(context);
        this.mContext = context;
        this.listener = listener;
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_hot_search;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        showTagList = new ArrayList<>(Arrays.asList("前端", "后端","数据库", "基础","运维", "测试","游戏","全栈","人工智能"));
        hideTagList = new ArrayList<>(Arrays.asList("大数据", "云计算", "爬虫","算法", "自动化","区块链", "深度学习"));

        CommonTagView showTagView = findViewById(R.id.showTagView);
        CommonTagView hideTagView = findViewById(R.id.hideTagView);
        showTagView.setList(showTagList);
        hideTagView.setList(hideTagList);
        CommonTagView.CallBackListener listener = text -> {
            this.listener.onConfirm(text);
        };
        showTagView.setCallBackListener(listener);
        hideTagView.setCallBackListener(listener);
        hideTagView.setVisibility(View.GONE);

        ShowAndCloseMoreView showAndCloseMoreView = findViewById(R.id.showAndCloseMore);
        showAndCloseMoreView.setCallBackListener(new ShowAndCloseMoreView.CallBackListener() {
            @Override
            public void showMore() {
                hideTagView.setVisibility(View.VISIBLE);
            }

            @Override
            public void closeMore() {
                hideTagView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.65f);
    }

    public interface CallbackListener {
        void onConfirm (String text);
    }
}
