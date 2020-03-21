<template>
  <div>
    <Tab :tab-counts="3" style="margin-top: 30px;">
      <div slot="titlePane1">店铺信息</div>
      <div slot="titlePane2">用户评价</div>
      <div slot="titlePane3">保障服务</div>

      <div slot="tabPane1">
        <!-- 店铺信息 -->
        <span v-if="decorate_items" v-for="(decorate_item, index) in decorate_items">
          <div style="display: flex;">
            <div>
              <img v-if="decorate_item.media_path" :src="decorate_item.media_path" width="220px" height="160px"/>
            </div>
            <div>
              {{decorate_item.decorate_text}}
            </div>
          </div>
          <span>
          </span>
        </span>
      </div>
      <div slot="tabPane2">
        <p>用户印象：</p>
        <VoteTags referer_type="vote_business" referer_id="1111"/>

        <!-- 评论模块 -->
        <IEasyComment :theme_pk="1" theme_type="business_theme_type" style="margin-top: 50px;"/>
      </div>
      <div slot="tabPane3">tabPane3</div>
    </Tab>
  </div>
</template>

<script>
  import Tab from "./Tab";
  import IEasyComment from "../Comment/IEasyComment"
  import {LoadDecorateItems} from "../../api"
  import VoteTags from "../Decorate/VoteTags";

  export default {
    name: "BusinessDecorate",
    components: {VoteTags, Tab, IEasyComment},
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
        const result = await LoadDecorateItems({
          referer_type: "business_decorate",
          referer_id: this.good.id,
          decorate_name: '商品详情装饰位'
        });
        if (result.status === "SUCCESS") {
          this.decorate = result.decorate;
          this.decorate_items = result.decorate_items;
        }
      },
    },
    mounted() {
      this.refreshLoadDecorateItems();
    }
  }
</script>

<style scoped>

</style>
