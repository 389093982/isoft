package com.linkknown.ilearning.model;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
public class queryCouponByIdResponse extends BaseResponse{

    private queryCouponByIdResponse.Coupon coupon;

    @Data
    @ToString
    public class Coupon implements Serializable {
        private String activity_id;
        private String coupon_amount;
        private String coupon_id;
        private String coupon_owner;
        private String coupon_state;
        private String coupon_type;
        private String created_by;
        private String created_time;
        private String discount_rate;
        private String goods_min_amount;
        private int id;
        private String last_updated_by;
        private String last_updated_time;
        private String target_id;
        private String target_type;
        private String youhui_type;
    }

}
