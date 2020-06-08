package com.linkknown.ilearning.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.linkknown.ilearning.R;

/**
 * 底部弹框组件
 */
public abstract class BottomPopupWindow2 extends android.widget.PopupWindow implements android.widget.PopupWindow.OnDismissListener {

    //常用参数
    private ViewGroup mParent;
    private Context mContext;
    private Activity mActivity;

    /**
     * 在构造器里面初始化布局
     */
    public BottomPopupWindow2(Activity activity) {
        mActivity = activity;
        mContext = activity;
        mParent = (ViewGroup) activity.getWindow().getDecorView();
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_window, mParent, false);

        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(mContext.getResources().getDimensionPixelSize(R.dimen.size_400));

        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable());
        setOutsideTouchable(true);

        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setOnDismissListener(this);
        initView();
    }

    //展示数据并初始化数据
    public void show() {
        showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.6f);
        //加载数据
        initData();
    }

    @Override
    public void onDismiss() {
        backgroundAlpha(1.0f);
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mActivity.getWindow().setAttributes(lp);
    }

    // 加载视图
    public abstract void initView();

    //加载数据
    public abstract void initData();
}
