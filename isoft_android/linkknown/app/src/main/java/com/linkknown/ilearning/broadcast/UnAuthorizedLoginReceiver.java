package com.linkknown.ilearning.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.linkknown.ilearning.util.LoginUtil;

// 401 状态码登录未授权广播
public class UnAuthorizedLoginReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        LoginUtil.showLoginOrAutoLoginDialog(context);
    }
}
