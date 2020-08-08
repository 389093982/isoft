
class ElementResponse {
  List<ElementItem> elements;
  String errorMsg;
  Placement placement;
  String status;

  ElementResponse(
      {this.elements,
        this.errorMsg,
        this.placement,
        this.status});

  ElementResponse.fromJson(Map<String, dynamic> json) {
    if (json['elements'] != null) {
      elements = new List<ElementItem>();
      json['elements'].forEach((v) {
        elements.add(new ElementItem.fromJson(v));
      });
    }
    errorMsg = json['errorMsg'];
    placement = json['placement'] != null
        ? new Placement.fromJson(json['placement'])
        : null;
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this.elements != null) {
      data['elements'] = this.elements.map((v) => v.toJson()).toList();
    }
    data['errorMsg'] = this.errorMsg;
    if (this.placement != null) {
      data['placement'] = this.placement.toJson();
    }
    data['status'] = this.status;
    return data;
  }
}

class ElementItem {
  String content;
  String createdBy;
  String createdTime;
  String elementLabel;
  String elementName;
  int id;
  String imgPath;
  String lastUpdatedBy;
  String lastUpdatedTime;
  String linkedRefer;
  String mdContent;
  int navigationLevel;
  int navigationParentId;
  String placement;
  int status;

  ElementItem(
      {this.content,
        this.createdBy,
        this.createdTime,
        this.elementLabel,
        this.elementName,
        this.id,
        this.imgPath,
        this.lastUpdatedBy,
        this.lastUpdatedTime,
        this.linkedRefer,
        this.mdContent,
        this.navigationLevel,
        this.navigationParentId,
        this.placement,
        this.status});

  ElementItem.fromJson(Map<String, dynamic> json) {
    content = json['content'];
    createdBy = json['created_by'];
    createdTime = json['created_time'];
    elementLabel = json['element_label'];
    elementName = json['element_name'];
    id = json['id'];
    imgPath = json['img_path'];
    lastUpdatedBy = json['last_updated_by'];
    lastUpdatedTime = json['last_updated_time'];
    linkedRefer = json['linked_refer'];
    mdContent = json['md_content'];
    navigationLevel = json['navigation_level'];
    navigationParentId = json['navigation_parent_id'];
    placement = json['placement'];
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['content'] = this.content;
    data['created_by'] = this.createdBy;
    data['created_time'] = this.createdTime;
    data['element_label'] = this.elementLabel;
    data['element_name'] = this.elementName;
    data['id'] = this.id;
    data['img_path'] = this.imgPath;
    data['last_updated_by'] = this.lastUpdatedBy;
    data['last_updated_time'] = this.lastUpdatedTime;
    data['linked_refer'] = this.linkedRefer;
    data['md_content'] = this.mdContent;
    data['navigation_level'] = this.navigationLevel;
    data['navigation_parent_id'] = this.navigationParentId;
    data['placement'] = this.placement;
    data['status'] = this.status;
    return data;
  }
}

class Placement {
  String createdBy;
  String createdTime;
  int elementLimit;
  int id;
  String lastUpdatedBy;
  String lastUpdatedTime;
  String placementDesc;
  String placementLabel;
  String placementName;
  String placementType;

  Placement(
      {this.createdBy,
        this.createdTime,
        this.elementLimit,
        this.id,
        this.lastUpdatedBy,
        this.lastUpdatedTime,
        this.placementDesc,
        this.placementLabel,
        this.placementName,
        this.placementType});

  Placement.fromJson(Map<String, dynamic> json) {
    createdBy = json['created_by'];
    createdTime = json['created_time'];
    elementLimit = json['element_limit'];
    id = json['id'];
    lastUpdatedBy = json['last_updated_by'];
    lastUpdatedTime = json['last_updated_time'];
    placementDesc = json['placement_desc'];
    placementLabel = json['placement_label'];
    placementName = json['placement_name'];
    placementType = json['placement_type'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['created_by'] = this.createdBy;
    data['created_time'] = this.createdTime;
    data['element_limit'] = this.elementLimit;
    data['id'] = this.id;
    data['last_updated_by'] = this.lastUpdatedBy;
    data['last_updated_time'] = this.lastUpdatedTime;
    data['placement_desc'] = this.placementDesc;
    data['placement_label'] = this.placementLabel;
    data['placement_name'] = this.placementName;
    data['placement_type'] = this.placementType;
    return data;
  }
}