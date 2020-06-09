package com.linkknown.ilearning.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class BaseResponse implements Serializable {

    // 状态
    private String status;
    private String errorMsg;

    public boolean isSuccess () {
        return "SUCCESS".equals(status);
    }

}