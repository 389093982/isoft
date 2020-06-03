package com.linkknown.ilearning.model;

import java.util.List;

import lombok.Data;

@Data
public class CouponListResponse extends BaseResponse {

    private List<Coupon> coupons;
    private Paginator paginator;

    @Data
    public static class Coupon {
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
        private String target_type;
        private String youhui_type;
    }
}