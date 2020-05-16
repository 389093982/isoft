package com.linkknown.ilearning.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HistoryResponse extends BaseResponse {

    private Paginator paginator;
    private List<History> historys;

    @Data
    public class History {

        private String created_by;
        private String created_time;
        private String history_desc;
        private String history_link;
        private String history_name;
        private String history_value;
        private int id;
        private String last_updated_by;
        private String last_updated_time;
    }
}
