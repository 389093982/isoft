<template>
</template>

<script>
  import {CheckHasLogin} from "../../tools"
  import {IsVip, refreshVipInfo} from "../../tools/vip"
  import {QueryRechargeRight} from "../../api"

  export default {
    name: "RechargeRight",
    data() {
      return {
        isVip: false,
      }
    },
    methods: {
      refreshRechargeRight: async function () {
        const result = await QueryRechargeRight({});
        if (result.status === "SUCCESS") {
          refreshVipInfo(result.vip_expired_time);
          this.checkVip();
        }
      },
      checkVip: function () {
        this.isVip = IsVip();
      }
    },
    mounted() {
      if (CheckHasLogin()) {
        this.refreshRechargeRight();
      }
    }
  }
</script>

<style scoped>

</style>
