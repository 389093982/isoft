package com.linkknown.ilearning.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.MainActivity;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

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
public class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    // 权限申请相关
    private String PERMISSION_ALL_MSG = "请授予权限，否则影响部分使用功能";
    private String[] perms = {Manifest.permission.CAMERA};

    @BindView(R.id.businessView)
    public TextView businessView;
    @BindView(R.id.userAgreementView)
    public TextView userAgreementView;

    @BindView(R.id.imageView)
    public ImageView imageView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = this;
        ButterKnife.bind(this);

        initView();

        if (EasyPermissions.hasPermissions(this, perms)) {
            // 延迟一段时间前往主页
            gotoMainActivityDelay();
        } else {
            EasyPermissions.requestPermissions(this, PERMISSION_ALL_MSG, Constants.PERMISSION_ALL_CODE, perms);
        }
    }

    private void initView() {
        businessView.setOnClickListener(v -> {
            UIUtils.gotoActivity(mContext, BusinessActivity.class);
            finish();
        });
        userAgreementView.setOnClickListener(v -> {
            UIUtils.gotoActivity(mContext, UserAgreementActivity.class);
            finish();
        });

        //动画
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.text_splash_position);

        imageView.startAnimation(animation);
    }

    // 延迟一段时间前往主页
    private void gotoMainActivityDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UIUtils.gotoActivity(getApplicationContext(), MainActivity.class);
                finish();
            }
        }, 3000);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将请求结果传递EasyPermission库处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 申请成功时调用
     * @param requestCode 请求权限的唯一标识码
     * @param perms 一系列权限
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
//        ToastUtil.showText(this, "用户授权成功1");
    }

    /**
     * 申请拒绝时调用
     * @param requestCode 请求权限的唯一标识码
     * @param perms 一系列权限
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
//        ToastUtil.showText(this, "用户授权失败2");

        /**
         　　* 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
         　　* 这时候，需要跳转到设置界面去，让用户手动开启。
         　　*/
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //从设置页面返回，判断权限是否申请。
            if (EasyPermissions.hasPermissions(this, perms)) {
//                ToastUtil.showText(mContext, "权限申请成功3");
            } else {
//                ToastUtil.showText(mContext, "权限申请失败4");
            }
        }

        if (requestCode == Constants.PERMISSION_ALL_CODE) {
            gotoMainActivityDelay();
        }
    }

    @AfterPermissionGranted(Constants.PERMISSION_ALL_CODE)
    public void onPermissionSuccess(){
//        ToastUtil.showText(mContext, "权限申请成功,执行后续方法5");
        gotoMainActivityDelay();
    }

}
