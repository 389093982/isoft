<template>
  <span>
    <div class="box" style="display: inline-block;color: red;">
      <span style="position: relative;">
        <img style="width: 120px;height: 150px;position: absolute;top: -90px;left: -20px;" src="../../../static/images/logo01.gif"/>
      </span>
      <div style="margin: 15px 5px 5px 50px;color: white;text-align: center;" class="isoft_font16">
        <p>创业必备</p>
        <p>限时免费领</p>
        <div style="position: relative;">
          <img style="position: absolute;right: 10px;top: -33px;width: 10px;height: 17px;" src="../../../static/images/right.png"/>
        </div>
      </div>
    </div>

    <div>
      ASASASASASASASASASASASASASSSSSSSSSSSdsfsgdrrrrrrrrrrrrrr
    </div>
  </span>
</template>

<script>
  export default {
    name: "HoverTip"
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
</style>
