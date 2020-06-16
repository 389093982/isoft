package com.linkknown.ilearning.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString
public class SecondLevelCommentResponse extends BaseResponse{

    private List<SecondLevelCommentResponse.Comment> comments;

    @Data
    public class Comment{
        private String comment_type;
        private String content;
        private String created_by;
        private String created_time;
        private String created_user_account;
        private String created_user_hat_in_use;
        private String created_user_nick_name;
        private String created_user_small_icon;
        private int created_user_vip_level;
        private int depth;
        private int id;
        private String last_updated_by;
        private String last_updated_time;
        private int org_parent_id;
        private int parent_id;
        private String refer_nick_name;
        private String refer_samll_icon;
        private String refer_user_account;
        private String refer_user_name;
        private int sub_amount;
        private int theme_pk;
        private String theme_type;
    }

}
