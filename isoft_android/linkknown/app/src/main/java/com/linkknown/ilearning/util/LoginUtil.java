package com.linkknown.ilearning.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.linkknown.ilearning.Constants;

import org.apache.commons.lang3.StringUtils;

public class LoginUtil {

    public static boolean isLoginUserName(Context mContext, String userName) {
        SharedPreferences preferences = mContext.getSharedPreferences(Constants.USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String userName0 = preferences.getString(Constants.USER_SHARED_PREFERENCES_USER_NAME, "");
        return StringUtils.equals(userName, userName0);
    }
}
