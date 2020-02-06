<template>
  <div class="isoft_bg_white isoft_pd10">
    <a @click="$router.push({path:'/ilearning/index'})" class="hovered hvr-grow hoverLinkColor mr5">返回首页</a>

    <a @click="$router.push({path:'/ilearning/about'})"
       class="hovered hvr-grow hoverLinkColor mr5">关于ILearning</a>

    <a @click="$router.push({path:'/user/guide'})" class="hovered hvr-grow hoverLinkColor mr5">站点引导</a>

    <a @click="$router.push({path:'/advertisement/apply'})" class="hovered hvr-grow hoverLinkColor mr5">广告位招租</a>

    <a @click="$router.push({path:'/user/mine/detail',query:{username:'mine'}})"
       class="hovered hvr-grow hoverLinkColor mr5">个人中心</a>

    <a @click="$router.push({ path: '/ilearning/mine/course_space'})" class="hovered hvr-grow hoverLinkColor">我的课程空间</a>
  </div>
</template>

<script>
  export default {
    name: "Site"
  }
</script>

<style scoped>

</style>
