package com.linkknown.ilearning.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ElementResponse extends BaseResponse {

    private List<Element> elements;
    private Placement placement;

    @Data
    public static class Element {
        private String content;
        private String created_by;
        private String created_time;
        private String element_label;
        private String element_name;
        private int id;
        private String img_path;
        private String last_updated_by;
        private String last_updated_time;
        private String linked_refer;
        private String md_content;
        private int navigation_level;
        private int navigation_parent_id;
        private String placement;
        private int status;
    }

    @Data
    public static class Placement {
        private int app_id;
        private String created_by;
        private String created_time;
        private int element_limit;
        private int id;
        private String last_updated_by;
        private String last_updated_time;
        private String placement_desc;
        private String placement_label;
        private String placement_name;
        private String placement_type;
    }
}
