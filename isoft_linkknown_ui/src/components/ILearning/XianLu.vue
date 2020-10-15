<template>
  <div class="xianlu_box">
    <div class="header">Java 教学线路图</div>
    <div class="content">
      <div class="left">
        <div class="title">
          精品课程体系 2.0 落地
          <Icon type="ios-arrow-down" class="arrow"/>

          <div class="banji">
            其它补习班学习编程费用基本上 1.5w ~ 2.8w 元，<br>
            链知网开班在即：只需 6800 元即可
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
              <div class="jiuye_car" style="text-align: right;margin: 20px 40px 10px 0;">
                <span style="position:relative;top:-8px;color: grey;font-size: 14px;">就业直通车：</span>
                <a href="https://www.zhipin.com/shanghai/" title="boss 直聘"><img src="../../../static/images/index/jiuye_boss.png"/></a>
                <a href="https://login.51job.com/login.php" title="前程无忧"><img src="../../../static/images/index/jiuye_51.png"/></a>
                <a href="https://landing.zhaopin.com/" title="智联招聘"><img src="../../../static/images/index/jiuye_zhilian.png"/></a>
                <a href="https://www.liepin.com/" title="猎聘网"><img src="../../../static/images/index/jiuye_lieping.png"/></a>
              </div>
            </div>
          </div>
        </div>
        <img src="../../../static/images/index/xianlu.png" @click="showBig($event)"/>

        <ShowBigImg ref="showBigImg"/>
      </div>
      <div class="right">
        <ul class="rightUl clear" :style="rightUlStyles">
          <li @mouseenter="hoverIndex = 0">
            <div style="margin:10px 0 0 5px">
              <h3>周锐讲师</h3>
            </div>
            <div style="margin:5px 5px 0 5px;font-size: 12px;line-height: 20px;height: 100px;">
              华为商城设计、开发负责人。
              有着丰富的开发和教学经验，曾参与大型电商项目、网页和微信移动小程序开发工作，负责前端开发工作。
              精通html，css，js等编程语言和数据库系统，对前端主流框架Vue及原生js有着深入的研究。
              热衷于教学，激情派讲师，痴迷于新技术的推广与教学
            </div>
          </li>
          <li @mouseenter="hoverIndex = 1">
            <div style="margin:10px 0 0 5px">
              <h3>二月讲师</h3>
            </div>
            <div style="margin:5px 5px 0 5px;font-size: 12px;line-height: 20px;height: 100px;">
              原东北收单行，外卡收单技术总监。
              从事JavaEE技术多年，一直致力于Java技术培训，热爱教育。
              深入掌握Spring、SpringMVC、MyBatis、SpringBoot,通过分析源码，讲解框架设计原理。
              曾参与开发了九台农商行外卡收单系统；某国企卡管、网关系统等。
            </div>
          </li>
          <li @mouseenter="hoverIndex = 2">
            <div style="text-align: center;margin-top: 50px;color: blue">
              <h3>官网试听，线下授课</h3>
            </div>
            <div style="text-align: center;margin-top: 20px">
              "助力职场第一棒 ^_^ "
            </div>
          </li>
          <li @mouseenter="hoverIndex = 3">
            <!-- QQ 群 -->
            <ul class="qqUl">
              <li style="text-align: center;padding-top: 25px;">
                链知网 Java 培训
                <div>
                  <img src="../../../static/images/index/qq_qun1.png" style="width: 50%;height: 50%;"/>
                </div>

                QQ 咨询群
              </li>
            </ul>
          </li>
        </ul>
     </div>
    </div>
  </div>
</template>

<script>
  import ShowBigImg from "../Common/img/ShowBigImg";
  export default {
    name: "XianLu",
    components: {ShowBigImg},
    data () {
      return {
        hoverIndex: 0,    // 四宫格默认选中索引
      }
    },
    methods: {
      // 显示和隐藏放大图
      showBig: function (event) {
        this.$refs.showBigImg.showBig(event.currentTarget.src);
      },
    },
    computed:{
      rightUlStyles () {
        let style = {};
        let color = ['#006c54', '#726930', '#5e7c85', '#6f6d85'][this.hoverIndex];
        style.borderTop = '4px solid ' + color;
        return style;
      }
    }
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
    width: 65%;
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
    padding: 10px 40px;
    bottom: -5px;
    left: 0;
    transform: translateY(100%);
    background-color: white;
    visibility: hidden;
  }
  .xianlu_box > .content > .left .title:hover > .banji {
    visibility: visible;
    height: 450px;
    box-shadow:  0 0 0 1px darkorange;
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
  .xianlu_box > .content > .left > img {
    width: 100%;
    height: 450px;
  }
  .xianlu_box > .content > .right {
    width: 33%;
    display: inline-block;
    min-height: 400px;
  }
  .xianlu_box > .content > .right {
    background-color: white;
  }
  .rightUl {
    border: 1px solid lightgray;
    padding: 10px;
  }
  .rightUl > li {
    list-style: none;
    float: left;
    width: 50%;
    height: 240px;
    cursor: pointer;
  }
  .rightUl > li:nth-child(1),.xianlu_box > .content > .right ul li:nth-child(3) {
    border-right: 1px dashed green;
  }
  .rightUl > li:nth-child(1),.xianlu_box > .content > .right ul li:nth-child(2) {
    border-bottom: 1px dashed green;
  }
  .qqUl > li {
    list-style: none;
  }

  .jiuye_car img {
    width: 25px;
    height: 25px;
  }
</style>
