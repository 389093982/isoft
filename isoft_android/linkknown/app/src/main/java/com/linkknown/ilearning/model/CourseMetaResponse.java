package com.linkknown.ilearning.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.ToString;


@Data
public class CourseMetaResponse extends BaseResponse {


    List<CourseMeta> courses;

    @Data
    @ToString
    public static class CourseMeta {

        private String course_name;
        private String course_author;
        private String course_type;
        private String small_image;
        private String course_sub_type;
        private String course_short_desc;
        private int course_number;
        private int watch_number;
        private String course_label;
    }
}
