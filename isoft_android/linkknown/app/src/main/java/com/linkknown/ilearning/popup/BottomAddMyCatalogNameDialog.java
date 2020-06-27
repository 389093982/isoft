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
import com.linkknown.ilearning.util.StringUtilEx;
import com.linkknown.ilearning.util.ui.ToastUtil;

import org.apache.commons.lang3.StringUtils;

/**
 * 添加博客分类专用
 */
public class BottomAddMyCatalogNameDialog {

    private Context mContext;
    private LinstenerCallback linstener;

    private Dialog dialog;
    private AppCompatEditText catalog_name;
    private AppCompatEditText catalog_desc;
    private AppCompatButton submitBtn;

    public BottomAddMyCatalogNameDialog(Context mContext, LinstenerCallback linstener) {
        this.mContext = mContext;
        this.linstener = linstener;
        // 创建底部对话框
        dialog = new Dialog(mContext, R.style.BottomDialog);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_my_catalog_name, null);
        dialog.setContentView(contentView);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = mContext.getResources().getDisplayMetrics().widthPixels - DisplayUtil.dp2px(mContext, 8f);
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
        catalog_name = dialog.getWindow().findViewById(R.id.catalog_name);
        catalog_desc = dialog.getWindow().findViewById(R.id.catalog_desc);
        submitBtn = dialog.getWindow().findViewById(R.id.submitBtn);
    }

    private void registerLinstener () {
        // 输入框添加监听
        catalog_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String _catalogName = StringUtils.trim(catalog_name.getText().toString());
                String _datalogDesc = StringUtils.trim(catalog_desc.getText().toString());
                if (StringUtilEx.isAllNotEmpty(_catalogName,_datalogDesc)){
                    submitBtn.setEnabled(true);
                }
            }
        });

        // 输入框添加监听
        catalog_desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String _catalogName = StringUtils.trim(catalog_name.getText().toString());
                String _datalogDesc = StringUtils.trim(catalog_desc.getText().toString());
                if (StringUtilEx.isAllNotEmpty(_catalogName,_datalogDesc)){
                    submitBtn.setEnabled(true);
                }
            }
        });

        // 提交按钮添加监听
        submitBtn.setOnClickListener(v -> {
            String _catalog_name = StringUtils.trim(catalog_name.getText().toString());
            String _catalog_desc = StringUtils.trim(catalog_desc.getText().toString());

            if (_catalog_name.length()>50){
                ToastUtil.showText(mContext,"分类名称长度不能超过50");
                return;
            }
            if (_catalog_name.contains("官方")){
                ToastUtil.showText(mContext,"分类名称不能含有 ‘官方’");
                return;
            }
            if (_catalog_name.contains("热门博客")){
                ToastUtil.showText(mContext,"分类名称不能含有‘热门博客’");
                return;
            }
            if (_catalog_desc.length()>200){
                ToastUtil.showText(mContext,"分类描述长度不能超过200");
                return;
            }

            if (StringUtilEx.isAllNotEmpty(_catalog_name,_catalog_desc)) {
                linstener.handleSubmit(_catalog_name,_catalog_desc);
            }
        });
    }

    public static interface LinstenerCallback {
        void handleSubmit(String _catalog_name,String _catalog_desc);
    }

    public void dismiss () {
        dialog.dismiss();
    }
}
