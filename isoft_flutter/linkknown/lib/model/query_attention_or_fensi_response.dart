class QueryAttentionOrFensiResponse {
  String errorMsg;
  Paginator paginator;
  List<AttentionOrFensi> queryDatas;
  String status;

  QueryAttentionOrFensiResponse(
      {this.errorMsg, this.paginator, this.queryDatas, this.status});

  QueryAttentionOrFensiResponse.fromJson(Map<String, dynamic> json) {
    errorMsg = json['errorMsg'];
    paginator = json['paginator'] != null
        ? new Paginator.fromJson(json['paginator'])
        : null;
    if (json['queryDatas'] != null) {
      queryDatas = new List<AttentionOrFensi>();
      json['queryDatas'].forEach((v) {
        queryDatas.add(new AttentionOrFensi.fromJson(v));
      });
    }
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['errorMsg'] = this.errorMsg;
    if (this.paginator != null) {
      data['paginator'] = this.paginator.toJson();
    }
    if (this.queryDatas != null) {
      data['queryDatas'] = this.queryDatas.map((v) => v.toJson()).toList();
    }
    data['status'] = this.status;
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

class AttentionOrFensi {
  int attentionCounts;
  String attentionTime;
  String birthday;
  String createdBy;
  String createdTime;
  String currentResidence;
  int fensiCounts;
  String gender;
  String hat;
  String hatInUse;
  String hometown;
  int id;
  String lastUpdatedBy;
  String lastUpdatedTime;
  String nickName;
  String passWd;
  String roleName;
  String smallIcon;
  String thirdUserType;
  String userName;
  int userPoints;
  String userSignature;
  String vipExpiredTime;
  int vipLevel;

  AttentionOrFensi(
      {this.attentionCounts,
        this.attentionTime,
        this.birthday,
        this.createdBy,
        this.createdTime,
        this.currentResidence,
        this.fensiCounts,
        this.gender,
        this.hat,
        this.hatInUse,
        this.hometown,
        this.id,
        this.lastUpdatedBy,
        this.lastUpdatedTime,
        this.nickName,
        this.passWd,
        this.roleName,
        this.smallIcon,
        this.thirdUserType,
        this.userName,
        this.userPoints,
        this.userSignature,
        this.vipExpiredTime,
        this.vipLevel});

  AttentionOrFensi.fromJson(Map<String, dynamic> json) {
    attentionCounts = json['attention_counts'];
    attentionTime = json['attention_time'];
    birthday = json['birthday'];
    createdBy = json['created_by'];
    createdTime = json['created_time'];
    currentResidence = json['current_residence'];
    fensiCounts = json['fensi_counts'];
    gender = json['gender'];
    hat = json['hat'];
    hatInUse = json['hat_in_use'];
    hometown = json['hometown'];
    id = json['id'];
    lastUpdatedBy = json['last_updated_by'];
    lastUpdatedTime = json['last_updated_time'];
    nickName = json['nick_name'];
    passWd = json['pass_wd'];
    roleName = json['role_name'];
    smallIcon = json['small_icon'];
    thirdUserType = json['third_user_type'];
    userName = json['user_name'];
    userPoints = json['user_points'];
    userSignature = json['user_signature'];
    vipExpiredTime = json['vip_expired_time'];
    vipLevel = json['vip_level'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['attention_counts'] = this.attentionCounts;
    data['attention_time'] = this.attentionTime;
    data['birthday'] = this.birthday;
    data['created_by'] = this.createdBy;
    data['created_time'] = this.createdTime;
    data['current_residence'] = this.currentResidence;
    data['fensi_counts'] = this.fensiCounts;
    data['gender'] = this.gender;
    data['hat'] = this.hat;
    data['hat_in_use'] = this.hatInUse;
    data['hometown'] = this.hometown;
    data['id'] = this.id;
    data['last_updated_by'] = this.lastUpdatedBy;
    data['last_updated_time'] = this.lastUpdatedTime;
    data['nick_name'] = this.nickName;
    data['pass_wd'] = this.passWd;
    data['role_name'] = this.roleName;
    data['small_icon'] = this.smallIcon;
    data['third_user_type'] = this.thirdUserType;
    data['user_name'] = this.userName;
    data['user_points'] = this.userPoints;
    data['user_signature'] = this.userSignature;
    data['vip_expired_time'] = this.vipExpiredTime;
    data['vip_level'] = this.vipLevel;
    return data;
  }
}