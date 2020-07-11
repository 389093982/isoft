package com.linkknown.ilearning.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 参数校验工具类
public class CheckParamUtil {

    public static final String REGEX_EMAIL = "^([a-zA-Z]|[0-9])(\\w|\\-)+@[a-zA-Z0-9]+\\.([a-zA-Z]{2,4})$";
    public static final String REGEX_PHONE = "^[1][3,4,5,7,8][0-9]{9}$";
    public static final String REGEX_PASSWD = "^[a-zA-Z0-9]{6,20}$";

    public static boolean checkRegex (String checkStr, String regex) {
        if (StringUtils.isNotEmpty(checkStr)) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(checkStr);
            return m.matches();
        }
        return false;
    }

}
