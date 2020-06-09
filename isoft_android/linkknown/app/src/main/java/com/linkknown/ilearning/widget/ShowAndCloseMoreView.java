package com.linkknown.ilearning.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.linkknown.ilearning.R;

public class ShowAndCloseMoreView extends LinearLayout {

    private View showMore;
    private View closeMore;

    private CallBackListener listener;

    public void setCallBackListener (CallBackListener listener) {
        this.listener = listener;
    }

    public ShowAndCloseMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.show_close_more, this);

        // 获取控件
        showMore = findViewById(R.id.showMore);
        closeMore = findViewById(R.id.closeMore);

        // 点击后互换显示
        showMore.setOnClickListener(v -> {
            showMore.setVisibility(View.GONE);
            closeMore.setVisibility(View.VISIBLE);
            // 向上通知
            listener.showMore();
        });
        closeMore.setOnClickListener(v -> {
            showMore.setVisibility(View.VISIBLE);
            closeMore.setVisibility(View.GONE);
            listener.closeMore();
        });
    }

    public interface CallBackListener {
        void showMore();
        void closeMore();
    }
}
