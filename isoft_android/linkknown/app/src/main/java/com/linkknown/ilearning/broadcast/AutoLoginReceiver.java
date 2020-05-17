package com.linkknown.ilearning.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

        setPermissionType(dialog);

        // true 代表点击空白可消失,false代表点击空白哦不可消失,不能点击外边和返回键取消对话框
        dialog.setCancelable(false);

        dialog.show();
    }

    private void setPermissionType(AlertDialog dialog) {
        //只有这样才能弹框
        // 需要把对话框的类型设为 TYPE_SYSTEM_ALERT,否则对话框无法在广播接收器里弹出
        if (Build.VERSION.SDK_INT >= 26) {    // 8.0 新特性
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }else{
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
    }
}
