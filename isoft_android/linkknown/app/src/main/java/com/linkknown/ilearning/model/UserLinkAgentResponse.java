package com.linkknown.ilearning.model;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserLinkAgentResponse extends BaseResponse {

    private Paginator paginator;
    private List<UserLinkAgent> userLinkAgentList;

    @Data
    public static class UserLinkAgent {

        private String user_name;
        private String agent_user_name;
        private String return_rate;
        private String settlement_time;
        private String bind_time;
        private String state;
        private String nick_name;
        private String small_icon;
        private String gender;
        private String user_signature;

    }

}
