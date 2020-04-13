<template>
  <div class="isoft_bg_white isoft_pd10" style="min-height: 370px">
    <div class="isoft_font_header" v-if="isSearchFlag">课程搜索结果</div>
    <div class="isoft_font_header" v-else>{{custom_label}}</div>
    <div style="column-count:2;border-top: 1px solid #eee;padding-top: 5px;">
      <HoverBigImg v-for="(course, index) in courses" :key="index"
                   width="100%" height="98px"
                   :src-img="course.small_image"
                   :label-text="course.course_name"
                   @onclick="$router.push({path:'/ilearning/courseDetail', query:{course_id: course.id}})"/>
    </div>
    <div v-if="isSearchFlag && !(courses && courses.length > 0)" style="text-align: center;padding-top: 10px;">
      <p>未搜索到和 "{{search_data}}" 相关的课程</p>
      <p class="isoft_hover_red2" @click="refreshCourseList">给我推荐一些</p>
      <p class="isoft_hover_red2" @click="handleReSearch">重新搜索</p>
    </div>
  </div>
</template>

<script>
  import HoverBigImg from "../Common/img/HoverBigImg"
  import {QueryCustomTagCourse, SearchCourseList} from "../../api"

  export default {
    name: "RankCourse",
    components: {HoverBigImg},
    props:{
      custom_tag: {
        type: String,
        default: 'hot',
      },
      custom_label: {
        type: String,
        default: '热门课程',
      }
    },
    data() {
      return {
        courses: [],
        isSearchFlag: false,    // 是否是搜索模式
        search_data: '',
      }
    },
    methods: {
      handleReSearch: function (){
        this.$emit("research");
      },
      refreshCustomTagCourse: async function () {
        const result = await QueryCustomTagCourse({custom_tag: this.custom_tag});
        if (result.status === "SUCCESS") {
          this.courses = result.custom_tag_courses.slice(0, 6);
        }
      },
      search: async function (search_data) {
        this.isSearchFlag = true;
        this.search_data = search_data;
        const result = await SearchCourseList({search: search_data});
        if (result.status === "SUCCESS") {
          this.courses = result.courses;
        }
      },
      refreshCourseList: function () {
        this.isSearchFlag = false;
        this.refreshCustomTagCourse('hot');
      }
    },
    mounted() {
      this.refreshCourseList();
    }
  }
</script>

<style scoped>
  .header {
    margin: 0 10px 10px 10px;
    position: relative;
    height: 40px;
    color: #111;
    font-size: 20px;
    font-weight: 400;
    line-height: 40px;
    white-space: nowrap;
  }
</style>
