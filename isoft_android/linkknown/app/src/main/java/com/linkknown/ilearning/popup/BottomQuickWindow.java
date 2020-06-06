package com.linkknown.ilearning.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.linkknown.ilearning.R;

import butterknife.ButterKnife;

/**
 * 底部快速弹出窗口
 */
public class BottomQuickWindow extends PopupWindow {

    private ViewGroup mParent;
    private Context mContext;

    public BottomQuickWindow (Activity activity) {
        mContext = activity;
        mParent = (ViewGroup) activity.getWindow().getDecorView();
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_bottom_quick_window, mParent, false);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(mContext.getResources().getDimensionPixelSize(R.dimen.size_400));

        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable());
        setOutsideTouchable(true);

        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        ButterKnife.bind(this, view);

        setAnimationStyle(R.style.popwindow_animation);
        initView();
    }

    private void initView() {

    }

    public void show() {
        showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
    }
}
