<template>
  <div>
    <div v-if="user">
      <div style="min-height: 140px;background: linear-gradient(to right, rgb(176, 108, 239), rgba(176,108,239,0.13));
          background-size: cover;background-position: 50%;background-repeat: no-repeat;">
      </div>
      <Row style="min-height: 150px;background-color: #ffffff;padding: 20px;">
        <Col span="3" style="top:-100px;">
          <div class="user_icon">
            <img class="isoft_hover_red"
                 style="cursor: pointer;border: 2px solid rgba(197,197,197,0.2);border-radius:50%;"
                 width="150" height="150" :src="user.small_icon" @error="defImg()">

            <span class="user_icon_tip isoft_hover_red"
                  style="size: 12px;background-color: #dbdbdb;padding: 3px 10px;border-radius: 5px;">
              <span v-if="isLoginUserName(user.user_name)">头像单调无味？赶快来换张新颖的头像吧</span>
              <span v-else>这么漂亮的头像，我咋没有！立即去设置</span>
            </span>

            <div class="user_icon_upload" style="margin: 0 0 0 40px;" v-if="isLoginUserName(user.user_name)">
              <IFileUpload ref="fileUpload" @uploadComplete="uploadComplete" :action="fileUploadUrl" uploadLabel="上传头像"/>
            </div>
          </div>
        </Col>
        <Col span="5" style="padding: 10px 0 0 3px;">
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
        </Col>
        <Col span="8" style="text-align: left;padding: 10px 0 0 3px ;">
          <div class="isoft_inline_ellipsis" style="padding-left: 20px">
            <div @click="$router.push({ path: '/iblog/blog_edit'})" style="color: #ff6900;padding-left: 15px;cursor: pointer" class="hvr-grow"><Icon type="ios-card-outline" style="font-size: 15px" />发布博客</div>
            <div @click="$router.push({ path: '/ilearning/course_space'})" style="color: #ff6900;padding-left: 15px;cursor: pointer" class="hvr-grow"><Icon type="ios-videocam-outline" style="font-size: 16px" />我的课程</div>
            <div @click="$router.push({path:'/ibook/book_list'})" style="color: #ff6900;padding-left: 15px;cursor: pointer" class="hvr-grow"><Icon type="ios-book-outline" style="font-size: 15px" />我的书单</div>
          </div>
        </Col>
        <Col span="8" style="padding: 0 0 0 3px">
          <Card style="width:350px;background:linear-gradient(90deg,#ebd2ae,#e8b66e);">
            <p slot="title">
              <Icon type="md-ribbon" style="font-size: 20px"/>
              会员权益
            </p>
            <a href="#" slot="extra" @click.prevent="changeLimit">
              <Icon type="ios-loop-strong"></Icon>
              <span style="color: red;font-size: 15px" class="hvr-grow" @click="$router.push({path:'/vipcenter/vipIntroduction'})">
                <Icon type="md-arrow-round-forward" />开通会员
              </span>
            </a>
            <Carousel autoplay loop arrow="never" dots="outside" trigger="hover" radius-dot autoplay-speed="6000">
              <CarouselItem>
                <div class="demo-carousel">
                  <ul style="padding-left: 10px">
                    <li><div class="hvr-grow" style="cursor: pointer;color: blue" @click="$router.push({path:'/resource/resourceList'})">订阅本站优秀热门资源</div></li>
                    <li><div class="hvr-grow" style="cursor: pointer;color: blue" @click="$router.push({path:'/resource/resourceList'})">订阅本站优秀热门资源</div></li>
                    <li><div class="hvr-grow" style="cursor: pointer;color: blue" @click="$router.push({path:'/resource/resourceList'})">订阅本站优秀热门资源</div></li>
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

    <div class="isoft_top5" style="min-height: 450px;">
      <Row>
        <Col span="16">
          <div>
            <div v-if="user">
              <div style="background-color: #ffffff;">
                <UserAbout :user-name="_userName"/>
              </div>
              <div class="isoft_top5" style="background-color: #ffffff;">
                <UserFavorite :user-name="_userName"/>
              </div>
            </div>
            <div v-else style="font-size: 20px;text-align: center;margin-top: 50px">
              <ForwardLogin></ForwardLogin>
            </div>
          </div>
        </Col>
        <Col span="8">
          <div>
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

  export default {
    name: "UserDetail",
    components: {UserFavorite, ForwardLogin, UserAbout, HotUser, IFileUpload},
    data() {
      return {
        fileUploadUrl: fileUploadUrl + "?table_name=user&table_field=small_icon",
        user: null,
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
        const result = await GetUserDetail(this.getUserName());
        if (result.status === "SUCCESS") {
          this.user = result.user;
          if (!checkEmpty(this.user.user_signature)) {
            this.user_signature = this.user.user_signature;
          }
        }
      }
    },
    mounted() {
      if (this.getUserName()) {
        this.refreshUserDetail();
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
        if (value && value.length > 200) {
          value = value.substring(0, 200) + '...';
        }
        return value;
      },
    }
  }
</script>

<style scoped>
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
      right: -105px;
      opacity: 0;
    }
    50% {
      position: absolute;
      top: 2px;
      right: -145px;
      color: #c50000;
      opacity: 1;
    }
    100% {
      position: absolute;
      top: 2px;
      right: -105px;
      opacity: 0;
    }
  }
</style>
