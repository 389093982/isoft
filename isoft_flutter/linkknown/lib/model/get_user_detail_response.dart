import 'package:linkknown/model/base.dart';

class GetUserDetailResponse {
  String errorMsg;
  String status;
  User user;

  GetUserDetailResponse({this.errorMsg, this.status, this.user});

  GetUserDetailResponse.fromJson(Map<String, dynamic> json) {
    errorMsg = json['errorMsg'];
    status = json['status'];
    user = json['user'] != null ? new User.fromJson(json['user']) : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['errorMsg'] = this.errorMsg;
    data['status'] = this.status;
    if (this.user != null) {
      data['user'] = this.user.toJson();
    }
    return data;
  }
}

class User {
  int attentionCounts;
  String birthday;
  String createdTime;
  String currentResidence;
  int fensiCounts;
  String gender;
  String hat;
  String hatInUse;
  String hometown;
  String nickName;
  String roleName;
  String smallIcon;
  String userName;
  int userPoints;
  String userSignature;
  String vipExpiredTime;
  int vipLevel;

  User(
      {this.attentionCounts,
        this.birthday,
        this.createdTime,
        this.currentResidence,
        this.fensiCounts,
        this.gender,
        this.hat,
        this.hatInUse,
        this.hometown,
        this.nickName,
        this.roleName,
        this.smallIcon,
        this.userName,
        this.userPoints,
        this.userSignature,
        this.vipExpiredTime,
        this.vipLevel});

  User.fromJson(Map<String, dynamic> json) {
    attentionCounts = json['attention_counts'];
    birthday = json['birthday'];
    createdTime = json['created_time'];
    currentResidence = json['current_residence'];
    fensiCounts = json['fensi_counts'];
    gender = json['gender'];
    hat = json['hat'];
    hatInUse = json['hat_in_use'];
    hometown = json['hometown'];
    nickName = json['nick_name'];
    roleName = json['role_name'];
    smallIcon = json['small_icon'];
    userName = json['user_name'];
    userPoints = json['user_points'];
    userSignature = json['user_signature'];
    vipExpiredTime = json['vip_expired_time'];
    vipLevel = json['vip_level'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['attention_counts'] = this.attentionCounts;
    data['birthday'] = this.birthday;
    data['created_time'] = this.createdTime;
    data['current_residence'] = this.currentResidence;
    data['fensi_counts'] = this.fensiCounts;
    data['gender'] = this.gender;
    data['hat'] = this.hat;
    data['hat_in_use'] = this.hatInUse;
    data['hometown'] = this.hometown;
    data['nick_name'] = this.nickName;
    data['role_name'] = this.roleName;
    data['small_icon'] = this.smallIcon;
    data['user_name'] = this.userName;
    data['user_points'] = this.userPoints;
    data['user_signature'] = this.userSignature;
    data['vip_expired_time'] = this.vipExpiredTime;
    data['vip_level'] = this.vipLevel;
    return data;
  }
}
