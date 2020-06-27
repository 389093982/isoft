package com.linkknown.ilearning.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper=true)
public class BlogListResponse extends BaseResponse{

    private List<BlogArticle> blogs;
    private Paginator paginator;

    @Data
    @ToString
    public static class BlogArticle implements Serializable {
        private String author;
        private int blog_status;
        private String blog_title;
        private String catalog_name;
        private int comments;
        private String created_time;
        private String first_img;
        private int id;
        private String last_updated_time;
        private int to_top;
        private int views;

        private boolean attention;

        private UserListResponse.User user;

    }

}
