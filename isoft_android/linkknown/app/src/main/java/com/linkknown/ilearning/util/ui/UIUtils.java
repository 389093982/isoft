package com.linkknown.ilearning.util.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;

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
        return url.replace("localhost", "192.168.1.2");
    }

}
