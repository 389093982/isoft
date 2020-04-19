<template>
  <div id="footer">
    <BackTop>
      <div class="top">返回</div>
    </BackTop>

    <!--友情链接-->
    <Row class="isoft_bg_white isoft_pd10">
      <FrindLink/>
    </Row>

    <Row class="isoft_bg_white isoft_pd10" style="border-top: 1px solid gainsboro;height: 150px;padding: 40px 0 0 0 ">
      <Col span="2">
        &nbsp;
      </Col>
      <Col span="4">
        <div>
          <div><b style="font-size: 16px">新手帮助</b></div>
          <span class="tipHoverOrange" @click="$router.push({path:'/user/guide'})" style="cursor: pointer;font-size: 14px">新手引导</span>
        </div>
      </Col>
      <Col span="4">
        <div>
          <div><b style="font-size: 16px">网站简介</b></div>
          <span class="tipHoverOrange" @click="$router.push({path:'/site/index'})" style="cursor: pointer;font-size: 14px">站点地图</span>
        </div>
      </Col>
      <Col span="4">
        <div>
          <div><b style="font-size: 16px">关于我们</b></div>
          <span class="tipHoverOrange" @click="$router.push({path:'/job/jobList'})" style="cursor: pointer;font-size: 14px">人才招聘</span><br>
          <span class="tipHoverOrange" @click="$router.push({path:'/advertisement/advApply'})" style="cursor: pointer;font-size: 14px">广告服务</span>
        </div>
      </Col>
      <Col span="4">
        <div>
          <div><b style="font-size: 16px">客服中心</b></div>
          <span class="tipHoverOrange" @click="$router.push({path:'/ilearning/advise'})" style="cursor: pointer;font-size: 14px">提出意见</span>
        </div>
      </Col>
      <Col span="4">
        <div>
          <div><b style="font-size: 16px">联系我们</b></div>
          <contact-us></contact-us>
        </div>
      </Col>
    </Row>

    <Row style="background-color: rgb(229, 229, 229)">
      <div style="width: 100%;height: 100px;text-align: center ;">
        <div style="margin-top: 20px;">
          <span class="hovered isoft_hover_red2 mr5">Copyright © 2019-{{getNowYear()}} LinkKnown 版权所有</span>
        </div>
        <div style="margin-top: 20px">
          <router-link to="/ilearning/domainName" target="_blank" class="hovered isoft_hover_red2 mr5">域名证书</router-link>&nbsp;
          <a href="http://www.beian.miit.gov.cn/" target="_blank" class="hovered isoft_hover_red2 mr5">ICP备案号:2020627762077</a>
        </div>
      </div>
    </Row>
  </div>
</template>

<script>
  import FrindLink from "./FrindLink"
  import ContactUs from "../Common/contactUs/contactUs";

  export default {
    name: "Footer",
    components: {ContactUs, FrindLink},
    methods: {
      getNowYear:function () {
        return new Date().getFullYear();
      }
    },
  }
</script>

<style scoped>
  dd {
    font-size: 16px;
    font-weight: bold;
    height: 26px;
    line-height: 26px;
  }

  dt {
    font-size: 14px;
    color: #666;
    cursor: pointer;
  }

  .top {
    padding: 10px;
    background: rgba(0, 153, 229, .7);
    color: #fff;
    text-align: center;
    border-radius: 2px;
  }

  #footer {
    position: relative;
    top: -12px;
  }
  .tipHoverOrange:hover{
    color: #ff6900;
  }
</style>
