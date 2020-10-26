<template>
  <div style="width: 1200px;margin: 0 auto;text-align: center;padding: 30px 0;">
    <a :href="randomHref()">
      <img src="../../../static/images/index/mianshibaodian.gif"/>
    </a>
  </div>
</template>

<script>
  export default {
    name: "MianShi",
    data () {
      return {
        mianshis: [
          'http://www.wityx.com/',
          'http://www.100mian.com/',
        ]
      }
    },
    methods: {
      randomHref: function () {
        return this.mianshis[Math.floor(Math.random() * this.mianshis.length)];
      }
    }
  }
</script>

<style scoped>

</style>
