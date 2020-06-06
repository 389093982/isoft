package com.linkknown.ilearning.model;

import java.util.ArrayList;
import java.util.Collections;
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

    public static List<ElementResponse.Element> getLevelOneClassifyElements (List<ElementResponse.Element> elements) {
        List<ElementResponse.Element> levelOneElements = new ArrayList<>();
        for (ElementResponse.Element element : elements) {
            if (element.getNavigation_level() == 0) {
                levelOneElements.add(element);
            }
        }
        Collections.sort(levelOneElements, (o1, o2) -> getLevelTwoClassifyElements(elements, o2.getId()).size() - getLevelTwoClassifyElements(elements, o1.getId()).size());
        return levelOneElements;
    }

    public static List<ElementResponse.Element> getLevelTwoClassifyElements (List<ElementResponse.Element> elements, int parentId) {
        List<ElementResponse.Element> levelTwoElements = new ArrayList<>();
        for (ElementResponse.Element element : elements) {
            if (element.getNavigation_parent_id() == parentId) {
                levelTwoElements.add(element);
            }
        }
        return levelTwoElements;
    }
}
