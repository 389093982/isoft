package com.linkknown.ilearning.util;

import java.util.concurrent.atomic.AtomicLong;

public class SecurityUtil {

    // 两次点击按钮之间的点击间隔
    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static final AtomicLong LAST_CLICK_TIME = new AtomicLong(0);

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - LAST_CLICK_TIME.get()) <= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        LAST_CLICK_TIME.set(curClickTime);

        return flag;
    }
}
