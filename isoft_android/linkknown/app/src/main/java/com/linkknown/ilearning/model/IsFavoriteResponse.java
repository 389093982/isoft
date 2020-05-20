package com.linkknown.ilearning.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class IsFavoriteResponse extends BaseResponse {

    public boolean isFavorite;

}
