package com.linkknown.ilearning.model.ui;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.FavoriteCountResponse;
import com.linkknown.ilearning.model.IsFavoriteResponse;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class CourseOperate {

    public static final String OPERATE_SHARE = "分享";
    public static final String OPERATE_PRIASE = "点赞";
    public static final String OPERATE_SHOU_CANG = "收藏";
    public static final String OPERATE_PLAY = "播放";

    // 操作名称
    private String operateName;
    // 操作图标
    private int operateIcon;
    // 操作数量
    private int operateNum;
    // 操作状态
    private int operateStatus;

    public static List<CourseOperate> getInitialCourseOperates () {
        List<CourseOperate> operates = new ArrayList<>();
        CourseOperate operate = new CourseOperate();
        operate.setOperateName(OPERATE_SHARE);
        operate.setOperateIcon(R.drawable.ic_share);
        operates.add(operate);

        operate = new CourseOperate();
        operate.setOperateName(OPERATE_PRIASE);
        operate.setOperateIcon(R.drawable.ic_priase);
        operates.add(operate);

        operate = new CourseOperate();
        operate.setOperateName(OPERATE_SHOU_CANG);
        operate.setOperateIcon(R.drawable.ic_shoucang);
        operates.add(operate);

        operate = new CourseOperate();
        operate.setOperateName(OPERATE_PLAY);
        operate.setOperateIcon(R.drawable.ic_play);
        operates.add(operate);
        return operates;
    }

    public static CourseOperate getCourseOperateByName (List<CourseOperate> operates, String operateName) {
        for (CourseOperate courseOperate: operates) {
            if (StringUtils.equals(courseOperate.getOperateName(), operateName)) {
                return courseOperate;
            }
        }
        return null;
    }

    @Data
    public static class CourseOperateResponseWrapper {
        // 收藏
        private IsFavoriteResponse collectIsFavoriteResponse;
        private FavoriteCountResponse collectFavoriteCountResponse;
        // 点赞
        private IsFavoriteResponse priaseIsFavoriteResponse;
        private FavoriteCountResponse priaseFavoriteCountResponse;
    }
}
