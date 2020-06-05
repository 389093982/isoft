package com.linkknown.ilearning.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.model.Paginator;

import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CommonUtil {

    // 判断当前页是否是最后一页
    public static boolean isLastPage (Paginator paginator) {
        return paginator != null && paginator.getCurrpage() == paginator.getTotalpages();
    }

    // 根据分隔符分割自定义标签语
    public static List<String> splitCommonTag (String str) {
        str = StringUtils.replace(str, "|", "/");
        String[] arr = StringUtils.split(str, "/");
        return Arrays.asList(arr);
    }

    // 创建 Bundle 主要是用于页面跳转通信传参
    public static Bundle createBundle (String key, String value) {
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        return bundle;
    }

    public static Bundle createBundle2 (String key1, String value1, String key2, String value2) {
        Bundle bundle = new Bundle();
        bundle.putString(key1, value1);
        bundle.putString(key2, value2);
        return bundle;
    }

    // 记录搜索历史
    public static void recordSearchHistory (Context mContext, String searchText) {
        Set<String> history = getSearchHistory(mContext);
        if (!history.contains(searchText)) {
            // 先添加进尾部
            history.add(searchText);
        }
        // 超过 20 条搜索记录则删除最早一条
        if (history.size() > 20){
            Iterator<String> iterator = history.iterator();
            if (iterator.hasNext()){
                iterator.remove();
            }
        }
        // 重新存储起来
        SharedPreferences preferences = mContext.getSharedPreferences(Constants.SEARCH_HISTORY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(Constants.SEARCH_HISTORY_SHARED_PREFERENCES_KEYWORDS, history);
        editor.apply();
    }

    // 读取搜索历史
    public static Set<String> getSearchHistory (Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences(Constants.SEARCH_HISTORY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getStringSet(Constants.SEARCH_HISTORY_SHARED_PREFERENCES_KEYWORDS, new LinkedHashSet<>());
    }
}
