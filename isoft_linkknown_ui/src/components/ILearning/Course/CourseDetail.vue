<template>
  <div>
    <div style="display: flex">
      <!-- 左侧课程详情部分 -->
      <div style="width: 65%;background-color: white">
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
              <div class="showPriceAndBuy" style="margin: 10px 0 0 0">
                <div v-if="course.isCharge==='vip'" style="color: #1bcc0b;font-size: 15px;margin-left: 50px">
                  会员专享
                </div>
                <div v-else-if="course.isCharge==='charge'" style="color: #ff6900">
                  <Row>
                    <Col span="14">
                        <span class="showPrice">
                          ¥<span style="font-size: 20px">{{course.price}}</span>
                        </span>
                        <span v-if="course.is_show_old_price==='Y'" style="font-size: 14px;color: grey;text-decoration:line-through;">
                          ¥{{course.old_price}}
                        </span>
                    </Col>
                  </Row>
                  <Row v-if="!isMyCourse(course.course_author)">
                    <Col span="9">
                      <span @click="addToShoppingCart('course_theme_type',course.id,course.price)">
                        <div class="addToShoppingCart">加入购物车</div>
                      </span>
                    </Col>
                    <Col span="6">
                      <span @click="toPay('course_theme_type',course.id)">
                        <div class="toBuy">立即购买</div>
                      </span>
                    </Col>
                  </Row>
                  <Row v-else>
                    <Col span="10" v-if="isMyCourse(course.course_author)">
                      <div class="myCourse" @click="$router.push({path:'/ilearning/courseSpace/editCourse',query:{course_id:course.id}})">我的课程</div>
                    </Col>
                  </Row>
                  <Row>
                    <div style="margin-top: 15px">
                      <Icon class="move_shopping_cart" type="ios-cart-outline" :size="40" @click="viewShoppingCart()" style="cursor: pointer"/>
                    </div>
                  </Row>
                </div>
                <div v-else style="color: #cc0000;font-size: 15px;margin-left: 50px">免费视频</div>
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

          <SepLine style="margin: 25px 0 30px 0;"/>

          <!-- 视频链接 -->
          <Row style="min-height: 200px;">
            <div v-for="(cVideo, index) in filter_cVideos" class="video_item" style="margin:0 10px 0 10px ;padding: 5px;"
                 :style="{backgroundColor:index===clickIndex?'rgba(172,168,167,0.2)':''}" @click="clickVideoName(index)">
              <span style="color: #9b9896">
                <span class="isoft_font" :style="{color:index===clickIndex?'#00c806':''}">
                  第{{index + 1 | modification}}集: {{cVideo.video_name | filterSuffix}}
                  <span v-if="isShowFreeChargeVipIcon">
                    <sup v-if="course.isCharge==='free' || index+1<=course.preListFree" style="color: #1bcc0b;margin: 0 0 0 2px">免费</sup>
                    <sup v-else-if="course.isCharge==='charge' && index+1>course.preListFree" style="color: #ff2525;margin: 0 0 0 2px">付费</sup>
                    <sup v-else-if="course.isCharge==='vip'" style="color: #ff2525;margin: 0 0 0 2px">vip</sup>
                  </span>
                </span>
              </span>
              <span style="float: right;">
                <span class="isoft_font12 isoft_hover_desc isoft_mr10" v-if="cVideo.duration > 0">时长&nbsp;{{cVideo.duration}}&nbsp;s</span>
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
                    <img v-else src="../../../../static/images/common_img/default.png" height="100" width="155"/>
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
          <VoteTags  v-if="course.id > 0" ref="voteTags" referer_type="vote_course" :referer_id="course.id"/>
          <!-- 评论模块 -->
          <IEasyComment :theme_pk="course.id" theme_type="course_theme_type" style="margin-top: 50px;"/>
        </div>
      </div>

      <div style="width: 34%;background-color: white;margin: 0 0 0 5px">
        <div class="isoft_bg_white">
          <img src="../../../../static/images/order/banner_coupon.png" style="width: 300px;height: 130px;"
               class="isoft_point_cursor" @click="$router.push('/payment/couponCenter')"/>

          <HotUser style="margin-left: 2px;"/>
        </div>
        <div class="isoft_bg_white">
          <HotRecommend showMode="list" style="margin-left: 2px;"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {GetHotCourseRecommend, IsFavorite, ShowCourseDetail, queryPayOrderList,ToggleFavorite,
    GetUserDetail,QueryDesignatedCoupon,ReceiveCoupon,addToShoppingCart} from "../../../api"
  import IEasyComment from "../../Comment/IEasyComment"
  import HotRecommend from "./HotRecommend"
  import HotUser from "../../User/HotUser"
  import CourseMeta from "./CourseMeta";
  import {checkHasLogin, getLoginUserName} from "../../../tools/sso";
  import {checkFastClick, CheckHasLoginConfirmDialog, GetToday_yyyyMMdd,GetTodayTime_yyyyMMddhhmmss,formatUTCtime,
    CheckHasLoginConfirmDialog2} from "../../../tools/index"
  import VoteTags from "../../Decorate/VoteTags";
  import ShowMore from "../../Elementviewers/showMore";
  import SepLine from "../../Common/SepLine";
  import Coupon from "../../Common/coupon/Coupon";

  export default {
    name: "CourseDetail",
    components: {Coupon, SepLine, ShowMore, CourseMeta, IEasyComment, HotRecommend, HotUser,VoteTags,},
    data() {
      return {
        isLoading: true,
        defaultImg: require('../../../../static/images/common_img/default.png'),
        course: {},                // 当前课程
        hasPaid:false,            //默认为未付款
        cVideos: [],              // 视频清单
        filter_cVideos: [],
        course_collect: false,  // 课程收藏
        course_praise: false,   // 课程点赞
        designated_reduce_coupon:'',    //指定券 - 减免
        designated_discount_coupon:'',    //指定券 - 打折
        clickIndex:0,
        minLen:5,
        recommendCourses:[],    // 推荐课程
        loginUser:null,
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
      // 先刷新是否已付费，再刷新课程
      refreshPaidAndCourse:async function(){
        if (this.getLoginUserName()) {
          console.log("用户已登录");
          const result = await queryPayOrderList({
            'user_name':this.getLoginUserName(),
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
            this.refreshCourseDetail();
          }
        }else {
          console.log("用户未登录");
          this.refreshCourseDetail();
        }
      },
      refreshCourseDetail: async function () {
        //刷新课程
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
            //更新 voteTags 标签
            this.$refs.voteTags.setRefererType("vote_course");
            this.$refs.voteTags.setRefererId(this.course.id);
            this.$refs.voteTags.refreshVoteTags();
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
      playSelectedVideo:function(course_id,video_id,index,preListFree){
        let vipLevel = this.loginUser==null?'0':this.loginUser.vip_level;
        let vipExpiredTime = this.loginUser==null?'19700101000000':formatUTCtime(this.loginUser.vip_expired_time,'yyyyMMddHHmmss');
        let nowTime = GetTodayTime_yyyyMMddhhmmss();
        let compare = parseInt(vipExpiredTime) > parseInt(nowTime);

        if(this.course.isCharge==='vip'){
          if((parseInt(vipLevel)>0 && compare) || this.course.course_author===getLoginUserName()){
            this.$router.push({path:'/ilearning/videoPlay',query:{course_id:course_id,video_id:video_id}});
          }else{
            this.$Message.warning("会员专享课程!");
          }
        }else{
          if (this.hasPaid || this.course.course_author===getLoginUserName() || index + 1 <= preListFree || this.course.isCharge==='free') {
            this.$router.push({path:'/ilearning/videoPlay',query:{course_id:course_id,video_id:video_id}});
          }else{
            this.$Message.warning("付费视频！");
          }
        }
      },
      changeShowMore:function (showMore) {
        showMore ? this.filter_cVideos = this.cVideos : this.filter_cVideos = this.cVideos.slice(0,this.minLen);
      },
      //判断是不是我的课程
      isMyCourse:function(course_author){
        return course_author==getLoginUserName();
      },
      //加入购物车
      addToShoppingCart:async function(type,id,price){
        if (checkFastClick()) {
          this.$Message.error("点击过快,请稍后重试!");
          return;
        }

        if (this.course.course_author == getLoginUserName()) {
          this.$Message.info("请勿购买自己的课程");
          return false;
        }

        let _this = this;
        CheckHasLoginConfirmDialog2(_this, async function () {
          let params = {
            'goods_type':type,
            'goods_id':id,
            'goods_price_on_add':price
          };
          const result = await addToShoppingCart(params);
          if (result.status === 'SUCCESS') {
            _this.$Message.success('加入成功！');
          }else{
            _this.$Message.error(result.errorMsg)
          }
        });

      },
      //购买此课程
      toPay:function (type,id) {
        if (checkFastClick()) {
          this.$Message.error("点击过快,请稍后重试!");
          return;
        }

        if (this.course.course_author == getLoginUserName()) {
          this.$Message.info("请勿购买自己的课程");
          return false;
        }

        let _this = this;
        CheckHasLoginConfirmDialog2(_this, async function () {
          _this.$router.push({path:'/payment/pay',name:'pay',params:{type:type,id:id}});
        });
      },
      //查看购物车
      viewShoppingCart:function () {
        CheckHasLoginConfirmDialog(this, {path: "/payment/shoppingCart"});
      },
      //刷新优惠券
      refreshCoupon:async function () {
        // 查指定券
        let params = {
          'today':GetToday_yyyyMMdd(),
          'target_type':'course',
          'target_id':this.$route.query.course_id,
        };
        const designatedResult = await QueryDesignatedCoupon(params);
        if (designatedResult.status === 'SUCCESS') {
          if (designatedResult.designated_reduce_coupon != null) {
            this.designated_reduce_coupon = designatedResult.designated_reduce_coupon;
          }
          if (designatedResult.designated_discount_coupon != null) {
            this.designated_discount_coupon = designatedResult.designated_discount_coupon;
          }
        }
      },
      //领券
      receiveCoupon:async function (activity_id) {
        let params = {
          'activity_id':activity_id
        };
        const result = await ReceiveCoupon(params);
        if (result.status === 'SUCCESS') {
          this.$Message.info('领券成功！');
        }else{
          this.$Message.error(result.errorMsg);
        }
        this.refreshCoupon();
      }
    },
    mounted: function () {
      this.refreshPaidAndCourse();
      this.refreshCoupon();
      this.SearchLoginUserDetail();
    },
    computed:{
      isShowFreeChargeVipIcon:function () {
        if(this.course.course_author===getLoginUserName() || this.hasPaid){
          return false;
        }else{
          return true;
        }
      }
    },
    watch: {
      "$route.params": function () {
        this.refreshPaidAndCourse();
        this.refreshCoupon();
      }
    },
    filters: {
      filterSuffix: function (value) {
        // 去除视频文件后缀
        return value.slice(value.indexOf(".")+1,value.lastIndexOf("."));
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
     background: url(../../../../static/images/common_img/ico_play.png) no-repeat;
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

  .move_shopping_cart{
    position:relative;
    animation: shopping_cart_move 8s linear infinite;
  }


  @keyframes shopping_cart_move {
    0%   {left:-100px;}
    100% {left:150px;}
  }

  @keyframes move {
    0%   {left:130px;}
    100% {left:-130px;}
  }

  .showPrice{
    cursor: pointer;
  }

  .myCourse{
    font-size: 12px;
    cursor: pointer;
    padding: 0 0 2px 10px ;
    height: 25px;
    width: 75px;
    background-color: rgba(220, 220, 220, 0.39);
    border-radius: 20px;
    border: 2px orange solid;
  }
  .myCourse:hover{
    color: rgba(255, 105, 0, 0.65);
    background-color: rgba(214, 214, 214, 0.99);
  }

  .addToShoppingCart{
    font-size: 12px;
    cursor: pointer;
    padding: 0 0 2px 6px ;
    height: 25px;
    width: 75px;
    background-color: rgba(220, 220, 220, 0.39);
    border-radius: 20px;
    border: 2px orange solid;
  }
  .addToShoppingCart:hover{
    color: rgba(255, 105, 0, 0.65);
    background-color: rgba(214, 214, 214, 0.99);
  }

  .toBuy{
    font-size: 12px;
    cursor: pointer;
    padding: 0 0 2px 10px ;
    height: 25px;
    width: 75px;
    background-color: rgba(220, 220, 220, 0.39);
    border-radius: 20px;
    border: 2px orange solid;
  }
  .toBuy:hover{
    color: rgba(255, 105, 0, 0.65);
    background-color: rgba(214, 214, 214, 0.99);
  }
</style>
