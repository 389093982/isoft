package com.linkknown.ilearning.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class KaoshiShijuanListResponse extends BaseResponse {

    private List<KaoshiShijuan> kaoshi_shijuans;

    @Data
    public class KaoshiShijuan implements Serializable {
        private String classify_name;
        private String created_by;
        private Date created_time;
        private int id;
        private int sum_score;
        private int is_completed;
        private String user_name;
    }

}
