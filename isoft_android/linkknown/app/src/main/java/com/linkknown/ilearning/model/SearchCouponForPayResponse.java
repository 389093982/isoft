package com.linkknown.ilearning.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchCouponForPayResponse extends BaseResponse {

    private List<SearchCouponForPayResponse.Coupon> coupons;

    @Data
    @ToString
    public class Coupon implements Serializable{
        private String activity_desc;
        private String activity_id;
        private String coupon_amount;
        private String coupon_id;
        private String coupon_owner;
        private String coupon_state;
        private String coupon_type;
        private String created_by;
        private String created_time;
        private String discount_rate;
        private String end_date;
        private String goods_min_amount;
        private int id;
        private String last_updated_by;
        private String last_updated_time;
        private String start_date;
        private String target_id;
        private String target_name;
        private String target_type;
        private String youhui_type;
    }

}
