<template>
  <!--左页眉-->
  <div id="headLineId">
    <div style="float: left;width: 30%">
      <code class="headLineClass">收藏本站</code>
      <code class="BlackSpaceClass">&nbsp;&nbsp;|&nbsp;&nbsp;</code>
      <code class="headLineClass">手机APP</code>
      <code class="BlackSpaceClass">&nbsp;&nbsp;|&nbsp;&nbsp;</code>
      <code class="headLineClass">公众号</code>
    </div>
    <!--右页眉-->
    <div style="float: right;width: 45%">
      <code class="headLineClass" @click="login">你好,请登录</code>
      <code class="BlackSpaceClass">&nbsp;</code>
      <code class="headLineClass" style="color: #e33333" @click="regist">免费注册</code>
      <code class="BlackSpaceClass">&nbsp;&nbsp;|&nbsp;&nbsp;</code>
      <code class="headLineClass">消息</code>
      <code class="BlackSpaceClass">&nbsp;&nbsp;|&nbsp;&nbsp;</code>
      <code class="headLineClass">邮件</code>
      <code class="BlackSpaceClass">&nbsp;&nbsp;|&nbsp;&nbsp;</code>
      <code class="headLineClass">在线客服</code>
      <code class="BlackSpaceClass">&nbsp;&nbsp;|&nbsp;&nbsp;</code>
      <code class="headLineClass">充值</code>
      <code class="BlackSpaceClass">&nbsp;&nbsp;|&nbsp;&nbsp;</code>
      <code class="headLineClass">退出</code>
    </div>
    <div style="clear: both"></div>
  </div>
</template>

<script>
	export default {
		name: "headLine",
    data(){
      return{

      }
    },
    methods:{
      login:async function () {
        this.$Message.info("Login");
        let result = await Login({});
      },
      regist:async function () {
        this.$Message.info("Regist");
        let result = await Regist({});
      },

    },
	}
</script>

<style scoped>
  #headLineId{
    background-color: #e3e4e5;padding: 5px 0 5px 30px
  }
  .headLineClass{
    color: #999;font-size: 13px;cursor: pointer;
  }
  .BlackSpaceClass{
    color: #999;font-size: 13px
  }
</style>
