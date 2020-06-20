package com.linkknown.ilearning.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.linkknown.ilearning.activity.dialog.DialogAutoRefreshTokenActivity;
import com.linkknown.ilearning.activity.dialog.DialogUserLoginConfirmActivity;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.RefreshTokenResponse;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

// 401 状态码登录未授权广播
public class UnAuthorizedLoginReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
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
                            showRefreshTokenDialog(context);
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
        Intent intent = new Intent(context, DialogUserLoginConfirmActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void showRefreshTokenDialog(Context context) {
        Intent intent = new Intent(context, DialogAutoRefreshTokenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
