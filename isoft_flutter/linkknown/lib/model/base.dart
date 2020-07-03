
class BaseResponse {
  String status;
  String errorMsg;

  BaseResponse();

  BaseResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    errorMsg = json['errorMsg'];
  }

  bool isSuccess () {
    return status == "SUCCESS";
  }
}
