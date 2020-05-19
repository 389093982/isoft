package com.linkknown.ilearning.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class CommonUtil {

    public static List<String> splitCommonTag (String str) {
        str = StringUtils.replace(str, "|", "/");
        String[] arr = StringUtils.split(str, "/");
        return Arrays.asList(arr);
    }
}
