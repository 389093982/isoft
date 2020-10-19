<template>
  <div>
    <div class="bg" style="color: white;">
      <div style="padding: 40px 60px;">
        <div style="float: left;font-size: 20px;color: #ff6400;">
          链知网&nbsp;|&nbsp;<span>商家入驻</span>
        </div>
        <div class="hoverBottomBorder" style="float: right;">
          招商中心
        </div>
      </div>

      <p style="font-size: 50px;line-height: 50px;margin-top: 115px;text-align: center;">
        加入我们，展现最真实的自我
      </p>

      <p style="font-size: 20px;line-height: 40px;text-align: center;margin-top: 50px;">
        网站开发、广告推广、素材设计、商品服务，发现更多精彩，期待您的入驻 全部类别
      </p>

      <div style="text-align: center;margin-top: 50px;">
        <span class="isoft_hover_red" style="font-size: 18px;border: 2px solid white;padding: 10px 30px;">免费入驻</span>
      </div>
    </div>

    <div class="business isoft_bg_white" style="padding: 30px 200px;display: flex;text-align: center;">
      <div style="width: 300px;">
        <img class="img" src="../../../static/images/business/zhuce.png"/>
        <p class="text2 isoft_hover_red isoft_hover_top10">注册链知网账号</p>
      </div>
      <div style="width: 300px;">
        <img class="img" src="../../../static/images/business/shiming.png"/>
        <p class="text2 isoft_hover_red isoft_hover_top10">实名认证并获取资格</p>
      </div>
      <div style="width: 300px;">
        <img class="img" src="../../../static/images/business/xuqiu.png"/>
        <p class="text2 isoft_hover_red isoft_hover_top10">发布您的作品或需求赚钱</p>
      </div>
    </div>


  </div>
</template>

<script>
  export default {
    name: "Introduce"
  }
</script>

<style scoped>
  .bg {
    background: url("../../../static/images/business/bg.jpg") no-repeat;
    background-size: 100% 100%;
    height: 450px;
  }

  .hoverBottomBorder:hover {
    cursor: pointer;
    border-bottom: 1px solid white;
  }

  .business .img {
    border-radius: 50%;
    width: 70px;
    height: 70px;
    padding: 5px;
    background-color: rgba(255, 182, 192, 0.15);
  }

  .business .text2 {
    font-size: 22px;
    line-height: 80px;
  }
</style>
