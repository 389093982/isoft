class FirstLevelCommentResponse {
  List<Comment> comments;
  String errorMsg;
  Paginator paginator;
  String status;

  FirstLevelCommentResponse(
      {this.comments, this.errorMsg, this.paginator, this.status});

  FirstLevelCommentResponse.fromJson(Map<String, dynamic> json) {
    if (json['comments'] != null) {
      comments = new List<Comment>();
      json['comments'].forEach((v) {
        comments.add(new Comment.fromJson(v));
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
    if (this.comments != null) {
      data['comments'] = this.comments.map((v) => v.toJson()).toList();
    }
    data['errorMsg'] = this.errorMsg;
    if (this.paginator != null) {
      data['paginator'] = this.paginator.toJson();
    }
    data['status'] = this.status;
    return data;
  }
}

class Comment {
  String commentType;
  String content;
  String createdBy;
  String createdTime;
  int depth;
  String hatInUse;
  int id;
  String lastUpdatedBy;
  String lastUpdatedTime;
  String nickName;
  int orgParentId;
  int parentId;
  String referUserName;
  String smallIcon;
  int subAmount;
  int themePk;
  String themeType;
  String userName;
  int vipLevel;

  Comment(
      {this.commentType,
        this.content,
        this.createdBy,
        this.createdTime,
        this.depth,
        this.hatInUse,
        this.id,
        this.lastUpdatedBy,
        this.lastUpdatedTime,
        this.nickName,
        this.orgParentId,
        this.parentId,
        this.referUserName,
        this.smallIcon,
        this.subAmount,
        this.themePk,
        this.themeType,
        this.userName,
        this.vipLevel});

  Comment.fromJson(Map<String, dynamic> json) {
    commentType = json['comment_type'];
    content = json['content'];
    createdBy = json['created_by'];
    createdTime = json['created_time'];
    depth = json['depth'];
    hatInUse = json['hat_in_use'];
    id = json['id'];
    lastUpdatedBy = json['last_updated_by'];
    lastUpdatedTime = json['last_updated_time'];
    nickName = json['nick_name'];
    orgParentId = json['org_parent_id'];
    parentId = json['parent_id'];
    referUserName = json['refer_user_name'];
    smallIcon = json['small_icon'];
    subAmount = json['sub_amount'];
    themePk = json['theme_pk'];
    themeType = json['theme_type'];
    userName = json['user_name'];
    vipLevel = json['vip_level'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['comment_type'] = this.commentType;
    data['content'] = this.content;
    data['created_by'] = this.createdBy;
    data['created_time'] = this.createdTime;
    data['depth'] = this.depth;
    data['hat_in_use'] = this.hatInUse;
    data['id'] = this.id;
    data['last_updated_by'] = this.lastUpdatedBy;
    data['last_updated_time'] = this.lastUpdatedTime;
    data['nick_name'] = this.nickName;
    data['org_parent_id'] = this.orgParentId;
    data['parent_id'] = this.parentId;
    data['refer_user_name'] = this.referUserName;
    data['small_icon'] = this.smallIcon;
    data['sub_amount'] = this.subAmount;
    data['theme_pk'] = this.themePk;
    data['theme_type'] = this.themeType;
    data['user_name'] = this.userName;
    data['vip_level'] = this.vipLevel;
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