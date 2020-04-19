import axios from 'axios'
import {checkNotEmpty, parseUrlParamsToObj} from "./index"
import {Regist} from "../api";

const config = {
  clientID:'af46aec6d4529d7cd7fd',
  clientSecret:'26ed56923769e65599573e85bfe76d5e6740c838',
};

const getAccessTokenByCode = async function (code, loginSuccessFunc) {
  const client_id = config.clientID;
  const client_secret = config.clientSecret;
  const tokenResponse = await axios({
    method: "get",
    url: "/github/login/oauth/access_token",      // proxyTable 中对 /github 的路由进行了代理
    params: {
      client_id,
      client_secret,
      code
    },
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
      "Access-Control-Allow-Methods": "PUT,POST,GET,DELETE,OPTIONS",
      accept: "application/json"
    }
  });
  if (tokenResponse && tokenResponse.data) {
    let obj = parseUrlParamsToObj(tokenResponse.data);
    if (checkNotEmpty(obj.access_token)) {
      await getUserInfoByAccessToken(obj.access_token, loginSuccessFunc);
    }
  }
};

const getUserInfoByAccessToken = async function(access_token, loginSuccessFunc) {
  axios({
    method: "GET",
    url: "/githubapi/user",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
      accept: "application/json",
      Authorization: `token ${access_token}`
    }
  }).then(res => {
    // 使用 login 字段作为用户的唯一标示，因为 email 可能为空
    if (res && res.data) {
      loginSuccessFunc(res.data.login);
    }
  });
};

export const redirectToGitHubLogin = function () {
  window.location.href = "https://github.com/login/oauth/authorize?client_id=" + config.clientID + "&scope=user,public_repo";
};

export const handleGitHubLoginResponse = async function (code, redirectUrl, loginSucessFunc, loginFailedFunc) {
  if (code === localStorage.getItem("github_login_code")) {
    loginFailedFunc("不能重复提交表单数据，请重新登录！");
    return;
  }
  localStorage.setItem("github_login_code", code);

  var loginFlag = false;
  await getAccessTokenByCode(code, async (login) => {
    // 登录成功拿到了用户名后执行 linkknown 登录
    await autoRegistFunc(login);                     // 先尝试去自动注册,已注册则不用注册
    loginSucessFunc(login, login, redirectUrl);      // 去登录
    loginFlag = true;
  });
  if (!loginFlag) {
    // 登录失败处理逻辑
    loginFailedFunc("登录失败，请检查 github 服务是否可以访问！");
  }
};

const autoRegistFunc = async function (userName) {
  // 第三方自动注册,账号、密码、昵称使用一致,第三方注册不需要验证码
  const result = await Regist({
    thirdName: 'github',
    username: userName,
    passwd: userName,
    nickname: userName,
  });
};

