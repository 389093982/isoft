<template>
  <div class="box" @mouseenter="mouseenter" :style="styles" style="position: relative;" @click="onclick">
    <img :src="srcImg" :style="styles" class="hoverScaleImg" @error="defImg()"/>
    <span v-if="labelText" class="labelText">{{labelText}}</span>
  </div>
</template>

<script>
  export default {
    name: "HoverBigImg",
    props: {
      srcImg: {
        type: String,
        default: "../../../assets/2.jpeg",
      },
      width: {
        type: String,
        default: "154px",
      },
      height: {
        type: String,
        default: "86px",
      },
      labelText: {
        type: String,
        default: "",
      }
    },
    data() {
      return {
        defaultImg: require('../../../assets/default.png'),
      }
    },
    methods: {
      defImg() {
        let img = event.srcElement;
        img.src = this.defaultImg;
        img.onerror = null; //防止闪图
      },
      mouseenter: function () {
        this.$emit("mouseenter");
      },
      onclick: function () {
        this.$emit("onclick");
      }
    },
    computed: {
      styles() {
        let style = {};
        style.width = this.width;
        style.height = this.height;
        return style;
      }
    },
  }
</script>

<style scoped>
  .box {
    overflow: hidden;
  }
  .box img {
    cursor: pointer;
    transition: all 2s;
  }
  .box img:hover {
    transform: scale(1.4);
  }

  .labelText {
    position: absolute;
    bottom: 0;
    width: 100%;
    height: 30px;
    line-height: 30px;
    text-align: center;
    background-color: rgba(0, 0, 0, 0.6);
    color: white;
  }
</style>
