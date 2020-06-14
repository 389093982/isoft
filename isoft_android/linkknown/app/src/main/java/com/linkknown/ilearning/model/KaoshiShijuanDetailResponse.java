package com.linkknown.ilearning.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class KaoshiShijuanDetailResponse extends BaseResponse {

    private List<KaoshiShijuanDetail> kaoshi_shijuandetail;

    @Data
    public class KaoshiShijuanDetail {

        private int get_score;
        private String given_answer;
        private int id;
        private int is_correct;
        private int shijuan_id;
        private String timu_answer;
        private String timu_answer_a;
        private String timu_answer_b;
        private String timu_answer_c;
        private String timu_answer_d;
        private String timu_answer_e;
        private String timu_answer_f;
        private String timu_question;
        private int timu_score;
    }
}
