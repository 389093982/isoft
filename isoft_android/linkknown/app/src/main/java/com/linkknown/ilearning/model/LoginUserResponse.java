package com.linkknown.ilearning.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class LoginUserResponse extends BaseResponse {

    private String domain;
    private long expireSecond;
    private String loginStatus;
    private String loginSuccess;
    private String nickName;
    private String redirectUrl;
    private String roleName;
    private String tokenString;
    private String userName;
    private String headerIcon;

    // 包含当前登录用户信息
    private UserDetailResponse userDetailResponse;
}
