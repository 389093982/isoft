<template>
  <div>

    <div class="bodyColor" style="height: 620px">
      <!--整体一行-->
      <Row>
        <!--左侧视频播放-->
        <Col span="15" style="margin-left: 80px">
          <div v-if="course && curVideo" style="height: 30px;margin: 10px 0 0 0 ;color: #ccc">
            正在播放: {{course.course_name}} / 第{{currentClickIndex+1 | modification}}集: {{curVideo.video_name | filterSuffix }}
          </div>
          <div v-else style="height: 30px;margin: 10px 0 0 0 ;color: #ccc">
            &nbsp;
          </div>
          <div>
            <video ref="video" class="videoClass" width="100%" height="100%" controls preload="auto" id="videoPath" autoplay="autoplay" controlslist="nodownload">
              <source src="" type="video/mp4">
              <source src="" type="video/ogg">
              您的浏览器不支持Video标签。
            </video>
          </div>
        </Col>
        <!--右侧竖块-->
        <Col span="6" style="margin-left: 30px;">
          <div style="margin:10px 0 0 0 ;width: 95%">
            <Tabs size="small">
              <TabPane :label="course.course_name">
                <!--本主题视频集数-->
                <div class="scrollBgColor" style="padding: 5px 0 0 10px ">
                  <vue-scroll :ops="scrollOps" style="width:99%;height:425px;">
                    <div v-for="(video, index) in cVideos" style="color: #ccc;padding: 1px">
                        <span class="video_item" :style="{color:index===currentClickIndex?'#00c806':''}" @click="clickVideoName(index)">
                          第{{index + 1 | modification}}集:&nbsp;{{video.video_name | filterSuffix | filterLimitFunc(12)}}
                        </span>
                        <span v-if="isShowFreeChargeVipIcon">
                          <sup v-if="course.isCharge==='free' || index+1<=course.preListFree" style="color: #1bcc0b;margin: 0 0 0 2px">免费</sup>
                          <sup v-else-if="course.isCharge==='charge' && index+1>course.preListFree" style="color: #ff2525;margin: 0 0 0 2px">付费</sup>
                          <sup v-else-if="course.isCharge==='vip'" style="color: #ff2525;margin: 0 0 0 2px">vip</sup>
                          <sup v-else>&nbsp;</sup>
                        </span>
                    </div>
                  </vue-scroll>
                </div>
              </TabPane>
              <TabPane label="推荐课程">
                <!--推荐课程-->
                <div class="scrollBgColor" style="padding: 5px 0 0 10px ">
                  <vue-scroll :ops="scrollOps" style="width:99%;height:425px;">
                    <div v-for="(course, index) in recommendCourses" class="course_item" :title="course.course_short_desc">
                      <span>【热荐课程】&nbsp;{{course.course_name}}</span>
                      <div class="course_small_image" style="width: 155px;">
                        <img v-if="course.small_image" :src="course.small_image" height="100" width="155"/>
                        <img v-else src="../../../../static/images/common_img/default.png" height="100" width="155"/>
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
      <!--播放器下方，课程&和作者基本信息-->
      <Row>
        <Col span="15" style="margin-left: 80px;color: #ccc">
          {{renderNickName(course.course_author)}} • {{course.course_name}} ,
          <span class="courseDes">共{{course.course_number}}集</span><br>
          {{course.course_type}} / {{course.course_sub_type}}<br>
          {{course.course_short_desc}}
        </Col>
      </Row>
    </div>

    <!--购买提示-->
    <ISimpleConfirmModal ref="comfirmModal" modal-title="温馨提示:" :modal-width="300" @handleSubmit="toPay()">
      <div style="text-align: center;color: #ff6900;font-size: 15px">
        <span>{{comfirmTips}}</span>
      </div>
    </ISimpleConfirmModal>

    <div>
      <div class="isoft_bg_white isoft_pd10">
        <HotRecommend :show-display-icon="true"></HotRecommend>
      </div>
    </div>

  </div>
</template>

