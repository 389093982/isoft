<template>
  <div>
    <div v-if="user">
      <div style="min-height: 140px;background: linear-gradient(to right, rgb(176, 108, 239), rgba(176,108,239,0.13));
          background-size: cover;background-position: 50%;background-repeat: no-repeat;">
      </div>
      <div style="margin: 0 5px 0 5px ">
        <Row :gutter="10" style="min-height: 150px;background-color: #ffffff;padding: 20px 0 0 0 ;">
          <Col span="3" style="top:-80px;">
            <div class="user_icon">
              <!--头像照片-->
              <img class="isoft_hover_red"
                   style="cursor: pointer;border: 2px solid grey;border-radius:50%;"
                   width="120" height="120" :src="user.small_icon" @error="defImg()">

              <!--头像上方文字提示-->
              <span class="user_icon_tip isoft_hover_red" style="size: 12px;background-color: #dbdbdb;padding: 3px 10px;border-radius: 5px;">
                <span v-if="isLoginUserName(user.user_name)">头像单调无味？赶快来换张新颖的头像吧</span>
                <span v-else @click="$router.push({path:'/user/detail'})">这么漂亮的头像，我咋没有！<span style="color: rgba(0,0,255,0.74)">立即去设置</span></span>
              </span>

              <!--修改头像-->
              <div class="user_icon_upload" style="margin: 0 0 0 12px;" v-if="isLoginUserName(user.user_name)">
                <UploadHeadSculpture ref="fileUpload" @uploadComplete="uploadComplete" :action="fileUploadUrl" uploadLabel="修改头像"/>
              </div>
            </div>
          </Col>
          <Col span="13" style="padding: 10px 0 0 3px;">
            <div>
              <b style="font-size: 18px">{{user.nick_name}}</b> / <code style="color: grey">{{user.user_name}}</code>
            </div>
            <div>
              个性签名:
              <textarea rows="3" cols="80" v-if="editSignFlag" v-model.trim="user_signature" maxlength="220" style="padding: 5px;" class="focus" @blur="handleEditSignFlag"></textarea>
              <span v-else class="hoverFlash isoft_text_rows">{{user_signature | filterLimitFunc}}</span>
              <Icon v-if="isLoginUserName(user.user_name) && !editSignFlag" class="isoft_hover_red isoft_point_cursor" type="ios-create-outline" :size="20" @click="editSign" style="color: #ff6900"/>
            </div>
            <div>
              加入时间：<Time :time="user.created_time" :interval="1"/>
            </div>
             <div v-if="isLoginUserName(user.user_name)">
               <div @click="" style="color: grey;padding-left: 0;cursor: pointer" class="hvr-grow"><Icon type="ios-person-add" style="font-size: 18px"/>个人信息</div>
               <div @click="$router.push({ path: '/sso/forget?pattern=2'})" style="color: grey;padding-left: 15px;cursor: pointer" class="hvr-grow"><Icon type="ios-cog" style="font-size: 18px"/>修改密码</div>
               <br><br>
               <div @click="$router.push({ path: '/iblog/blog_edit'})" style="color: #ff6900;padding-left: 0;cursor: pointer" class="hvr-grow"><Icon type="ios-list-box-outline" style="font-size: 15px" />发布博客</div>
               <div @click="$router.push({ path: '/ilearning/course_space'})" style="color: #ff6900;padding-left: 15px;cursor: pointer" class="hvr-grow"><Icon type="ios-videocam-outline" style="font-size: 16px" />我的课程</div>
               <div @click="$router.push({path:'/ibook/book_list'})" style="color: #ff6900;padding-left: 15px;cursor: pointer" class="hvr-grow"><Icon type="ios-book-outline" style="font-size: 15px" />书单列表</div>
            </div>

          </Col>
          <Col span="8" style="padding: 0 0 0 3px">
            <Card style="width:350px;background:linear-gradient(90deg,#ebd2ae,#e8b66e);">
              <p slot="title" style="height: 25px">
                <Icon class="VIPicon" @click="$router.push({path:'/vipcenter/vipIntroduction'})" type="md-ribbon"/>
                会员权益
              </p>
              <a href="#" slot="extra" @click.prevent="changeLimit">
                <Icon type="ios-loop-strong"></Icon>
                <span style="color: red;font-size: 15px" class="hvr-grow" @click="$router.push({path:'/vipcenter/vipIntroduction'})">
                <Icon type="md-arrow-round-forward" />开通会员
              </span>
              </a>
              <Carousel autoplay arrow="never" dots="outside" trigger="hover" radius-dot :autoplay-speed="6000">
                <CarouselItem>
                  <div class="demo-carousel">
                    <ul style="padding-left: 10px">
                      <li>
                        <div class="hvr-grow" style="cursor: pointer;color: blue"
                             @click="$router.push({path:'/resource/list'})">订阅本站优秀热门资源
                        </div>
                      </li>
                      <li>
                        <div class="hvr-grow" style="cursor: pointer;color: blue"
                             @click="$router.push({path:'/resource/list'})">订阅本站优秀热门资源
                        </div>
                      </li>
                      <li>
                        <div class="hvr-grow" style="cursor: pointer;color: blue"
                             @click="$router.push({path:'/resource/list'})">订阅本站优秀热门资源
                        </div>
                      </li>
                    </ul>
                  </div>
                </CarouselItem>
                <CarouselItem>
                  <div class="demo-carousel">
                    <ul style="padding-left: 10px">
                      <li><div class="hvr-grow" style="cursor: pointer;color: blue" @click="$router.push({path:'/advertisement/apply'})">本站广告推广</div></li>
                      <li><div class="hvr-grow" style="cursor: pointer;color: blue" @click="$router.push({path:'/advertisement/apply'})">本站广告推广</div></li>
                      <li><div class="hvr-grow" style="cursor: pointer;color: blue" @click="$router.push({path:'/advertisement/apply'})">本站广告推广</div></li>
                    </ul>
                  </div>
                </CarouselItem>
              </Carousel>
            </Card>
          </Col>
        </Row>
      </div>
    </div>

    <div class="isoft_top5" style="min-height: 450px;">
      <Row>
        <Col span="16">
          <div>
            <div v-if="user">
              <div style="background-color: #ffffff;">
                <!--用户相关的博文、课程、书本-->
                <UserAbout :user-name="_userName" :titleLimitLenth="20"/>
              </div>
              <div class="isoft_top5" style="background-color: #ffffff;">
                <!--他/她收藏的图书-->
                <UserFavorite :user-name="_userName"/>
              </div>
            </div>
            <div v-else style="font-size: 20px;text-align: center;margin-top: 50px;padding: 50px;">
              <Spin fix size="large" v-if="isLoading">
                <div class="isoft_loading"></div>
              </Spin>
              <ForwardLogin v-else></ForwardLogin>
            </div>

          </div>
        </Col>
        <Col span="8">
          <div>
            <!--用户排行榜-->
            <HotUser/>
          </div>
        </Col>
      </Row>
    </div>
  </div>
