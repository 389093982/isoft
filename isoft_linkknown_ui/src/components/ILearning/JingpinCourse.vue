<template>
  <div>

    <!--搜索框-->
    <div style="width: 100%;height: 70px;background-color: white;border:1px solid wheat;border-radius: 50px">
      <div style="float: left;width: 50%">
        <div style="font-size: 30px;margin: 25px 0 0 180px;color: rgba(128,128,128,0.58)">Stay hungry，Stay foolish</div>
      </div>
      <div style="float: left;width: 50%;padding: 15px 170px 0 0 ">
        <Affix :offset-top="60">
          <ISearch @submitFunc="submitFunc"></ISearch>
        </Affix>
      </div>
      <div style="clear: both"></div>
    </div>

    <!--课程分类-->
    <CourseType ></CourseType>

    <!--热门课程推荐-->
    <div class="isoft_bg_white isoft_pd10">
      <HotRecommend :show-display-icon="true"></HotRecommend>
    </div>

  </div>
</template>

<script>
  import CourseType from "./CourseType/CourseType"
  import ISearch from "../Common/search/ISearch"
  import HotRecommend from "./Course/HotRecommend"
  import ToolBox from "../Background/CMS/ToolBox"
  import HorizontalLinks from "../Elementviewers/HorizontalLinks"
  import IHotRecommand from "../Common/recommend/IHotRecommand"
  import IndexCarousel from "./IndexCarousel";
  import BookRank from "./BookRank";
  import WarmHeartRecommend from "./WarmHeartRecommend";
  import BlogRank from "./BlogRank";
  import ShowModulars from "./ShowModulars";

  export default {
    name: "JingpinCourse",
    components: {
      CourseType,
      ShowModulars,
      BlogRank,
      WarmHeartRecommend,
      BookRank,
      IndexCarousel,
      IHotRecommand,
      HorizontalLinks,
      HotRecommend,
      ToolBox,
      ISearch,
    },
    data() {
      return {

      }
    },
    methods:{
      submitFunc: function (search_data) {
        if (search_data.length === 0) {
          return false;
        }
        this.$router.push({path: '/ilearning/courseSearch', query: {search:search_data}});
      },
      chooseCourseType: function (course_type, course_sub_type) {
        // params是路由的一部分
        // query是拼接在url后面的参数
        // 由于动态路由也是传递params的,所以在 this.$router.push() 方法中path不能和params一起使用,否则params将无效.需要用name来指定页面
        this.$router.push({path: '/ilearning/courseSearch', query: {search: course_sub_type}});
      },
      toggle: function (data) {
        alert(data);
      }
    },
  }
</script>

<style scoped>
  #courseSearchId{
    border-style:solid;border-color:#ff537c;width: 400px;height: 35px;padding: 2px 0 2px 8px;position: absolute;left:400px;top:100px;
  }
  #courseSearchButtonId{
    border-style:solid;border-color:#ff537c;width:60px;height: 35px;background-color: #ff537c;cursor: pointer;position: absolute;left:800px;top:100px;
  }
  #courseSearchCodeId{
    color: white;cursor: pointer;font-size: 15px;
  }
</style>
