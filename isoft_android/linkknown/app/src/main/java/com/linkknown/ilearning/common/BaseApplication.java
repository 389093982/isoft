package com.linkknown.ilearning.common;

import android.app.Application;
import android.content.Context;

// 主要是为了在工具类中方便获取全局 application 级别的 Context
// 需要在清单文件中的 <application下加一个属性 android:name=".BaseApplication"
// 这样就可以在工具类中获取context了
public class BaseApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

}
