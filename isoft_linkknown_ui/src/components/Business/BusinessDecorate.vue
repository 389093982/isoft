<template>
  <div>
    <Tab :tab-counts="3" style="margin-top: 30px;">
      <div slot="titlePane1">保障服务</div>
      <div slot="titlePane2">用户评价</div>
      <div slot="titlePane3">店铺信息</div>

      <div slot="tabPane1">tabPane1</div>
      <div slot="tabPane2">
        <p>用户体验：</p>
        <span class="isoft_tag4">沟通顺畅（167）</span>
        <span class="isoft_tag4">专业水平高（167）</span>
        <span class="isoft_tag4">严谨认真（117）</span>
        <span class="isoft_tag4">交付准时（167）</span>
        <span class="isoft_tag4">交付准时（167）</span>
        <span class="isoft_tag4">交付准时（117）</span>
        <span class="isoft_tag4">交付准时（167）</span>
        <span class="isoft_tag4">交付准时（167）</span>
        <span class="isoft_tag4">交付准时（167）</span>
        <span class="isoft_tag4">交付准时（168）</span>
        <span class="isoft_tag4">交付准时（167）</span>
        <span class="isoft_tag4">交付准时（167）</span>
        <span class="isoft_tag4">交付准时（167）</span>
        <span class="isoft_tag4">交付准时（167）</span>
        <span class="isoft_tag4">交付准时（167）</span>
      </div>
      <div slot="tabPane3">
        <img style="width: 80%;height: 450px;"
             src="https://homesitetask.zbjimg.com/homesite/task/流量营销.jpg/origine/2f23cacd-9b1c-42a0-9267-9138eba349ed"/>
        <span v-if="decorate_items" v-for="(decorate_item, index) in decorate_items">
          {{decorate_item.decorate_text}}
        </span>
      </div>
    </Tab>
  </div>
</template>

<script>
  import Tab from "./Tab";
  import {LoadDecorateData} from "../../api"

  export default {
    name: "BusinessDecorate",
    components: {Tab},
    props: {
      good: {
        type: Object,
        default: null,
      }
    },
    data() {
      return {
        decorate: null,
        decorate_items: null,
      }
    },
    methods: {
      refreshLoadDecorateData: async function () {
        const result = await LoadDecorateData({referer_type: "business_decorate", referer_id: this.good.id});
        if (result.status === "SUCCESS") {
          this.decorate = result.decorate;
          this.decorate_items = result.decorate_items;
        }
      }
    },
    mounted() {
      this.refreshLoadDecorateData();
    }
  }
</script>

<style scoped>

</style>
