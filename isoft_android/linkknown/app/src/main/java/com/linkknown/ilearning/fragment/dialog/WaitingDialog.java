package com.linkknown.ilearning.fragment.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.linkknown.ilearning.R;

/**
 * 请稍后加载对话框
 */
public class WaitingDialog extends DialogFragment {

    private static final String TAG = "WaitingDialogFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //点击外部不可取消
        setCancelable(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));  // 无背景色
        window.requestFeature(Window.FEATURE_NO_TITLE);  // 无标题栏
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_waiting, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置对话框属性
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.0f; // 背景透明度: 全透明
        window.setAttributes(windowParams);
    }

    /**
     * Activity中显示加载对话框
     *
     * @param activity
     */
    public void showDialog(AppCompatActivity activity) {
        if (isAdded()) return;
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        show(activity.getSupportFragmentManager(), TAG);
    }

    /**
     * Fragment中显示加载对话框
     *
     * @param fragment
     */
    public void showDialog(Fragment fragment) {
        if (isAdded()) return;
        show(fragment.getChildFragmentManager(), TAG);
    }

    /**
     * 隐藏对话框
     */
    public void dismissDialog() {
        if (isAdded()) dismiss();
    }
}
