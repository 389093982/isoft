<template>
  <div>
    <!--为您精选、热门、特色、高评-->
    <div style="margin: 2px 0 0 0;">
      <WarmHeartRecommend/>
    </div>

    <!--下三，33:34:33-->
    <div style="display: flex;" class="isoft_top5">
      <!--热门书单-->
      <div style="width: 33%;">
        <BookRank class="isoft_bg_white isoft_pd10"/>
      </div>
      <div style="width: 34%;padding: 0 5px;">
        <BookRank class="isoft_bg_white isoft_pd10"/>
      </div>
      <div style="width: 33%;">
        <BookRank class="isoft_bg_white isoft_pd10"/>
      </div>
    </div>

    <!--热门博客-->
    <div style="display:flex;" class="isoft_top5">
      <div style="width: 33%;">
        <BlogRank class="isoft_bg_white isoft_pd10"/>
      </div>
      <div style="width: 34%;padding: 0 5px;">
        <BlogRank class="isoft_bg_white isoft_pd10"/>
      </div>
      <div style="width: 33%;">
        <BlogRank class="isoft_bg_white isoft_pd10"/>
      </div>
    </div>

  </div>
</template>

<script>
  const WarmHeartRecommend = () => import("@/components/ILearning/WarmHeartRecommend");
  const BookRank = () => import("@/components/ILearning/BookRank");
  const BlogRank = () => import("@/components/ILearning/BlogRank");

  export default {
    name: "SearchRecommend",
    components:{WarmHeartRecommend,BookRank,BlogRank},
    data (){
      return {
      }
    },
  }
</script>

<style scoped>

</style>
