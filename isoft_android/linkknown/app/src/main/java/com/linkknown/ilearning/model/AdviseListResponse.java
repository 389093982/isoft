package com.linkknown.ilearning.model;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdviseListResponse extends BaseResponse {

    private List<Advise> advises;
    private Paginator paginator;

    @Data
    public static class Advise {
        private String advise;
        private String advise_type;
        private String created_by;
        private Date created_time;
        private int id;
    }
}
