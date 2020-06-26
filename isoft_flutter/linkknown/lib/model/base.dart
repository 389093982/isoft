class BaseResponse {
  String status;
  String errorMsg;

  bool isSuccess () {
    return status == "SUCCESS";
  }
}

