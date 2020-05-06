<template>
  <div class="isoft_bg_white isoft_mg10 isoft_pd20">
    <GoodMeta v-if="good" :good="good">
      <div slot="footer" v-if="orderInfo">
        <span v-if="orderInfo.payment_status === 1" class="isoft_button_red2 isoft_point_cursor">付款</span>
      </div>
    </GoodMeta>
  </div>
</template>

<script>
  import {GetGoodsDetail, GetOrderDetail} from "../../api"
  import GoodMeta from "./GoodMeta";

  export default {
    name: "PayConfirm",
    components: {GoodMeta},
    data() {
      return {
        good: null,
        orderInfo: null,
      }
    },
    methods: {
      refreshGoodDetail: async function () {
        let params = {
          'id':this.$route.query.good_id
        };
        const result = await GetGoodsDetail(params);
        if (result.status === "SUCCESS") {
          this.good = result.good;
        }
      },
      refreshOrderInfo: async function () {
        let params = {
          'orderCode':this.$route.query.orderCode
        };
        const result = await GetOrderDetail(params);
        if (result.status === "SUCCESS") {
          this.orderInfo = result.orderInfo;
        }
      }
    },
    mounted() {
      this.refreshGoodDetail();
      this.refreshOrderInfo();
    }
  }
</script>

<style scoped>

</style>
