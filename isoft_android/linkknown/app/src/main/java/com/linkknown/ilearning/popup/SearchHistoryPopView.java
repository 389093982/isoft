package com.linkknown.ilearning.popup;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.flexbox.FlexboxLayout;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.Set;

public class SearchHistoryPopView extends BottomPopupView {

    private CallbackListener listener;

    private Context mContext;
    public SearchHistoryPopView(@NonNull Context context, CallbackListener listener) {
        super(context);
        this.mContext = context;
        this.listener = listener;
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_search_history;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        FlexboxLayout flexboxLayout = findViewById(R.id.flexboxLayout);
        // TODO 显示排序有点问题
        Set<String> searchTexts = CommonUtil.getSearchHistory(mContext);
        LinkedList<String> searchTextList = new LinkedList<>(searchTexts);
        flexboxLayout.removeAllViews();
        for (int i = 0; i < searchTextList.size(); i++) {
            // 根据布局动 id 态创建 TextView
            final TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_tag, flexboxLayout, false);
            textView.setText(searchTextList.get(i));
            // 设置随机背景色，文字使用白色
            textView.setBackgroundColor(UIUtils.getRandomResourceColor(i));
            textView.setTextColor(Color.WHITE);

            // 点击事件
            textView.setOnClickListener(v -> {
                listener.onConfirm(textView.getText().toString());
                // 点击完成后隐藏 popupwindow
                onDismiss();
                dismiss();
            });
            flexboxLayout.addView(textView);
        }

        TextView clearHisoty = findViewById(R.id.clearHisoty);
        clearHisoty.setOnClickListener(v -> {
            // 清空搜索历史
            CommonUtil.clearSearchHistory(mContext);
            // 同时清空 UI
            flexboxLayout.removeAllViews();
        });
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.45f);
    }

    public interface CallbackListener {
        void onConfirm (String text);
    }
}
