package com.linkknown.ilearning.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.linkknown.ilearning.util.ui.ToastUtil;

public class PermissionUtil {

    // 动态请求悬浮窗权限
    public static void requestOverlayPermission(Activity activity) {
        if (!Settings.canDrawOverlays(activity)){
            int sdkInt = Build.VERSION.SDK_INT;
            if (sdkInt >= Build.VERSION_CODES.O) {  // 8.0 以上
                ToastUtil.showText(activity.getApplicationContext(), "请打开显示悬浮窗开关!");

                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                activity.startActivityForResult(intent, 0);

            } else if (sdkInt >= Build.VERSION_CODES.M) {   // 6.0-8.0
                ToastUtil.showText(activity.getApplicationContext(), "请打开显示悬浮窗开关!");

                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                activity.startActivityForResult(intent, 0);
            }
        }
    }
}

