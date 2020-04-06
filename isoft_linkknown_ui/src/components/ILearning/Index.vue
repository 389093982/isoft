<template>
  <div>
    <div class="section01_bg">
      <LinkKnownQuickSearch @handleShow="handleShow"/>
    </div>

    <SearchRecommend v-if="tabIndex === 1"/>
    <!--新闻公告-->
    <div v-else-if="tabIndex === 2" style="display: flex;justify-content: center;">
      <NewsNotice style="width: 33%;" :placement_name="GLOBAL.placement_Index_news_list"/>
      <NewsNotice style="width: 33%;" :placement_name="GLOBAL.placement_Index_news_list"/>
      <NewsNotice style="width: 33%;" :placement_name="GLOBAL.placement_Index_news_list"/>
    </div>
    <!--热门应用-->
    <div v-else-if="tabIndex === 3" style="display: flex;justify-content: center;">
      <ToolBox style="width: 33%;" :placement_name="GLOBAL.placement_host_app_recommand"/>
      <ToolBox style="width: 33%;" :placement_name="GLOBAL.placement_host_app_recommand"/>
      <ToolBox style="width: 33%;" :placement_name="GLOBAL.placement_host_app_recommand"/>
    </div>
    <!--热门产品-->
    <IBeautifulTabLink v-else-if="tabIndex === 4"/>
    <IHotRecommand v-else-if="tabIndex === 5" :placement_name="GLOBAL.placement_hot_project_recommod" :minHeight="608"/>

    <!--发现-->
    <div class="isoft_bg_white isoft_pd10" style="margin-top: 5px;position: relative;left: -15px">
      <HorizontalLinks :placement_name="GLOBAL.placement_want_to_find"/>
    </div>
  </div>
</template>

<script>

  // 最顶部的使用同步加载组件
  import LinkKnownQuickSearch from "./LinkKnownQuickSearch"
  import SearchRecommend from "./SearchRecommend";
  // 其它部位使用异步加载组件
  const ToolBox = () => import("@/components/Background/CMS/ToolBox");
  const IBeautifulTabLink = () => import("@/components/Common/link/IBeautifulTabLink");
  const HorizontalLinks = () => import("@/components/Elementviewers/HorizontalLinks");
  const IHotRecommand = () => import("@/components/Common/recommend/IHotRecommand");
  const BookRank = () => import("@/components/ILearning/BookRank");

  const NewsNotice = () => import("@/components/ILearning/Course/NewsNotice");

  export default {
    name: "Index",
    components: {
      SearchRecommend,
      BookRank,
      IHotRecommand,
      HorizontalLinks,
      NewsNotice,
      ToolBox,
      IBeautifulTabLink,
      LinkKnownQuickSearch,
    },
    data(){
      return {
        tabIndex: 1,    // 默认显示第一个标签页
      }
    },
    methods:{
      handleShow: function (index) {
        this.tabIndex = index;
      }
    }
  }
</script>

<style scoped>
  .section01_bg {
    height: 400px;
    background: url(../../assets/bg.jpg) no-repeat;
    background-size: 100% 100%;
  }
</style>
