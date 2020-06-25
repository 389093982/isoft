package com.linkknown.ilearning.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper=true)
public class PayOrderResponse extends BaseResponse{

    private List<PayOrder> orders;
    private Paginator paginator;

    @Data
    @ToString
    public static class PayOrder implements Serializable {
        private String order_id;
        private String trans_time;
        private String user_name;
        private String goods_type;
        private String goods_id;
        private String goods_desc;
        private BigDecimal paid_amount;
        private String goods_img;
        private String pay_result;
        private BigDecimal goods_original_price;
        private String activity_type;
        private String activity_type_bind_id;
        private String order_time;
        private String code_url;

    }

}
