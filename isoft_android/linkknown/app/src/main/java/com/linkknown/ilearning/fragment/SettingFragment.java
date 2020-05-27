package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.CacheDataUtil;

public class SettingFragment extends PreferenceFragmentCompat {

    private Context mContext;

    public static final String PREFERENCE_CLEAR_CACHE = "clearCache";
    // 清理缓存前
    public static final int CLEAR_CACHE_BEFORE = 0;
    // 清理缓存后
    public static final int CLEAR_CACHE_AFTER = 1;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_base, rootKey);

        mContext = getContext();

        initCacheDataSize(0);

        findPreference(PREFERENCE_CLEAR_CACHE).setOnPreferenceClickListener(preference -> {
            initCacheDataSize(1);
            CacheDataUtil.clearAllCache(mContext);
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
