import {CheckHasLogin} from "./index";

export const refreshVipInfo = function (vip_expired_time) {
  if (CheckHasLogin()) {
    localStorage.setItem("vip_expired_time", vip_expired_time);
  } else {
    localStorage.removeItem("vip_expired_time");
  }
};

export const IsVip = function () {
  let vip_expired_time = localStorage.getItem("vip_expired_time");
  let isNotExpired = new Date(vip_expired_time).getTime() - new Date().getTime() > 0;
  return CheckHasLogin() && isNotExpired;
};
