class IsFavoriteResponse {
  String errorMsg;
  bool isFavorite;
  String status;

  IsFavoriteResponse({this.errorMsg, this.isFavorite, this.status});

  IsFavoriteResponse.fromJson(Map<String, dynamic> json) {
    errorMsg = json['errorMsg'];
    isFavorite = json['isFavorite'];
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['errorMsg'] = this.errorMsg;
    data['isFavorite'] = this.isFavorite;
    data['status'] = this.status;
    return data;
  }
}