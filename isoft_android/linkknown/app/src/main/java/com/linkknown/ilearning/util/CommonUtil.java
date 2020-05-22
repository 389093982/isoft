package com.linkknown.ilearning.util;

import android.os.Bundle;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class CommonUtil {

    public static List<String> splitCommonTag (String str) {
        str = StringUtils.replace(str, "|", "/");
        String[] arr = StringUtils.split(str, "/");
        return Arrays.asList(arr);
    }

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
}
