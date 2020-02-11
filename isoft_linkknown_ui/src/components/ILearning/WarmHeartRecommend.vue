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
        <div style="height: 40px;padding:7px;margin-bottom:10px;background-color: rgba(228,228,228,0.4);">
          <a class="hovered hvr-grow hoverLinkColor mr5" @click="refreshCustomTagCourse('hot')">热门</a>
          <a class="hovered hvr-grow hoverLinkColor mr5" @click="refreshCustomTagCourse('special')">特色</a>
          <a class="hovered hvr-grow hoverLinkColor mr5" @click="refreshCustomTagCourse('high_comment')">高评</a>
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
        display_courses: [],
      }
    },
    methods: {
      refreshCustomTagCourse: async function (custom_tag) {
        const result = await QueryCustomTagCourse({custom_tag: custom_tag});
        if (result.status == "SUCCESS") {
          // 九宫格
          this.display_courses = result.custom_tag_courses.slice(0, 9);
        }
      }
    },
    mounted() {
      this.refreshCustomTagCourse('hot');
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
