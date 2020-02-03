<template>
  <div class="isoft_bg_white isoft_pd20">
    <p>
      <span>{{advertisement.advertisement_label}}</span> &nbsp;&nbsp;
      <span>累计访问次数：<span style="color: red;">{{advertisement.access_num}}</span></span> &nbsp;&nbsp;
      <span style="color: green;">说明：统计依据以最近 7 天不重复访问 ip 作为一次计数，只展示最近 50 条访问记录</span>
    </p>

    <div v-if="advertisement_logs" style="margin-top: 30px;">
      <Row v-for="(advertisement_log,index) in advertisement_logs"
           style="border-bottom: 1px solid #d7dde4;padding: 5px 0px;">
        <Col span="12">{{advertisement_log.access_ip}}</Col>
        <Col span="12">{{advertisement_log.last_updated_time}}</Col>
      </Row>
    </div>
  </div>
</template>

<script>
  import {GetAdvstAccessLog} from "../../api"

  export default {
    name: "AccessLog",
    data() {
      return {
        advertisement: null,
        advertisement_logs: null,
      }
    },
    methods: {
      refreshAdvstAccessLog: async function (id) {
        const result = await GetAdvstAccessLog({id: id});
        if (result.status == "SUCCESS") {
          this.advertisement = result.advertisement;
          this.advertisement_logs = result.advertisement_logs;
        }
      },

    },
    mounted() {
      if (this.$route.query.id != null) {
        this.refreshAdvstAccessLog(this.$route.query.id);
      }
    }
  }
</script>

<style scoped>

</style>
