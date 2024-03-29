package com.linkknown.ilearning.util.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.linkknown.ilearning.BuildConfig;
import com.linkknown.ilearning.R;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import io.reactivex.disposables.Disposable;

public class UIUtils {

    public static void disposeSecurity (Disposable ... disposables){
        for (Disposable disposable : disposables) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

    public static Animation loadAnimation (Context mContext, int animResId) {
        Animation animation = AnimationUtils.loadAnimation(mContext, animResId);
        return animation;
    }

    public static void gotoActivity (Context ctx, Class clazz){
        gotoActivity(ctx, clazz, null);
    }

    public static void gotoActivity (Context ctx, Class clazz, IntentParamWrapper wrapper){
        Intent intent = new Intent();
        intent.setClass(ctx, clazz);
        if (wrapper != null) {
            intent = wrapper.wrapper(intent);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        setImage(context, imageView, imageUrl, -1);
    }

    /**
     * radius 圆角图片角度
     * @param context
     * @param imageView
     * @param imageUrl
     * @param radius
     */
    public static void setImage (Context context, ImageView imageView, String imageUrl, int radius) {
        imageUrl = ObjectUtils.defaultIfNull(imageUrl, "");

        // 此处无需对 imageUrl 进行判空，否则会导致空图片地址无法设置复用的问题
        RequestOptions options;
        if (radius > 0){
            //设置图片圆角角度
            RoundedCorners roundedCorners = new RoundedCorners(radius);
            options = RequestOptions.bitmapTransform(roundedCorners);
        } else {
            options = new RequestOptions();
        }

        Glide.with(context)
                .load(UIUtils.replaceMediaUrl(imageUrl))
                .apply(options.placeholder(R.drawable.loading).error(R.drawable.not_found))
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
    public static void showMessageForSnackBar (Context mContext, View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        // 为Snackbar添加背景颜色
        snackbar.getView().setBackgroundColor(UIUtils.getResourceColor(mContext, R.color.colorPrimary));
        snackbar.show();
    }

    private static final int[] RANDOM_COLORS = new int[]{
            Color.parseColor("#90C5F0"),
            Color.parseColor("#91CED5"),
            Color.parseColor("#F88F55"),
            Color.parseColor("#C0AFD0"),
            Color.parseColor("#E78F8F"),
            Color.parseColor("#67CCB7"),
            Color.parseColor("#F6BC7E")
    };

    public static int getRandomResourceColor() {
        Random random = new Random();
        return RANDOM_COLORS[random.nextInt(RANDOM_COLORS.length)];
    }

    public static int getRandomResourceColor(int position) {
        return RANDOM_COLORS[position % RANDOM_COLORS.length];
    }

    // 根据资源目录下的颜色 id 获取颜色
    public static int getResourceColor(Context mContext, int colorId) {
        return ContextCompat.getColor(mContext, colorId);
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
                return R.drawable.ic_male;
            case "女":
            case "female":
                return R.drawable.ic_female;
            default:
                return R.drawable.ic_female;
        }
    }

    /**
     * 设置顶部为透明状态 -- 全透明
     * @param activity
     */
    public static void setTopTransparent(Activity activity){
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    };


}
