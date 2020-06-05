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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索历史弹框
 */
public class SearchHistoryPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    @BindView(R.id.hot_flexbox_layout)
    FlexboxLayout hot_flexbox_layout;
    @BindView(R.id.mine_flexbox_layout)
    FlexboxLayout mine_flexbox_layout;

    private ViewGroup mParent;
    private Context mContext;
    private Activity mActivity;
    private OnConfirmListener mConfirmListener;

    /**
     * 在构造器里面初始化布局
     */
    public SearchHistoryPopupWindow (Activity activity) {
        mActivity = activity;
        mContext = activity;
        mParent = (ViewGroup) activity.getWindow().getDecorView();
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_search_history, mParent, false);

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

    private void initView () {
        hot_flexbox_layout.setFlexDirection(FlexDirection.ROW);
        hot_flexbox_layout.setFlexWrap(FlexWrap.WRAP);

        mine_flexbox_layout.setFlexDirection(FlexDirection.ROW);
        mine_flexbox_layout.setFlexWrap(FlexWrap.WRAP);
    }

    private void initData() {
        // 先清空再重新添加
        hot_flexbox_layout.removeAllViews();
        mine_flexbox_layout.removeAllViews();

        for (int i = 0; i < 10; i++) {
            final TextView tv = createTextView(i, hot_flexbox_layout, "测试1111");
            hot_flexbox_layout.addView(tv);
        }

        // 读取搜索历史
        // TODO 显示排序有点问题
        Set<String> searchTexts = CommonUtil.getSearchHistory(mContext);
        LinkedList<String> searchTextList = new LinkedList<>(searchTexts);
        for (int i = 0; i < searchTextList.size(); i++) {
            final TextView tv = createTextView(i, mine_flexbox_layout, searchTextList.get(i));
            mine_flexbox_layout.addView(tv);
        }
    }

    // 动态创建 textView
    private TextView createTextView(int position, FlexboxLayout layout, String text) {
        // 根据布局动 id 态创建 TextView
        final TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_tag, layout, false);
        tv.setText(text);
        // 设置随机背景色，文字使用白色
        tv.setBackgroundColor(UIUtils.getRandomResourceColor(position));
        tv.setTextColor(Color.WHITE);

        // 点击事件
        tv.setOnClickListener(v -> {
            mConfirmListener.onConfirm(tv.getText().toString());
            // 点击完成后隐藏 popupwindow
            onDismiss();
            dismiss();
        });
        return tv;
    }

    public void registerListener (OnConfirmListener listener) {
        mConfirmListener = listener;
    }

    public void show() {
        showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.6f);

        // 每次显示都要更新数据
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

    public interface OnConfirmListener {
        void onConfirm(String text);
    }
}
