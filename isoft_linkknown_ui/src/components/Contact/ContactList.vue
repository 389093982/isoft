<template>
  <div class="isoft_bg_white">
    <Row>
      <Col span="1" offset="10">
        <img src="../../../static/images/common_img/xiaodou.png" style="width: 35px;height: 40px;margin-top: 5px"/>
      </Col>
      <Col span="6">
        <div style="color: #999;font-size: 14px;font-weight: bold;margin-top: 20px">
          链知网智能<span style="color: #ff6900">小豆</span>为您服务
        </div>
      </Col>
    </Row>

    <!--彩色分底线-->
    <SepLine style="margin: 10px 0;"></SepLine>

    <div class="content" style="display: flex;">
      <!--左侧-->
      <div style="width: 30%">
        <div style="border: 1px solid #eee;">
          <!--最近联系-->
          <div class="isoft_info_tip isoft_font12">最近联系</div>
          <div class="selfContact isoft_pd20">
            <p class="isoft_point_cursor isoft_hover_red"
               v-for="(cUser, index) in contactUsers" @click="freeContact(cUser.contact_user_name, cUser._nick_name)">
              {{cUser._nick_name}} - <span class="isoft_font12 isoft_color_grey">{{cUser.contact_user_name}}</span>
            </p>
          </div>
        </div>
      </div>
      <!--中间聊天区域-->
      <div style="width: 40%;min-height: 500px;padding: 0 10px;">
        <!--上边聊天记录, 模仿微信聊天背景样式来-->
        <div id="messageDiv" style="border: 1px solid #d2d2d2;height: 370px;overflow-y:scroll;padding: 20px 5px 40px 5px ;background-color: rgba(160,160,160,0.18)">
          <div style="text-align: center;margin-bottom: 5px;">
            <span class="isoft_tag1">^_^智能小豆为您 <span class="isoft_color_green1">{{loginUserNickName}}</span> 解答</span>
          </div>

          <div v-if="isLoginUserName">
            <div style="text-align: center;"><span class="isoft_tag1">自己和自己聊天是不是有点蠢啊，^_^</span></div>
          </div>
          <div v-else>
            <div style="text-align: center;">
              <span class="seeMore isoft_point_cursor" style="margin-left: 5px" @click="refreshOldContactData">查看更早信息</span>
            </div>
            <div v-for="(msg, index) in contactMessages">
              <!--提问者-->
              <div v-if="msg.user_name === $route.query.userName" :class="msg.user_name === $route.query.userName ? 'answer' : 'ask'" style="width: 85%;float: left;margin-top: 40px;position: relative;margin-bottom: 20px">
                <img src="../../../static/images/common_img/default.png" style="width: 35px;height: 35px;"/>
                <span class="requestTime">{{loginUserNickName}} • <Time :time="msg.last_updated_time" :interval="1"/></span>
                <div class="contact requestContent">{{msg.message_text}}</div>
              </div>
              <!--回答者-->
              <div v-else-if="msg.user_name !== $route.query.userName" :class="msg.user_name === $route.query.userName ? 'answer' : 'ask'" style="width: 85%;float: right;margin-top: 40px;position: relative;margin-bottom: 20px">
                <div class="contact answerContent">{{msg.message_text}}</div>
                <span class="answerTime"><Time :time="msg.last_updated_time" :interval="1"/></span>
                <div style="text-align: right">
                  <img src="../../../static/images/common_img/default.png" style="width: 35px;height: 35px;"/>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!--下边发送内容编辑框-->
        <div style="margin-top:5px;position: relative;">
          <textarea rows="3" placeholder="按 Enter 键发送" style="background-color: #ffffff;border-color: #eee;height: 80px;width: 100%;padding: 0 10px;" maxlength="70" v-model.trim="sendMsgText" @keyup.enter="sendMsg"></textarea>
          <span class="isoft_tag1 isoft_point_cursor hover_green" style="position: absolute;bottom: 8px;right: 20px;" @click="sendMsg">&nbsp;&nbsp;立刻发送&nbsp;&nbsp;</span>
        </div>
      </div>
      <!-- 常见问题 -->
      <div style="width: 30%;">
          <!--自助服务  常见问题-->
          <div class="isoft_info_tip isoft_font12">
            <span class="isoft_point_cursor" :class="showHelpPattern === 1 ? 'isoft_color_red': ''" @click="showHelpPattern = 1">自助服务</span>&nbsp;|&nbsp;
            <span class="isoft_point_cursor" :class="showHelpPattern === 2 ? 'isoft_color_red': ''" @click="showHelpPattern = 2">常见问题</span>
          </div>
          <div class="selfAssistent isoft_pd20 clear" v-if="showHelpPattern === 1">
            <a>
              <img src="../../../static/images/common_img/default.png" style="width: 30px;height: 30px;"/><p>友情链接</p>
            </a>
            <a>
              <img src="../../../static/images/common_img/default.png" style="width: 30px;height: 30px;"/><p>问题反馈</p>
            </a>
            <a>
              <img src="../../../static/images/common_img/default.png" style="width: 30px;height: 30px;"/><p>商业合作</p>
            </a>
          </div>
          <div class="selfAssistent isoft_pd20" v-if="showHelpPattern === 2">
            <p>如何找回链知网账号密码？</p>
            <p>链知网有 Java 课程吗？</p>
          </div>
        </div>
      </div>
    </div>
