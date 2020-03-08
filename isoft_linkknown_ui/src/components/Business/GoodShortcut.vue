<template>
  <span style="font-size: 12px;color: #999;float: right;">
    <span class="pl">
      <div style="cursor: pointer;display: inline-block;" class="amount">
        评论数:0
        <BusinessComment class="comment" style="position: absolute;z-index: 999;"/>
      </div>
    </span>
    &nbsp;&nbsp;&nbsp;&nbsp;
    <span class="cjl">
      <span class="amount">成交量:0</span>
      <span class="info">专业的事找专业的人，<a @click="showExpertise">我要发挥我的专长</a></span>
    </span>

    <div>
      <!-- 专长 -->
      <Expertise ref="expertise"/>
    </div>
  </span>
</template>

<script>
  import BusinessComment from "./BusinessComment"
  import Expertise from "./Expertise"

  export default {
    name: "GoodShortcut",
    components: {BusinessComment, Expertise},
    methods: {
      showExpertise: function () {
        this.$refs.expertise.showModal();
      },
    }
  }
</script>

<style scoped>
  .cjl .info {
    display: none;
  }

  .cjl:hover .info {
    display: inline-block;
  }

  .cjl:hover .amount {
    display: none;
  }

  .pl .amount .comment {
    display: none;
  }

  .pl .amount:hover .comment {
    display: block;
  }
</style>
