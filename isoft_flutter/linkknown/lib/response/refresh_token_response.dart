
class RefreshTokenResponse {
  String errorMsg;
  String expireSecond;
  String status;
  String tokenString;

  RefreshTokenResponse(
      {this.errorMsg, this.expireSecond, this.status, this.tokenString});

  RefreshTokenResponse.fromJson(Map<String, dynamic> json) {
    errorMsg = json['errorMsg'];
    expireSecond = json['expireSecond'];
    status = json['status'];
    tokenString = json['tokenString'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['errorMsg'] = this.errorMsg;
    data['expireSecond'] = this.expireSecond;
    data['status'] = this.status;
    data['tokenString'] = this.tokenString;
    return data;
  }
}
