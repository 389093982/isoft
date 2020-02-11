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
  import {QueryCustomTagCourse, ShowCourseDetail, videoPlayUrl} from "../../../api"

  export default {
    name: "VideoPay",
    data() {
      return {
        recommendCourses: [],
        cVideos: [],
        curVideoId: -1, // 当前播放视频 id
      }
    },
    methods: {
      playVideo: function (video_id) {
        this.curVideoId = video_id;
        var xhr = new XMLHttpRequest();                                                     //创建XMLHttpRequest对象
        xhr.open('GET', videoPlayUrl + "?video_id=" + video_id, true);                      //配置请求方式、请求地址以及是否同步
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
      },
      addPlayNextEventListener: function () {
        var _this = this;
        var video = document.getElementById("videoPath");
        video.addEventListener("ended", function () {
          let nexts = _this.cVideos.filter(video => video.id > _this.curVideoId);
          if (nexts != null && nexts != undefined && nexts.length > 0) {
            let video_id = nexts[0].id;
            _this.playVideo(video_id);
          }
        });
      },
      refreshCorsedetail: async function () {
        const course_id = this.$route.query.course_id;
        const result = await ShowCourseDetail(course_id);
        if (result.status == "SUCCESS") {
          // 根据 id 顺序排序
          this.cVideos = result.cVideos.sort((video1, video2) => video1.id < video2.id);
        }
      }
    },
    mounted: function () {
      // 加载当前课程所有视频资源,为自动播放下一集做准备
      this.refreshCorsedetail();
      // 加载热门推荐课程列表
      this.refreshCustomTagCourse('recommand');
      // 播放当前视频
      this.playVideo(this.$route.query.video_id);
      // 注册播放下一集事件
      this.addPlayNextEventListener();
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
