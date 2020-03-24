import cronValidate from "./cron"
import {checkAdminLogin, checkHasLogin, checkNotLogin, checkSSOLogin, getLoginUserName} from "./sso"
import Storage from "./storage"
import {GetUserInfoByNames} from "../api";

export const _store = new Storage();

export const checkEmpty = function checkEmpty(checkStr) {
  if (checkStr == null || checkStr == undefined || checkStr == "") {
    return true;
  }
  return false;
};

export const checkArrayEmpty = function checkArrayEmpty(arr) {
  return arr == null || arr.length === 0;
};

export const checkArrayNotEmpty = function checkArrayNotEmpty(arr) {
  return !checkArrayEmpty(arr);
};

export const checkNotEmpty = function checkNotEmpty(checkStr) {
  return !checkEmpty(checkStr);
};

// 判断值 value 是否是列表 validList 中
export function oneOf(value, validList) {
  for (let i = 0; i < validList.length; i++) {
    if (value === validList[i]) {
      return true;
    }
  }
  return false;
}

// 判断字符串是否包含子串
export function checkContainsInString(str, subStr) {
  return str.indexOf(subStr) != -1
}

// 根据正则验证字符串
export function validatePatternForString(pattern, str) {
  return pattern.test(str);
}

// 校验只能含有字母数字和下划线
export function validateCommonPatternForString(str) {
  var uPattern = /^[a-zA-Z0-9_]{1,}$/;
  return uPattern.test(str);
}

// 校验用户名
export function validateUserName(username) {
  // 6至20位，以字母开头，字母，数字，减号，下划线!
  var uPattern = /^[a-zA-Z]([-_a-zA-Z0-9]{5,19})+$/;
  return uPattern.test(username);
}

// 校验邮箱
export function validateEmail(email) {
  var uPattern = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
  return uPattern.test(email);
}

// 校验密码
export function validatePasswd(passwd) {
  // 最少6位，至少1个大小写字母，数字和特殊字符!
  var pPattern = /^.*(?=.{6,})(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$/;
  return pPattern.test(passwd);
}

// 时间格式化
export function formatDate(date, fmt) {
  let o = {
    'M+': date.getMonth() + 1, // 月份
    'd+': date.getDate(), // 日
    'h+': date.getHours(), // 小时
    'm+': date.getMinutes(), // 分
    's+': date.getSeconds(), // 秒
    'S': date.getMilliseconds() // 毫秒
  }
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
  }
  for (var k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
    }
  }
  return fmt;
}

export const MapAttrsForArray = function (arrs, attrName) {
  let attrs = arrs.map(arr => arr[attrName]);
  return attrs;
}

export const RenderUserInfoByName = async function (user_name) {
  const result = await GetUserInfoByNames({usernames: user_name});
  let userInfos = [];
  if (result.status === "SUCCESS") {
    userInfos = result.users;
  }
  return new Promise(function (resolve, reject) {
    resolve(userInfos);
  });
};

// 根据用户名查询用户昵称,并且将昵称拼装在原来的数组中
export const FillUserNickNameInfoByName = async function (item, attrName) {
  const items = FillUserNickNameInfoByNames([item], attrName);
  // 返回原始数组
  return new Promise(function (resolve, reject) {
    resolve(items[0]);
  });
};

// 根据用户名查询用户昵称,并且将昵称拼装在原来的数组中
export const FillUserNickNameInfoByNames = async function (arrs, attrName) {
  // 获取所有的用户名
  let user_names = MapAttrsForArray(arrs, attrName);
  // 去重并发送请求
  user_names = Array.from(new Set(user_names));
  const result = await GetUserInfoByNames({usernames: user_names.join(",")});
  let userInfos = [];
  if (result.status === "SUCCESS") {
    userInfos = result.users;
  }
  // 拼装填充用户昵称
  for (var i = 0; i < arrs.length; i++) {
    let item = arrs[i];
    let user_names = userInfos.filter(userinfo => userinfo.user_name === item[attrName]);
    if (user_names != null && user_names.length > 0) {
      item["_nick_name"] = user_names[0].nick_name;
      item["_small_icon"] = user_names[0].small_icon;
    }
    arrs[i] = item;
  }
  // 返回原始数组
  return new Promise(function (resolve, reject) {
    resolve(arrs);
  });
};

export const RenderUserInfoByNames = async function (arrs, attrName) {
  let user_names = MapAttrsForArray(arrs, attrName);
  user_names = Array.from(new Set(user_names));
  const result = await GetUserInfoByNames({usernames: user_names.join(",")});
  let userInfos = [];
  if (result.status === "SUCCESS") {
    userInfos = result.users;
  }
  return new Promise(function (resolve, reject) {
    resolve(userInfos);
  });
};

