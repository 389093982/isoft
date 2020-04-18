<template>
  <div>

    <div class="isoft_bg_white isoft_pd10" style="border-top-left-radius: 6px;border-top-right-radius: 6px">
      <IBeautifulCard title="课程分类">
        <div slot="content" style="padding: 20px 10px;">
          <div>
            <div style="border-bottom: 2px solid #edf1f2;padding: 0px 0px 5px 0px;">
              <a href="javascript:;" @click="showCourseType=true" style="color: red;">热门分类</a>
              <a href="javascript:;" @click="showCourseType=!showCourseType" style="color: red;float: right;margin-right: 950px">
                <IBeautifulLink style="font-size: 14px;"><show-more default-desc="其他分类"></show-more></IBeautifulLink>
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
  import ShowMore from "../../Elementviewers/showMore";

  export default {
    name: "CourseType",
    components: {ShowMore, ISearch, HotCourseType, TotalCourseType, IBeautifulCard, IBeautifulLink},
    data() {
      return {
        showCourseType: true,
      }
    },
    methods: {
      searchFunc: function (data) {
        this.$router.push({path: '/ilearning/courseSearch', query: {search: data}});
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
    }
  }
</script>

<style scoped>
  @import "../../../assets/css/isoft_common.css";

</style>
