<template>
  <div class="isoft_bg_white isoft_pd10">
    <a @click="$router.push({path:'/ilearning/index'})" class="hovered hvr-grow hoverLinkColor mr5">返回首页</a>
    <a @click="$router.push({path:'/ilearning/about'})" class="hovered hvr-grow hoverLinkColor mr5">关于LinkKnown</a>
    <a @click="$router.push({path:'/user/guide'})" class="hovered hvr-grow hoverLinkColor mr5">站点引导</a>
    <a @click="$router.push({path:'/advertisement/apply'})" class="hovered hvr-grow hoverLinkColor mr5">广告位招租</a>
    <a @click="$router.push({path:'/user/detail',query:{username:'mine'}})" class="hovered hvr-grow hoverLinkColor mr5">个人中心</a>
    <a @click="$router.push({ path: '/ilearning/course_space'})" class="hovered hvr-grow hoverLinkColor">我的课程空间</a>
    <a @click="$router.push({ path: '/igood/good_list'})" class="hovered hvr-grow hoverLinkColor">我要赚钱</a>
    <a @click="$router.push({ path: '/ifound/discount_list'})" class="hovered hvr-grow hoverLinkColor">聚优惠</a>
    <a @click="$router.push({ path: '//ifound/activity_list'})" class="hovered hvr-grow hoverLinkColor">聚活动</a>
    <a @click="$router.push({ path: '/ifound/activity_list'})" class="hovered hvr-grow hoverLinkColor">商业合作</a>
    <a @click="$router.push({ path: '/expert/ask_expert'})" class="hovered hvr-grow hoverLinkColor">求问专家</a>
  </div>
</template>

<script>
  export default {
    name: "Site"
  }
</script>

<style scoped>

</style>
