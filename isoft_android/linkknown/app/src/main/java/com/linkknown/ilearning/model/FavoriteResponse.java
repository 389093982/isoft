package com.linkknown.ilearning.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FavoriteResponse extends BaseResponse {

    private List<Favorite> favorites;

    @Data
    public class Favorite {

        private int favorite_id;
        private String favorite_type;
        private int id;
        private String user_name;
    }

}
