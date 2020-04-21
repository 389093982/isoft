<template>
  <div style="height: 60px;background-color: white">
    <div style="float: left;width: 20%;">
      <img src="../../../../static/images/common_img/linkknown_logo02.png" style="width: 80px;height: 35px;margin: 13px 0 0 50px"/>
    </div>
    <div style="float: left;width: 60%;padding: 20px 0 0 0;">
      <div style="background-color: #fef2f2;width:80%;height: 25px;text-align: center;border-radius: 10px;border: 1px #ffb4a8 solid">
        <div style="margin: 2px 0 0 0 ">
          依据《网络安全法》，为保障您的账户安全和正常使用，请尽快
          <a href="javascript:;" @click="$router.push({path:'/sso/regist', query: { pattern: 1 }})"><code style="color: #eb7d37;margin-left: 5px"><u>完善账户信息</u></code></a>
        </div>
      </div>
    </div>
    <div style="float: right;width: 20%;padding: 20px 0 0 0 ">
      <b id="question"></b>
      <span style="font-size: 14px;"><a @click="$router.push({path:'/ilearning/advise'})"><b style="color: #ff6900">意见 / 吐槽</b></a></span>
    </div>
    <div style="clear: both"></div>
  </div>
</template>

<script>
  export default {
    name: "LoginHeader",
    components: {}
  }
</script>

<style scoped>
  #question {
    display: inline-block;
    width: 18px;
    height: 14px;
    margin: 0 5px;
    background: url('../../../../static/images/sso/question_icon.png') no-repeat;
    overflow: hidden;
    vertical-align: middle;
  }
  a {
    color: #666;
    text-decoration: none;
  }
  a:hover {
    color: #E4393C;
    text-decoration: underline;
  }
</style>
