package com.linkknown.ilearning.util.ui;

import android.content.Context;
import android.content.Intent;

public class UIUtils {

    public static void gotoActivity (Context ctx, Class clazz){
        Intent intent = new Intent();
        intent.setClass(ctx, clazz);
        ctx.startActivity(intent);
    }
}
