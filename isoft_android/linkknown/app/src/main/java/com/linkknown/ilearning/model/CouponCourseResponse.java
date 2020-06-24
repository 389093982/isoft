package com.linkknown.ilearning.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
public class CouponCourseResponse extends BaseResponse implements Serializable {

    private List<CouponCourseResponse.Course> courseList;
    private Paginator paginator;

    @Data
    @ToString
    public static class Course implements Serializable {
        private int comments;
        private String course_author;
        private String course_label;
        private String course_name;
        private int course_number;
        private String course_short_des;
        private String course_short_desc;
        private String course_sub_type;
        private String course_type;
        private String created_by;
        private String created_time;
        private String custom_tag;
        private String custom_tag_name;
        private int id;
        private String isCharge;
        private String last_updated_by;
        private String last_updated_time;
        private String media_type;
        private int preListFree;
        private String price;
        private String old_price;
        private String is_show_old_price;
        private int score;
        private String small_image;
        private int watch_number;
    }


}
