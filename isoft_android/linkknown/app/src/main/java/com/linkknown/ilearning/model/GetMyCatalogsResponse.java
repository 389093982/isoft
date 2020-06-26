package com.linkknown.ilearning.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper=true)
public class GetMyCatalogsResponse extends BaseResponse{

    private List<Catalogs> catalogs;
    private Paginator paginator;

    @Data
    @ToString
    public static class Catalogs implements Serializable {
        private String author;
        private String catalog_desc;
        private String catalog_name;
        private String created_by;
        private String created_time;
        private int id;
        private String last_updated_by;
        private String last_updated_time;
    }

}
