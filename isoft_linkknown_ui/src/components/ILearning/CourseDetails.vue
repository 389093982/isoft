<template>
  <div>

    <WarmHeartRecommend style="margin-top: 5px;"/>

    <Row style="margin-top: 5px;">
      <Col span="24" style="padding-right: 5px;">
        <HotRecommend class="isoft_bg_white isoft_pd10" :show-display-icon="true"/>
      </Col>
    </Row>

    <div class="isoft_bg_white isoft_pd10 isoft_top5">
      <Row :gutter="10">
        <Col span="6">
          <div class="hoverBorderShadow" style="height: 100px;padding: 20px;cursor: pointer;"
               @click="$router.push({path:'/ilearning/index'})">
            <Row>
              <Col span="8">
                <img src="../../../static/images/course.jpg" style="width: 52px;height: 52px;"/>
              </Col>
              <Col span="16">
                <h4>精品课程</h4>
                <p>好评如潮好评如潮</p>
              </Col>
            </Row>
          </div>
        </Col>
        <Col span="6">
          <div class="hoverBorderShadow" style="height: 100px;padding: 20px;cursor: pointer;"
               @click="$router.push({path:'/iblog/blog_list'})">
            <Row>
              <Col span="8">
                <img src="../../../static/images/blog.jpg" style="width: 52px;height: 52px;"/>
              </Col>
              <Col span="16">
                <h4>话题博客</h4>
                <p>好评如潮好评如潮</p>
              </Col>
            </Row>
          </div>
        </Col>
        <Col span="6">
          <div class="hoverBorderShadow" style="height: 100px;padding: 20px;cursor: pointer;"
               @click="$router.push({path:'/ibook/book_list'})">
            <Row>
              <Col span="8">
                <img src="../../../static/images/book.jpg" style="width: 52px;height: 52px;"/>
              </Col>
              <Col span="16">
                <h4>热门书单</h4>
                <p>好评如潮好评如潮</p>
              </Col>
            </Row>
          </div>
        </Col>
        <Col span="6">
          <div class="hoverBorderShadow" style="height: 100px;padding: 20px;cursor: pointer;"
               @click="$router.push({path:'/site/index'})">
            <Row>
              <Col span="8">
                <img src="../../../static/images/more.jpg" style="width: 52px;height: 52px;"/>
              </Col>
              <Col span="16">
                <h4>更多资源</h4>
                <p>查看更多关于链知网的相关资源</p>
              </Col>
            </Row>
          </div>
        </Col>
      </Row>
    </div>

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
  import ChooseCourseType from "./LogoAndSearch"
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
    name: "CourseDetails",
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