<script>
  import {GetUserDetail,QueryCustomTagCourse, queryPayOrderList, ShowCourseDetailForApp, videoPlayUrl} from "../../../api"
  import HotRecommend from "./HotRecommend";
  import {checkFastClick,checkEmpty,GetTodayTime_yyyyMMddhhmmss,formatUTCtime} from "../../../tools/index"
  import {getLoginUserName} from "../../../tools/sso"
  import ISimpleConfirmModal from "../../Common/modal/ISimpleConfirmModal";

  export default {
    name: "VideoPlay",
    components: {ISimpleConfirmModal, HotRecommend},
    data() {
      return {
        loginUser:null,
        user:'',                   //作者信息
        recommendCourses: [],
        cVideos: [],               //课程的Videos
        course: '',                //当前课程
        curVideo: '',              //当前播放视频
        currentClickIndex:0,      //当前播放video索引
        comfirmTips:'',           //付款弹框提示
        hasPaid:false,            //默认为未付款

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
        },
      }
    },
    methods: {
      refreshPaidAndCourse:async function(){
        if (this.loginUserName) {
          console.log("用户已登录");
          const result = await queryPayOrderList({
            'user_name':this.loginUserName,
            'goods_type':'course_theme_type',
            'goods_id':this.$route.query.course_id,
            'currentPage':1,
            'offset':10,
            'pay_result':'SUCCESS',
          });
          if (result.status === 'SUCCESS') {
            if (result.orders.length === 1 && result.orders[0].pay_result === 'SUCCESS') {
              this.hasPaid = true;
            }
            this.refreshCorsedetail();
          }
        }else {
          console.log("用户未登录");
          this.refreshCorsedetail();
        }
      },
      refreshCorsedetail: async function () {
        //刷新课程
        const course_id = this.$route.query.course_id;
        const result = await ShowCourseDetailForApp({course_id:course_id});
        if (result.status === "SUCCESS") {
          this.course = result.course;
          this.cVideos = result.cVideos.sort((video1, video2) => video1.id > video2.id);// 根据id来排序的
          this.playVideo(this.cVideos.filter(video => video.id == this.$route.query.video_id)[0]);
          let params = {
            'userName':this.course.course_author
          };
          const userInfo = await GetUserDetail(params);
          if (userInfo.status === "SUCCESS") {
            this.user = userInfo.user;
          }
        }
      },
      SearchLoginUserDetail: async function () {
        //加载用户信息
        let params = {
          'userName':getLoginUserName(),
        };
        const result = await GetUserDetail(params);
        if (result.status === "SUCCESS") {
          this.loginUser = result.user;
        }
      },
      //播放视频
      playVideo: function (curVideo) {
        let vipLevel = this.loginUser==null?'0':this.loginUser.vip_level;
        let vipExpiredTime = this.loginUser==null?'19700101000000':formatUTCtime(this.loginUser.vip_expired_time,'yyyyMMddHHmmss');
        let nowTime = GetTodayTime_yyyyMMddhhmmss();
        let compare = parseInt(vipExpiredTime) > parseInt(nowTime);

        if(this.course.isCharge==='vip'){
          if((parseInt(vipLevel)>0 && compare) || this.course.course_author===getLoginUserName()){
            //2.右侧选中播放指示
            this.currentClickIndex = this.cVideos.indexOf(curVideo);
            //3.设置当前curVideo
            this.curVideo = curVideo;
            //4.播放curVideo
            this.play(curVideo);
          }else{
            this.$Message.warning("会员专享课程!");
          }
        }else{
          //1.收费判断
          if (this.hasPaid || this.course.course_author===getLoginUserName() || this.cVideos.indexOf(curVideo) + 1 <= this.course.preListFree || this.course.isCharge==='free') {
            //2.右侧选中播放指示
            this.currentClickIndex = this.cVideos.indexOf(curVideo);
            //3.设置当前curVideo
            this.curVideo = curVideo;
            //4.播放curVideo
            this.play(curVideo);
          }else{
            this.comfirmTips = "付费视频，前去购买?";
            this.$refs.comfirmModal.showModal();
          }
        }
      },
      play:function (curVideo) {
        // 使用对象流方式获取

        // let xhr = new XMLHttpRequest();                                                      //创建XMLHttpRequest对象
        // xhr.open('GET', videoPlayUrl + "?video_id=" + curVideo.id, true);                   //配置请求方式、请求地址以及是否同步
        // xhr.responseType = 'blob';                                                            //设置结果类型为blob;
        // xhr.onload = function (e) {
        //   if (this.status === 200) {
        //     // 获取blob对象
        //     let blob = this.response;
        //     // 获取blob对象地址，并把值赋给容器
        //     document.getElementById("videoPath").setAttribute("src", URL.createObjectURL(blob));
        //   }
        // };
        // xhr.send();

        // 使用链接地址访问
        document.getElementById("videoPath").setAttribute("src", curVideo.first_play);
      },
      addPlayNextEventListener: function () {
        let _this = this;
        let video = document.getElementById("videoPath");
        video.addEventListener("ended", function () {
          let nextVideo = _this.cVideos.filter(video => video.id > _this.curVideo.id);
          if (nextVideo != null && nextVideo !== undefined && nextVideo.length > 0) {
            _this.playVideo(nextVideo[0])
          }
        });
      },
      clickVideoName:function (index) {
        if (checkFastClick()){
          this.$Message.error("点击过快,请稍后重试!");
          return;
        }
        this.playVideo(this.cVideos[index]);
      },
      catchPlayError: function(){
        let _this = this;
        let video = document.getElementById("videoPath");
        video.addEventListener("error", function () {
          console.log("如果加载进来之后播放视频报错，那么这里做个处理,0.1秒后执行");
          setTimeout(function () {
            console.log("0.1秒到了，开始执行");
            _this.playVideo(_this.curVideo);
          }, 100);
        });
      },
      refreshCustomTagCourse: async function (custom_tag) {
        const result = await QueryCustomTagCourse({custom_tag: custom_tag});
        if (result.status === "SUCCESS") {
          this.recommendCourses = result.custom_tag_courses;
        }
      },
      renderNickName: function (user_name) {
        return !checkEmpty(this.user.nick_name) ? this.user.nick_name : user_name;
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
      },
      toPay:function () {
        this.$refs.comfirmModal.hideModal();
        this.$router.push({path:'/payment/pay',name:'pay',params:{type:'course_theme_type',id:this.course.id}});
      }
    },
    computed:{
      loginUserName: function () {
        return getLoginUserName();
      },
      isShowFreeChargeVipIcon:function(){
        if (this.course.course_author===getLoginUserName() || this.hasPaid) {
          return false;
        }else{
          return true;
        }
      },
    },
    mounted: function () {
      //判断是否已付费 & 刷新课程
      this.refreshPaidAndCourse();
      // 注册播放下一集事件
      this.addPlayNextEventListener();
      //捕获播放报错，并处理
      this.catchPlayError();
      // 加载热门推荐课程列表
      this.refreshCustomTagCourse('recommand');
      //获取登录用户信息
      this.SearchLoginUserDetail();
    },
    filters: {
      filterSuffix: function (value) {
        // 去除视频文件后缀
        return value.slice(value.indexOf(".")+1,value.lastIndexOf("."));
      },
      modification:function (value) {
        return value < 10 ? '0'+value : value
      },
      // 内容超长则显示部分
      filterLimitFunc:function (value,limitLenth) {
        if (value.length > limitLenth) {
          return value.slice(0,limitLenth) + ' · · ·'
        }else {
          return value
        }
      }
    },
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
    color: #00c806;
    cursor: pointer;
  }

  .course_item {
    color: #ccc;
    cursor: pointer;
  }

  .course_item:hover {
    color: #00c806;
  }

  .course_item:hover > .course_small_image{
    display: block;
  }

  .course_small_image{
    display: none;
  }

  .ico_play {
    background: url(../../../../static/images/common_img/ico_play.png) no-repeat;
    background-size: 20px;
    position: relative;
    top: -30px;
    left: 130px;
    width: 20px;
    height: 20px;
  }
</style>
