<template>
  <div>
    <div v-if="user">
      <div style="min-height: 140px;background: linear-gradient(to right, rgba(29,255,139,0.14), rgba(206,54,255,0.23));
          background-size: cover;background-position: 50%;background-repeat: no-repeat;"></div>

      <Row style="min-height: 150px;background-color: #ffffff;padding: 20px;">
        <Col span="6" style="top:-100px;">
          <img class="isoft_hover_red hover_img" style="cursor: pointer;border: 2px solid rgba(197,197,197,0.2);"
               width="150" height="150" :src="user.small_icon" @error="defImg()">

          <div style="margin: 0 0 0 40px;" v-if="$route.query.username === 'mine'">
            <IFileUpload ref="fileUpload" @uploadComplete="uploadComplete" :action="fileUploadUrl" uploadLabel="上传头像"/>
          </div>
        </Col>
        <Col span="12" style="padding-top: 30px;">
          <p style="margin-bottom: 20px;">加入时间：
            <Time :time="user.created_time" :interval="1"/>
          </p>

          <h3>{{user.nick_name}} / {{user.user_name}}</h3>
          <p>这家伙很懒，什么个性签名都没有留下</p>
        </Col>
        <Col span="6" style="padding-top: 100px;text-align: right;">
          <Button @click="$router.push({ path: '/iblog/blog_edit'})">发&nbsp;&nbsp;&nbsp;&nbsp;帖</Button>
          <Button @click="$router.push({path:'/user/mine/detail',query:{username:'mine'}})">编辑个人资料</Button>
        </Col>
      </Row>
    </div>

    <div style="min-height: 150px;background-color: #ffffff;margin: 10px 0 0 0; padding:10px;">
      <Row :gutter="10">
        <Col span="16">
          <UserAbout :user-name="_userName"/>
        </Col>
        <Col span="8">
          <HotUser/>
        </Col>
      </Row>
    </div>
  </div>
</template>

<script>
  import {fileUploadUrl, GetUserDetail, UpdateUserIcon} from "../../api"
  import HotUser from "./HotUser"
  import {GetLoginUserName} from "../../tools"
  import IFileUpload from "../Common/file/IFileUpload"
  import UserAbout from "./UserAbout";

  export default {
    name: "UserDetail",
    components: {UserAbout, HotUser, IFileUpload},
    data() {
      return {
        fileUploadUrl: fileUploadUrl + "?table_name=user&table_field=small_icon",
        user: null,
        defaultImg: require('../../assets/default.png'),
      }
    },
    methods: {
      defImg() {
        let img = event.srcElement;
        img.src = this.defaultImg;
        img.onerror = null; //防止闪图
      },
      uploadComplete: async function (data) {
        if (data.status == "SUCCESS") {
          this.$refs.fileUpload.hideModal();
          let uploadFilePath = data.fileServerPath;
          const result = await UpdateUserIcon(GetLoginUserName(), uploadFilePath);
          if (result.status == "SUCCESS") {
            this.refreshUserDetail();
          }
        }
      },
      refreshUserDetail: async function () {
        let userName = this.$route.query.username == 'mine' ? GetLoginUserName() : this.$route.query.username;
        const result = await GetUserDetail(userName);
        if (result.status == "SUCCESS") {
          this.user = result.user;
        }
      }
    },
    mounted() {
      if (this.$route.query.username != undefined && this.$route.query.username != null) {
        this.refreshUserDetail();
      }
    },
    computed: {
      _userName: function () {
        return this.$route.query.username == 'mine' ? GetLoginUserName() : this.$route.query.username;
      }
    },
    watch: {
      '$route': 'refreshUserDetail'
    },
  }
</script>

<style scoped>
  .hover_img {
    position: relative;
    transition: transform 1s ease-in;
  }

  .hover_img:hover {
    transform: rotateY(360deg);
  }
</style>
