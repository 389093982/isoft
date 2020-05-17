package com.linkknown.ilearning.model;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RefreshTokenResponse extends BaseResponse {

    private String tokenString;
    private long expireSecond;
}
