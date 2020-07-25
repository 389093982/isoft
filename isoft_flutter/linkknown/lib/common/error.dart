
class LinkKnownError {
  int errorCode;
  String errorMsg;

  LinkKnownError(this.errorCode, this.errorMsg);

  LinkKnownError.unAuthorizedLogin({String msg = "UnAuthorizedLogin"}) {
    errorCode = 401;
    errorMsg = msg;
  }

}
