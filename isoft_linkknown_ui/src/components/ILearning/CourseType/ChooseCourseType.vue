<template>
  <div>
    <div class="isoft_bg_white" style="padding: 10px 10px 0 10px;">
      <div style="float: left;width: 20%;">
        <img src="../../../../static/images/linkknown_logo02.png" style="width: 170px;height: 85px;"/>
      </div>
      <div style="float: left;width: 40%;height: 85px; background-color: rgba(255,105,0,0.25)">
        <img src="../../../../static/images/index_showline.gif" style="width: 100%;height: 85px;"/>
      </div>
      <div style="float: left;width: 40%;">
        <div style="margin: 10px 20px 0 0 ">
          <ISearch @submitFunc="searchFunc"/>
        </div>
        <div style="margin: 8px 0 0 70px ">
          <a @click="$router.push({path:'/ilearning/about'})" class="hovered hvr-grow hoverLinkColor mr5">关于LinkKnown</a>
          <a @click="$router.push({path:'/user/guide'})" class="hovered hvr-grow hoverLinkColor mr5">站点引导</a>
          <a @click="$router.push({path:'/user/mine/detail',query:{username:'mine'}})" class="hovered hvr-grow hoverLinkColor mr5">个人中心</a>
        </div>
      </div>

      <div style="clear: both"></div>
    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_bordertop_red" style="margin-top: 5px;">
      <IBeautifulCard title="课程天地">
        <div slot="content" style="padding: 20px 10px;">
          <div>
            <div style="border-bottom: 2px solid #edf1f2;padding: 0px 0px 5px 0px;">
              <a href="javascript:;" @click="showCourseType=true" style="color: red;">热门课程推荐</a>
              <a href="javascript:;" @click="showCourseType=!showCourseType" style="color: red;float: right;">
                <IBeautifulLink style="font-size: 14px;"> 更多</IBeautifulLink>
              </a>
            </div>
            <div>
              <HotCourseType :placement_name="GLOBAL.placement_host_course_type_carousel" v-show="showCourseType===true"
                             @chooseCourseType="chooseCourseType"/>
              <TotalCourseType v-show="showCourseType===false" @chooseCourseType="chooseCourseType"/>
            </div>
          </div>
        </div>
      </IBeautifulCard>
    </div>

  </div>
</template>

<script>
  import HotCourseType from "./HotCourseType"
  import TotalCourseType from "./TotalCourseType"
  import ISearch from "../../Common/search/ISearch"
  import IBeautifulCard from "../../Common/card/IBeautifulCard"
  import IBeautifulLink from "../../Common/link/IBeautifulLink"

  export default {
    name: "ChooseCourseType",
    components: {ISearch, HotCourseType, TotalCourseType, IBeautifulCard, IBeautifulLink},
    data() {
      return {
        showCourseType: true,
      }
    },
    methods: {
      searchFunc: function (data) {
        this.$router.push({path: '/ilearning/course_search', query: {search: data}});
      },
      chooseCourseType: function (course_type, course_sub_type) {
        // params是路由的一部分
        // query是拼接在url后面的参数
        // 由于动态路由也是传递params的,所以在 this.$router.push() 方法中path不能和params一起使用,否则params将无效.需要用name来指定页面
        this.$router.push({path: '/ilearning/course_search', query: {search: course_sub_type}});
      },
      toggle: function (data) {
        alert(data);
      }
    }
  }
</script>

<style scoped>
  @import "../../../assets/css/isoft_common.css";

</style>
