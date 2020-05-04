package com.linkknown.ilearning.model;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class CourseSearchResponse extends BaseResponse {

    private List<Course> courses;
    private Paginator paginator;

    @Data
    public class Course {
        private int comments;
        private String course_author;
        private String course_label;
        private String course_name;
        private int course_number;
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
        private int score;
        private String small_image;
        private int watch_number;
    }

    @Data
    public class Paginator {
        private int currpage;
        private int firstpage;
        private int lastpage;
        private List<Integer> pages;
        private int pagesize;
        private int totalcount;
        private int totalpages;
    }



}
