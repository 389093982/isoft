class QueryIsAttentionResponse {
  int attentionRecords;
  String errorMsg;
  String status;

  QueryIsAttentionResponse({this.attentionRecords, this.errorMsg, this.status});

  QueryIsAttentionResponse.fromJson(Map<String, dynamic> json) {
    attentionRecords = json['attention_records'];
    errorMsg = json['errorMsg'];
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['attention_records'] = this.attentionRecords;
    data['errorMsg'] = this.errorMsg;
    data['status'] = this.status;
    return data;
  }
}