package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceViewHolder;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.LoginActivity;
import com.linkknown.ilearning.util.CacheDataUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

public class SettingFragment extends PreferenceFragmentCompat {

    private Context mContext;
    private Handler handler = new Handler();

    public static final String PREFERENCE_CLEAR_CACHE = "clearCache";
    public static final String PREFERENCE_USER_AGREEMENT = "userAgreement";
    public static final String PREFERENCE_LOGOUT = "logout";
    // 清理缓存前
    public static final int CLEAR_CACHE_BEFORE = 0;
    // 清理缓存后
    public static final int CLEAR_CACHE_AFTER = 1;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_base, rootKey);

        mContext = getContext();

        initCacheDataSize(CLEAR_CACHE_BEFORE);

        findPreference(PREFERENCE_CLEAR_CACHE).setOnPreferenceClickListener(preference -> {
            // 先清理重新检测存储空间
            CacheDataUtil.clearAllCache(mContext);
            initCacheDataSize(CLEAR_CACHE_AFTER);
            return true;
        });

        findPreference(PREFERENCE_LOGOUT).setOnPreferenceClickListener(preference -> {
            if (LoginUtil.checkHasLogin(mContext)) {
                LoginUtil.logout(mContext);
                UIUtils.showMessageForSnackBar(mContext, getView(), "账号退出成功！");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UIUtils.gotoActivity(mContext, LoginActivity.class);
                    }
                }, 3000);
            }
            return true;
        });
    }

    private void initCacheDataSize (int pattern) {
        if (pattern == CLEAR_CACHE_BEFORE) {
            findPreference(PREFERENCE_CLEAR_CACHE).setSummary(String.format("已使用空间 %s", CacheDataUtil.getTotalCacheSize(mContext)));
        } else if (pattern == CLEAR_CACHE_AFTER) {
            findPreference(PREFERENCE_CLEAR_CACHE).setSummary(String.format("清理完成，使用空间 %s", CacheDataUtil.getTotalCacheSize(mContext)));
        }
    }
}
