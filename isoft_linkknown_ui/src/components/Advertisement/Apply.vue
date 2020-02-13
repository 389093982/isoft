<template>
  <div>
    <div class="isoft_bg_white" style="padding: 20px;">
      <p>广告投放说明</p>
      <p class="isoft_font12">1、用户可以在本站点发布广告，每个广告位对应一个链接地址，跳往用户指定站点;</p>
      <p class="isoft_font12">不接受违法、色情、赌博类站点的广告，一经发现永久查封，欢迎大家举报;</p>
      <p class="isoft_font12">广告位租出后不接受退订，未到期不能更换广告位;</p>
      <p class="isoft_font12">广告到期，续租广告者，请及时联系管理员;</p>
      <Button type="success" @click="manageAdvertisement"
              style="position: relative;float:right;right: 20px;bottom: 50px;">管理我的广告
      </Button>
    </div>

    <div class="isoft_bg_white" style="margin: 5px 0 5px 0;padding: 20px;">
      <p style="height: 30px;line-height: 30px;">热门广告列表</p>

      <RandomAdmt :show-right-text="false" :advertisement-amount="10"/>
    </div>

    <div class="isoft_bg_white" style="margin: 5px 0 5px 0;padding: 10px 10px 0 10px;">
      广告费用说明
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
        CheckHasLoginConfirmDialog(this, {path: "/advertisement/manage"});
      }
    }
  }
</script>

<style scoped>


</style>