</template>

<script>
  import {EditUserSignature, fileUploadUrl, GetUserDetail, UpdateUserIcon} from "../../api"
  import HotUser from "./HotUser"
  import {checkEmpty, GetLoginUserName} from "../../tools"
  import IFileUpload from "../Common/file/IFileUpload"
  import UserAbout from "./UserAbout";
  import ForwardLogin from "../SSO/ForwardLogin";
  import UserFavorite from "./UserFavorite";
  import UploadHeadSculpture from "../Common/file/UploadHeadSculpture";

  export default {
    name: "UserDetail",
    components: {UploadHeadSculpture, UserFavorite, ForwardLogin, UserAbout, HotUser, IFileUpload},
    data() {
      return {
        fileUploadUrl: fileUploadUrl + "?table_name=user&table_field=small_icon",
        user: null,
        isLoading: true,
        defaultImg: require('../../assets/default.png'),
        editSignFlag: false,
        user_signature: '这家伙很懒，什么个性签名都没有留下',
      }
    },
    methods: {
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
      uploadComplete: async function (data) {
        if (data.status === "SUCCESS") {
          this.$refs.fileUpload.hideModal();
          let uploadFilePath = data.fileServerPath;
          const result = await UpdateUserIcon(GetLoginUserName(), uploadFilePath);
          if (result.status === "SUCCESS") {
            this.refreshUserDetail();
          }
        }
      },
      getUserName: function () {
        return !checkEmpty(this.$route.query.username) ? this.$route.query.username : GetLoginUserName();
      },
      refreshUserDetail: async function () {
        this.isLoading = true;
        try {
          const result = await GetUserDetail(this.getUserName());
          if (result.status === "SUCCESS") {
            this.user = result.user;
            if (!checkEmpty(this.user.user_signature)) {
              this.user_signature = this.user.user_signature;
            }
          }
        } finally {
          this.isLoading = false;
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
  .VIPicon{
    color: #ff6900;
    font-size: 20px;
    cursor: pointer;

    position:relative;
    animation-name:myfirst;
    animation-duration:8s;
    animation-iteration-count:infinite;
    animation-direction:alternate;
    animation-play-state:running;
    /* Safari and Chrome: */
    -webkit-animation-name:myfirst;
    -webkit-animation-duration:8s;
    -webkit-animation-iteration-count:infinite;
    -webkit-animation-direction:alternate;
    -webkit-animation-play-state:running;
  }

  @keyframes myfirst
  {
    0%   {color:red; left:0px; top:0px;}
    25%  {color:yellow; left:230px; top:0px;}
    50%  {color:blue; left:230px; top:10px;}
    75%  {color:green; left:0px; top:10px;}
    100% {color:red; left:0px; top:0px;}
  }

  @-webkit-keyframes myfirst /* Safari and Chrome */
  {
    0%   {color:red; left:0px; top:0px;}
    25%  {color:yellow; left:230px; top:0px;}
    50%  {color:blue; left:230px; top:10px;}
    75%  {color:green; left:0px; top:10px;}
    100% {color:red; left:0px; top:0px;}
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

  .user_icon .user_icon_upload {
    display: none;
  }

  .user_icon:hover .user_icon_upload {
    display: block;
  }

  .user_icon_tip {
    animation: user_icon_tip_move 5s infinite;
  }

  li {list-style-type:none;}

  @keyframes user_icon_tip_move {
    0% {
      position: absolute;
      top: 2px;
      right: -135px;
      opacity: 0;
    }
    50% {
      position: absolute;
      top: 2px;
      right: -175px;
      color: #c50000;
      opacity: 1;
    }
    100% {
      position: absolute;
      top: 2px;
      right: -135px;
      opacity: 0;
    }
  }
</style>
