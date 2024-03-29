package com.linkknown.ilearning.model;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString
public class FirstLevelCommentResponse extends BaseResponse {
    private List<Comment> comments;
    private Paginator paginator;

    @Data
    public class Comment {

        private String comment_type;
        private String content;
        private String created_by;
        private String created_time;
        private int depth;
        private String hat_in_use;
        private int id;
        private String last_updated_by;
        private String last_updated_time;
        private String nick_name;
        private int org_parent_id;
        private int parent_id;
        private String refer_user_name;
        private String small_icon;
        private int sub_amount;
        private int theme_pk;
        private String theme_type;
        private String user_name;
        private int vip_level;
    }

}
