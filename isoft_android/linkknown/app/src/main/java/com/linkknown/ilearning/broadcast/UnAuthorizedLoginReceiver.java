package com.linkknown.ilearning.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.LoginActivity;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.RefreshTokenResponse;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

// 401 状态码登录未授权广播
public class UnAuthorizedLoginReceiver extends BroadcastReceiver {

    private long startTime;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 记录开始时间
        startTime = System.currentTimeMillis();
        if (LoginUtil.checkCanRefreshToken(context)) {
            // 自动刷新 tokenString，成功后显示弹框动画，不成功提示
            autoRefreshToken(context);
        } else {
            // 弹框提示用户去登录
            showRedirectLoginConfirmDialog(context);
        }
    }

    private void autoRefreshToken(Context context) {
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
                            RefreshTokenUtil.showRefreshTokenDialog(context, startTime);
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

    private void showRedirectLoginConfirmDialog(Context context) {
        /**
         * 对话框弹框以下错误解决方案：
         * Caused by: java.lang.IllegalStateException: You need to use a Theme.AppCompat theme (or descendant) with this activity.
         * <style name="LinkKnownDialog"  parent="Theme.AppCompat.Light.Dialog.Alert">
         */
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.LinkKnownDialog);
        alertDialog.setView(R.layout.dialog_user_loginconfirm);
        AlertDialog dialog = alertDialog.create();

        RefreshTokenUtil.setPermissionType(dialog);

        // true 代表点击空白可消失,false代表点击空白哦不可消失,不能点击外边和返回键取消对话框
        dialog.setCancelable(false);

        dialog.show();

        View btnCancel = dialog.findViewById(R.id.btn_cancel);
        View btnSubmit = dialog.findViewById(R.id.btn_submit);

        // 点击关闭对话框
        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        btnSubmit.setOnClickListener(v -> {
            // 关闭对话框并前往登录页面
            dialog.dismiss();
            UIUtils.gotoActivity(context, LoginActivity.class);
        });
    }

}
