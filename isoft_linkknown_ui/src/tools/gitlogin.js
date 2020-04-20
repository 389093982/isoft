import {checkEmpty} from "./index"
import {GitHubLogin} from "../api";
import {setLoginInfo} from "./sso";

const config = {
    clientID:'af46aec6d4529d7cd7fd',
  clientSecret:'26ed56923769e65599573e85bfe76d5e6740c838',
};

export const redirectToGitHubLogin = function () {
  window.location.href = "https://github.com/login/oauth/authorize?client_id=" + config.clientID + "&scope=user,public_repo";
};

export const handleAfterGitHubLogin = async function (code, redirectUrl, loginFailedFunc) {
  if (code === localStorage.getItem("github_login_code")) {
    // loginFailedFunc("不能重复提交表单数据，请重新登录！");
    return;
  }
  localStorage.setItem("github_login_code", code);

  const result = await GitHubLogin({
    code: code,
    client_id: config.clientID,
    client_secret: config.clientSecret,
    redirectUrl: redirectUrl,
  });
  if (result.loginSuccess === true || result.loginSuccess === "SUCCESS") {
      setLoginInfo(result);
      if (!checkEmpty(result.redirectUrl)) {
        // 跳往需要跳转的页面,并设置cookie
        window.location.href = result.redirectUrl;
      } else {
        window.location.href = "/";
      }
  } else {
      // 登录失败处理逻辑
      loginFailedFunc(result.errorMsg);
  }
};


