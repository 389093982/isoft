<template>
  <div class="isoft_container isoft_bg_white isoft_pd20" style="min-height: 500px;">
    <div style="margin: 10px 0;border-bottom: 2px solid #bcbcbc;">
      <span class="isoft_font_header" @click="$router.push({path:'/found/list/newsList'})">新闻</span>
      <span class="isoft_font_header" @click="$router.push({path:'/found/list/activityList'})">活动</span>
    </div>
    <div>
      <router-view/>
    </div>
  </div>
</template>

<script>

  export default {
    name: "FoundList",
  }
</script>

<style scoped>

</style>
