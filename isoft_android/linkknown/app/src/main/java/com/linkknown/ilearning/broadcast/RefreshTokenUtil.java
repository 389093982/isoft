package com.linkknown.ilearning.broadcast;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

public class RefreshTokenUtil {

    public static void showRefreshTokenDialog (Context context, long startTime) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.LinkKnownDialog);
        alertDialog.setView(R.layout.dialog_user_refreshtoken);
        AlertDialog dialog = alertDialog.create();

        setPermissionType(dialog);

        dialog.setCancelable(false);

        dialog.show();

        // 定时修改顶部 tip ... 动画效果,一个点二个点三个点逐渐显示
        intervalChangeHeaderTip(dialog);

        ImageView headerIcon = dialog.findViewById(R.id.headerIcon);

        // 替换默认头像
        UIUtils.setImage(context, headerIcon, LoginUtil.getHeaderIcon(context));

        // 为头像设置旋转动画
        headerIcon.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_anim));

        // 延时关闭对话框
        dismissDelay(dialog, startTime);
    }

    public static void setPermissionType(AlertDialog dialog) {
        //只有这样才能弹框
        // 需要把对话框的类型设为 TYPE_SYSTEM_ALERT,否则对话框无法在广播接收器里弹出
        if (Build.VERSION.SDK_INT >= 26) {    // 8.0 新特性
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        } else {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
    }

    // 修改对话框顶部三个点显示
    private static void intervalChangeHeaderTip(AlertDialog dialog) {
        final AtomicInteger number = new AtomicInteger(0);

        /**
         * CountDownTimer timer = new CountDownTimer(3000, 1000)中，
         * 第一个参数表示总时间，第二个参数表示间隔时间。
         * 意思就是每隔一秒会回调一次方法onTick，然后1秒之后会回调onFinish方法。
         */
        CountDownTimer timer = new CountDownTimer(Constants.REFRESH_TOKEN_DIALOG_SHOW_TIME * 1000, 800) {
            public void onTick(long millisUntilFinished) {
                TextView refreshTokenHeaderText = dialog.findViewById(R.id.refreshTokenHeaderText);
                if (!StringUtils.equals(Constants.REFRESH_TOKEN_LOGIN_SUCCESS, refreshTokenHeaderText.getText().toString())){
                    SpannableString spannableText = new SpannableString(refreshTokenHeaderText.getText().toString());
                    int start = refreshTokenHeaderText.getText().toString().indexOf(".");   // 第一个点的索引位置
                    start = start + (number.get() % 4);                                     // 实际隐藏点的起始位置
                    int end = refreshTokenHeaderText.length();
                    // 设置需要隐藏点颜色为透明色
                    spannableText.setSpan(new ForegroundColorSpan(UIUtils.getColorWithAlpha(0.01f, Color.WHITE)), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    refreshTokenHeaderText.setText(spannableText);
                    number.set(number.get() + 1);
                }
            }

            public void onFinish() {

            }
        };
        //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
        timer.start();
    }

    // 延时关闭对话框,主要是为了体验,自动刷新过快时能让动画播放完
    private static void dismissDelay(AlertDialog dialog, long startTime) {
        Handler handler = new Handler();

        // 延迟 getDelayTime() 毫秒后设置文字 tip 为登录成功！，再过 1 秒后关闭对话框
        handler.postDelayed(() -> {
            TextView refreshTokenHeaderText = dialog.findViewById(R.id.refreshTokenHeaderText);
            refreshTokenHeaderText.setText(Constants.REFRESH_TOKEN_LOGIN_SUCCESS);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 对话框至少保持 1 s 再消失
                    dialog.dismiss();
                }
            }, 1000);
        }, getDelayTime(startTime));
    }

    private static long getDelayTime(long startTime) {
        long endTime = System.currentTimeMillis();
        return endTime - startTime > Constants.REFRESH_TOKEN_DIALOG_SHOW_TIME * 1000 ? 10 : Constants.REFRESH_TOKEN_DIALOG_SHOW_TIME * 1000 - (endTime - startTime);
    }
}
