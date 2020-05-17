package com.linkknown.ilearning.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

public class LoginUtil {

    public static void logout (Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences(Constants.USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
    }

    public static boolean isLoginUserName(Context mContext, String userName) {
        SharedPreferences preferences = mContext.getSharedPreferences(Constants.USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String userName0 = preferences.getString(Constants.USER_SHARED_PREFERENCES_USER_NAME, "");
        return StringUtils.equals(userName, userName0);
    }

    public static String getLoginUserName (Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences(Constants.USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_SHARED_PREFERENCES_USER_NAME, "");
    }

    public static boolean checkHasLogin (Context mContext) {
        return StringUtils.isNotEmpty(getLoginUserName (mContext));
    }

    public static void showLoginOrAutoLoginDialog (Context context, ConfirmDialogCallback callback) {
        // 弹出对话框前去登录
        showLoginConfirmDialog(context, callback);
        // 自动登录

    }

    /**
     * 显示弹出框
     */
    public static void showLoginConfirmDialog(final Context context, final ConfirmDialogCallback callback) {
        // 获取WindowManager
        final WindowManager mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 类型
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // 设置flag
        params.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSPARENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;

        final View mView = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.dialog_login_confirm, null);
        TextView dialog_tip = (TextView) mView.findViewById(R.id.dialog_tip);
        TextView dialog_btn_ok = (TextView) mView.findViewById(R.id.dialog_btn_ok);
        TextView dialog_btn_close = (TextView) mView.findViewById(R.id.dialog_btn_close);

        dialog_btn_ok.setText("重新登录");
        dialog_btn_close.setText("退出登录");
        dialog_tip.setText("该账户在其他设备登录,若不是您在操作,请及时修改密码以防泄露信息");
        dialog_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐藏弹窗
                mWindowManager.removeView(mView);
                callback.onPositive();
            }
        });

        dialog_btn_close.setOnClickListener(v -> {
            mWindowManager.removeView(mView);
            callback.onNegative();
        });

        mWindowManager.addView(mView, params);

    }

    public static interface ConfirmDialogCallback {
        void onPositive();
        void onNegative();
    }
}
