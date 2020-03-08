<template>
  <div
    style="margin:15px;padding:50px;background-color: #fff;border: 1px solid #e6e6e6;border-radius: 4px;min-height: 500px;">
    <GoodMeta v-if="good" :good="good">
      <div slot="footer" style="text-align: right;margin-right: 100px;">
        <span class="isoft_button_red2 isoft_point_cursor">在线咨询</span>
        <span class="isoft_button_red2 isoft_point_cursor">立即购买</span>
      </div>
    </GoodMeta>

    <div>
      <BusinessDetail2/>
    </div>
  </div>
</template>

<script>
  import {GetGoodDetail} from "../../api"
  import IBeautifulLink from "../Common/link/IBeautifulLink";
  import BusinessDetail2 from "./BusinessDetail2";
  import GoodMeta from "./GoodMeta";
  import GoodShortcut from "./GoodShortcut";

  export default {
    name: "BusinessDetail",
    components: {GoodShortcut, GoodMeta, BusinessDetail2, IBeautifulLink},
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
