package com.linkknown.ilearning.util;

import org.apache.commons.lang3.StringUtils;

// 字符串工具类扩展
public class StringUtilEx {

    public static String getFirstNotEmptyStr (String ...strs) {
        for (String str : strs) {
            if (StringUtils.isNotEmpty(str)) {
                return str;
            }
        }
        return "";
    }
}
