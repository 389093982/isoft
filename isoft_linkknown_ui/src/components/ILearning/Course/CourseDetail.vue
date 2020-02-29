<template>
  <div>
    <Row>
      <!-- 左侧课程详情部分 -->
      <Col span="16" style="padding-right: 5px;">
        <div class="isoft_bg_white isoft_pd20">
          <!-- 头部 -->
          <Row class="header">
            <Col span="8">
              <h4 class="isoft_inline_ellipsis">课程名称：{{course.course_name}}</h4>
              <div class="course_img">
                <img :src="course.small_image" width="180" height="120" @error="defImg()"/>
                <div class="course_name">{{course.course_name}}</div>
              </div>
            </Col>
            <Col span="16">
              <CourseMeta v-if="course && course.course_author" :course="course"/>
              <p>
                <a href="javascript:;" v-if="course_collect===true" @click="toggle_favorite(course.id,'course_collect', '取消收藏')">取消收藏</a>
                <a href="javascript:;" v-else @click="toggle_favorite(course.id,'course_collect', '收藏')">加入收藏</a>&nbsp;
                <a href="javascript:;" v-if="course_parise===true" @click="toggle_favorite(course.id,'course_praise', '取消点赞')">取消点赞</a>
                <a href="javascript:;" v-else @click="toggle_favorite(course.id,'course_praise', '点赞')">我要点赞</a>
              </p>
            </Col>
          </Row>
          <hr style="margin-top: 10px;">
          <!-- 视频链接 -->
          <Row style="margin: 10px 0;min-height: 200px;">
            <div v-for="(cVideo, index) in cVideos" class="video_item" style="margin: 0px 10px;padding: 10px;">
              <IBeautifulLink>第 {{index + 1}} 集：{{cVideo.video_name}}</IBeautifulLink>
              <router-link :to="{path:'/ilearning/video_play',query:{course_id:course.id,video_id:cVideo.id}}">
                <Button style="float: right;" size="small" type="success" class="hovered hvr-grow">立即播放</Button>
              </router-link>
            </div>
            <Spin fix size="large" v-if="isLoading">
              <div class="isoft_loading"></div>
            </Spin>
          </Row>
          <hr>
        </div>

        <div class="isoft_bg_white isoft_top5 isoft_pd20" style="min-height: 600px;">
          <!-- 评论模块 -->
          <IEasyComment :theme_pk="course.id" theme_type="course_theme_type" style="margin-top: 50px;"/>
        </div>
      </Col>

      <Col span="8">
        <div class="isoft_bg_white">
          <UserAbout :userName="course.course_author" :titleLimitLenth="10"/>
        </div>
        <div class="isoft_bg_white">
          <HotUser style="margin-left: 2px;"/>
        </div>
        <div class="isoft_bg_white">
          <HotRecommend showMode="list" style="margin-left: 2px;"/>
        </div>
      </Col>
    </Row>
  </div>
</template>

<script>
  import {ShowCourseDetail, ToggleFavorite} from "../../../api"
  import IEasyComment from "../../Comment/IEasyComment"
  import HotRecommend from "./HotRecommend"
  import UserAbout from "../../User/UserAbout"
  import HotUser from "../../User/HotUser"
  import CourseMeta from "./CourseMeta";
  import {checkHasLogin, getLoginUserName} from "../../../tools/sso";
  import {CheckHasLoginConfirmDialog} from "../../../tools/index"

  export default {
    name: "CourseDetail",
    components: {CourseMeta, IEasyComment, HotRecommend, UserAbout, HotUser},
    data() {
      return {
        isLoading: true,
        defaultImg: require('../../../assets/default.png'),
        // 当前课程
        course: {},
        // 视频清单
        cVideos: [],
        // 课程收藏
        course_collect: false,
        // 课程点赞
        course_parise: false,
      }
    },
    methods: {
      defImg() {
        let img = event.srcElement;
        img.src = this.defaultImg;
        img.onerror = null; //防止闪图
      },
      checkHasLogin:function(){
        return checkHasLogin();
      },
      getLoginUserName:function(){
        return getLoginUserName();
      },
      refreshCourseDetail: async function () {
        this.isLoading = true;
        try {
          const course_id = this.$route.query.course_id;
          let user_name;
          if (this.checkHasLogin) {
            user_name = this.getLoginUserName();
          }else {
            user_name = null;
          }
          let params = {
            'course_id':course_id,
            'user_name':user_name,
          };
          const result = await ShowCourseDetail(params);
          if (result.status === "SUCCESS") {
            this.course = result.course;
            this.cVideos = result.cVideos;
            this.course_collect = result.course_collect;
            this.course_parise = result.course_parise;
          }
        } finally {
          this.isLoading = false;
        }
      },
      toggle_favorite: async function (favorite_id, favorite_type, message) {
        if (checkHasLogin()) {
          const result = await ToggleFavorite({favorite_id, favorite_type});
          if (result.status === "SUCCESS") {
            this.$Message.success(message + "成功!");
            this.refreshCourseDetail();
          }
        }else {
          CheckHasLoginConfirmDialog(this, {path: "/ilearning/course_detail?course_id="+this.$route.query.course_id});
        }
      },
    },
    mounted: function () {
      this.refreshCourseDetail(this.$route.query.course_id);
    },
    watch: {
      "$route.params": "refreshCourseDetail"      // 如果 $route.params 有变化,会再次执行该方法
    }
  }
</script>

<style scoped>
  .header a {
    color: red;
  }

  .course_img {
    width: 180px;
    height: 120px;
    cursor: pointer;
  }

  .course_img .course_name {
    display: none;
    padding: 2px 0 0 10px;
    background-color: rgba(0, 0, 0, 0.6);
    color: white;
    height: 24px;
    position: relative;
    top: 0px;
    transition: all ease-in 1s;
  }

  .course_img:hover .course_name {
    display: block;
    top: -30px;
  }

  .video_item:hover {
    background-color: rgba(218, 16, 0, 0.05);
    cursor: pointer;
  }
</style>
