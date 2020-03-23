<template>
  <div>
    <div class="isoft_bg_white" style="padding: 20px;">
      <p>广告投放说明</p>
      <p class="isoft_font12">1、用户可以在本站点发布广告，每个广告位对应一个链接地址，跳往用户指定站点;</p>
      <p class="isoft_font12">2、不接受违法、色情、赌博类站点的广告，一经发现永久查封，欢迎大家举报;</p>
      <p class="isoft_font12">3、广告位租出后不接受退订，广告到期，续租广告者，请及时联系管理员;</p>
      <Button type="success" @click="manageAdvertisement"
              style="position: relative;float:right;right: 20px;bottom: 50px;">管理我的广告
      </Button>
    </div>

    <div class="isoft_bg_white" style="margin: 5px 0 5px 0;padding: 20px;">
      <p style="height: 30px;line-height: 30px;">热门广告列表</p>

      <RandomAdmt :show-right-text="false" :advertisement-amount="10"/>
    </div>

    <div class="isoft_bg_white isoft_pd10" style="margin: 5px 0 5px 0;">
      广告使用说明
      <p style="color: #8d8d8d;">
        链知网致力于推动互联网技术分享，整合互联网资源，传播互联网文化，为 IT 人营造一个便捷的学习、办公、创业的环境。新用户注册可免费发布一条时长为 1 个月的广告。
        开通会员可获得 10 条广告资格，且支持链知网和其它友情应用推广资格。
      </p>
    </div>
  </div>
</template>

<script>
  import {CheckHasLoginConfirmDialog} from "../../tools"
  import RandomAdmt from "./RandomAdmt";

  export default {
    name: "Apply",
    components: {RandomAdmt},
    data() {
      return {}
    },
    methods: {
      manageAdvertisement: function () {
        CheckHasLoginConfirmDialog(this, {path: "/advertisement/advManage"});
      }
    }
  }
</script>

<style scoped>


</style>
