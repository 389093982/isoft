<template>
  <div class="isoft_bg_white isoft_pd10">
    <div class="header">为您精选</div>
    <Row>
      <Col span="12" style="height:365px;">
        <video ref="video" style="width:100%;height:100%;object-fit: fill" controls
               src="http://localhost:6001/api/files/W43qGEPL5hTcqa4Els4r4gbgdol5KjE3Zv0Ho5L1A-s=.mp4">
          <source type="video/mp4">
          <source type="video/ogg">
          您的浏览器不支持Video标签。
        </video>
      </Col>
      <Col span="12" style="padding-left: 10px;">
        <div v-if="custom_tags && custom_tags.length > 0"
             style="height: 40px;padding:7px;margin-bottom:10px;background-color: rgba(228,228,228,0.4);">
          <a class="hovered hvr-grow hoverLinkColor mr5" v-for="(tag, index) in custom_tags"
             @click="getCustomTagCourses(tag.custom_tag)">{{tag.custom_tag}}</a>
        </div>
        <div style="column-count:3;">
          <HoverBigImg v-for="(course, index) in display_courses"
                       class="hoverBorderShadow" width="100%" height="98px"
                       :src-img="course.small_image" style="float: left;margin: 0 10px 10px 0;"
                       @onclick="$router.push({path:'/ilearning/course_detail', query:{course_id: course.id}})"/>
        </div>
      </Col>
    </Row>

  </div>
</template>

<script>
  import HoverBigImg from "../Common/img/HoverBigImg"
  import {QueryCustomTagCourse} from "../../api"

  export default {
    name: "WarmHeartRecommend",
    components: {HoverBigImg},
    data() {
      return {
        custom_tags: [],
        custom_tag_courses: [],
        display_courses: [],
      }
    },
    methods: {
      getCustomTagCourses: function (custom_tag) {
        // 九宫格
        this.display_courses = this.custom_tag_courses.filter(course => course.custom_tag == custom_tag).slice(0, 9);
      },
      refreshCustomTagCourse: async function () {
        const result = await QueryCustomTagCourse();
        if (result.status == "SUCCESS") {
          this.custom_tags = result.custom_tags;
          this.custom_tag_courses = result.custom_tag_courses;
          if (this.custom_tags && this.custom_tags.length > 0) {
            this.getCustomTagCourses(this.custom_tags[0].custom_tag);
          }
        }
      }
    },
    mounted() {
      this.refreshCustomTagCourse();
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
