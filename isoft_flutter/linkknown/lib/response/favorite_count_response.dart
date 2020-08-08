
class FavoriteCountResponse {
  int counts;
  String errorMsg;
  String status;

  FavoriteCountResponse({this.counts, this.errorMsg, this.status});

  FavoriteCountResponse.fromJson(Map<String, dynamic> json) {
    counts = json['counts'];
    errorMsg = json['errorMsg'];
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['counts'] = this.counts;
    data['errorMsg'] = this.errorMsg;
    data['status'] = this.status;
    return data;
  }
}