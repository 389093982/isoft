package com.linkknown.ilearning.model;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserAttentionListResponse extends BaseResponse {

    private Paginator paginator;
    private List<QueryData> queryDatas;

    @Data
    public class QueryData {

        private int attention_counts;
        private Date attention_time;
        private Date birthday;
        private String created_by;
        private Date created_time;
        private String current_residence;
        private int fensi_counts;
        private String gender;
        private String hat;
        private String hat_in_use;
        private String hometown;
        private int id;
        private String last_updated_by;
        private Date last_updated_time;
        private String nick_name;
        private String pass_wd;
        private String role_name;
        private String small_icon;
        private String third_user_type;
        private String user_name;
        private int user_points;
        private String user_signature;
        private Date vip_expired_time;
        private int vip_level;
    }

}
