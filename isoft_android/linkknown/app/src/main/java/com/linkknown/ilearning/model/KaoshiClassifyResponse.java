package com.linkknown.ilearning.model;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class KaoshiClassifyResponse extends BaseResponse {

    private List<KaoshiClassify> kaoshi_classifys;

    @Data
    public class KaoshiClassify {
        private String classify_name;
        private String classify_desc;
        private String classify_image;
        private String created_by;
        private Date created_time;
        private int id;
    }

}
