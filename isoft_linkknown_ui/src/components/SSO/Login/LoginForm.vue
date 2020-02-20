<template>
  <div id="login_area">
    <div id="login_header">
      <div style="float: left;width: 50%;">
        <div style="margin: 15px 0 0 20px ">
          <h2>密码登录</h2>
        </div>
      </div>
      <div style="float: left;width: 50%">
        <div style="margin: 15px 0 0 70px ">
          <h2><a style="color:#2E82FF" href="/">访问首页</a></h2>
        </div>
      </div>
      <div style="clear: both"></div>
    </div>

    <div id="login_submit_content">
      <input class="focus" name="username" v-model.trim="username" placeholder="请输入账号" type="text" style="width: 90%;height: 40px;margin: 15px 0 0 15px;padding-left: 10px " required @keyup.enter="login"/>
      <input type="password" style="display:none">
      <input class="focus" name="passwd" v-model.trim="passwd" placeholder="请输入密码" type="password" style="width: 90%;height: 40px;margin: 15px 0 0 15px;padding-left: 10px " autocomplete="new-password" required @keyup.enter="login"/>
      <div style="margin: 0 0 0 0 ">
        <div style="float: left;width: 50%;">
          <div style="margin: 8px 0 0 15px ">
            <span style="font-size: 12px;color: red" v-if="showError">{{errorMsg}}</span>
          </div>
        </div>
        <div style="float: right;width: 50%;">
          <div style="float:right;padding: 8px 15px 0 0 ">
            <router-link :to="{path:'/sso/forget', query: { pattern: 2 }}">忘记密码</router-link>
          </div>
        </div>
        <div style="clear: both"></div>
      </div>
      <input id="submit" type="submit" value="登    录" @click="login">
    </div>

    <div id="login_footer">
      <!--<router-link :to="{path:'/sso/user/loginProblem'}" style="float: left;color: #2e82ff;">常见登录问题</router-link>以后做成可以第三方登录-->
      <router-link :to="{path:'/sso/regist', query: { pattern: 1 }}" style="float: right;color: #2e82ff;"><Icon type="ios-arrow-dropright-circle" />立即注册</router-link>
    </div>
  </div>
</template>

<script>
  import {Login} from "../../../api"
  import {checkEmpty, setCookie, strSplit} from "../../../tools"

  export default {
    name: "LoginForm",
    data() {
      return {
        username:'',
        passwd:'',
        showError: false,
        errorMsg: "登录失败!",
      }
    },
    methods: {
      login: async function () {
        let redirectUrl = "";
        var arr = strSplit(window.location.href, "?redirectUrl=");
        if (arr.length == 2) {
          redirectUrl = arr[1];
        }
        var username = this.username;
        var passwd = this.passwd
        if (username.length === 0) {
          this.showError = true;
          this.errorMsg = '请填写账号';
          return false;
        }
        if (passwd.length === 0) {
          this.showError = true;
          this.errorMsg = '请填写密码';
          return false;
        }
        var result = await Login(username, passwd, decodeURIComponent(redirectUrl));
        if (result.loginSuccess == true || result.loginSuccess == "SUCCESS") {
          setCookie("tokenString", result.tokenString, 365, result.domain);
          setCookie("userName", username, 365, result.domain);
          setCookie("nickName", result.nickName, 365, result.domain);
          setCookie("isLogin", "isLogin", 365, result.domain);
          setCookie("roleName", result.roleName, 365, result.domain);
          let expireSecond = new Date().getTime() + result.expireSecond * 1000;     // 时间戳
          setCookie("expireSecond", expireSecond, 365, result.domain);
          if (!checkEmpty(result.redirectUrl)) {
            // 跳往需要跳转的页面,并设置cookie
            window.location.href = result.redirectUrl;
          } else {
            window.location.href = "/";
          }
        } else {
          this.showError = true;
          this.errorMsg = result.errorMsg;
        }
      },

    }
  }
</script>

<style scoped>
  #login_area {
    width: 320px;
    height: 300px;
    background: white;
    float: right;
    margin-top: 80px;
    margin-right: 150px;
    position: relative;
  }

  #login_header {
    width: 100%;height: 50px;
  }

  #login_submit_content{

  }

  #submit {
    width: 90%;
    height: 40px;
    margin: 8px 0 0 15px ;
    margin-bottom: 20px;
    display: block;
    line-height: 40px;
    font-size: 16px;
    font-weight: 800;
    cursor: pointer;
    color: #fff;
    background: #3f89ec;
    border: 0;
  }

  #login_footer {
    position: absolute;
    bottom: 0;
    width: 100%;
    height: 60px;
    line-height: 60px;
    background: #f0f6ff;
    padding: 0 28px;
  }

  a {
    color: #666;
    text-decoration: none;
  }

  a:hover {
    color: #E4393C;
    text-decoration: underline;
  }

  .focus:focus {
    background-color: #ffffff;
    border-color: #2c5bff;
  }
</style>
