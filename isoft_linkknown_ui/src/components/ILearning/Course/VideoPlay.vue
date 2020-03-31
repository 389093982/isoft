<template>
  <div>

    <div class="bodyColor" style="height: 620px">
      <!--整体一行-->
      <Row>
        <!--左侧视频播放-->
        <Col span="15" style="margin-left: 80px">
          <div v-if="course && curVideo" style="height: 30px;margin: 10px 0 0 0 ;color: #999">
            正在播放: {{course.course_name}} / 第{{currentClickIndex+1}}集: {{curVideo.video_name | filterSuffix }}
          </div>
          <div>
            <video ref="video" class="videoClass" width="100%" height="100%" controls preload="auto" id="videoPath" autoplay="autoplay" controlslist="nodownload">
              <source type="video/mp4">
              <source type="video/ogg">
              您的浏览器不支持Video标签。
            </video>
          </div>
        </Col>
        <!--右侧分上下-->
        <Col span="5" style="margin-left: 30px">
          <!--右侧竖块-->
          <div style="margin:10px 0 0 0 ;">
            <Tabs size="small">
              <TabPane :label="course.course_name">
                <!--本主题视频集数-->
                <div class="scrollBgColor" style="padding: 5px 0 0 10px ">
                  <vue-scroll :ops="scrollOps" style="width:99%;height:425px;">
                    <div v-for="(video, index) in cVideos" style="color: #999;cursor: pointer" @click="clickCourse(index)">
                      <div class="video_item" :style="{color:index===currentClickIndex?'#00c806':''}">第{{index + 1}}集:&nbsp;{{video.video_name | filterSuffix}}</div>
                    </div>
                  </vue-scroll>
                </div>
              </TabPane>
              <TabPane label="推荐课程">
                <!--推荐课程-->
                <div class="scrollBgColor" style="padding: 5px 0 0 10px ">
                  <vue-scroll :ops="scrollOps" style="width:99%;height:425px;">
                    <div class="course_item" v-for="(course, index) in recommendCourses">
                      <div>{{course.course_name}}</div>
                      <div class="course_small_image" style="width: 155px;">
                        <img v-if="course.small_image" :src="course.small_image" height="100" width="155"/>
                        <img v-else src="../../../assets/default.png" height="100" width="155"/>
                        <div class="ico_play"></div>
                      </div>
                    </div>
                  </vue-scroll>
                </div>
              </TabPane>
            </Tabs>

          </div>
        </Col>
      </Row>
      <Row>
        <Col style="margin-left: 80px">
          这一块待定
        </Col>
      </Row>
    </div>

    <div>
      <div class="isoft_bg_white isoft_pd10">
        <HotRecommend :show-display-icon="true"></HotRecommend>
      </div>
    </div>

  </div>
</template>

<script>
  import {QueryCustomTagCourse, ShowCourseDetail, videoPlayUrl} from "../../../api"
  import HotRecommend from "./HotRecommend";

  export default {
    name: "VideoPlay",
    components: {HotRecommend},
    data() {
      return {
        recommendCourses: [],
        cVideos: [],
        course: '',
        curVideo: '',//当前播放视频
        currentClickIndex:0,

        // 滚动条设置
        scrollOps: {
          vuescroll: {
            mode: 'native',//native 使用与PC ， slide 使用移动端
            sizeStrategy: 'percent',//如果父容器不是固定高度，请设置为 number , 否则保持默认的percent即可
            detectResize: true//是否检测内容尺寸发生变化
          },
          scrollPanel: {
            initialScrollY: false,//只要组件mounted之后自动滚动的距离。 例如 100 or 10%
            scrollingX: false,//是否启用 x 或者 y 方向上的滚动
            scrollingY: true,
          },
          rail: {
            keepShow: false //是否即使 bar 不存在的情况下也保持显示
          },
          bar: {
            keepShow: true,//是否一直显示
            hoverStyle: true,
            onlyShowBarOnScroll: false, //是否只有滚动的时候才显示滚动条
            background: "#333",//滚动条颜色
            opacity: 1,//滚动条透明度
          }
        }
      }
    },
    methods: {
      refreshCorsedetail: async function () {
        const course_id = this.$route.query.course_id;
        const result = await ShowCourseDetail({course_id:course_id});
        if (result.status === "SUCCESS") {
          this.course = result.course;
          // 根据id来排序的
          this.cVideos = result.cVideos.sort((video1, video2) => video1.id > video2.id);
          this.curVideo = this.cVideos.filter(video => video.id == this.$route.query.video_id)[0];
          this.currentClickIndex = this.cVideos.indexOf(this.curVideo);
        }
      },
      playVideo: function (video_id) {
        // 右侧选中播放指示
        this.currentClickIndex = this.cVideos.indexOf(this.curVideo);
        //播放curVideo
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
      addPlayNextEventListener: function () {
        let _this = this;
        let video = document.getElementById("videoPath");
        video.addEventListener("ended", function () {
          let nextVideo = _this.cVideos.filter(video => video.id > _this.curVideo.id);
          if (nextVideo != null && nextVideo !== undefined && nextVideo.length > 0) {
            _this.curVideo = nextVideo[0];
          }
        });
      },
      clickCourse:function (index) {
        this.currentClickIndex = index;
        this.curVideo = this.cVideos[index];
      },
      refreshCustomTagCourse: async function (custom_tag) {
        const result = await QueryCustomTagCourse({custom_tag: custom_tag});
        if (result.status === "SUCCESS") {
          this.recommendCourses = result.custom_tag_courses;
        }
      },
      sleep:function (numberMillis) {
        let now = new Date();
        let exitTime = now.getTime() + numberMillis;
        while (true) {
          now = new Date();
          if (now.getTime() > exitTime){
            return;
          }
        }
      }
    },
    mounted: function () {
      // 加载当前课程所有视频资源,为自动播放下一集做准备
      this.refreshCorsedetail();
      // 注册播放下一集事件
      this.addPlayNextEventListener();
      // 加载热门推荐课程列表
      this.refreshCustomTagCourse('recommand');
    },
    filters: {
      filterSuffix: function (value) {
        // 去除视频文件后缀
        return value.slice(0, value.indexOf("."));
      }
    },
    watch: {
      curVideo: function () {
        this.playVideo(this.curVideo.id);
      }
    }
  }
</script>

<style scoped>

  .bodyColor{
    background-color: rgba(35,35,37,0.95)
  }

  .scrollBgColor{
    background-color: #282828;
  }

  .videoClass {
    object-fit:fill;
    width:795px;
    height:450px;
  }

  .video_item:hover {
    color: rgba(0, 200, 6, 0.68);
    cursor: pointer;
  }

  .course_item {
    color: #999;
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
