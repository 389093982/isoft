package com.linkknown.ilearning.util.ui;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static void showText (Context ctx, String text) {
        Toast toast = Toast.makeText(ctx, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
