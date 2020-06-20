package com.linkknown.ilearning.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.LoginActivity;
import com.linkknown.ilearning.activity.dialog.DialogAutoRefreshTokenActivity;
import com.linkknown.ilearning.activity.dialog.DialogUserLoginConfirmActivity;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.LoginUserResponse;
import com.linkknown.ilearning.model.RefreshTokenResponse;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.lxj.xpopup.XPopup;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class LoginUtil {

    public static boolean isNotUnauthorized (Throwable e) {
        return !isUnauthorized(e);
    }

    private static boolean isUnauthorized (Throwable e) {
        if (e instanceof HttpException) {
            int code = ((HttpException) e).code();
            return code == 401;
        }
        return false;
    }

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

    public static SharedPreferences getUserInfoSharedPreferences(Context mContext) {
        return mContext.getSharedPreferences(Constants.USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    // 获取登录过期时间
    private static long getExpiredTime(Context mContext) {
        SharedPreferences preferences = getUserInfoSharedPreferences(mContext);
        return preferences.getLong(Constants.USER_SHARED_PREFERENCES_EXPIRED_TIME, -1);
    }

    // 记住账号、密码和登录成功后的 tokenString
    // 注册时记住账号没有 tokenString，登录成功后记住账号有 tokenString
    public static void memoryAccount(Context mContext, String userName, String passwd, LoginUserResponse loginUserResponse) {
        SharedPreferences preferences = getUserInfoSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.USER_SHARED_PREFERENCES_USER_NAME, userName);
        editor.putString(Constants.USER_SHARED_PREFERENCES_PASSWD, passwd);
        editor.putString(Constants.USER_SHARED_PREFERENCES_USER_INFO, new Gson().toJson(loginUserResponse));

        if (loginUserResponse != null && loginUserResponse.isSuccess()) {
            editor.putString(Constants.USER_SHARED_PREFERENCES_TOKEN_STRING, loginUserResponse.getTokenString());
            editor.putString(Constants.USER_SHARED_PREFERENCES_USER_NICK_NAME, loginUserResponse.getNickName());
            editor.putString(Constants.USER_SHARED_PREFERENCES_USER_HEADER_ICON, loginUserResponse.getHeaderIcon());
            editor.putString(Constants.USER_SHARED_PREFERENCES_IS_LOGIN, "isLogin");
            editor.putString(Constants.USER_SHARED_PREFERENCES_ROLE_NAME, loginUserResponse.getRoleName());
            // 过期时间,毫秒数
            long expiredTime = new Date().getTime() + loginUserResponse.getExpireSecond() * 1000;
            editor.putLong(Constants.USER_SHARED_PREFERENCES_EXPIRED_TIME, expiredTime);
        }
        editor.apply();
    }

    public static void memoryRefreshToken(Context mContext, String tokenString, long expireSecond) {
        SharedPreferences preferences = getUserInfoSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.USER_SHARED_PREFERENCES_TOKEN_STRING, tokenString);
        // 过期时间,毫秒数
        long expiredTime = new Date().getTime() + expireSecond * 1000;
        editor.putLong(Constants.USER_SHARED_PREFERENCES_EXPIRED_TIME, expiredTime);
        editor.apply();
    }

    // 获取登录后的 tokenString
    public static String getTokenString (Context mContext) {
        SharedPreferences preferences = getUserInfoSharedPreferences(mContext);
        return preferences.getString(Constants.USER_SHARED_PREFERENCES_TOKEN_STRING, "");
    }

    // 注销登出时只清除 tokenString,而不清除账号和密码
    public static void logout(Context mContext) {
        SharedPreferences preferences = getUserInfoSharedPreferences(mContext);
        preferences.edit().remove(Constants.USER_SHARED_PREFERENCES_TOKEN_STRING).apply();
    }

    public static boolean isLoginUserName(Context mContext, String userName) {
        SharedPreferences preferences = getUserInfoSharedPreferences(mContext);
        String userName0 = preferences.getString(Constants.USER_SHARED_PREFERENCES_USER_NAME, "");
        return checkHasLogin(mContext) && StringUtils.equals(userName, userName0);
    }

    public static String getHeaderIcon(Context mContext) {
        SharedPreferences preferences = getUserInfoSharedPreferences(mContext);
        return preferences.getString(Constants.USER_SHARED_PREFERENCES_USER_HEADER_ICON, "");
    }

    public static LoginUserResponse getLoginUserInfo(Context mContext) {
        SharedPreferences preferences = getUserInfoSharedPreferences(mContext);
        return new Gson().fromJson(preferences.getString(Constants.USER_SHARED_PREFERENCES_USER_INFO, ""), LoginUserResponse.class);
    }

    public static String getLoginNickName(Context mContext) {
        SharedPreferences preferences = getUserInfoSharedPreferences(mContext);
        return preferences.getString(Constants.USER_SHARED_PREFERENCES_USER_NICK_NAME, "");
    }

    public static String getLoginUserName(Context mContext) {
        SharedPreferences preferences = getUserInfoSharedPreferences(mContext);
        return preferences.getString(Constants.USER_SHARED_PREFERENCES_USER_NAME, "");
    }

    // 判断是否登录,需要判断用户名是否有效和 tokenString 是否有效
    public static boolean checkHasLogin(Context mContext) {
        return StringUtils.isNotEmpty(getLoginUserName(mContext)) && StringUtils.isNotEmpty(getTokenString(mContext))
                && !checkHasExpired(mContext);
    }

    public static void showLoginOrAutoLoginDialog(Context context) {
        if (LoginUtil.checkCanRefreshToken(context)) {
            // 弹出对话框前去登录
            showRefreshTokenDialog(context);
        } else {
            // 自动刷新 tokenString，成功后显示弹框动画，不成功提示
            showRedirectLoginConfirmDialog(context);
        }
    }

    public static void showRedirectLoginConfirmDialog(Context context) {
        Intent intent = new Intent(context, DialogUserLoginConfirmActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showRefreshTokenDialog(Context context) {
        LinkKnownApiFactory.getLinkKnownApi().refreshToken(LoginUtil.getTokenString(context))
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<RefreshTokenResponse>() {
                    @Override
                    public void onNext(RefreshTokenResponse refreshTokenResponse) {
                        if (refreshTokenResponse.isSuccess()) {
                            String tokenString = refreshTokenResponse.getTokenString();
                            long expireSecond = refreshTokenResponse.getExpireSecond();
                            LoginUtil.memoryRefreshToken(context, tokenString, expireSecond);


                            // 登录成功，且登录信息存储成功后，弹出自动刷新过场动画
                            Intent intent = new Intent(context, DialogAutoRefreshTokenActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            ToastUtil.showText(context, "登录失败，请稍后重试~");
                        }
                    }

                    // 接口调用失败就不弹框了，直接提示就行
                    @Override
                    public void onError(Throwable e) {
                        Log.e("refreshToken =>", "refreshToken error");
                        ToastUtil.showText(context, "登录失败，请稍后重试~");
                    }
                });
    }

}
