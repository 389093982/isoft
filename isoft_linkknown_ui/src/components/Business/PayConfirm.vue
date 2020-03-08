<template>
  <div class="isoft_bg_white isoft_mg10 isoft_pd20">
    <div v-if="good">
      <Row>
        <GoodMeta :good="good"/>
      </Row>
      <Row style="text-align: right;" v-if="orderInfo">
        <Button v-if="orderInfo.payment_status == 1">付款</Button>
      </Row>
    </div>
  </div>
</template>

<script>
  import {GetGoodDetail, GetOrderDetail} from "../../api"
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
        const result = await GetGoodDetail(this.$route.query.good_id);
        if (result.status == "SUCCESS") {
          this.good = result.good;
        }
      },
      refreshOrderInfo: async function () {
        const result = await GetOrderDetail(this.$route.query.orderCode);
        if (result.status == "SUCCESS") {
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
