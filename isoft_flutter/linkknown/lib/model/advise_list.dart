
class AdviseListResponse {
  List<Advise> advises;
  String errorMsg;
  Paginator paginator;
  String status;

  AdviseListResponse(
      {this.advises,
        this.errorMsg,
        this.paginator,
        this.status});

  AdviseListResponse.fromJson(Map<String, dynamic> json) {
    if (json['advises'] != null) {
      advises = new List<Advise>();
      json['advises'].forEach((v) {
        advises.add(new Advise.fromJson(v));
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
    if (this.advises != null) {
      data['advises'] = this.advises.map((v) => v.toJson()).toList();
    }
    data['errorMsg'] = this.errorMsg;
    if (this.paginator != null) {
      data['paginator'] = this.paginator.toJson();
    }
    data['status'] = this.status;
    return data;
  }
}

class Advise {
  String advise;
  String adviseType;
  String createdBy;
  String createdTime;
  int id;

  Advise(
      {this.advise,
        this.adviseType,
        this.createdBy,
        this.createdTime,
        this.id});

  Advise.fromJson(Map<String, dynamic> json) {
    advise = json['advise'];
    adviseType = json['advise_type'];
    createdBy = json['created_by'];
    createdTime = json['created_time'];
    id = json['id'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['advise'] = this.advise;
    data['advise_type'] = this.adviseType;
    data['created_by'] = this.createdBy;
    data['created_time'] = this.createdTime;
    data['id'] = this.id;
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
