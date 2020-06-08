package com.linkknown.ilearning;

import java.util.Arrays;
import java.util.List;

public class Constants {

    // 新安装应用时, 申请权限
    public static final int REQUEST_CODE_ASK_PERMISSIONS = 999;

    // 每页数量
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_SIZE2 = 15;

    public static final String COURSE_HOT_RECOMMEND_PREFIX = "COURSE_HOT_RECOMMEND_PREFIX_";
    public static final String COURSE_CUSTOM_TAG_PREFIX = "COURSE_CUSTOM_TAG_PREFIX_";

    // 课程收藏类型
    public static final String FAVORITE_TYPE_COURSE_COLLECT = "course_collect";
    public static final String FAVORITE_TYPE_COURSE_PRIASE = "course_priase";

    // 用户信息相关
    public static final String USER_SHARED_PREFERENCES = "USER_SHARED_PREFERENCES";
    public static final String USER_SHARED_PREFERENCES_USER_NAME = "USER_SHARED_PREFERENCES_USER_NAME";
    public static final String USER_SHARED_PREFERENCES_PASSWD = "USER_SHARED_PREFERENCES_PASSWD";
    public static final String USER_SHARED_PREFERENCES_TOKEN_STRING = "USER_SHARED_PREFERENCES_TOKEN_STRING";
    public static final String USER_SHARED_PREFERENCES_USER_NICK_NAME = "USER_SHARED_PREFERENCES_USER_NICK_NAME";
    public static final String USER_SHARED_PREFERENCES_USER_INFO = "USER_SHARED_PREFERENCES_USER_INFO";
    public static final String USER_SHARED_PREFERENCES_USER_HEADER_ICON = "USER_SHARED_PREFERENCES_USER_HEADER_ICON";
    public static final String USER_SHARED_PREFERENCES_IS_LOGIN = "USER_SHARED_PREFERENCES_IS_LOGIN";
    public static final String USER_SHARED_PREFERENCES_ROLE_NAME = "USER_SHARED_PREFERENCES_ROLE_NAME";
    public static final String USER_SHARED_PREFERENCES_EXPIRED_TIME = "USER_SHARED_PREFERENCES_EXPIRED_TIME";

    // 搜索历史
    public static final String SEARCH_HISTORY_SHARED_PREFERENCES = "SEARCH_HISTORY";
    public static final String SEARCH_HISTORY_SHARED_PREFERENCES_KEYWORDS = "SEARCH_HISTORY__SHARED_PREFERENCES_KEYWORDS";

    public static final String USER_NAME = "USER_NAME";

    public static final int NEWS_CHANNEL_ENABLE = 1;
    public static final int NEWS_CHANNEL_DISABLE = 0;

    public static final String TIP_SYSTEM_ERROR = "系统繁忙，请稍后重试~";

    // 刷新 tokenString 对话框显示时长,单位秒
    public static final int REFRESH_TOKEN_DIALOG_SHOW_TIME = 4;

    public static final String REFRESH_TOKEN_LOGIN_SUCCESS = "登录成功";

    public static final String COURSE_PLAY_NO_COURSE_NUM_TIP = "暂无剧情，敬请期待~";

    // ----------------- 占位符 --------------
    // 首页热门课程分类
    public static final String PLACEMENT_HOT_COURSE_TYPE_CAROUSEL = "placement_host_course_type_carousel";

    public static final List<String> COURSE_SEARCH_HINT_LIST = Arrays.asList("搜索课程名称或者视频关键词进行搜索", "学习如逆水行舟不进则退", "书读百遍其义自见", "每日坚持学习做最好的自己");
}
