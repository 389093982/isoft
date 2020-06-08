package com.linkknown.ilearning.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import java.util.LinkedList;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 底部搜索弹框
 */
public abstract class BottomPopupWindow extends android.widget.PopupWindow implements android.widget.PopupWindow.OnDismissListener {

    //弹框左侧的提示
    @BindView(R.id.left_tip)
    public TextView left_tip;

    //弹框右侧的提示
    @BindView(R.id.right_tip)
    public TextView right_tip;

    //弹框展示的 showTextView 内容布局
    @BindView(R.id.showTextView)
    public FlexboxLayout showTextView;
    
    //弹框展示的 showTagView 内容布局
    @BindView(R.id.showTagView)
    public RecyclerView showTagView;

    //常用参数
    private ViewGroup mParent;
    private Context mContext;
    private Activity mActivity;

    /**
     * 在构造器里面初始化布局
     */
    public BottomPopupWindow(Activity activity) {
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

        ButterKnife.bind(this, view);
        initView();
    }


    //初始化弹框的视图,使线性布局能自动换行
    private void initView () {
        showTextView.setFlexDirection(FlexDirection.ROW);
        showTextView.setFlexWrap(FlexWrap.WRAP);
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

    //加载数据
    public abstract void initData();

    //动态创建 textView
    public abstract TextView createView(Integer position, FlexboxLayout layout, String text);

    //确认
    public abstract void onConfirm(String text);

    //设置弹框左侧提示语
    public void setLeftTip(String tip){
        left_tip.setText(tip);
    };

    //设置弹框右侧提示语
    public void setRightTip(String tip){
        right_tip.setText(tip);
    };

    // 是否展示左侧和右侧提示
    public void showLeftRightTip (boolean showLeftTip, boolean showRightTip) {
        //左侧提示是否展示
        left_tip.setVisibility(showLeftTip ? View.VISIBLE : View.GONE);
        //右侧提示是否展示
        right_tip.setVisibility(showRightTip ? View.VISIBLE : View.GONE);
    }

    //显示 showTextView
    public void showTextView(Boolean flag){
        if (flag){
            showTextView.setVisibility(View.VISIBLE);
            showTagView(false);//显示textView 就不显示 tagView
        }else{
            showTextView.setVisibility(View.GONE);
        }
    };

    //显示 showTagView
    public void showTagView(Boolean flag){
        if (flag){
            showTagView.setVisibility(View.VISIBLE);
            showTextView(false);//显示tagView 就不显示 textView
        }else{
            showTagView.setVisibility(View.GONE);
        }
    };
    
}
