<template>
  <div v-if="user">
    <div :class="{my_center: myCenterBgShow,other_center: otherCenterBgShow }" style="position:relative;height:150px;padding: 0 30px;display: flex;">
      <!--头像-->
      <div style="position: relative;left: 170px; color: white;font-size: 28px;">
        <div style="cursor: pointer;position: absolute;top: 32px;">
          <div @click="toUserInfo(user.user_name)">
            <!--帽子 & 头像-->
            <HatAndFacePicture :src="user.small_icon" :vip_level="user.vip_level" :hat_in_use="user.hat_in_use" :hat_relative_top="-98"></HatAndFacePicture>
          </div>
        </div>
      </div>
      <!--基本信息-->
      <div style="position: absolute;top: 40px;left: 300px;color: white">
        <div>
          <b style="font-size: 18px">{{user.nick_name}}</b> / <code>{{user.user_name}}</code>
        </div>
        <div>
          入驻时间:<Time :time="user.created_time" :interval="1"/>
          <i><span style="font-size: 12px;margin-left: 10px">积分:{{user.user_points}}</span></i>
          <i v-if="user.vip_level>0"><span style="font-size: 12px;margin-left: 10px">VIP</span></i>
        </div>
        <div>
          <i>个性签名:
            <textarea rows="1" cols="80" v-if="editSignFlag" v-model.trim="user_signature" maxlength="30" style="padding: 5px;" class="focus" @blur="handleEditSignFlag"></textarea>
            <span v-else class="hoverFlash isoft_text_rows">{{user_signature | filterLimitFunc}}</span>
            <Icon v-if="isLoginUserName(user.user_name) && !editSignFlag" class="isoft_hover_red isoft_point_cursor" type="ios-create-outline" :size="20" @click="editSign"/>
          </i>
         </div>
        <!--关注&粉丝-->
        <div>
          <Row>
            <Col span="4">
              <span style="font-size: 10px">关注:&nbsp;</span>
              <span style="cursor: pointer" @click="QueryAttentionOrFensi('Attention')">{{user.attention_counts}}</span>
            </Col>
            <Col span="4" offset="1">
              <span style="font-size: 10px">粉丝:&nbsp;</span>
              <span style="cursor: pointer" @click="QueryAttentionOrFensi('Fensi')">{{user.fensi_counts}}</span>
            </Col>
          </Row>
        </div>
      </div>
      <!--设置、消息-->
      <div v-if="isLoginUserName(user.user_name)" style="position: absolute;top: 20px;left: 600px;color: white;display: flex">
        <div @click="$router.push({ path: '/user/set'})" style="padding-left: 15px;cursor: pointer" class="hvr-grow">
          <Icon type="ios-cog-outline" style="font-size: 25px"/>
        </div>
        <div @click="$router.push({ path: '/message/messageList'})" style="padding-left: 15px;cursor: pointer">
          <Icon class="messageTipClass" type="ios-chatbubbles-outline"/>
        </div>
      </div>
    </div>

    <div class="isoft_top5" style="min-height: 450px;justify-content:space-between;">
      <div style="display: flex;">
        <div v-if="user" style="width: 65%;padding: 0 0 0 20px ;background-color: white">
          <!--第一行是主菜单: 发布博客、 我的课程、 书单列表、 我的订单 -->
          <div v-if="isLoginUserName(user.user_name)" style="background-color: white;height: 80px">
            <Row>
              <Col span="3" offset="3">
                <div @click="$router.push({ path: '/iblog/blogArticleEdit'})" style="margin: 10px 0 0 0 ;cursor: pointer;text-align: center" class="hvr-grow isoft_hover_red">
                  <Icon type="ios-create-outline" style="font-size: 40px" />
                  <div style="font-size: 12px;">发布博客</div>
                </div>
              </Col>
              <Col span="3">
                <div @click="$router.push({ path: '/ilearning/courseSpace'})" style="margin: 10px 0 0 0 ;cursor: pointer;text-align: center" class="hvr-grow isoft_hover_red">
                  <Icon type="ios-videocam-outline" style="font-size: 40px" />
                  <div style="font-size: 12px;">我的课程</div>
                </div>
              </Col>
              <Col span="3">
                <div @click="$router.push({path:'/ibook/bookList'})" style="margin: 10px 0 0 0 ;cursor: pointer;text-align: center" class="hvr-grow isoft_hover_red">
                  <Icon type="ios-book-outline" style="font-size: 40px" />
                  <div style="font-size: 12px;">书单列表</div>
                </div>
              </Col>
              <Col span="3">
                <div @click="$router.push({ path: '/payment/shoppingCart'})" style="margin: 10px 0 0 0 ;cursor: pointer;text-align: center" class="hvr-grow isoft_hover_red">
                  <Icon type="ios-cart-outline" style="font-size: 40px"/>
                  <div style="font-size: 12px;">购物车</div>
                </div>
              </Col>
              <Col span="3">
                <div @click="$router.push({ path: '/payment/orderList'})" style="margin: 10px 0 0 0 ;cursor: pointer;text-align: center" class="hvr-grow isoft_hover_red">
                  <Icon type="ios-list-box-outline" style="font-size: 40px" />
                  <div style="font-size: 12px;">我的订单</div>
                </div>
              </Col>
              <Col span="3">
                <div @click="$router.push({ path: '/payment/myCouponList'})" style="margin: 10px 0 0 0 ;cursor: pointer;text-align: center" class="hvr-grow isoft_hover_red">
                  <Icon type="logo-usd" style="font-size: 40px" />
                  <div style="font-size: 12px;">我的优惠券</div>
                </div>
              </Col>
            </Row>
          </div>

          <!--用户相关的博文、课程、书本-->
          <div style="background-color: #ffffff;">
            <UserAbout :user-name="_userName" :titleLimitLenth="20"/>
          </div>

          <!--他/她收藏的图书-->
          <div class="isoft_top5">
            <UserFavorite ref="userFavorite"/>
          </div>
        </div>
        <div style="width: 30.5%;margin: 0 0 0 5px;background-color: white ">
          <!--会员卡片-->
          <OpenVipCard v-if="isLoginUserName(user.user_name)" style="margin-bottom: 5px;"></OpenVipCard>
          <!--用户排行榜-->
          <HotUser style="margin-top: 20px"/>
          <!--等你来答-->
          <WaitYourAnswer style="margin-top: 40px"></WaitYourAnswer>
        </div>
      </div>
    </div>
  </div>
  <!--未登录显示-->
  <div v-else style="background-color: antiquewhite">
    <div style="display: flex;">
      <div style="width: 65%;font-size: 20px;text-align: center;margin-top: 120px;padding: 50px;">
        <Spin fix size="large" v-if="isLoading">
          <div class="isoft_loading"></div>
        </Spin>
        <ForwardLogin v-else></ForwardLogin>
      </div>
      <div style="width: 31%;">
        <!--用户排行榜-->
        <HotUser style="margin-top: 5px"/>
        <!--等你来答-->
        <WaitYourAnswer style="margin-top: 5px"></WaitYourAnswer>
      </div>
    </div>
  </div>
