package com.linkknown.ilearning.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.RefreshTokenResponse;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

// 自动登录广播
public class AutoLoginReceiver extends BroadcastReceiver {

    private Handler handler = new Handler();
    private long startTime;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 记录开始时间
        startTime = System.currentTimeMillis();
        if (LoginUtil.checkCanRefreshToken(context)) {
            refreshTokenDialog(context);
        } else {
            redirectToLoginConfirmDialog(context);
        }
    }

    private void refreshTokenDialog(Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.LinkKnownDialog);
        alertDialog.setView(R.layout.dialog_user_refreshtoken);
        AlertDialog dialog = alertDialog.create();

        setPermissionType(dialog);

        dialog.setCancelable(false);

        dialog.show();

        // 定时修改顶部 tip ... 动画效果,一个点二个点三个点逐渐显示
        this.intervalChangeHeaderTip(dialog);

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
                        }
                        dismissDelay(dialog);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("refreshToken =>", "refreshToken error");
                        dismissDelay(dialog);

                        ToastUtil.showText(context, "服务器异常，请稍后重试~");
                        // 延迟一秒再弹出去登录对话框
//                        redirectToLoginConfirmDialog(context);
                    }

                    private void dismissDelay(AlertDialog dialog) {
                        handler.postDelayed(() -> {
                            // 对话框至少保持 4 s 再消失
                            dialog.dismiss();
                        }, getDelayTime());
                    }

                    private long getDelayTime() {
                        long endTime = System.currentTimeMillis();
                        return endTime - startTime > Constants.REFRESH_TOKEN_DIALOG_SHOW_TIME * 1000 ? 10 : Constants.REFRESH_TOKEN_DIALOG_SHOW_TIME * 1000 - (endTime - startTime);
                    }
                });
    }

    // 修改对话框顶部三个点显示
    private void intervalChangeHeaderTip(AlertDialog dialog) {
        final AtomicInteger number = new AtomicInteger(0);

        /**
         * CountDownTimer timer = new CountDownTimer(3000, 1000)中，
         * 第一个参数表示总时间，第二个参数表示间隔时间。
         * 意思就是每隔一秒会回调一次方法onTick，然后1秒之后会回调onFinish方法。
         */
        CountDownTimer timer = new CountDownTimer(Constants.REFRESH_TOKEN_DIALOG_SHOW_TIME * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                TextView refreshTokenHeaderText = dialog.findViewById(R.id.refreshTokenHeaderText);

                SpannableString spannableText = new SpannableString(refreshTokenHeaderText.getText().toString());
                int start = refreshTokenHeaderText.getText().toString().indexOf(".");   // 第一个点的索引位置
                start = start + (number.get() % 4);                                     // 实际隐藏点的起始位置
                int end = refreshTokenHeaderText.length();
                // 设置需要隐藏点颜色为透明色
                spannableText.setSpan(new ForegroundColorSpan(UIUtils.getColorWithAlpha(0.01f, Color.WHITE)), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                refreshTokenHeaderText.setText(spannableText);
                number.set(number.get() + 1);
            }

            public void onFinish() {

            }
        };
        //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
        timer.start();
    }


    private void redirectToLoginConfirmDialog(Context context) {
        /**
         * 对话框弹框以下错误解决方案：
         * Caused by: java.lang.IllegalStateException: You need to use a Theme.AppCompat theme (or descendant) with this activity.
         * <style name="LinkKnownDialog"  parent="Theme.AppCompat.Light.Dialog.Alert">
         */
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.LinkKnownDialog);
        alertDialog.setView(R.layout.dialog_user_loginconfirm);
        AlertDialog dialog = alertDialog.create();

        setPermissionType(dialog);

        // true 代表点击空白可消失,false代表点击空白哦不可消失,不能点击外边和返回键取消对话框
        dialog.setCancelable(false);

        dialog.show();

        View btnCancel = dialog.findViewById(R.id.btn_cancel);
        View btnSubmit = dialog.findViewById(R.id.btn_submit);

        // 点击关闭对话框
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnSubmit.setOnClickListener(v -> dialog.dismiss());
    }

    private void setPermissionType(AlertDialog dialog) {
        //只有这样才能弹框
        // 需要把对话框的类型设为 TYPE_SYSTEM_ALERT,否则对话框无法在广播接收器里弹出
        if (Build.VERSION.SDK_INT >= 26) {    // 8.0 新特性
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        } else {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
    }

}
