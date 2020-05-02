<template>
  <div>
    <div v-if="hasLogin">
      <!-- 显示详细消息 -->
      <div v-if="showDetail" class="isoft_bg_white isoft_pd20;" style="min-height: 550px">
        <div style="padding: 20px 0 0 0 ;text-align: center">
          <h4 v-if="isVip">尊贵的 vip 用户，您享有本站所有的 <a @click="$router.push({path:'/vipcenter/vipIntroduction'})">权益！</a></h4>
        </div>
        <!--遍历消息-->
        <div style="min-height: 420px;padding: 20px 0 0 100px">
          <div v-for="(message, index) in messages" style="padding: 13px 0 0 0 ;">
            <span class="isoft_color_green1">【系统消息】</span>
            <Time class="isoft_color_grey3" :time="message.last_updated_time" :interval="1"/> &nbsp;{{message.message_text}}
          </div>
        </div>
        <!--分页-->
        <Page :total="total" :page-size="offset" show-total show-sizer :styles="{'text-align': 'center','margin-top': '10px'}" @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
      </div>
      <!-- 显示简略消息 -->
      <div v-else style="font-size: 14px;line-height: 16px;color: black;padding: 0 10px 15px 10px;">
        <div style="padding: 20px 0 0 0 ;text-align: center">
          <h4 v-if="isVip">尊贵的 vip 用户，您享有本站所有的 <a @click="$router.push({path:'/vipcenter/vipIntroduction'})">权益！</a></h4>
        </div>
        <div class="isoft_font12" style="margin: 10px 0;">
          <p v-for="(message, index) in messages" style="margin: 8px 0px;">{{message.message_text}}</p>
        </div>
        <div style="text-align: right;" @click="$router.push({path: '/message/messageList'});">
          <Button size="small" type="success">查看更多</Button>
        </div>
      </div>
    </div>
    <div v-else>
      <div style="background-color: rgba(255,102,0,0.19);text-align: center;color: #2B3F52">
        您还未登录，请先<b style="color: rgba(5,71,255,0.62);" @click="cancelUser">登录</b>哦！
      </div>
    </div>

  </div>

</template>

<script>
  import {LoginAddr, QueryPageMessageList} from "../../api"
  import {CheckHasLogin} from "../../tools/index"
  import {deleteLoginInfo} from "../../tools/sso"
  import {IsVip} from "../../tools/vip"

  export default {
    name: "MessageList",
    props: {
      showDetail: {
        type: Boolean,
        default: true,
      }
    },
    data() {
      return {
        isVip: false,
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 10,
        hasLogin:false,
        messages: [],
      }
    },
    methods: {
      refreshMessageList: async function () {
        let offset = this.showDetail ? this.offset : 4;
        const result = await QueryPageMessageList({current_page: this.current_page, offset: offset});
        if (result.status === "SUCCESS") {
          this.messages = result.messages;
          this.total = result.paginator.totalcount;
        }
      },
      handleChange(page) {
        this.current_page = page;
        this.refreshMessageList();
      },
      handlePageSizeChange(pageSize) {
        this.offset = pageSize;
        this.refreshMessageList();
      },
      cancelUser () {
        deleteLoginInfo();
        window.location.href = LoginAddr + "?redirectUrl=" + window.location.href;
      }
    },
    mounted() {
      let result = CheckHasLogin();
      if (result) {
        this.hasLogin = true;
        this.refreshMessageList();
      }else{
        this.hasLogin = false;
      }
      this.isVip = IsVip();
    }
  }
</script>

<style scoped>

</style>
