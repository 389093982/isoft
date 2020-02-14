<template>
  <div>

    <!--高度这里就设置400px-->
    <div style="height: 400px;">
      <div style="float: left;width: 70%;">
        <!--首页轮播图-->
        <IndexCarousel></IndexCarousel>
      </div>
      <div style="float: left;width: 30%;">
        <!--新闻公告-->
        <NewsNotice :placement_name="GLOBAL.placement_Index_news_list"/>
      </div>
      <div style="clear: both"></div>
    </div>

    <!--精品课程相关-->
    <!--为您精选、热门、特色、高评-->
    <div style="margin: 40px 0 0 0 ">
      <WarmHeartRecommend/>
    </div>

    <!--话题博客相关-->
    <!--热门博客-->
    <div class="isoft_bg_white isoft_pd10 isoft_top5">
      <BlogRank/>
    </div>

    <!--热门书单相关-->
    <!--热门书单-->
    <div>
      <BookRank class="isoft_bg_white isoft_pd10"/>
    </div>

    <!--热门资源相关-->
    <div>
      <!--热门应用-->
      <div class="isoft_bg_white isoft_pd10 isoft_top5">
        <ToolBox :placement_name="GLOBAL.placement_host_app_recommand"/>
      </div>
      <!--热门产品-->
      <div class="isoft_bg_white isoft_pd10" style="margin-top: 5px;">
        <IBeautifulTabLink/>
      </div>
      <!-- 热门项目推荐 -->
      <div class="isoft_bg_white isoft_pd10" style="margin-top: 5px;">
        <IHotRecommand :placement_name="GLOBAL.placement_hot_project_recommod"/>
      </div>
    </div>

    <!--展示模块  目前是4个展示-->
    <show-modulars></show-modulars>

    <!--您是不是想找-->
    <div class="isoft_bg_white isoft_pd10" style="margin-top: 5px;">
      <HorizontalLinks :placement_name="GLOBAL.placement_want_to_find"/>
    </div>
  </div>
</template>

<script>
  import HotRecommend from "./Course/HotRecommend"
  import NewsNotice from "./Course/NewsNotice"
  import ToolBox from "../Background/CMS/ToolBox"
  import IBeautifulTabLink from "../Common/link/IBeautifulTabLink"
  import HorizontalLinks from "../Elementviewers/HorizontalLinks"
  import IHotRecommand from "../Common/recommend/IHotRecommand"
  import IndexCarousel from "./IndexCarousel";
  import BookRank from "./BookRank";
  import WarmHeartRecommend from "./WarmHeartRecommend";
  import BlogRank from "./BlogRank";
  import ShowModulars from "./ShowModulars";

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
      HotRecommend,
      NewsNotice,
      ToolBox,
      IBeautifulTabLink,
    },
  }
</script>

<style scoped>

</style>
