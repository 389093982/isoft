package com.linkknown.ilearning.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
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
        private String custom_tag;
        private String custom_tag_name;
    }

    @Data
    public static class MultiItemTypeCourseMeta implements MultiItemEntity {

        public static final int ITEM_TYPE_LIST = 0;
        public static final int ITEM_TYPE_GRID = 1;

        private CourseMeta courseMeta;
        private int itemType;

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        @Override
        public int getItemType() {
            return itemType;
        }

        public static List<MultiItemTypeCourseMeta> setItemType (List<CourseMeta> courseMetas, int itemType) {
            List<MultiItemTypeCourseMeta> multiItemTypeCourseMetas = new ArrayList<>();
            for (CourseMeta courseMeta : courseMetas) {
                MultiItemTypeCourseMeta multiItemTypeCourseMeta = new MultiItemTypeCourseMeta();
                multiItemTypeCourseMeta.setItemType(itemType);
                multiItemTypeCourseMeta.setCourseMeta(courseMeta);
                multiItemTypeCourseMetas.add(multiItemTypeCourseMeta);
            }
            return multiItemTypeCourseMetas;
        }

        public static List<MultiItemTypeCourseMeta> modifyItemType (List<MultiItemTypeCourseMeta> multiItemTypeCourseMetas, int itemType) {
            for (MultiItemTypeCourseMeta courseMeta : multiItemTypeCourseMetas) {
                courseMeta.setItemType(itemType);
            }
            return multiItemTypeCourseMetas;
        }
    }
}
