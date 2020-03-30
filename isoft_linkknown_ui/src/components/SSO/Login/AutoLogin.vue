<template>
  <div class="autoLogin" v-if="showAutoLogin">
    自动登录动画效果奥
  </div>
</template>

<script>
  export default {
    name: "AutoLogin",
    data() {
      return {
        showAutoLogin: false,
      }
    },
    computed: {
      autoLogin() {
        return this.$store.state.autoLogin;　　//需要监听的数据
      }
    },
    watch: {
      autoLogin: function (newData, oldCData) {
        var _this = this;
        // 自动登录动画加载 2 s后消失
        _this.showAutoLogin = true;
        setTimeout(function () {
          _this.showAutoLogin = false;
          _this.$store.state.autoLogin = false;
        }, 2000);
      },
    },
  }
</script>

<style scoped>
  .autoLogin {
    position: fixed;
    width: 180px;
    height: 200px;
    left: 0;
    right: 0;
    margin-left: auto;
    margin-right: auto;
    top: 20%;
    z-index: 999;
    background-color: rgba(0, 128, 0, 0.07);
  }
</style>
