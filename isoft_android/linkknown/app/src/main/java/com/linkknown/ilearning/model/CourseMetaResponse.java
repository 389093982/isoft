package com.linkknown.ilearning.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
public class CourseMetaResponse extends BaseResponse {

    @SerializedName(value = "courses", alternate = {"custom_tag_courses"})
    List<CourseMeta> courses;
    private Paginator paginator;

    @Data
    @ToString
    public static class CourseMeta {

        private int id;
        private String course_name;
        private String course_author;
        private String course_type;
        private String small_image;
        private String course_sub_type;
        private String course_short_desc;
        private int course_number;
        private int watch_number;
        private String course_label;
        private String isCharge;
    }
}
