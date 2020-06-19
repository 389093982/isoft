package com.linkknown.ilearning.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class FileUploadResponse extends BaseResponse{

    private String fileName;
    private String fileServerPath;
    private String fileSize;
    private String duration;

}
