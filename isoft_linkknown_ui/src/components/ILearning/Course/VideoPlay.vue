<template>
  <div style="background-color: #232325;height: 800px;width: 99.5%">
    <Row>
      <Col span="15" style="position: relative;left: 80px;">
        <div v-if="course && curVideo" style="height: 30px;margin: 10px 0 0 0 ;color: #999">
          正在播放: {{course.course_name}} / {{curVideo.video_name | filterSuffix }}
        </div>
        <div>
          <video ref="video" width="100%" height="100%" controls preload="auto" id="videoPath" autoplay="autoplay" controlslist="nodownload">
            <source type="video/mp4">
            <source type="video/ogg">
            您的浏览器不支持Video标签。
          </video>
        </div>
      </Col>
      <Col span="6" offset="2">
        <div style="font-size: 20px;margin:35px 0 0 0 ;color: #999">
          推荐课程
        </div>
        <div style="padding: 10px 0 0 10px ;">
          <div class="course_item" v-for="(course, index) in recommendCourses">
            <div>{{course.course_name}}</div>
            <div class="course_small_image" style="width: 155px;">
              <img v-if="course.small_image" :src="course.small_image" height="100" width="155"/>
              <img v-else src="../../../assets/default.png" height="100" width="155"/>
              <div class="ico_play"></div>
            </div>

          </div>
        </div>
      </Col>
    </Row>
  </div>
</template>

<script>
  import {QueryCustomTagCourse, ShowCourseDetail, videoPlayUrl} from "../../../api"

  export default {
    name: "VideoPlay",
    data() {
      return {
        recommendCourses: [],
        cVideos: [],
        course: '',
        curVideo: '',
        curVideoId: -1, // 当前播放视频 id
      }
    },
    methods: {
      playVideo: function (video_id) {
        this.curVideoId = video_id;
        let xhr = new XMLHttpRequest();                                                     //创建XMLHttpRequest对象
        xhr.open('GET', videoPlayUrl + "?video_id=" + video_id, true);                      //配置请求方式、请求地址以及是否同步
        xhr.responseType = 'blob';                                                          //设置结果类型为blob;
        xhr.onload = function (e) {
          if (this.status === 200) {
            // 获取blob对象
            let blob = this.response;
            // 获取blob对象地址，并把值赋给容器
          document.getElementById("videoPath").setAttribute("src", URL.createObjectURL(blob));
          }
        };
        xhr.send();
      },
      refreshCustomTagCourse: async function (custom_tag) {
        const result = await QueryCustomTagCourse({custom_tag: custom_tag});
        if (result.status === "SUCCESS") {
          this.recommendCourses = result.custom_tag_courses;
        }
      },
      addPlayNextEventListener: function () {
        let _this = this;
        let video = document.getElementById("videoPath");
        video.addEventListener("ended", function () {
          let nexts = _this.cVideos.filter(video => video.id > _this.curVideoId);
          if (nexts != null && nexts !== undefined && nexts.length > 0) {
            let video_id = nexts[0].id;
            _this.playVideo(video_id);
          }
        });
      },
      refreshCorsedetail: async function () {
        const course_id = this.$route.query.course_id;
        const result = await ShowCourseDetail({course_id:course_id});
        if (result.status === "SUCCESS") {
          this.course = result.course;
          // 根据 id 顺序排序
          this.cVideos = result.cVideos.sort((video1, video2) => video1.id < video2.id);
          this.curVideo = this.cVideos.filter(video => video.id == this.$route.query.video_id)[0];
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
    },
    filters: {
      filterSuffix: function (value) {
        // 去除视频文件后缀
        return value.slice(0, value.indexOf("."));
      }
    },
    watch: {
      curVideoId: function (curVideoId) {
        this.curVideo = this.cVideos.filter(video => video.id == curVideoId)[0];
      }
    }
  }
</script>

<style scoped>
  video {
    object-fit:fill;
    width:795px;
    height:450px;
  }

  .course_item {
    color: #999;
    padding: 5px;
    cursor: pointer;
  }

  .course_item:hover {
    color: white;
  }

  .course_item:hover .course_small_image{
    display: block;
  }

  .course_small_image{
    display: none;
  }

  .ico_play {
    background: url(../../../assets/ico_play.png) no-repeat;
    background-size: 20px;
    position: relative;
    top: -30px;
    left: 130px;
    width: 20px;
    height: 20px;
  }

</style>
