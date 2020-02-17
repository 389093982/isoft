<template>
  <div>
    <div v-if="user">
      <div style="min-height: 140px;background: linear-gradient(to right, rgba(29,255,139,0.14), rgba(206,54,255,0.23));
          background-size: cover;background-position: 50%;background-repeat: no-repeat;"></div>

      <Row style="min-height: 150px;background-color: #ffffff;padding: 20px;">
        <Col span="6" style="top:-100px;">
          <div class="user_icon">
            <img class="isoft_hover_red hover_img" style="cursor: pointer;border: 2px solid rgba(197,197,197,0.2);"
                 width="150" height="150" :src="user.small_icon" @error="defImg()">

            <span class="user_icon_tip isoft_hover_red"
                  style="size: 12px;background-color: #dbdbdb;padding: 3px 10px;border-radius: 5px;">
              <span v-if="isLoginUserName(user.user_name)">头像单调无味？赶快来换张新颖的头像吧</span>
              <span v-else>这么漂亮的头像，我咋没有！立即去设置</span>
            </span>

            <div class="user_icon_upload" style="margin: 0 0 0 40px;" v-if="isLoginUserName(user.user_name)">
              <IFileUpload ref="fileUpload" @uploadComplete="uploadComplete" :action="fileUploadUrl"
                           uploadLabel="上传头像"/>
            </div>
          </div>
        </Col>
        <Col span="12" style="padding-top: 30px;">
          <p style="margin-bottom: 20px;">加入时间：
            <Time :time="user.created_time" :interval="1"/>
          </p>

          <h3>{{user.nick_name}} / {{user.user_name}}</h3>
          <p>
            <textarea rows="3" cols="80" v-if="editSignFlag" v-model.trim="user_signature" maxlength="220"
                      style="padding: 5px;" class="focus" @blur="handleEditSignFlag"></textarea>
            <span v-else class="hoverFlash isoft_text_rows">{{user_signature | filterLimitFunc}}</span>
            <Icon v-if="isLoginUserName(user.user_name) && !editSignFlag" class="isoft_hover_red isoft_point_cursor"
                  type="ios-create-outline" :size="20"
                  @click="editSign"/>
          </p>
        </Col>
        <Col span="6" style="padding-top: 100px;text-align: right;">
          <Button @click="$router.push({ path: '/iblog/blog_edit'})">发&nbsp;&nbsp;&nbsp;&nbsp;帖</Button>
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
  import {EditUserSignature, fileUploadUrl, GetUserDetail, UpdateUserIcon} from "../../api"
  import HotUser from "./HotUser"
  import {checkEmpty, GetLoginUserName} from "../../tools"
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
  .hover_img {
    position: relative;
    transition: transform 1s ease-in;
  }

  .hover_img:hover {
    transform: rotateY(360deg);
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

  @keyframes user_icon_tip_move {
    0% {
      position: absolute;
      top: 2px;
      right: -105px;
    }
    50% {
      position: absolute;
      top: 2px;
      right: -145px;
      color: #c50000;
    }
    100% {
      position: absolute;
      top: 2px;
      right: -105px;
    }
  }
</style>
