package com.linkknown.ilearning.popup;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.DisplayUtil;

import org.apache.commons.lang3.StringUtils;

/**
 * 封装的底部快捷编辑对话框
 */
public class BottomQuickEidtDialog {

    private Context mContext;
    private LinstenerCallback linstener;

    private Dialog dialog;
    private AppCompatEditText textView;
    private AppCompatButton button;

    public BottomQuickEidtDialog (Context mContext, LinstenerCallback linstener) {
        this.mContext = mContext;
        this.linstener = linstener;
        // 创建底部对话框
        dialog = new Dialog(mContext, R.style.BottomDialog);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_content_comment, null);
        dialog.setContentView(contentView);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = mContext.getResources().getDisplayMetrics().widthPixels - DisplayUtil.dp2px(mContext, 16f);
        params.bottomMargin = DisplayUtil.dp2px(mContext, 8f);
        contentView.setLayoutParams(params);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        dialog.show();

        initView();

        // 注册事件监听
        registerLinstener();
    }

    private void initView () {
        textView = dialog.getWindow().findViewById(R.id.commentText);
        button = dialog.getWindow().findViewById(R.id.commentSubmitBtn);
    }

    private void registerLinstener () {
        // 输入框添加监听
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                button.setEnabled(StringUtils.isNotEmpty(StringUtils.trim(textView.getText().toString())));
            }
        });
        // 提交按钮添加监听
        button.setOnClickListener(v -> {
            String content = StringUtils.trim(textView.getText().toString());
            if (StringUtils.isNotEmpty(content)) {
                linstener.handleSubmit(content);
            }
        });
    }

    public static interface LinstenerCallback {
        void handleSubmit(String text);
    }

    public void dismiss () {
        dialog.dismiss();
    }
}
