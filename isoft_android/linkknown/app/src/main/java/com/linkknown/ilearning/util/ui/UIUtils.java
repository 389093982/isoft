package com.linkknown.ilearning.util.ui;

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
import com.linkknown.ilearning.R;

public class UIUtils {

    public static void gotoActivity (Context ctx, Class clazz){
        Intent intent = new Intent();
        intent.setClass(ctx, clazz);
        ctx.startActivity(intent);
    }

    public static void gotoActivity (Context ctx, Class clazz, IntentParamWrapper wrapper){
        Intent intent = new Intent();
        intent.setClass(ctx, clazz);
        intent = wrapper.wrapper(intent);
        ctx.startActivity(intent);
    }

    public interface IntentParamWrapper {
        Intent wrapper (Intent intent);
    }

    public static String replaceMediaUrl (String url) {
        return url.replace("localhost", "192.168.1.4");
    }

    public static void setImage (Context context, ImageView imageView, String imageUrl){
        Glide.with(context)
                .load(UIUtils.replaceMediaUrl(imageUrl))
                .apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.error_image))
                .into(imageView);
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

}
