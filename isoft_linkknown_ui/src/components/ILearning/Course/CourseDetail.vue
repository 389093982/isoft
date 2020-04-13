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
                <div class="course_name">
                  <span class="move_course_name">{{course.course_name}}</span>
                </div>
                <div class="ico_play"></div>
              </div>
              <div style="margin: 5px 0 0 40px">
                <span v-if="course.isCharge==='charge'" style="color: #ff6900">
                  <Icon type="logo-yen" /><span style="font-size: 20px">{{course.price}}</span>
                  &nbsp;&nbsp;|&nbsp;&nbsp;<span style="cursor: pointer;" @click="toPay('course',course.id)"><Icon type="md-cart" /><span>购买</span></span>
                </span>
                <span v-else style="color: #cc0000;font-size: 15px">免费视频</span>
              </div>
            </Col>
            <Col span="16">
              <!--课程详情介绍-->
              <CourseMeta v-if="course && course.course_author" :course="course"/>
              <p>
                <a href="javascript:;" v-if="course_collect === true" @click="toggle_favorite(course.id,'course_collect', '取消收藏')" style="color: #ff6900">
                  <Icon type="md-bookmark" style="font-size: 20px" />
                  已收藏
                </a>
                <a href="javascript:;" v-else @click="toggle_favorite(course.id,'course_collect', '收藏')" style="color: grey">
                  <Icon type="md-bookmark" style="font-size: 20px" />
                  收藏
                </a>&nbsp;
                &nbsp;&nbsp;
                <a href="javascript:;" v-if="course_praise === true" @click="toggle_favorite(course.id,'course_praise', '取消点赞')" style="color: #cc0000">
                  <Icon class="hvr-grow" type="md-heart" style="font-size: 20px;cursor: pointer;" />
                  已点赞
                </a>
                <a href="javascript:;" v-else @click="toggle_favorite(course.id,'course_praise', '点赞')" style="color: grey">
                  <Icon class="hvr-grow" type="md-heart" style="font-size: 20px;cursor: pointer;" />
                  点赞
                </a>
              </p>
            </Col>
          </Row>

          <SepLine/>

          <!-- 视频链接 -->
          <Row style="margin: 10px 0;min-height: 200px;">
            <div v-for="(cVideo, index) in filter_cVideos" class="video_item" style="margin:0 10px 0 10px ;padding: 5px;"
                 :style="{backgroundColor:index===clickIndex?'rgba(172,168,167,0.2)':''}" @click="clickVideoName(index)">
              <span style="color: #9b9896">
                <span class="isoft_font" :style="{color:index===clickIndex?'#00c806':''}">
                  第{{index + 1 | modification}}集: {{cVideo.video_name | filterSuffix}}
                  <sup v-if="course.isCharge==='free'" style="color: #ff6900;margin: 0 0 0 2px">免费</sup>
                  <sup v-else-if="course.isCharge==='charge' && index+1<=course.preListFree" style="color: #ff6900;margin: 0 0 0 2px">免费</sup>
                  <sup v-else>&nbsp;</sup>
                </span>
              </span>
              <span style="float: right;">
                <span class="isoft_font12 isoft_color_grey isoft_mr10" v-if="cVideo.duration > 0">时长&nbsp;{{cVideo.duration}}&nbsp;s</span>
                <Button size="small" type="success" class="hovered hvr-grow" @click="playSelectedVideo(course.id,cVideo.id,index,course.preListFree)">立即播放</Button>
              </span>
            </div>
            <div v-if="cVideos.length===0" class="video_item" style="margin-right: 10px;padding: 10px;color: #00c806">
              ^_^ 作者还未上传视频哦...
            </div>
            <!--查看更多-->
            <div style="text-align: center;" v-if="cVideos.length > minLen">
              <span class="isoft_tag1"><show-more @changeShowMore="changeShowMore"></show-more></span>
            </div>
            <div v-else>
              <!--集数低于5，展示其他课程-->
              <br>
              <span style="margin-left: 8px"><i>为您推荐:</i></span>
              <Row>
                <Col span="6" v-for="course in recommendCourses">
                  <div class="courseBorder" style="position: relative;cursor: pointer"
                       @click="$router.push({path:'/ilearning/courseDetail',query:{course_id:course.id}})">
                    <img v-if="course.small_image" :src="course.small_image" height="100" width="155"/>
                    <img v-else src="../../../assets/default.png" height="100" width="155"/>
                    <div class="isoft_font12 isoft_inline_ellipsis isoft_color_grey" style="text-align: center;">
                      {{course.course_name}}
                    </div>
                    <span v-if="course.isCharge==='free'" class="isoft_free">免费</span>
                  </div>
                </Col>
              </Row>
            </div>
            <Spin fix size="large" v-if="isLoading">
              <div class="isoft_loading"></div>
            </Spin>
          </Row>
        </div>

        <div class="isoft_bg_white isoft_top5 isoft_pd20" style="min-height: 600px;">
          <p>学者印象：</p>
          <VoteTags  v-if="course.id > 0" ref="VoteTags"/>
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
  import {GetHotCourseRecommend, IsFavorite, ShowCourseDetail, ToggleFavorite} from "../../../api"
  import IEasyComment from "../../Comment/IEasyComment"
  import HotRecommend from "./HotRecommend"
  import UserAbout from "../../User/UserAbout"
  import HotUser from "../../User/HotUser"
  import CourseMeta from "./CourseMeta";
  import {checkHasLogin, getLoginUserName} from "../../../tools/sso";
  import {CheckHasLoginConfirmDialog} from "../../../tools/index"
  import VoteTags from "../../Decorate/VoteTags";
  import ShowMore from "../../Elementviewers/showMore";
  import SepLine from "../../Common/SepLine";

  export default {
    name: "CourseDetail",
    components: {SepLine, ShowMore, CourseMeta, IEasyComment, HotRecommend, UserAbout, HotUser,VoteTags,},
    data() {
      return {
        isLoading: true,
        defaultImg: require('../../../assets/default.png'),
        // 当前课程
        course: {},
        // 视频清单
        cVideos: [],
        filter_cVideos: [],
        // 课程收藏
        course_collect: false,
        // 课程点赞
        course_praise: false,
        clickIndex:0,
        minLen:5,
        // 推荐课程
        recommendCourses:[],
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
        this.clickIndex = 0;
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
            this.filter_cVideos = result.cVideos.slice(0,this.minLen);
            this.refreshFavoriteStatus();
            if (this.filter_cVideos.length<this.minLen){
              // 如果集数少于5，那么做一个推荐
              this.refreshHotRecommend()
            }
            this.$refs.VoteTags.setRefererType("vote_course");
            this.$refs.VoteTags.setRefererId(this.course.id);
            this.$refs.VoteTags.refreshVoteTags();
          }
        } finally {
          this.isLoading = false;
        }
      },
      refreshHotRecommend: async function () {
        const result = await GetHotCourseRecommend();
        if (result.status === "SUCCESS") {
          // 暂定展示4推荐条视频
          this.recommendCourses = result.courses.slice(0,4);
        }
      },
      refreshFavoriteStatus: async function () {
        if (checkHasLogin() && this.course) {
          let result = await IsFavorite({favorite_id: this.course.id, favorite_type: "course_collect"});
          if (result.status === "SUCCESS") {
            this.course_collect = result.isFavorite;
          }

          result = await IsFavorite({favorite_id: this.course.id, favorite_type: "course_praise"});
          if (result.status === "SUCCESS") {
            this.course_praise = result.isFavorite;
          }
        }
      },
      toggle_favorite: async function (favorite_id, favorite_type, message) {
        if (checkHasLogin()) {
          const result = await ToggleFavorite({favorite_id, favorite_type});
          if (result.status === "SUCCESS") {
            this.refreshFavoriteStatus();
            this.$Message.success(message + "成功!");
          }
        }else {
          CheckHasLoginConfirmDialog(this, {path: "/ilearning/courseDetail?course_id="+this.$route.query.course_id});
        }
      },
      clickVideoName:function (index) {
        this.clickIndex = index;
      },
      playSelectedVideo:function(course_id,video_id,index,preListFree){
        if (this.course.isCharge === 'charge' && index + 1 > preListFree) {
          this.$Message.warning("付费视频！");
          return;
        }
        this.$router.push({path:'/ilearning/videoPlay',query:{course_id:course_id,video_id:video_id}});
      },
      changeShowMore:function (showMore) {
        showMore ? this.filter_cVideos = this.cVideos : this.filter_cVideos = this.cVideos.slice(0,this.minLen);
      },
      //购买此课程
      toPay:function (type,id) {
        this.$router.push({path:'/payment/pay',query:{type:type,id:id}});
      }
    },
    mounted: function () {
      this.refreshCourseDetail(this.$route.query.course_id);
    },
    watch: {
      "$route.params": "refreshCourseDetail"      // 如果 $route.params 有变化,会再次执行该方法
    },
    filters: {
      filterSuffix: function (value) {
        // 去除视频文件后缀
        return value.slice(0, value.indexOf("."));
      },
      modification:function (value) {
        return value < 10 ? '0'+value : value
      }
    },
  }
</script>

<style scoped>

  .courseBorder{
    width:176px;
    padding: 10px 0 0 10px;
    border: 1px solid #FFFFFF;
  }

  .courseBorder:hover{
    background-color: rgba(214, 214, 214, 0.5);
    border: 1px solid #d0cdd2;
  }

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
    background-color: rgba(176, 173, 171, 0.15);
    cursor: pointer;
  }

   .ico_play {
     background: url(../../../assets/ico_play.png) no-repeat;
     background-size: 20px;
     position: absolute;
     top: 125px;
     left: 150px;
     width: 20px;
     height: 20px;
   }

  .move_course_name{
    position:relative;
    animation: move 3s linear infinite;
  }

  @keyframes move {
    0%   {left:130px;}
    100% {left:-130px;}
  }
</style>
