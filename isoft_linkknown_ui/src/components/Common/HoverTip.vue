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

    <div class="center_box" v-if="showBusiness">
      <span class="isoft_point_cursor" @click="showBusiness = false"
            style="position: absolute;right: 10px;top: 10px;"><Icon type="md-close" size="20"/></span>
      <img src="../../../static/images/jianzhi.png" style="width: 280px;height: 450px;"/>
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
    position: fixed;
    top: 80px;
    left: 10%;
    right: 10%;
    padding: 18px 20px;
    background: rgba(248, 248, 248, 1);
    box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.16);
    border-radius: 6px;
  }
</style>
