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

    public static boolean isAllNotEmpty (String ... strs) {
        if (strs == null || strs.length == 0){
            return false;
        }
        for (String str : strs){
            if (StringUtils.isEmpty(str)) {
                return false;
            }
        }
        return true;
    }


    /*
     * Java文件操作 获取不带扩展名的文件名
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
}
