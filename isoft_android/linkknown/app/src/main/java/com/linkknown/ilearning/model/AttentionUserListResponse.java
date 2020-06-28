package com.linkknown.ilearning.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper=true)
public class AttentionUserListResponse extends BaseResponse{

    private List<User> AttentionUserList;

    @ToString
    @Data
    public static class User implements Serializable {
        private String attention_object_id;
    }

}
