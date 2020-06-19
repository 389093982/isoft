package com.linkknown.ilearning.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class KaoshiShijuanResponse extends BaseResponse {

    private KaoshiShijuan kaoshi_shijuan;

    @Data
    public class KaoshiShijuan implements Serializable {
        private String classify_name;
        private String classify_desc;
        private String classify_image;
        private String created_by;
        private Date created_time;
        private String kaoshi_start_time;
        private String kaoshi_end_time;
        private int id;
        private int sum_score;
        private int is_completed;
        private String user_name;
    }

}
