
class LinkKnownError {
  int errorCode;
  String errorMsg;

  LinkKnownError(this.errorCode, this.errorMsg);

  LinkKnownError.unAuthorizedLogin({String msg = "您还未登录.."}) {
    errorCode = 401;
    errorMsg = msg;
  }

}
