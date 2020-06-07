package com.linkknown.ilearning.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
public class PayShoppinpCartResponse extends BaseResponse{

    private List<PayShoppinpCartResponse.ShoppingCart> goodsData;
    private Paginator paginator;

    @Data
    @ToString
    public static class ShoppingCart implements Serializable {
        private String course_name;
        private String goods_img;
        private String small_image;
        private String goods_id;
        private String goods_type;
        private String goods_count;
        private BigDecimal price;
        private BigDecimal goods_price_on_add;
        private String add_time;
    }

}
