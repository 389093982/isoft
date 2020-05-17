package com.linkknown.ilearning.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.LoginUserResponse;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class LoginUtil {

    // 判断过期时间是否是最近 3 小时之内
    public static boolean checkRecently (Context mContext) {
        return new Date().getTime() - getExpiredTime(mContext) <  3 * 3600 * 1000;
    }

    // 之前登录过，即有 tokenString
    // 在最近过期 N 小时内才可以自动登录
    public static boolean checkCanRefreshToken (Context mContext) {
        return StringUtils.isNotEmpty(getTokenString(mContext)) && checkRecently(mContext);
    }

    // 判断登录 tokenString 是否已经过期
    public static boolean checkHasExpired (Context mContext) {
        return new Date().getTime() >= getExpiredTime(mContext);
    }

    // 获取登录过期时间
    private static long getExpiredTime(Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences(Constants.USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getLong(Constants.USER_SHARED_PREFERENCES_EXPIRED_TIME, -1);
    }

    // 记住账号、密码和登录成功后的 tokenString
    // 注册时记住账号没有 tokenString，登录成功后记住账号有 tokenString
    public static void memoryAccount(Context mContext, String userName, String passwd, LoginUserResponse loginUserResponse) {
        SharedPreferences preferences = mContext.getSharedPreferences(Constants.USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.USER_SHARED_PREFERENCES_USER_NAME, userName);
        editor.putString(Constants.USER_SHARED_PREFERENCES_PASSWD, passwd);

        if (loginUserResponse != null && loginUserResponse.isSuccess()) {
            editor.putString(Constants.USER_SHARED_PREFERENCES_TOKEN_STRING, loginUserResponse.getTokenString());
            editor.putString(Constants.USER_SHARED_PREFERENCES_USER_NICK_NAME, loginUserResponse.getNickName());
            editor.putString(Constants.USER_SHARED_PREFERENCES_IS_LOGIN, "isLogin");
            editor.putString(Constants.USER_SHARED_PREFERENCES_ROLE_NAME, loginUserResponse.getRoleName());
            // 过期时间,毫秒数
            long expiredTime = new Date().getTime() + loginUserResponse.getExpireSecond() * 1000;
            editor.putLong(Constants.USER_SHARED_PREFERENCES_EXPIRED_TIME, expiredTime);
        }
        editor.apply();
    }

    // 获取登录后的 tokenString
    public static String getTokenString (Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences(Constants.USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_SHARED_PREFERENCES_TOKEN_STRING, "");
    }

    // 注销登出时只清除 tokenString,而不清除账号和密码
    public static void logout(Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences(Constants.USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().remove(Constants.USER_SHARED_PREFERENCES_TOKEN_STRING).apply();
    }

    public static boolean isLoginUserName(Context mContext, String userName) {
        SharedPreferences preferences = mContext.getSharedPreferences(Constants.USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String userName0 = preferences.getString(Constants.USER_SHARED_PREFERENCES_USER_NAME, "");
        return StringUtils.equals(userName, userName0);
    }

    public static String getLoginUserName(Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences(Constants.USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_SHARED_PREFERENCES_USER_NAME, "");
    }

    // 判断是否登录,需要判断用户名是否有效和 tokenString 是否有效
    public static boolean checkHasLogin(Context mContext) {
        return StringUtils.isNotEmpty(getLoginUserName(mContext)) && StringUtils.isNotEmpty(getTokenString(mContext));
    }

    public static void showLoginOrAutoLoginDialog(Context context, ConfirmDialogCallback callback) {
        // 弹出对话框前去登录
        showLoginConfirmDialog2(context, callback);
        // 自动登录

    }

    public static void showLoginConfirmDialog2(final Context context, final ConfirmDialogCallback callback) {
        new AlertDialog.Builder(context).setTitle("您还未登录，前去登录？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", (dialog, which) -> callback.onPositive())
                .setNegativeButton("返回", (dialog, which) -> callback.onNegative()).show();
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

        final View mView = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.dialog_user_loginconfirm, null);
        TextView dialog_tip = (TextView) mView.findViewById(R.id.dialog_tip);
        TextView btn_submit = (TextView) mView.findViewById(R.id.btn_submit);
        TextView btn_cancel = (TextView) mView.findViewById(R.id.btn_cancel);

        btn_submit.setText("重新登录");
        btn_cancel.setText("退出登录");
        dialog_tip.setText("该账户在其他设备登录,若不是您在操作,请及时修改密码以防泄露信息");
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐藏弹窗
                mWindowManager.removeView(mView);
                callback.onPositive();
            }
        });

        btn_cancel.setOnClickListener(v -> {
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
