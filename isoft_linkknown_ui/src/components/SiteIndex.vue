<template>
  <div class="isoft_bg_white isoft_pd10">
    <a @click="$router.push({path:'/ilearning/index'})" class="hovered hvr-grow isoft_hover_red2 mr5">返回首页</a>
    <a @click="$router.push({path:'/ilearning/about'})" class="hovered hvr-grow isoft_hover_red2 mr5">关于LinkKnown</a>
    <a @click="$router.push({path:'/user/guide'})" class="hovered hvr-grow isoft_hover_red2 mr5">站点引导</a>
    <a @click="$router.push({path:'/advertisement/advApply'})" class="hovered hvr-grow isoft_hover_red2 mr5">广告位招租</a>
    <a @click="$router.push({path:'/user/userDetail',query:{username:'mine'}})" class="hovered hvr-grow isoft_hover_red2 mr5">个人中心</a>
    <a @click="$router.push({ path: '/ilearning/courseSpace'})" class="hovered hvr-grow isoft_hover_red2">我的课程空间</a>
    <a @click="$router.push({ path: '/business/businessList'})" class="hovered hvr-grow isoft_hover_red2">我要赚钱</a>
    <a @click="$router.push({ path: '/ifound/discount_list'})" class="hovered hvr-grow isoft_hover_red2">聚优惠</a>
    <a @click="$router.push({ path: '//ifound/activity_list'})" class="hovered hvr-grow isoft_hover_red2">聚活动</a>
    <a @click="$router.push({ path: '/ifound/activity_list'})" class="hovered hvr-grow isoft_hover_red2">商业合作</a>
    <a @click="$router.push({ path: '/expert/askExpert'})" class="hovered hvr-grow isoft_hover_red2">求问专家</a>
  </div>
</template>

<script>
  export default {
    name: "SiteIndex"
  }
</script>

<style scoped>

</style>
