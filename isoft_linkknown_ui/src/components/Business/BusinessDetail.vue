<template>
  <div
    style="margin:0 15px;padding:50px;background-color: #fff;border: 1px solid #e6e6e6;border-radius: 4px;min-height: 500px;">
    <GoodMeta v-if="good" :good="good">
      <div slot="footer">
        <span class="isoft_button_red2 isoft_point_cursor">立即购买</span>
      </div>
    </GoodMeta>

    <div>
      <BusinessDecorate v-if="good" :good="good"/>
    </div>
  </div>
</template>

<script>
  import {GetGoodDetail} from "../../api"
  import IBeautifulLink from "../Common/link/IBeautifulLink";
  import BusinessDecorate from "./BusinessDecorate";
  import GoodMeta from "./GoodMeta";
  import GoodShortcut from "./GoodShortcut";

  export default {
    name: "BusinessDetail",
    components: {GoodShortcut, GoodMeta, BusinessDecorate, IBeautifulLink},
    data() {
      return {
        good: null,
      }
    },
    methods: {
      refreshGoodDetail: async function (good_id) {
        const result = await GetGoodDetail(good_id);
        if (result.status === "SUCCESS") {
          this.good = result.good;
        }
      }
    },
    mounted() {
      if (this.$route.query.id != undefined && this.$route.query.id != null) {
        this.refreshGoodDetail(this.$route.query.id);
      }
    }
  }
</script>

<style scoped>

</style>
