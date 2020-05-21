package com.linkknown.ilearning.util.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.linkknown.ilearning.BuildConfig;
import com.linkknown.ilearning.R;

import org.apache.commons.lang3.StringUtils;

public class UIUtils {

    public static void gotoActivity (Context ctx, Class clazz){
        gotoActivity(ctx, clazz, null);
    }

    public static void gotoActivity (Context ctx, Class clazz, IntentParamWrapper wrapper){
        Intent intent = new Intent();
        intent.setClass(ctx, clazz);
        if (wrapper != null) {
            intent = wrapper.wrapper(intent);
        }
        ctx.startActivity(intent);

        if (ctx instanceof Activity) {
            // Android中不同Activity之间的切换是不可避免的事情，那么怎么才能让Acitivity的切换更优雅呢，
            // Android中提供了一个方法来解决这个问题，即overridePendingTransition(A，B)函数
            ((Activity)ctx).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }

    public interface IntentParamWrapper {
        Intent wrapper (Intent intent);
    }

    public static String replaceMediaUrl (String url) {
        return url.replace("http://localhost:6001", BuildConfig.BASE_URL);
    }

    public static void setImage (Context context, ImageView imageView, String imageUrl){
        if (StringUtils.isNotEmpty(imageUrl)){
            Glide.with(context)
                    .load(UIUtils.replaceMediaUrl(imageUrl))
                    .apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.error_image))
                    .into(imageView);
        }
    }

    public static void setTextViewDrawbleImg (Context mContext, TextView textView, int id, int left, int top, int right, int bottom) {
        Drawable icon = ContextCompat.getDrawable(mContext, id);

        //setBounds(left,top,right,bottom)里的参数从左到右分别是
        //drawable的左边到textview左边缘+padding的距离，drawable的上边离textview上边缘+padding的距离
        //drawable的右边边离textview左边缘+padding的距离，drawable的下边离textview上边缘+padding的距离
        //所以right-left = drawable的宽，top - bottom = drawable的高
        icon.setBounds(left, top,right, bottom);
        textView.setCompoundDrawables(icon, null, null, null);
    }

    // SnackBar 显示消息
    public static void showMessageForSnackBar (View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }


    /**
     * 给color添加透明度
     * @param alpha 透明度 0f～1f
     * @param baseColor 基本颜色
     * @return
     */
    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

    public static int getGenderImageResource (String gender) {
        //设置用户性别
        switch (gender) {
            case "男":
            case "male":
                return R.drawable.ic_user_male;
            case "女":
            case "female":
                return R.drawable.ic_user_female;
            default:
                return R.drawable.ic_user_gay_border;
        }
    }

    public static int getVipLevelImageResource (int vip) {
        switch (vip) {
            case 1:
                return R.drawable.ic_lv1;
            case 2:
                return R.drawable.ic_lv2;
            case 3:
                return R.drawable.ic_lv3;
            case 4:
                return R.drawable.ic_lv4;
            case 5:
                return R.drawable.ic_lv5;
            case 6:
                return R.drawable.ic_lv6;
            case 7:
                return R.drawable.ic_lv7;
            case 8:
                return R.drawable.ic_lv8;
            case 9:
                return R.drawable.ic_lv9;
            default:
                return R.drawable.ic_lv0;
        }
    }

}
