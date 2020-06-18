package com.linkknown.ilearning.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDetailResponse extends BaseResponse implements Serializable {

    private User user;

    @Data
    public static class User implements Serializable {
        private Date birthday;
        private String created_time;
        private String current_residence;
        private String gender;
        private String hat;
        private String hat_in_use;
        private String hometown;
        private String nick_name;
        private String role_name;
        private String small_icon;
        private String user_name;
        private int user_points;
        private String user_signature;
        private String vip_expired_time;
        private int vip_level;
        private int attention_counts;
        private int fensi_counts;
    }
}
