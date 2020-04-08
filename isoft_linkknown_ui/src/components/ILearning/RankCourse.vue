<template>
  <div class="isoft_bg_white isoft_pd10">
    <div class="isoft_font_header" v-if="isSearchFlag">课程搜索结果</div>
    <div class="isoft_font_header" v-else>精选课程</div>
    <Row>
      <Col span="24" style="padding-left: 10px;">
        <div style="height: 40px;padding:7px;margin-bottom:10px;background-color: rgba(228,228,228,0.4);">
          <a class="hovered hvr-grow isoft_hover_red2 mr5" @click="refreshCustomTagCourse('hot')"
             :style="{color: checked_tag === 'hot' ? 'red' : ''}">热门</a>
          <a class="hovered hvr-grow isoft_hover_red2 mr5" @click="refreshCustomTagCourse('special')"
             :style="{color: checked_tag === 'special' ? 'red' : ''}">特色</a>
          <a class="hovered hvr-grow isoft_hover_red2 mr5" @click="refreshCustomTagCourse('high_comment')"
             :style="{color: checked_tag === 'high_comment' ? 'red' : ''}">高评</a>
        </div>
        <div style="column-count:3;height: 350px">
          <HoverBigImg v-for="(course, index) in display_courses" :key="index"
                       width="100%" height="98px"
                       :src-img="course.small_image"
                       :label-text="course.course_name"
                       @onclick="$router.push({path:'/ilearning/courseDetail', query:{course_id: course.id}})"/>
        </div>
      </Col>
    </Row>

  </div>
</template>

<script>
  import HoverBigImg from "../Common/img/HoverBigImg"
  import {QueryCustomTagCourse, SearchCourseList} from "../../api"

  export default {
    name: "RankCourse",
    components: {HoverBigImg},
    data() {
      return {
        checked_tag: 'hot',
        display_courses: [],
        isSearchFlag: false,    // 是否是搜索模式
      }
    },
    methods: {
      refreshCustomTagCourse: async function (custom_tag) {
        this.checked_tag = custom_tag;
        const result = await QueryCustomTagCourse({custom_tag: custom_tag});
        if (result.status === "SUCCESS") {
          // 九宫格
          this.display_courses = result.custom_tag_courses.slice(0, 9);
        }
      },
      search: async function (search_data) {
        this.isSearchFlag = true;
        const result = await SearchCourseList({search: search_data});
        if (result.status === "SUCCESS") {
          this.display_courses = result.courses;
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
