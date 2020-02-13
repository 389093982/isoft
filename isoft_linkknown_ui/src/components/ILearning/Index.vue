<template>
  <div>
    <!--首页logo图片 和 搜索框 -->
    <LogoAndSearch></LogoAndSearch>

    <!--课程分类-->
    <CourseType></CourseType>

    <!--为您精选、热门、特色、高评-->
    <WarmHeartRecommend style="margin-top: 5px;"/>

    <Row style="margin-top: 5px;">
      <Col span="17" style="padding-right: 5px;">
        <!--热门课程推荐-->
        <HotRecommend class="isoft_bg_white isoft_pd10" :show-display-icon="true"/>
      </Col>
      <Col span="7">

        <!--热门书单-->
        <BookRank class="isoft_bg_white isoft_pd10"/>

        <!--热门应用-->
        <div class="isoft_bg_white isoft_pd10 isoft_top5">
          <ToolBox :placement_name="GLOBAL.placement_host_app_recommand"/>
        </div>

        <!--新闻公告-->
        <div class="isoft_bg_white isoft_pd10 isoft_top5">
          <NewsNotice :placement_name="GLOBAL.placement_Index_news_list"/>
        </div>

        <!--热门博客-->
        <div class="isoft_bg_white isoft_pd10 isoft_top5">
          <BlogRank/>
        </div>
      </Col>
    </Row>

    <!--展示模块  目前是4个展示-->
    <show-modulars></show-modulars>

    <!-- 热门项目推荐 -->
    <div class="isoft_bg_white isoft_pd10" style="margin-top: 5px;">
      <IHotRecommand :placement_name="GLOBAL.placement_hot_project_recommod"/>
    </div>

    <!--热门产品-->
    <div class="isoft_bg_white isoft_pd10" style="margin-top: 5px;">
      <IBeautifulTabLink/>
    </div>

    <!--您是不是想找-->
    <div class="isoft_bg_white isoft_pd10" style="margin-top: 5px;">
      <HorizontalLinks :placement_name="GLOBAL.placement_want_to_find"/>
    </div>
  </div>
</template>

<script>
  import LogoAndSearch from "./LogoAndSearch"
  import CourseType from "./CourseType/CourseType"
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
      LogoAndSearch,
      CourseType,
      HotRecommend,
      NewsNotice,
      ToolBox,
      IBeautifulTabLink,
    },
  }
</script>

<style scoped>

</style>
