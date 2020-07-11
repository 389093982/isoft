<template>
  <div>

    <div style="float: left;width: 20%;">
      <img src="../../../../static/images/common_img/linkknown_logo02.png" style="width: 80px;height: 35px;margin: 13px 0 0 50px"/>
    </div>
    <div id="header">
      <div class="login-link" id="login_link" style="position: absolute;top: 20px;right: 120px;">
        <span style="font-size: 15px;font-weight: inherit;">已有账号,前去<a href="/sso/login/">登录</a></span>
      </div>
    </div>

    <div style="height: 5px;display: block;width:100%;background: linear-gradient(to right, red, blue);opacity:0.1;margin-top: 8px"></div>

    <Row style="position: relative;top: -30px;">
      <Col span="14">
        <Forget v-if="$route.query.pattern && $route.query.pattern === 2"/>
        <Regist v-else/>
      </Col>
      <Col span="8" style="margin-top: 20px">
        <div style="margin: 40px 0;min-height: 360px;padding:25px;background: linear-gradient(to right, rgba(255, 0, 0, 0.05), rgba(0, 0, 255, 0.06));">
          <h3 style="color: #7800ff;padding: 5px;">
            <Icon type="ios-paper"></Icon>
            账号注册协议
          </h3>
          <hr>
          <div style="font-size: 12px;font-family: Tahoma, Helvetica, 'Microsoft Yahei', 微软雅黑, Arial, STHeiti;">
            <p style="line-height: 30px;">1、现阶段需使用手机 / 邮箱注册并登录</p>
            <p style="line-height: 30px;">2、手机 / 邮箱验证码,有效时间 5 分钟</p>
            <p style="line-height: 30px;">3、本站欢迎大家共同参与学习自己感兴趣的教程， 并分享自己制作的<b>原创</b>视频教程,为linkknown公共学习区域贡献自己的力量</p>
            <p style="line-height: 30px;">4、为制造良好的互联网环境，用户不得在本站发布相关法律、法规禁止的内容， 一经发现，立即封停账号</p>
          </div>
        </div>
      </Col>
    </Row>

    <div>
      <hr style="color: rgba(128,128,128,0.13)">
      <LoginFooter/>
    </div>

  </div>
</template>

<script>
  import LoginFooter from "./LoginFooter"
  import Regist from "./Regist";
  import Forget from "./Forget";

  export default {
    name: "RegistOrForget",
    components: {Regist, Forget, LoginFooter},
    data() {
      return {}
    },
    methods: {}
  }
</script>

<style scoped>
  #header {
    background-color: rgb(255, 255, 255);
    text-align: center;
    height: 50px;
    padding: 5px;
  }

  a:hover {
    color: #E4393C;
    text-decoration: underline;
  }
</style>
