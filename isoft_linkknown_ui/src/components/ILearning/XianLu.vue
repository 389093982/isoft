<template>
  <div class="xianlu_box">
    <div class="header">Java 教学线路图</div>
    <div class="content">
      <div class="left">
        <div class="title">
          精品课程体系 2.0 落地
          <Icon type="ios-arrow-down" class="arrow"/>

          <div class="banji">
            其它补习班学习编程费用基本上 1.5w ~ 2.8w 元，链知网开班在即：只需 6800 元即可

            boss 直聘

            <div style="border: 1px dashed lightgray;">
              <div class="banjiDiv">
                <ul class="clear">
                  <li>
                    <span class="date">预计开班日期：2020-09-10</span>
                    <span class="operate" title="已过期">预约报名</span>
                  </li>
                  <li>
                    <span class="date">预计开班日期：2020-10-10</span>
                    <span class="operate" title="已过期">预约报名</span>
                  </li>
                  <li>
                    <span class="date">预计开班日期：2020-11-10</span>
                    <span class="operate enable" title="开班在即">预约报名</span>
                  </li>
                  <li>
                    <span class="date">预计开班日期：2020-12-10</span>
                    <span class="operate enable" title="开班在即">预约报名</span>
                  </li>
                </ul>
              </div>
              <span class="isoft_button_red3" style="width: 50%;margin: 10px auto;">我要咨询</span>
            </div>
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
    /*height: 0;*/
    height: 500px;
    padding: 10px 40px;
    bottom: -5px;
    left: 0;
    transform: translateY(100%);
    background-color: white;
    /*visibility: hidden;*/
    transition: height 1s linear;
  }
  .xianlu_box > .content > .left .title:hover > .banji {
    visibility: visible;
    height: 500px;
  }
  .xianlu_box > .content > .left .title > .banji .banjiDiv li {
    list-style: none;
    width: 50%;
    float: left;
    text-align: center;
    border-bottom: 1px dashed lightgray;
  }
  .xianlu_box > .content > .left .title > .banji .banjiDiv li .date {
    font-size: 14px;
    color: grey;
  }
  .xianlu_box > .content > .left .title > .banji .banjiDiv li .operate {
    padding: 4px 10px;
    border-radius: 4px;
    background-color: #c0c0c0;
    font-size: 14px;
    color: white;
    margin-left: 8px;
  }
  .xianlu_box > .content > .left .title > .banji .banjiDiv li .operate.enable {
    cursor: pointer;
    background-color: red;
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
