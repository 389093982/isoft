package com.linkknown.ilearning.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class QueryIsAttentionResponse extends BaseResponse{

    private Integer attention_records;

}
