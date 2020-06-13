// 多端调用接口,移动端、 web 端和本应用交互接口

const android = window.application_android;
const web = window.application_web;

export function getLoginNickName() {
  if (android !== null && android !== undefined) {
    return android.getLoginNickName();
  } else {
    return web.getLoginNickName();
  }
}

export function checkHasLogin() {
  if (android !== null && android !== undefined) {
    return android.checkHasLogin();
  } else {
    return web.checkHasLogin();
  }
}
