<template>
  <div id="footer">
    <BackTop>
      <div class="top">返回</div>
    </BackTop>

    <!--友情链接-->
    <Row class="isoft_bg_white isoft_pd10">
      <FrindLink/>
    </Row>

    <Row class="isoft_bg_white isoft_pd10" style="border-top: 1px solid gainsboro">
      <Col span="4" style="padding: 50px;">
        &nbsp;
      </Col>
      <Col span="4" style="padding: 50px;">
        <dl>
          <dd>新手帮助</dd>
          <dt @click="$router.push({path:'/user/guide'})">新手引导</dt>
        </dl>
      </Col>
      <Col span="4" style="padding: 50px;">
        <dl>
          <dd>网站简介</dd>
          <dt @click="$router.push({path:'/site/index'})">站点地图</dt>
        </dl>
      </Col>
      <Col span="4" style="padding: 50px;">
        <dl>
          <dd>关于我们</dd>
          <dt @click="$router.push({path:'/job/jobList'})">人才招聘</dt>
          <dt @click="$router.push({path:'/advertisement/apply'})">广告服务</dt>
        </dl>
      </Col>
      <Col span="4" style="padding: 50px;">
        <dl>
          <dd>客服中心</dd>
          <dt @click="$router.push({path:'/ilearning/advise'})">提出意见</dt>
        </dl>
      </Col>
      <Col span="4" style="padding: 50px;">
        &nbsp;
      </Col>
    </Row>

    <Row>
      <div style="width: 100%;height: 255px;padding: 30px 0 0 3px;text-align: center ;background-color: rgb(229, 229, 229)">
        <p style="margin: 40px;" class="hovered hoverLinkColor mr5">Copyright © 2019-2020 LinkKnown 版权所有</p>
        <div style="margin-top: 30px">
          <a href="http://www.beian.miit.gov.cn/" target="_blank" class="hovered hoverLinkColor mr5">ICP备案号:2020627762077</a>
        </div>
      </div>
    </Row>
  </div>
</template>

<script>
  import FrindLink from "./FrindLink"

  export default {
    name: "Footer",
    components: {FrindLink},
    methods: {}
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
</style>
