class SecondLevelCommentResponse {
  List<Comment> comments;
  String errorMsg;
  Paginator paginator;
  String status;

  SecondLevelCommentResponse(
      {this.comments, this.errorMsg, this.paginator, this.status});

  SecondLevelCommentResponse.fromJson(Map<String, dynamic> json) {
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
  String createdUserAccount;
  String createdUserHatInUse;
  String createdUserNickName;
  String createdUserSmallIcon;
  int createdUserVipLevel;
  int depth;
  int id;
  String lastUpdatedBy;
  String lastUpdatedTime;
  int orgParentId;
  int parentId;
  String referNickName;
  String referSamllIcon;
  String referUserAccount;
  String referUserName;
  int subAmount;
  int themePk;
  String themeType;

  Comment(
      {this.commentType,
        this.content,
        this.createdBy,
        this.createdTime,
        this.createdUserAccount,
        this.createdUserHatInUse,
        this.createdUserNickName,
        this.createdUserSmallIcon,
        this.createdUserVipLevel,
        this.depth,
        this.id,
        this.lastUpdatedBy,
        this.lastUpdatedTime,
        this.orgParentId,
        this.parentId,
        this.referNickName,
        this.referSamllIcon,
        this.referUserAccount,
        this.referUserName,
        this.subAmount,
        this.themePk,
        this.themeType});

  Comment.fromJson(Map<String, dynamic> json) {
    commentType = json['comment_type'];
    content = json['content'];
    createdBy = json['created_by'];
    createdTime = json['created_time'];
    createdUserAccount = json['created_user_account'];
    createdUserHatInUse = json['created_user_hat_in_use'];
    createdUserNickName = json['created_user_nick_name'];
    createdUserSmallIcon = json['created_user_small_icon'];
    createdUserVipLevel = json['created_user_vip_level'];
    depth = json['depth'];
    id = json['id'];
    lastUpdatedBy = json['last_updated_by'];
    lastUpdatedTime = json['last_updated_time'];
    orgParentId = json['org_parent_id'];
    parentId = json['parent_id'];
    referNickName = json['refer_nick_name'];
    referSamllIcon = json['refer_samll_icon'];
    referUserAccount = json['refer_user_account'];
    referUserName = json['refer_user_name'];
    subAmount = json['sub_amount'];
    themePk = json['theme_pk'];
    themeType = json['theme_type'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['comment_type'] = this.commentType;
    data['content'] = this.content;
    data['created_by'] = this.createdBy;
    data['created_time'] = this.createdTime;
    data['created_user_account'] = this.createdUserAccount;
    data['created_user_hat_in_use'] = this.createdUserHatInUse;
    data['created_user_nick_name'] = this.createdUserNickName;
    data['created_user_small_icon'] = this.createdUserSmallIcon;
    data['created_user_vip_level'] = this.createdUserVipLevel;
    data['depth'] = this.depth;
    data['id'] = this.id;
    data['last_updated_by'] = this.lastUpdatedBy;
    data['last_updated_time'] = this.lastUpdatedTime;
    data['org_parent_id'] = this.orgParentId;
    data['parent_id'] = this.parentId;
    data['refer_nick_name'] = this.referNickName;
    data['refer_samll_icon'] = this.referSamllIcon;
    data['refer_user_account'] = this.referUserAccount;
    data['refer_user_name'] = this.referUserName;
    data['sub_amount'] = this.subAmount;
    data['theme_pk'] = this.themePk;
    data['theme_type'] = this.themeType;
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