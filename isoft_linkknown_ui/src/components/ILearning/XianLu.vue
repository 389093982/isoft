<template>
  <div class="xianlu_box">
    <div class="header">Java 教学线路图</div>
    <div class="content">
      <div class="left">
        <div class="title">
          精品课程体系 1.0 落地
          <Icon type="ios-arrow-down" class="arrow"/>

          <div class="banji">
            baoban
          </div>
        </div>
        <img src="../../../static/images/index/xianlu.png"/>
      </div>
      <div class="right">
        <ul>
          <li></li>
          <li></li>
          <li></li>
          <li></li>
        </ul>
     </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "XianLu"
  }
</script>

<style scoped>
  .xianlu_box {
    width: 90%;
    margin: 50px auto;
    background-color: #fafafa;
    min-height: 200px;
  }
  .xianlu_box > .header {
    height: 100px;
    line-height: 100px;
    text-align: center;
    font-size: 40px;
  }
  .xianlu_box > .content > .left {
    width: 60%;
    display: inline-block;
    min-height: 400px;
    padding: 10px;
  }
  .xianlu_box > .content > .left .title {
    height: 50px;
    line-height: 50px;
    background-image: linear-gradient(to right, #d8d8d8, #e8e8e8);
    margin-bottom: 5px;
    font-size: 18px;
    padding: 0 20px;
    position: relative;
  }
  .xianlu_box > .content > .left .title > .banji {
    position: absolute;
    width: 100%;
    height: 0;
    padding: 10px;
    bottom: -5px;
    left: 0;
    transform: translateY(100%);
    background-color: white;
    visibility: hidden;
    transition: height 1s linear;
  }
  .xianlu_box > .content > .left .title:hover > .banji {
    visibility: visible;
    height: 500px;
  }
  .xianlu_box > .content > .left .title > .arrow {
    float: right;
    margin-top: 20px;
    cursor: pointer;
  }
  .xianlu_box > .content > .left .title > .arrow:hover {
    color: darkorange;
    transform: rotateZ(180deg);
  }
  .xianlu_box > .content > .left img {
    width: 100%;
    height: 500px;
  }
  .xianlu_box > .content > .right {
    width: 38%;
    display: inline-block;
    min-height: 400px;
  }
  .xianlu_box > .content > .right {
    background-color: white;
  }
  .xianlu_box > .content > .right ul li {
    list-style: none;
    float: left;
    width: 50%;
    height: 250px;
  }
  .xianlu_box > .content > .right ul li:nth-child(1),.xianlu_box > .content > .right ul li:nth-child(3) {
    border-right: 1px dashed green;
  }
  .xianlu_box > .content > .right ul li:nth-child(1),.xianlu_box > .content > .right ul li:nth-child(2) {
    border-bottom: 1px dashed green;
  }
</style>
