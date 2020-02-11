<template>
  <div class="isoft_bg_white isoft_pd10" style="height: 800px;">
    <Row :gutter="10">
      <Col span="16">
        <div style="margin: 0 auto;">
          <video ref="video" width="100%" height="100%" controls preload="auto" id="videoPath" autoplay="autoplay"
                 controlslist="nodownload">
            <source type="video/mp4">
            <source type="video/ogg">
            您的浏览器不支持Video标签。
          </video>
        </div>
      </Col>
      <Col span="8" style="padding: 0px 20px;">
        <div class="isoft_auto_with title">
          推荐课程
        </div>
        <div style="padding: 10px 0px;border-top: 2px solid #edeff0;">
          <div class="course_item isoft_inline_ellipsis" v-for="(course, index) in recommendCourses">
            {{course.course_name}}
          </div>
        </div>
      </Col>
    </Row>
  </div>
</template>

<script>
  import {QueryCustomTagCourse, videoPlayUrl} from "../../../api"

  export default {
    name: "VideoPay",
    data() {
      return {
        recommendCourses: [],
      }
    },
    methods: {
      playVideo: function () {
        var xhr = new XMLHttpRequest();                                                     //创建XMLHttpRequest对象
        xhr.open('GET', videoPlayUrl + "?video_id=" + this.$route.query.video_id, true);    //配置请求方式、请求地址以及是否同步
        xhr.responseType = 'blob';                                                          //设置结果类型为blob;
        xhr.onload = function (e) {
          if (this.status === 200) {
            // 获取blob对象
            var blob = this.response;
            // 获取blob对象地址，并把值赋给容器
            $("#videoPath").attr("src", URL.createObjectURL(blob));
          }
        };
        xhr.send();
      },
      refreshCustomTagCourse: async function (custom_tag) {
        const result = await QueryCustomTagCourse({custom_tag: custom_tag});
        if (result.status == "SUCCESS") {
          this.recommendCourses = result.custom_tag_courses;
        }
      }
    },
    mounted: function () {
      this.refreshCustomTagCourse('recommand');
      this.playVideo();
    }
  }
</script>

<style scoped>
  .title {
    font-size: 18px;
    font-weight: normal;
    height: 35px;
    line-height: 35px;
    font-family: "微软雅黑";
  }

  .title::after {
    content: "";
    display: block;
    height: 3px;
    border-bottom: 3px solid red;
  }

  .course_item {
    padding: 5px 8px;
    cursor: pointer;
  }

  .course_item:hover {
    background-color: rgba(0, 191, 20, 0.68);
    color: white;
  }
</style>