</template>

<script>
  import {EditUserSignature, GetUserDetail,QueryAttentionOrFensi} from "../../api"
  import HotUser from "./HotUser"
  import {checkEmpty, GetLoginUserName} from "../../tools"
  import IFileUpload from "../Common/file/IFileUpload"
  import UserAbout from "./UserAbout";
  import ForwardLogin from "../SSO/ForwardLogin";
  import UserFavorite from "./UserFavorite";
  import UploadHeadSculpture from "../Common/file/UploadHeadSculpture";
  import HatAndFacePicture from "../Common/HatAndFacePicture/HatAndFacePicture";
  import OpenVipCard from "../VipCenter/OpenVipCard";
  import WaitYourAnswer from "../Expert/WaitYourAnswer";

  export default {
    name: "UserDetail",
    components: {WaitYourAnswer, OpenVipCard, HatAndFacePicture, UploadHeadSculpture,
      UserFavorite, ForwardLogin, UserAbout, HotUser, IFileUpload},
    data() {
      return {
        myCenterBgShow:false,
        otherCenterBgShow:false,
        user: null,
        isLoading: true,
        defaultImg: require('../../../static/images/common_img/default.png'),
        editSignFlag: false,
        user_signature: '',
      }
    },
    methods: {
      toUserInfo:function(user_name){
        if (this.isLoginUserName(user_name)) {
          this.$router.push({ path: '/user/set/userInfo'})
        }
      },
      handleEditSignFlag: async function () {
        const result = await EditUserSignature({"user_signature": this.user_signature});
        if (result.status === "SUCCESS") {
          this.refreshUserDetail();
        }
        this.editSignFlag = false;
      },
      editSign: function () {
        this.editSignFlag = true;
      },
      isLoginUserName: function (user_name) {
        return user_name === GetLoginUserName();
      },
      defImg() {
        let img = event.srcElement;
        img.src = this.defaultImg;
        img.onerror = null; //防止闪图
      },
      getUserName: function () {
        return !checkEmpty(this.$route.query.username) ? this.$route.query.username : GetLoginUserName();
      },
      refreshUserDetail: async function () {
        this.refreshBg();
        //加载用户信息
        this.isLoading = true;
        try {
          let params = {
            'userName':this.getUserName()
          };
          const result = await GetUserDetail(params);
          if (result.status === "SUCCESS") {
            this.user = result.user;
            if (!checkEmpty(this.user.user_signature)) {
              this.user_signature = this.user.user_signature;
            }else {
              this.user_signature = '这家伙很懒，什么个性签名都没有留下';
            }
          }
        } finally {
          this.isLoading = false;
          //刷新用户信息后，刷新一下图书
          this.$nextTick(() => {
            this.refreshUserFavorite()
          });
        }
      },
      QueryAttentionOrFensi:async function(AttentionOrFensi){
        let params = {
          'attention_object_type':'user',
          'AttentionOrFensi':AttentionOrFensi,
          'current_page':1,
          'offset':10
        };
        const result = await QueryAttentionOrFensi(params);
        if (result.status === 'SUCCESS') {
          alert(result.queryDatas);
        }
      },
      //修改主题背景颜色图片
      refreshBg:function () {
        if (this.$route.query.username) {
          if (this.isLoginUserName(this.$route.query.username)) {
            this.myCenterBgShow = true;
            this.otherCenterBgShow = false;
          }else {
            this.myCenterBgShow = false;
            this.otherCenterBgShow = true;
          }
        }else {
          this.myCenterBgShow = true;
          this.otherCenterBgShow = false;
        }
      },
      //更新收藏图书//强制刷新
      refreshUserFavorite:function () {
        if (this.$route.query.username) {
          this.$refs.userFavorite.refreshUserFavoriteList(this.$route.query.username);
        }else {
          this.$refs.userFavorite.refreshUserFavoriteList(GetLoginUserName());
        }
      }
    },
    mounted() {
      if (this.getUserName()) {
        this.refreshUserDetail();
      } else {
        this.isLoading = false;
      }
    },
    computed: {
      _userName: function () {
        return this.getUserName();
      }
    },
    watch: {
      '$route': 'refreshUserDetail'
    },
    filters: {
      // 内容超长则显示部分
      filterLimitFunc: function (value) {
        if (value && value.length > 50) {
          value = value.substring(0, 50) + '...';
        }
        return value;
      },
    }
  }
