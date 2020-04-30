package com.linkknown.ilearning.model;

import java.util.List;

import lombok.Data;


@Data
public class CourseMetaResponse extends BaseResponse {

    List<CourseMeta> courseMetas;

    @Data
    public static class CourseMeta {
        private String courseName;
        private String courseAuthor;
        private String courseType;
        private String smallImage;
        private String courseSubType;
        private String courseShortDesc;
        private int courseNumber;
        private int watchNumber;
        private String courseLabel;
    }
}
