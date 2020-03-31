<template>
  <div class="autoLogin" v-if="showAutoLogin">
    <div class="isoft_color_grey isoft_font16" style="text-align: center;margin: 50px 0 20px 0;">
      <span v-if="!animationend">自动登录中<span v-for="(item, index) in showDotNumber">.</span></span>
      <span v-else>登录成功！</span>
    </div>
    <div style="margin-top: 20px;">
      <div style="text-align: center;position: relative;">
        <img ref="imgAnimation" :class="showImgAnimationFlag ? 'imgAnimation' : ''" class="center" src=""
             style="position: absolute;width: 100px;height: 100px;border-radius: 50%;" @error="defImg()"/>
        <span v-if="animationend" class="myicon-tick-checked center" style="position: absolute;opacity: 0.5"></span>
      </div>
    </div>

    <div style="margin: 150px 0 0 110px;">
      <Button @click="resetImgAnimation()">重置</Button>
      <Button @click="showImgAnimation()">测试效果</Button>
    </div>
  </div>
</template>

<script>
  export default {
    name: "AutoLogin",
    data() {
      return {
        showAutoLogin: false,
        defaultImg: require('../../../assets/default.png'),
        showImgAnimationFlag: false,
        animationend: false,
        showDotTimer: null,   // 修改点数量的动画
        showDotNumber: 0,     // 显示点数量
      }
    },
    methods: {
      defImg() {
        let img = event.srcElement;
        img.src = this.defaultImg;
        img.onerror = null; //防止闪图
      },
      resetImgAnimation: function () {
        this.showImgAnimationFlag = false;
        this.animationend = false;
      },
      showImgAnimation: function () {
        this.resetImgAnimation();
        this.showImgAnimationFlag = true;
        this.$nextTick(() => {  // 数据渲染后才触发
          // 滚动到底部
          this.initEventListener();
        });

      },
      initEventListener() {
        var _this = this;
        this.$refs.imgAnimation.addEventListener("animationstart", function () {
          console.log("animationstart");
          _this.showDotTimer = window.setInterval(function () {
            if (_this.showDotNumber < 3) {
              _this.showDotNumber = _this.showDotNumber + 1;
            } else {
              _this.showDotNumber = 1;
            }
          }, 500);
        });
        this.$refs.imgAnimation.addEventListener("animationend", function () {
          console.log("animationend");
          window.clearInterval(_this.showDotTimer);
          _this.showDotNumber = 0;
          _this.animationend = true;

          setTimeout(function () {
            // 动画执行完成后 1s 关闭对话框
            _this.showAutoLogin = false;
            _this.$store.state.autoLogin = false;
          }, 1000);
        });
      }
    },
    computed: {
      autoLogin() {
        return this.$store.state.autoLogin;　　//需要监听的数据
      }
    },
    watch: {
      autoLogin: function (newData, oldCData) {
        if (newData === true) {  // _this.$store.state.autoLogin === true
          var _this = this;
          // 自动登录动画加载 3 s后消失
          _this.showAutoLogin = true;
          // 动画执行 2s 后结束
          _this.showImgAnimation();
        }
      },
    },
  }
</script>

<style scoped>
  .autoLogin {
    position: fixed;
    width: 350px;
    height: 300px;
    left: 0;
    right: 0;
    margin-left: auto;
    margin-right: auto;
    top: 20%;
    z-index: 999;
    background-color: white;
    box-shadow: 0 0 10px rgba(0, 0, 0, .1);
  }

  .imgAnimation {
    animation: autoLogin 2s linear;
    animation-iteration-count: 1;
    animation-fill-mode: forwards;
    box-sizing: content-box;
  }

  @keyframes autoLogin {
    0% {
      transform: rotateZ(0deg);
    }
    25% {
      transform: rotateZ(90deg);
    }
    50% {
      transform: rotateZ(180deg);
    }
    75% {
      transform: rotateZ(270deg);
    }
    100% {
      transform: rotateZ(360deg);
      box-shadow: 0 0 10px green;
    }
  }

  .center {
    left: 0;
    right: 0;
    margin-left: auto;
    margin-right: auto;
  }

  .myicon-tick-checked {
    display: inline-block;
    position: relative;
    width: 100px;
    height: 100px;
    border-radius: 50%;
  }

  .myicon-tick-checked:before, .myicon-tick-checked:after {
    content: '';
    pointer-events: none;
    position: absolute;
    color: white;
    border: 1px green;
    background-color: green;
  }

  .myicon-tick-checked:before {
    width: 10px;
    height: 10px;
    left: 30%;
    top: 55%;
    transform: skew(0deg, 50deg);
  }

  .myicon-tick-checked:after {
    width: 30px;
    height: 10px;
    left: 40%;
    top: 42%;
    transform: skew(0deg, -50deg);
  }
</style>
