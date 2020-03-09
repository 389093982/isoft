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
        <span v-if="decorate_items" v-for="(decorate_item, index) in decorate_items">
          {{decorate_item.decorate_text}}
        </span>
      </div>
    </Tab>
  </div>
</template>

<script>
  import Tab from "./Tab";
  import {LoadDecorateItems} from "../../api"

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
      refreshLoadDecorateItems: async function () {
        const result = await LoadDecorateItems({referer_type: "business_decorate", referer_id: this.good.id, index: 0});
        if (result.status === "SUCCESS") {
          this.decorate = result.decorate;
          this.decorate_items = result.decorate_items;
        }
      }
    },
    mounted() {
      this.refreshLoadDecorateItems();
    }
  }
</script>

<style scoped>

</style>
