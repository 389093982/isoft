package com.linkknown.ilearning.model;

import lombok.Data;

@Data
public class BaseResponse {

    // 状态
    private String status;
    private String errorMsg;

    public boolean isSuccess () {
        return "SUCCESS".equals(status);
    }

}