</template>

<script>
  import {
    checkArrayEmpty,
    checkEmpty,
    checkFastClick,
    CheckHasLoginConfirmDialog,
    CheckHasLoginConfirmDialog2,
    checkNotEmpty,
    FillUserNickNameInfoByNames,
    GetLoginUserName
  } from "../../tools";
  import {AddContactMessage, GetContactMessage, GetContactUserList} from "../../api"
  import {getNickName} from "../../tools/sso";
  import SepLine from "../Common/SepLine";

  export default {
    name: "ContactList",
    components: {SepLine},
    data() {
      return {
        sendMsgText: '',
        showHelpPattern: 1,  // 1、自助服务 2、常见问题
        contactMessages: [],
        contactUsers: [],
        refreshContactUserTimer: null,
        refreshContactDataTimer: null,
        minTime: null,  // 当前消息的最早时间
        maxTime: null,  // 当前消息的最新时间
        isRefreshing: false,
        isSending: false,

      }
    },
    methods: {
      freeContact: function (userName, nickName) {
        CheckHasLoginConfirmDialog(this, {path: '/contact/contactList', query: {userName, nickName}});
      },
      sendMsg: function () {
        var _this = this;
        if (checkEmpty(_this.sendMsgText) || _this.isSending) {     // 正在发送中则点击无效
          return;
        }
        try {
          _this.isSending = true;
          CheckHasLoginConfirmDialog2(this, async function () {
            const result = await AddContactMessage({
              contact_user_name: _this.$route.query.userName,
              message_type: 'default', message_text: _this.sendMsgText
            });
            if (result.status === "SUCCESS") {
              _this.sendMsgText = '';
              _this.refreshContactData(1);
            }
          });
        } finally {
          _this.isSending = false;
        }
      },
      getTimestamp: function (newFlag) {
        // 新消息
        if (newFlag > 0) {
          if (checkArrayEmpty(this.contactMessages)) {
            return new Date().getTime() - 3600 * 24 * 30 * 1000;
          } else {
            return Math.max.apply(Math, this.contactMessages.map(m => m.last_updated_timestamp));
          }
        } else {   // 历史消息
          if (checkArrayEmpty(this.contactMessages)) {
            return new Date().getTime();
          } else {
            return Math.min.apply(Math, this.contactMessages.map(m => m.last_updated_timestamp));
          }
        }
      },
      refreshOldContactData: function () {
        if (!checkFastClick()) {
          this.refreshContactData(-1);
        }
      },
      refreshContactData: async function (newFlag) {
        if (this.isRefreshing && checkFastClick()) {     // 防止并发造成重复数据
          return;
        }
        try {
          this.isRefreshing = true;
          if (checkNotEmpty(this.$route.query.userName) && checkNotEmpty(this.$route.query.nickName)) {
            const result = await GetContactMessage({
              contact_user_name: this.$route.query.userName,
              newFlag: newFlag,
              timestamp: this.getTimestamp(newFlag),
            });
            if (result.status === "SUCCESS") {
              this.smartRenderContactMessage(result, newFlag);
            }
          }
        } finally {
          this.isRefreshing = false;
        }

      },
      // 智能组装数据
      smartRenderContactMessage: async function (result, newFlag) {
        if (result == null || checkArrayEmpty(result.contactMessages)) {
          return;
        }
        let newContactMessages = await FillUserNickNameInfoByNames(result.contactMessages, "contact_user_name");
        this.contactMessages = newFlag > 0 ? this.contactMessages.concat(newContactMessages.reverse()) : newContactMessages.reverse().concat(this.contactMessages);
        this.$nextTick(() => {  // 数据渲染后才滚动到底部
          // 滚动到底部
          var scrollDom = this.$el.querySelector("#messageDiv");
          scrollDom.scrollTop = scrollDom.scrollHeight;
        });
      },
      refreshContactUserList: async function () {
        const result = await GetContactUserList();
        if (result.status === "SUCCESS") {
          this.contactUsers = await FillUserNickNameInfoByNames(result.contactUsers, "contact_user_name");
        }
      },
      firstRefreshContactData: function () {
        // 先立即执行一次,然后再 10 s 后定时执行
        this.refreshContactUserList();
        this.refreshContactData(-1);

        this.refreshContactUserTimer = setInterval(this.refreshContactUserList, 10000);
        this.refreshContactDataTimer = setInterval(() => this.refreshContactData(1), 10000);
      }
    },
    computed: {
      isLoginUserName: function () {
        return GetLoginUserName() === this.$route.query.userName;
      },
      loginUserNickName: function () {
        return getNickName();
      },
    },
    mounted() {
      this.firstRefreshContactData();
    },
    beforeDestroy() {
      if (this.refreshContactDataTimer != null) {
        clearInterval(this.refreshContactDataTimer);
      }
      if (this.refreshContactUserTimer != null) {
        clearInterval(this.refreshContactUserTimer);
      }
    },
    watch: {
      '$route': 'firstRefreshContactData'
    },
  }
