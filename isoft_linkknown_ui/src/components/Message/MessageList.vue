<template>
  <div>

    <div v-if="hasLogin">
      <!-- 显示详细消息 -->
      <div v-if="showDetail" class="isoft_bg_white isoft_pd10">
        <div v-for="(message, index) in messages">
          {{message.message_text}}
        </div>
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
  import {QueryPageMessageList,LoginAddr} from "../../api"
  import {CheckHasLogin,delCookie} from "../../tools/index"

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
        hasLogin:false,
        messages: [],
      }
    },
    methods: {
      refreshMessageList: async function () {
        const result = await QueryPageMessageList({});
        if (result.status == "SUCCESS") {
          this.messages = result.messages;
        }
      },
      cancelUser () {
        delCookie("tokenString");
        delCookie("userName");
        delCookie("isLogin");
        window.location.href = LoginAddr + "?redirectUrl=" + window.location.href;
      }
    },
    mounted() {
      let result = CheckHasLogin();
      if (result) {
        this.hasLogin = true
        this.refreshMessageList();
      }else{
        this.hasLogin = false
      }
    }
  }
</script>

<style scoped>

</style>
