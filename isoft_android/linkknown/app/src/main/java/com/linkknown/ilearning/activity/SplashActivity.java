package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.linkknown.ilearning.MainActivity;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.ui.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

// 动画简介 translate 位置转移动画效果
//    整型值:
//    fromXDelta 属性为动画起始时 X坐标上的位置
//    toXDelta   属性为动画结束时 X坐标上的位置
//    fromYDelta 属性为动画起始时 Y坐标上的位置
//    toYDelta   属性为动画结束时 Y坐标上的位置
//    注意:
//    没有指定，
//    默认是以自己为相对参照物
//    长整型值：
//    duration  属性为动画持续时间
//    说明: 时间以毫秒为单位
//    在这些属性里面还可以加上%和p,例如:
//    android:toXDelta="100%",表示自身的100%,也就是从View自己的位置开始。
//    android:toXDelta="80%p",表示父层View的80%,是以它父层View为参照的。
//    fillBefore是指动画结束时画面停留在此动画的第一帧。默认值为true
//    fillAfter是指动画结束是画面停留在此动画的最后一帧。默认值为false

// 开屏页，进入应用后的首页
// 主要用于预加载数据、展示一些广告和动画
public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.imageView)
    public ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        //动画1
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.text_splash_position);

        imageView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 动画结束后调往主页
                UIUtils.gotoActivity(getApplicationContext(), MainActivity.class);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