</script>

<style scoped>

  .my_center {
    background: url(../../../static/images/common_img/my_center.jpg) no-repeat;
    height: 150px;
    background-size: 100% 100%;
  }

  .other_center {
    background: url(../../../static/images/common_img/other_center.jpg) no-repeat;
    height: 150px;
    background-size: 100% 100%;
  }

  .focus:focus {
    background-color: #ffffff;
    border-color: #2c5bff;
  }

  .hoverFlash {
    cursor: pointer;
    transition: all 1s ease;
  }

  .hoverFlash:hover {
    color: #8a0000;
  }

  li {list-style-type:none;}

  .messageTipClass{
    font-size: 22px;

    position:relative;
    animation-name:messageTip;
    animation-duration:1s;
    animation-iteration-count:infinite;
    animation-play-state:running;
    /* Safari and Chrome: */
    -webkit-animation-name:messageTip;
    -webkit-animation-duration:1s;
    -webkit-animation-iteration-count:infinite;
    -webkit-animation-play-state:running;
  }

  @keyframes messageTip {
    0%   {font-size: 22px;}
    50%  {font-size: 28px;}
    100% {font-size: 22px;}
  }

  @-webkit-keyframes messageTip /* Safari and Chrome */{
    0%   {font-size: 22px;}
    50%  {font-size: 28px;}
    100% {font-size: 22px;}
  }

</style>