</script>

<style scoped>
  .content {
    min-height: 500px;
    padding: 5px 60px;
  }

  .selfAssistent a {
    float: left;
    margin: 10px 8px;
    display: inline-block;
    text-align: center;
  }

  .selfAssistent p {
    color: grey;
  }

  textarea::-webkit-input-placeholder {
    /* placeholder颜色  */
    color: #aab2bd;
    /* placeholder字体大小  */
    font-size: 12px;
  }
  .hover_green:hover{
    color: white;
    background-color: #61ca61;
  }

  .seeMore {
    display: inline-block;
    background-color: rgba(209, 209, 209, 0.75);
    border-radius: 3px;
    height: 24px;
    line-height: 24px;
    font-size: 12px;
    color: #333;
    text-align: center;
    padding: 0 5px;
    font-weight: 100;
  }

  .ask > .contact {
    font-size: 12px;
    background: rgba(64, 248, 55, 0.56);
    color: #525252;
    padding: 2px 4px 2px 4px ;
    border-radius: 5px;
  }

  .answer > .contact {
    font-size: 12px;
    background: white;
    color: #525252;
    padding: 2px 4px 2px 4px ;
    border-radius: 5px;
  }

  .requestTime{
    font-size: 12px;
    color: grey;
    position: absolute;
    top: -2px;
    left: 45px;
  }
  .requestContent{
    position: absolute;
    top: 20px;
    left: 45px;
  }
  .answerTime{
    font-size: 12px;
    color: grey;
    position: absolute;
    top: -2px;
    right: 45px;
  }
  .answerContent{
    position: absolute;
    top: 20px;
    right: 45px;
  }
</style>
