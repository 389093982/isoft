<template>
  <span>
    <div class="box" style="display: inline-block;" @click="showBusiness = true">
      <span style="position: relative;">
        <img style="width: 120px;height: 150px;position: absolute;top: -90px;left: -20px;" src="../../../static/images/logo01.gif"/>
        <!--<img style="width: 120px;height: 150px;position: absolute;top: -90px;left: -20px;" src="../../../static/images/logo02.gif"/>-->
      </span>
      <div style="margin: 15px 5px 5px 50px;color: white;text-align: center;" class="isoft_font16">
        <p>创业必备</p>
        <p>点击我了解</p>
        <div style="position: relative;">
          <img style="position: absolute;right: 10px;top: -33px;width: 10px;height: 17px;" src="../../../static/images/right.png"/>
        </div>
      </div>
    </div>

    <div class="center_box" v-if="showBusiness" style="display: flex;">
      <span class="isoft_point_cursor isoft_close" @click="showBusiness = false"><Icon type="md-close" size="20"/></span>
      <img src="../../../static/images/jianzhi.png" style="width: 280px;height: 450px;"/>
      <div style="width: 100%;padding: 10px 0 10px 20px;display: flex;">
        <div style="width: 60%;padding: 15px 10px;">
          BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB
        </div>
        <div style="width: 40%;border-left: 1px dashed #c5c5c5;padding: 15px 10px;">
          <p style="color: #f60;font-size: 18px;">90%的合作用户</p>
          <p style="font-size: 18px;">通过链知网获得了兼职收入</p>
        </div>
      </div>
    </div>
  </span>
</template>

<script>
  export default {
    name: "HoverTip",
    data (){
      return {
        showBusiness: false,
      }
    }
  }
</script>

<style scoped>
  .box {
    width: 190px;
    height: 74px;
    position: fixed;
    bottom: 80px;
    background: rgba(0,0,0,.75);
    border-top-right-radius: 37px;
    border-bottom-right-radius: 37px;
    cursor: pointer;
    z-index: 100;
    zoom: .8;
    left: -158px;
    transition: left 1s ease;
  }
  .box:hover {
    left: 0;
  }
  .center_box {
    z-index: 10;
    position: fixed;
    top: 80px;
    left: 10%;
    right: 10%;
    padding: 18px 20px;
    background: rgba(248, 248, 248, 1);
    box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.16);
    border-radius: 6px;
  }

  .isoft_close {
    position: absolute;
    right: 15px;
    top: 15px;
    padding: 2px;
    color: #959595;
    background: rgba(248, 248, 248, 1);
    border: 1px solid #959595;
    border-radius: 50%;
    transition: transform 0.5s ease-in;
  }
  .isoft_close:hover {
    color: #000000;
    border: 1px solid #000000;
    transform: rotateZ(360deg);
  }
</style>
