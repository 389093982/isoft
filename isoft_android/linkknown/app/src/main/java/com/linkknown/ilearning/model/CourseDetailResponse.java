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
public class CourseDetailResponse extends BaseResponse implements Serializable {

    private List<CVideo> cVideos ;
    private Course course;
    private UserDetailResponse.User user;
    private PayOrderResponse.PayOrder payOrder;

    @Data
    @ToString
    public class Course implements Serializable {
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
    public class CVideo implements Serializable {
        private int course_id;
        private int duration;
        private int id;
        private String video_name;
        private String first_play;
        private String second_play;
    }

    @Data
    @ToString
    public static class MultiItemTypeCVideo implements MultiItemEntity, Serializable {
        public static final int ITEM_TYPE_LIST = 0;
        public static final int ITEM_TYPE_GRID = 1;

        private CVideo cVideo;
        private int itemType;

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public int getItemType() {
            return itemType;
        }

        public static List<MultiItemTypeCVideo> setItemType (List<CVideo> cVideos, int itemType) {
            List<MultiItemTypeCVideo> multiItemTypeCVideos = new ArrayList<>();
            for (CVideo cVideo : cVideos) {
                MultiItemTypeCVideo multiItemTypeCVideo = new MultiItemTypeCVideo();
                multiItemTypeCVideo.setItemType(itemType);
                multiItemTypeCVideo.setCVideo(cVideo);
                multiItemTypeCVideos.add(multiItemTypeCVideo);
            }
            return multiItemTypeCVideos;
        }

        public static List<MultiItemTypeCVideo> modifyItemType (List<MultiItemTypeCVideo> multiItemTypeCVideos, int itemType) {
            for (MultiItemTypeCVideo multiItemTypeCVideo : multiItemTypeCVideos) {
                multiItemTypeCVideo.setItemType(itemType);
            }
            return multiItemTypeCVideos;
        }
    }
}
