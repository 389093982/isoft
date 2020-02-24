<template>
  <div>

    <div v-if="hasLogin">

      <h4>尊贵的 vip 用户，您享有本站所有的权益！</h4>

      <!-- 显示详细消息 -->
      <div v-if="showDetail" class="isoft_bg_white isoft_pd20" style="min-height: 300px;">
        <div v-for="(message, index) in messages">
          {{message.message_text}}
        </div>

        <Page :total="total" :page-size="offset" show-total show-sizer
              :styles="{'text-align': 'center','margin-top': '10px'}"
              @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
      </div>

      <!-- 显示简略消息 -->
      <div v-else style="font-size: 14px;line-height: 16px;color: black;padding: 10px;">
        <div v-for="(message, index) in messages">
          <p style="margin: 5px 0px;">{{message.message_text}}</p>
        </div>

        <div style="text-align: right;" @click="$router.push({path: '/message/message_list'});">
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
        const result = await QueryPageMessageList({current_page: this.current_page, offset: this.offset});
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
