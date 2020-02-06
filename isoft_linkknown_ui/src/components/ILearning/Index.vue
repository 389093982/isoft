<template>
  <div>
    <ChooseCourseType/>

    <WarmHeartRecommend style="margin-top: 5px;"/>

    <Row style="margin-top: 5px;">
      <Col span="17" style="padding-right: 5px;">
        <HotRecommend class="isoft_bg_white isoft_pd10" :show-display-icon="true"/>
      </Col>
      <Col span="7">
        <GoodRank class="isoft_bg_white isoft_pd10"/>

        <div class="isoft_bg_white isoft_pd10">
          <ToolBox :placement_name="GLOBAL.placement_host_app_recommand"/>
          <NewsNotice :placement_name="GLOBAL.placement_Index_news_list"/>
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

    <div class="isoft_bg_white isoft_pd10" style="margin-top: 5px;">
      <Fun/>
    </div>
  </div>
</template>

<script>
  import ChooseCourseType from "./CourseType/ChooseCourseType"
  import HotRecommend from "./Course/HotRecommend"
  import NewsNotice from "./Course/NewsNotice"
  import ToolBox from "../Background/CMS/ToolBox"
  import IBeautifulTabLink from "../Common/link/IBeautifulTabLink"
  import Fun from "./Fun"
  import HorizontalLinks from "../Elementviewers/HorizontalLinks"
  import IHotRecommand from "../Common/recommend/IHotRecommand"
  import IndexCarousel from "./IndexCarousel";
  import GoodRank from "./GoodRank";
  import WarmHeartRecommend from "./WarmHeartRecommend";

  export default {
    name: "Index",
    components: {
      WarmHeartRecommend,
      GoodRank,
      IndexCarousel,
      IHotRecommand,
      HorizontalLinks,
      ChooseCourseType,
      HotRecommend,
      NewsNotice,
      ToolBox,
      IBeautifulTabLink,
      Fun
    },
  }
</script>

<style scoped>
  /* 引入公共样式库 */

</style>
