package com.linkknown.ilearning.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
public class CourseDetailResponse extends BaseResponse {

    private List<CVideos> cVideos ;
    private Course course;

    @Data
    @ToString
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
    @ToString
    public class CVideos {
        private int course_id;
        private int duration;
        private int id;
        private String video_name;
    }
}
