import {checkContainsInString, checkEmpty, setCookie} from "./index"

const _checkAdminLogin = function () {
  let roleName = localStorage.getItem("roleName");
  return roleName === "admin" && !_checkHasExpired();

}

const _checkHasLogin = function () {
  let userName = localStorage.getItem("userName");
  var isLogin = localStorage.getItem("isLogin");
  var token = localStorage.getItem("tokenString");
  return !checkEmpty(userName) && !checkEmpty(isLogin) && !checkEmpty(token) && isLogin === "isLogin" && !_checkHasExpired();
};

const _checkHasExpired = function () {
  var expiredTime = localStorage.getItem("expiredTime");
  if (expiredTime != null && new Date().getTime() < expiredTime) {
    return false;
  }
  return true;
}

const _getLoginUserName = function () {
  return _checkHasExpired() ? "" : localStorage.getItem("userName");
};

// sso 登陆拦截
const _checkSSOLogin = function (to, from, next) {
  // 1、必须要登录但是没登录
  // 2、必须要 admin 登录但是没登录或者不是 admin 登录
  if ((_mustLogin(to.path) && !_checkHasLogin()) || (_mustAdminLogin(to.path) && (!_checkHasLogin() || !_checkAdminLogin()))) {
    if (!checkContainsInString(from.path, "/sso/login/")) {
      // 跳往登录页面
      window.location.href = "/sso/login/?redirectUrl=" + window.location.href;
    }
  } else {
    next();
  }
};

const _mustLogin = function (target) {
  // 注册必须要登录的地址白名单
  return target.indexOf("/ilearning/courseSpace") >= 0;
}

const _mustAdminLogin = function (target) {
  // 包含 /background/ 必须 admin 登录
  return checkContainsInString(target, "/background/");
}

const _checkNotLogin = function () {
  if (checkContainsInString(window.location.href, "/sso/login") || checkContainsInString(window.location.href, "/sso/regist")) {
    return true;
  }
  return false;
}

const _deleteLoginInfo = function () {
  localStorage.removeItem("tokenString");
  localStorage.removeItem("userName");
  localStorage.removeItem("isLogin");
  localStorage.removeItem("expiredTime");
}

const _getNickName = function () {
  return _checkHasExpired() ? "" : localStorage.getItem("nickName");
}

const _setLoginInfo = function (loginResult, username) {
  localStorage.setItem("tokenString", loginResult.tokenString);
  localStorage.setItem("userName", username);
  localStorage.setItem("nickName", loginResult.nickName);
  localStorage.setItem("isLogin", "isLogin");
  localStorage.setItem("roleName", loginResult.roleName);
  let expiredTime = new Date().getTime() + loginResult.expireSecond * 1000;     // 毫秒数
  localStorage.setItem("expiredTime", expiredTime);
  setCookie("tokenString", loginResult.tokenString, 365, loginResult.domain);
}

export const checkSSOLogin = (to, from, next) => _checkSSOLogin(to, from, next);
export const checkNotLogin = () => _checkNotLogin();
export const checkHasLogin = () => _checkHasLogin();
export const checkAdminLogin = () => _checkAdminLogin();
export const getLoginUserName = () => _getLoginUserName();
export const deleteLoginInfo = () => _deleteLoginInfo();
export const getNickName = () => _getNickName();
export const setLoginInfo = (loginResult, username) => _setLoginInfo(loginResult, username);
