<template>
  <div>

    <!--高度这里就设置400px-->
    <div style="height: 400px;">
      <div style="float: left;width: 70%;">
        <!--首页轮播图-->
        <IndexCarousel></IndexCarousel>
      </div>
      <div style="float: left;width: 30%;position: relative;top: -5px;">
        <!--新闻公告-->
        <NewsNotice :placement_name="GLOBAL.placement_Index_news_list"/>
      </div>
      <div style="clear: both"></div>
    </div>

    <!--为您精选、热门、特色、高评-->
    <div style="margin: 40px 0 0 0;">
      <WarmHeartRecommend/>
    </div>


    <!--备注：下面5个热门相关做成上2下3的布局-->

    <!--上二,7:3-->
    <div style="float: left;width: 70%;height: 600px">
      <!--热门博客-->
      <div class="isoft_bg_white isoft_pd10 isoft_top5" style="margin-right: 4px">
        <IBeautifulTabLink/>
      </div>
    </div>
    <div style="float: right;width: 30%;height: 600px">
      <!--热门应用-->
      <div class="isoft_bg_white isoft_pd10 isoft_top5">
        <ToolBox :placement_name="GLOBAL.placement_host_app_recommand"/>
      </div>
    </div>
    <div style="clear: both"></div>

    <!--下三，33:34:33-->
    <div style="float: left;width: 33%;height: 600px">
      <!--热门书单-->
      <div style="margin-top: 5px;margin-right: 4px">
        <BookRank class="isoft_bg_white isoft_pd10"/>
      </div>
    </div>
    <div style="float: left;width: 34%;height: 600px">
      <!--热门产品-->
      <div class="isoft_bg_white isoft_pd10" style="margin-top: 5px;margin-right: 4px">
        <BlogRank/>
      </div>
    </div>
    <div style="float: left;width: 33%;height: 600px">
      <!-- 热门项目推荐 -->
      <div class="isoft_bg_white isoft_pd10" style="margin-top: 5px;">
        <IHotRecommand :placement_name="GLOBAL.placement_hot_project_recommod"/>
      </div>
    </div>
    <div style="clear: both"></div>

    <!--展示模块  目前是4个展示-->
    <show-modulars></show-modulars>

    <!--发现-->
    <div class="isoft_bg_white isoft_pd10" style="margin-top: 5px;position: relative;left: -15px">
      <HorizontalLinks :placement_name="GLOBAL.placement_want_to_find"/>
    </div>
  </div>
</template>

<script>

  const ToolBox = () => import("@/components/Background/CMS/ToolBox");
  const IBeautifulTabLink = () => import("@/components/Common/link/IBeautifulTabLink");
  const HorizontalLinks = () => import("@/components/Elementviewers/HorizontalLinks");
  const IHotRecommand = () => import("@/components/Common/recommend/IHotRecommand");
  const IndexCarousel = () => import("@/components/ILearning/IndexCarousel");
  const BookRank = () => import("@/components/ILearning/BookRank");
  const WarmHeartRecommend = () => import("@/components/ILearning/WarmHeartRecommend");
  const ShowModulars = () => import("@/components/ILearning/ShowModulars");
  const BlogRank = () => import("@/components/ILearning/BlogRank");
  const NewsNotice = () => import("@/components/ILearning/Course/NewsNotice");

  export default {
    name: "Index",
    components: {
      ShowModulars,
      BlogRank,
      WarmHeartRecommend,
      BookRank,
      IndexCarousel,
      IHotRecommand,
      HorizontalLinks,
      NewsNotice,
      ToolBox,
      IBeautifulTabLink,
    },
  }
</script>

<style scoped>

</style>
