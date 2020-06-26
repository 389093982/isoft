package com.linkknown.ilearning.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CouponDatasResponse extends BaseResponse implements Serializable {

    private List<CouponDatasResponse.Coupon> couponDatas;
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
        private String discount_rate;
        private String end_date;
        private String goods_min_amount;
        private int id;
        private String last_updated_by;
        private String start_date;
        private String target_id;
        private String target_name;
        private String target_type;
        private String youhui_type;
    }

}
