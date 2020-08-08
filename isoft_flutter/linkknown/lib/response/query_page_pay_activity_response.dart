class QueryPagePayActivityResponse {
  List<ActivityData> activityDatas;
  String errorMsg;
  Paginator paginator;
  String status;

  QueryPagePayActivityResponse(
      {this.activityDatas, this.errorMsg, this.paginator, this.status});

  QueryPagePayActivityResponse.fromJson(Map<String, dynamic> json) {
    if (json['activityDatas'] != null) {
      activityDatas = new List<ActivityData>();
      json['activityDatas'].forEach((v) {
        activityDatas.add(new ActivityData.fromJson(v));
      });
    }
    errorMsg = json['errorMsg'];
    paginator = json['paginator'] != null
        ? new Paginator.fromJson(json['paginator'])
        : null;
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this.activityDatas != null) {
      data['activityDatas'] =
          this.activityDatas.map((v) => v.toJson()).toList();
    }
    data['errorMsg'] = this.errorMsg;
    if (this.paginator != null) {
      data['paginator'] = this.paginator.toJson();
    }
    data['status'] = this.status;
    return data;
  }
}

class ActivityData {
  String activityDesc;
  String activityId;
  String activityType;
  String createdBy;
  String createdTime;
  String endDate;
  int id;
  String lastUpdatedBy;
  String lastUpdatedTime;
  String organizer;
  String startDate;
  int typeEntityAccount;

  ActivityData(
      {this.activityDesc,
        this.activityId,
        this.activityType,
        this.createdBy,
        this.createdTime,
        this.endDate,
        this.id,
        this.lastUpdatedBy,
        this.lastUpdatedTime,
        this.organizer,
        this.startDate,
        this.typeEntityAccount});

  ActivityData.fromJson(Map<String, dynamic> json) {
    activityDesc = json['activity_desc'];
    activityId = json['activity_id'];
    activityType = json['activity_type'];
    createdBy = json['created_by'];
    createdTime = json['created_time'];
    endDate = json['end_date'];
    id = json['id'];
    lastUpdatedBy = json['last_updated_by'];
    lastUpdatedTime = json['last_updated_time'];
    organizer = json['organizer'];
    startDate = json['start_date'];
    typeEntityAccount = json['type_entity_account'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['activity_desc'] = this.activityDesc;
    data['activity_id'] = this.activityId;
    data['activity_type'] = this.activityType;
    data['created_by'] = this.createdBy;
    data['created_time'] = this.createdTime;
    data['end_date'] = this.endDate;
    data['id'] = this.id;
    data['last_updated_by'] = this.lastUpdatedBy;
    data['last_updated_time'] = this.lastUpdatedTime;
    data['organizer'] = this.organizer;
    data['start_date'] = this.startDate;
    data['type_entity_account'] = this.typeEntityAccount;
    return data;
  }
}

class Paginator {
  int currpage;
  int firstpage;
  int lastpage;
  List<int> pages;
  int pagesize;
  int totalcount;
  int totalpages;

  Paginator(
      {this.currpage,
        this.firstpage,
        this.lastpage,
        this.pages,
        this.pagesize,
        this.totalcount,
        this.totalpages});

  Paginator.fromJson(Map<String, dynamic> json) {
    currpage = json['currpage'];
    firstpage = json['firstpage'];
    lastpage = json['lastpage'];
    pages = json['pages'].cast<int>();
    pagesize = json['pagesize'];
    totalcount = json['totalcount'];
    totalpages = json['totalpages'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['currpage'] = this.currpage;
    data['firstpage'] = this.firstpage;
    data['lastpage'] = this.lastpage;
    data['pages'] = this.pages;
    data['pagesize'] = this.pagesize;
    data['totalcount'] = this.totalcount;
    data['totalpages'] = this.totalpages;
    return data;
  }
}