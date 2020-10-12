<template>
  <div>
    <div>
      <LinkKnownQuickSearch id="quickSearch" @handleShow="handleShow" @submitFunc="handleSubmitFunc"/>
    </div>

    <!--课程、博客、书单-->
    <SearchRecommend ref="searchRecommend" v-show="tabIndex === 1"/>
    <!--新闻公告-->
    <div v-show="tabIndex === 2" class="isoft_top5" style="display: flex;justify-content: center;">
      <div style="width: 33%;">
        <NewsNotice class="isoft_bg_white isoft_pd10" :placement_name="GLOBAL.placement_Index_news_list"/>
      </div>
      <div style="width: 34%;padding: 0 5px;">
        <NewsNotice class="isoft_bg_white isoft_pd10" :placement_name="GLOBAL.placement_Index_news_list"/>
      </div>
      <div style="width: 33%;">
        <NewsNotice class="isoft_bg_white isoft_pd10" :placement_name="GLOBAL.placement_Index_news_list"/>
      </div>
    </div>
    <!--热门应用-->
    <div v-show="tabIndex === 3" style="display: flex;justify-content: center;">
      <div style="width: 33%;">
        <ToolBox class="isoft_bg_white isoft_pd10" :placement_name="GLOBAL.placement_host_app_recommand"/>
      </div>
      <div style="width: 34%;padding: 0 5px;">
        <ToolBox class="isoft_bg_white isoft_pd10" :placement_name="GLOBAL.placement_host_app_recommand"/>
      </div>
      <div style="width: 33%;">
        <ToolBox class="isoft_bg_white isoft_pd10" :placement_name="GLOBAL.placement_host_app_recommand"/>
      </div>
    </div>
    <!--热门产品-->
    <IBeautifulTabLink v-show="tabIndex === 4" class="isoft_bg_white isoft_pd10"/>

    <IHotRecommand v-show="tabIndex === 5" class="isoft_bg_white isoft_pd10" :placement_name="GLOBAL.placement_hot_project_recommod" :minHeight="608"/>

    <!-- 行业简介 -->
    <HangYe/>
    <!-- 宣传 -->
    <XuanChuan/>

    <!--发现-->
    <div class="isoft_bg_white" style="margin-top: 5px;padding: 10px 20px;">
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

  const NewsNotice = () => import("@/components/ILearning/Course/NewsNotice");
  const HangYe = () => import("@/components/ILearning/HangYe");
  const XuanChuan = () => import("@/components/ILearning/XuanChuan");

  export default {
    name: "Index",
    components: {
      XuanChuan,
      HangYe,
      SearchRecommend,
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
      },
      handleSubmitFunc: function (search_data) {
        this.tabIndex = 1;
        this.$refs.searchRecommend.search(search_data);
      }
    }
  }
</script>

<style scoped>

</style>