export const RenderUserIcon = function (userInfos, user_name) {
  if (userInfos != null) {
    let user_names = userInfos.filter(userinfo => userinfo.user_name === user_name);
    if (user_names != null && user_names.length > 0) {
      return user_names[0].small_icon;
    }
  }
  return null;
};

export const RenderNickName = function (userInfos, user_name) {
  if (userInfos != null) {
    let user_names = userInfos.filter(userinfo => userinfo.user_name === user_name);
    if (user_names != null && user_names.length > 0) {
      return user_names[0].nick_name;
    }
  }
  return user_name;
};

export const RenderVipLevel = function (userInfos, user_name) {
  if (userInfos != null) {
    let user_names = userInfos.filter(userinfo => userinfo.user_name === user_name);
    if (user_names != null && user_names.length > 0) {
      return user_names[0].vip_level;
    }
  }
  return user_name;
};

export const RenderHatInUse = function (userInfos, user_name) {
  if (userInfos != null) {
    let user_names = userInfos.filter(userinfo => userinfo.user_name === user_name);
    if (user_names != null && user_names.length > 0) {
      return user_names[0].hat_in_use;
    }
  }
  return user_name;
};

export const CheckSSOLogin = (to, from, next) => checkSSOLogin(to, from, next);
export const CheckNotLogin = () => checkNotLogin();
export const CheckHasLogin = () => checkHasLogin();
export const GetLoginUserName = () => getLoginUserName();
export const CheckAdminLogin = () => checkAdminLogin();

export const validateCron = (cron) => cronValidate(cron);

export function goToTargetLink(url) {
  // 打开新窗口跳转
  window.open(url, '_blank');
}

// 字符串分割函数
export function strSplit(str, sep) {
  return str.split(sep);
}

// 字符串重复 n 次
export function getRepeatStr(str, n) {
  return new Array(n + 1).join(str);
}

/**
 * 数组元素交换位置
 * @param {array} arr 数组
 * @param {number} index1 添加项目的位置
 * @param {number} index2 删除项目的位置
 * index1和index2分别是两个数组的索引值，即是两个要交换元素位置的索引值，如1，5就是数组中下标为1和5的两个元素交换位置
 */
export function swapArray(arr, index1, index2) {
  arr[index1] = arr.splice(index2, 1, arr[index1])[0];
  return arr;
}

export function joinArray(arr1, arr2) {
  [].push.apply(arr1, arr2);
  return arr1;
}


var timestamp1 = new Date().getTime();

export function checkFastClick() {
  var timestamp2 = new Date().getTime();
  var diff = timestamp2 - timestamp1;
  timestamp1 = timestamp2;  // 记录上一次点击时间
  return diff < 2000;
}

export function handleSpecial(data) {
  // 1. + URL 中+号表示空格 %2B
  // 2. 空格 URL中的空格可以用+号或者编码 %20
  // 3. / 分隔目录和子目录 %2F
  // 4. ? 分隔实际的 URL 和参数 %3F
  // 5. % 指定特殊字符 %25
  // 6. # 表示书签 %23
  // 7. & URL 中指定的参数间的分隔符 %26
  // 8. = URL 中指定参数的值 %3D
  data = data.replace(/\+/g, "%2B");
  data = data.replace(/\%/g, "%25");
  data = data.replace(/\#/g, "%23");
  data = data.replace(/\&/g, "%26");
  return data;
}

export function CheckHasLoginConfirmDialog(node, pushObj) {
  var _this = node;
  _this.$store.dispatch('ShowCheckHasLoginConfirmDialog', {
    "callback": function () {
      _this.$router.push(pushObj);
    }
  });
}

export function CheckHasLoginConfirmDialog2(node, callback) {
  var _this = node;
  _this.$store.dispatch('ShowCheckHasLoginConfirmDialog', {
    "callback": function () {
      callback();
    }
  });
}

export function copyObj(obj) {
  return JSON.parse(JSON.stringify(obj));
}

// 跨域设置 cookie
export function setCookie(c_name, value, expiredays, domain) {
  if (checkContainsInString(domain, "localhost")) {
    // instead for localhost you should use false
    domain = false
  }
  var exdate = new Date();
  exdate.setDate(exdate.getDate() + expiredays);
  //判断是否需要跨域存储
  if (domain) {
    // egg：path=/;domain=xueersi.com";
    document.cookie = c_name + "=" + escape(value) + ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString()) + ";path=/;domain=" + domain;
  } else {
    document.cookie = c_name + "=" + escape(value) + ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString()) + ";path=/";
  }
}
