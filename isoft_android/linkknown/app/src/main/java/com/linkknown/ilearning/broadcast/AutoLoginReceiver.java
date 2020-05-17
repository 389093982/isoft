package com.linkknown.ilearning.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

import com.linkknown.ilearning.R;

// 自动登录广播
public class AutoLoginReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /**
         * 对话框弹框以下错误解决方案：
         * Caused by: java.lang.IllegalStateException: You need to use a Theme.AppCompat theme (or descendant) with this activity.
         * <style name="LinkKnownDialog"  parent="Theme.AppCompat.Light.Dialog.Alert">
         */
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.LinkKnownDialog);
        alertDialog.setView(R.layout.dialog_login_confirm);
        AlertDialog dialog = alertDialog.create();
        // 需要把对话框的类型设为 TYPE_SYSTEM_ALERT,否则对话框无法在广播接收器里弹出
        // dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }
}
