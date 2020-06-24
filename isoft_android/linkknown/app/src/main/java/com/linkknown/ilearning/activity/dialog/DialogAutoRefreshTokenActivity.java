package com.linkknown.ilearning.activity.dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

public class DialogAutoRefreshTokenActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        // 去除 title
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        //顶部设置为透明
        UIUtils.setTopTransparent(this);

        setContentView(R.layout.dialog_auto_refreshtoken);

        initView();
    }

    private void initView() {
        TextView refreshTokenHeaderText = findViewById(R.id.refreshTokenHeaderText);

        // 定时修改顶部 tip ... 动画效果,一个点二个点三个点逐渐显示
        intervalChangeHeaderTip(refreshTokenHeaderText);

        ImageView headerIcon = findViewById(R.id.headerIcon);

        // 替换默认头像
        UIUtils.setImage(mContext, headerIcon, LoginUtil.getHeaderIcon(mContext));

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_anim);
        // 给动画设置匀速
        LinearInterpolator interpolator = new LinearInterpolator();
        animation.setInterpolator(interpolator);
        // 为头像设置旋转动画，setAnimation 只执行一次，startAnimation 动画执行多次
        headerIcon.setAnimation(animation);

        // 延时关闭对话框
        dismissDelay(refreshTokenHeaderText);

    }

    // 修改对话框顶部三个点显示
    private static void intervalChangeHeaderTip(TextView refreshTokenHeaderText) {
        final AtomicInteger number = new AtomicInteger(0);

        /**
         * CountDownTimer timer = new CountDownTimer(3000, 1000)中，
         * 第一个参数表示总时间，第二个参数表示间隔时间。
         * 意思就是每隔一秒会回调一次方法onTick，然后1秒之后会回调onFinish方法。
         */
        CountDownTimer timer = new CountDownTimer(Constants.REFRESH_TOKEN_DIALOG_SHOW_TIME * 1000, 800) {
            public void onTick(long millisUntilFinished) {
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
    private void dismissDelay(TextView refreshTokenHeaderText) {
        Handler handler = new Handler();

        // 延迟 getDelayTime() 毫秒后设置文字 tip 为登录成功！，再过 1 秒后关闭对话框
        handler.postDelayed(() -> {
            refreshTokenHeaderText.setText(Constants.REFRESH_TOKEN_LOGIN_SUCCESS);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 对话框至少保持 1 s 再消失
                    finish();
                }
            }, 1000);
        }, Constants.REFRESH_TOKEN_DIALOG_SHOW_TIME * 1000);
    }


    @Override
    public void finish() {
        super.finish();

        // activity 退出动画
        this.overridePendingTransition(0,0);
    }
}
