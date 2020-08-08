class GetUserInfoByNamesResponse {
  String errorMsg;
  String status;
  List<User> users;

  GetUserInfoByNamesResponse({this.errorMsg, this.status, this.users});

  GetUserInfoByNamesResponse.fromJson(Map<String, dynamic> json) {
    errorMsg = json['errorMsg'];
    status = json['status'];
    if (json['users'] != null) {
      users = new List<User>();
      json['users'].forEach((v) {
        users.add(new User.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['errorMsg'] = this.errorMsg;
    data['status'] = this.status;
    if (this.users != null) {
      data['users'] = this.users.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class User {
  String gender;
  String hat;
  String hatInUse;
  String nickName;
  String smallIcon;
  String userName;
  String userSignature;
  int vipLevel;

  User(
      {this.gender,
        this.hat,
        this.hatInUse,
        this.nickName,
        this.smallIcon,
        this.userName,
        this.userSignature,
        this.vipLevel});

  User.fromJson(Map<String, dynamic> json) {
    gender = json['gender'];
    hat = json['hat'];
    hatInUse = json['hat_in_use'];
    nickName = json['nick_name'];
    smallIcon = json['small_icon'];
    userName = json['user_name'];
    userSignature = json['user_signature'];
    vipLevel = json['vip_level'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['gender'] = this.gender;
    data['hat'] = this.hat;
    data['hat_in_use'] = this.hatInUse;
    data['nick_name'] = this.nickName;
    data['small_icon'] = this.smallIcon;
    data['user_name'] = this.userName;
    data['user_signature'] = this.userSignature;
    data['vip_level'] = this.vipLevel;
    return data;
  }
}