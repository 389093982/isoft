package com.linkknown.ilearning.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.internal.FlowLayout;
import com.linkknown.ilearning.R;

import java.util.List;

public class CommonTagView extends LinearLayout {

    private Context mContext;
    private FlowLayout flowLayout;

    private CallBackListener listener;

    public void setCallBackListener (CallBackListener listener) {
        this.listener = listener;
    }

    public CommonTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.layout_flow, this);

        // 获取控件
        flowLayout = findViewById(R.id.flowLayout);
    }

    public void setList (List<String> tagNames) {
        flowLayout.removeAllViews();
        for (int i = 0; i < tagNames.size(); i++) {
            TextView textView = (TextView) View.inflate(mContext,  R.layout.item_common_tag, null);
            textView.setText(tagNames.get(i));
            flowLayout.addView(textView);

            textView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onConfirm(textView.getText().toString());
                }

            });
        }
    }

    public interface CallBackListener {
        void onConfirm(String text);
    }
}
