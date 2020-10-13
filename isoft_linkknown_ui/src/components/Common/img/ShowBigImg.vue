<template>
  <!-- 图片放大图 -->
  <div id="enlarge_images" v-if="showBigImg">
    <div class="modalShade"></div>
    <div class="modalClose" @click="hideBig($event)">X</div>
    <img class="modalContent" :src="bigImg" >
  </div>
</template>

<script>
  export default {
    name: "ShowBigImg",
    data () {
      return {
        showBigImg: false,
        bigImg: '',
      }
    },
    methods: {
      showBig: function (imgAddr) {
        this.showBigImg = true;
        this.bigImg= imgAddr;
        document.getElementsByTagName("body")[0].style.overflow = 'hidden';
      },
      hideBig: function (event) {
        this.showBigImg = false;
        document.getElementsByTagName("body")[0].style.overflow = 'visible';
      },
    }
  }
</script>

<style scoped>
  /* 图片放大区域 */
  #enlarge_images .modalShade {
    position: fixed;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    width: 100%;
    height: 100%;
    background-color: black;
    opacity: 0.3;
    z-index: 100;
  }
  #enlarge_images .modalContent {
    width: 80%;
    height: 80%;
    position: fixed;
    left: 50%;
    top: 50%;
    transform: translateX(-50%) translateY(-50%);
    z-index: 101;
  }
  #enlarge_images .modalClose {
    position: fixed;
    right: 10%;
    top: 10%;
    transform: translateX(-20px) translateY(20px);
    color: #979797;
    font-size: 30px;
    cursor: pointer;
    z-index: 102;
    transition: transform 0.2s ease-in-out;
  }
  #enlarge_images .modalClose:hover {
    transform: translateX(-20px) translateY(20px) rotateZ(180deg);
  }
</style>
