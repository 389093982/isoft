class UserLinkAgentResponse {
  String errorMsg;
  Paginator paginator;
  String status;
  List<UserLinkAgent> userLinkAgentList;

  UserLinkAgentResponse(
      {this.errorMsg, this.paginator, this.status, this.userLinkAgentList});

  UserLinkAgentResponse.fromJson(Map<String, dynamic> json) {
    errorMsg = json['errorMsg'];
    paginator = json['paginator'] != null
        ? new Paginator.fromJson(json['paginator'])
        : null;
    status = json['status'];
    if (json['userLinkAgentList'] != null) {
      userLinkAgentList = new List<UserLinkAgent>();
      json['userLinkAgentList'].forEach((v) {
        userLinkAgentList.add(new UserLinkAgent.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['errorMsg'] = this.errorMsg;
    if (this.paginator != null) {
      data['paginator'] = this.paginator.toJson();
    }
    data['status'] = this.status;
    if (this.userLinkAgentList != null) {
      data['userLinkAgentList'] =
          this.userLinkAgentList.map((v) => v.toJson()).toList();
    }
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

class UserLinkAgent {
  String agentUserName;
  String bindTime;
  String gender;
  String nickName;
  String returnRate;
  String settlementTime;
  String smallIcon;
  String state;
  String userName;
  String userSignature;

  UserLinkAgent(
      {this.agentUserName,
        this.bindTime,
        this.gender,
        this.nickName,
        this.returnRate,
        this.settlementTime,
        this.smallIcon,
        this.state,
        this.userName,
        this.userSignature});

  UserLinkAgent.fromJson(Map<String, dynamic> json) {
    agentUserName = json['agent_user_name'];
    bindTime = json['bind_time'];
    gender = json['gender'];
    nickName = json['nick_name'];
    returnRate = json['return_rate'];
    settlementTime = json['settlement_time'];
    smallIcon = json['small_icon'];
    state = json['state'];
    userName = json['user_name'];
    userSignature = json['user_signature'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['agent_user_name'] = this.agentUserName;
    data['bind_time'] = this.bindTime;
    data['gender'] = this.gender;
    data['nick_name'] = this.nickName;
    data['return_rate'] = this.returnRate;
    data['settlement_time'] = this.settlementTime;
    data['small_icon'] = this.smallIcon;
    data['state'] = this.state;
    data['user_name'] = this.userName;
    data['user_signature'] = this.userSignature;
    return data;
  }
}