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
      <input @click="showInputPasswordType()" class="focus" name="username" v-model.trim="username" placeholder="手机 / 邮箱..." type="text" style="width: 90%;height: 40px;margin: 15px 0 0 15px;padding-left: 10px " required @keyup.enter="login"/>
      <input v-if="inputPasswordType" type="password" style="display:none">
      <input class="focus" name="passwd" v-model.trim="passwd" placeholder="密码..." type="password" style="width: 90%;height: 40px;margin: 15px 0 0 15px;padding-left: 10px " autocomplete="new-password" required @keyup.enter="login"/>
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
      <input class="isoft_button_blue" id="submit" type="submit" value="登    录" @click="login">
    </div>

    <div id="login_footer">
      <span @click="gotoGithubLogin" class="isoft_point_cursor" style="float: left;color: black;">
        <Icon type="logo-github" size="20" title="使用 github 账号登录"/>
      </span>
      <router-link :to="{path:'/sso/regist', query: { pattern: 1 }}" style="float: right;color: #2e82ff;"><Icon type="ios-arrow-dropright-circle" />立即注册</router-link>
    </div>
  </div>
</template>

<script>
  import {Login} from "../../../api"
  import {checkEmpty, checkNotEmpty, strSplit, getWebIndex} from "../../../tools"
  import {setLoginInfo} from "../../../tools/sso"
  import {redirectToGitHubLogin, handleAfterGitHubLogin} from "../../../tools/gitlogin"
  import {web_index} from "../../../tools/gitlogin";

  export default {
    name: "LoginForm",
    data() {
      return {
        username:'',
        passwd:'',
        showError: false,
        errorMsg: "登录失败!",
        inputPasswordType:false,
      }
    },
    methods: {
      // 登录目前支持邮箱登录和 github 账号登录
      // 登录前需要存储 RedirectUrl，主要用于后台解析 domain 和登录完成后地址跳转
      // RedirectUrl 存储在 localStorage 中，github 登录跳转后的地址无法定制参数，所以需要将 RedirectUrl 存储在 localStorage 中，以便后面跳转使用
      gotoGithubLogin: function (){
        // 登录之前先存储 redirectUrl
        this.storageRedirectUrl();

        redirectToGitHubLogin();
      },
      showInputPasswordType:function(){
        this.inputPasswordType=true;
      },
      storageRedirectUrl: function (){
        let redirectUrl = getWebIndex();
        var arr = strSplit(window.location.href, "?redirectUrl=");
        if (arr.length === 2) {
          redirectUrl = arr[1];
        }
        localStorage.setItem("redirectUrl", decodeURIComponent(redirectUrl));
      },
      login: async function () {
        // 登录之前先存储 redirectUrl
        this.storageRedirectUrl();

        // 准备请求参数
        let redirectUrl = localStorage.getItem("redirectUrl");
        var username = this.username;
        var passwd = this.passwd;
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
        // 实际登录接口
        this.postLogin(username, passwd, redirectUrl);
      },
      postLogin: async function (username, passwd, redirectUrl) {
        var result = await Login({
          username,
          passwd,
          redirectUrl: decodeURIComponent(redirectUrl),
        });
        if (result.loginSuccess === true || result.loginSuccess === "SUCCESS") {
          // 登录成功后将登录信息设置到 cookie 中
          setLoginInfo(result);

          // 成功登录后将用户名和密码存储起来，用于后面再次登录自动填充表单
          localStorage.setItem("__userName", this.username);
          localStorage.setItem("__passwd", this.passwd);
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
      // 自动填充表单
      fillMemoryLoginInfo: function () {
        if (checkNotEmpty(localStorage.getItem("__userName")) && checkNotEmpty(localStorage.getItem("__passwd"))) {
          this.username = localStorage.getItem("__userName");
          this.passwd = localStorage.getItem("__passwd");
        }
      }
    },
    async mounted () {
      // 自动填充表单
      this.fillMemoryLoginInfo();

      if (checkNotEmpty(this.$route.query.code)) {
        await handleAfterGitHubLogin(this.$route.query.code,
          localStorage.getItem("redirectUrl"),
          (msg) => this.$Message.error(msg));
      }
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
    margin: 8px 0 0 15px ;
    margin-bottom: 20px;
  }

  #login_footer {
    position: absolute;
    bottom: 0;
    width: 100%;
    height: 60px;
    line-height: 60px;
    background: white;
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
