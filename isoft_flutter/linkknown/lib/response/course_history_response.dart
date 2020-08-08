class CourseHistoryResponse {
  String errorMsg;
  List<History> historys;
  Paginator paginator;
  String status;

  CourseHistoryResponse(
      {this.errorMsg, this.historys, this.paginator, this.status});

  CourseHistoryResponse.fromJson(Map<String, dynamic> json) {
    errorMsg = json['errorMsg'];
    if (json['historys'] != null) {
      historys = new List<History>();
      json['historys'].forEach((v) {
        historys.add(new History.fromJson(v));
      });
    }
    paginator = json['paginator'] != null
        ? new Paginator.fromJson(json['paginator'])
        : null;
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['errorMsg'] = this.errorMsg;
    if (this.historys != null) {
      data['historys'] = this.historys.map((v) => v.toJson()).toList();
    }
    if (this.paginator != null) {
      data['paginator'] = this.paginator.toJson();
    }
    data['status'] = this.status;
    return data;
  }
}

class History {
  String createdBy;
  String createdTime;
  String historyDesc;
  String historyLink;
  String historyName;
  String historyValue;
  int id;
  String lastUpdatedBy;
  String lastUpdatedTime;

  History(
      {this.createdBy,
        this.createdTime,
        this.historyDesc,
        this.historyLink,
        this.historyName,
        this.historyValue,
        this.id,
        this.lastUpdatedBy,
        this.lastUpdatedTime});

  History.fromJson(Map<String, dynamic> json) {
    createdBy = json['created_by'];
    createdTime = json['created_time'];
    historyDesc = json['history_desc'];
    historyLink = json['history_link'];
    historyName = json['history_name'];
    historyValue = json['history_value'];
    id = json['id'];
    lastUpdatedBy = json['last_updated_by'];
    lastUpdatedTime = json['last_updated_time'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['created_by'] = this.createdBy;
    data['created_time'] = this.createdTime;
    data['history_desc'] = this.historyDesc;
    data['history_link'] = this.historyLink;
    data['history_name'] = this.historyName;
    data['history_value'] = this.historyValue;
    data['id'] = this.id;
    data['last_updated_by'] = this.lastUpdatedBy;
    data['last_updated_time'] = this.lastUpdatedTime;
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