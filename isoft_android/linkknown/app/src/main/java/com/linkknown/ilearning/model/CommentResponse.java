package com.linkknown.ilearning.model;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString
public class CommentResponse extends BaseResponse {
    private List<Comment> comments;
    private Paginator paginator;

    @Data
    @ToString
    public class Comment {

        private String comment_type;
        private String content;
        private String created_by;
        private Date created_time;
        private int depth;
        private String hat_in_use;
        private int id;
        private String last_updated_by;
        private Date last_updated_time;
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

    @Data
    @ToString
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
