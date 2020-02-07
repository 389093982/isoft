<template>
  <div>
    <ChooseCourseType/>

    <WarmHeartRecommend style="margin-top: 5px;"/>

    <Row style="margin-top: 5px;">
      <Col span="17" style="padding-right: 5px;">
        <HotRecommend class="isoft_bg_white isoft_pd10" :show-display-icon="true"/>
      </Col>
      <Col span="7">
        <BookRank class="isoft_bg_white isoft_pd10"/>

        <div class="isoft_bg_white isoft_pd10 isoft_top5">
          <ToolBox :placement_name="GLOBAL.placement_host_app_recommand"/>
        </div>

        <div class="isoft_bg_white isoft_pd10 isoft_top5">
          <NewsNotice :placement_name="GLOBAL.placement_Index_news_list"/>
        </div>

        <div class="isoft_bg_white isoft_pd10 isoft_top5">
          <BlogRank/>
        </div>
      </Col>
    </Row>

    <div class="isoft_bg_white isoft_pd10" style="margin-top: 5px;">
      <!-- 热门开源项目 -->
      <IHotRecommand :placement_name="GLOBAL.placement_hot_project_recommod"/>
    </div>

    <div class="isoft_bg_white isoft_pd10" style="margin-top: 5px;">
      <IBeautifulTabLink/>
    </div>

    <div class="isoft_bg_white isoft_pd10" style="margin-top: 5px;">
      <HorizontalLinks :placement_name="GLOBAL.placement_want_to_find"/>
    </div>
  </div>
</template>

<script>
  import ChooseCourseType from "./CourseType/ChooseCourseType"
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

  export default {
    name: "Index",
    components: {
      BlogRank,
      WarmHeartRecommend,
      BookRank,
      IndexCarousel,
      IHotRecommand,
      HorizontalLinks,
      ChooseCourseType,
      HotRecommend,
      NewsNotice,
      ToolBox,
      IBeautifulTabLink,
    },
  }
</script>

<style scoped>
  /* 引入公共样式库 */

</style>
