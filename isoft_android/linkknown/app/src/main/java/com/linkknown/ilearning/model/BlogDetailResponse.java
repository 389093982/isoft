package com.linkknown.ilearning.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper=true)
public class BlogDetailResponse extends BaseResponse{

    private Blog blog;

    @Data
    @ToString
    public static class Blog implements Serializable {
        private String author;
        private int blog_status;
        private String blog_title;
        private int blog_type;
        private String catalog_name;
        private int comments;
        private String content;
        private String created_by;
        private String created_time;
        private String custom_tag;
        private int edits;
        private String first_img;
        private int id;
        private String key_words;
        private String last_updated_by;
        private String last_updated_time;
        private String link_href;
        private int to_top;
        private int views;
    }

}
