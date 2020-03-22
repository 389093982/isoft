<template>
  <div v-if="showNotLogin">
    <div class="shade"></div>

    <div class="dialog">
      <div class="header">
        <span class="isoft_font16">温馨提示：</span>
      </div>
      <div class="content">
        <p class="isoft_color_grey3 isoft_font16">您还未登录！前往登录？</p>
        <span class="isoft_point_cursor" style="float: right;">注册账号</span>
      </div>
      <div class="footer">
        <div class="isoft_button_blue" style="width: 40%;position:absolute;left: 20px;" @click="cancel">取消</div>
        <div class="isoft_button_blue" style="width: 40%;position:absolute;right: 20px;" @click="submit">确认</div>
      </div>
    </div>
  </div>
</template>

<script>
  import {CheckHasLogin} from "../../../tools";

  export default {
    name: "CheckHasLoginConfirmDialog",
    data() {
      return {
        showNotLogin: false,
      }
    },
    methods: {
      cancel: function () {
        this.showNotLogin = false;
      },
      submit: function () {
        this.showNotLogin = false;
        window.location.href = "/sso/login/?redirectUrl=" + window.location.href;
      }
    },
    computed: {
      loginCallback() {
        return this.$store.state.CheckHasLoginConfirmDialogCallBack;　　//需要监听的数据
      }
    },
    watch: {
      loginCallback: function (newCallBack, oldCallBack) {
        if (CheckHasLogin()) {
          this.showNotLogin = false;
          newCallBack();
        } else {
          this.showNotLogin = true;
        }
      },
    },
  }
</script>

<style scoped>
  .shade {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: #d0d0d0;
    opacity: 0.6;
    z-index: 9998;
  }

  .dialog {
    position: fixed;
    top: 100px;
    left: 0;
    right: 0;
    width: 400px;
    height: 220px;
    margin-left: auto;
    margin-right: auto;
    background-color: white;
    z-index: 9999;
    transition: all .3s ease;
  }

  .dialog:hover {
    border-radius: 10px;
  }

  .header {
    padding: 0 20px;
    background-color: rgb(107, 103, 255);
    height: 60px;
    line-height: 60px;
    color: white;
  }

  .content {
    padding: 30px 20px;
    height: 100px;
  }

  .footer {
    border-top: 1px solid #eee;
    padding: 10px 20px 0 20px;
    height: 60px;
  }
</style>
