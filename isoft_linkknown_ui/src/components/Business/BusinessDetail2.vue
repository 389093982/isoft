<template>
  <div>
    <Tab :tab-counts="3" style="margin-top: 30px;">
      <div slot="titlePane1">保障服务</div>
      <div slot="titlePane2">用户评价</div>
      <div slot="titlePane3">店铺信息</div>

      <div slot="tabPane1">tabPane1</div>
      <div slot="tabPane2">tabPane2</div>
      <div slot="tabPane3">tabPane3</div>
    </Tab>
  </div>
</template>

<script>
  import Tab from "./Tab";

  export default {
    name: "BusinessDetail2",
    components: {Tab}
  }
</script>

<style scoped>

</style>
