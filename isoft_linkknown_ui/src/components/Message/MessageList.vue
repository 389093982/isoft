<template>
  <div>
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

</template>

<script>
  import {QueryPageMessageList} from "../../api"

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
        messages: [],
      }
    },
    methods: {
      refreshMessageList: async function () {
        const result = await QueryPageMessageList({});
        if (result.status == "SUCCESS") {
          this.messages = result.messages;
        }
      }
    },
    mounted() {
      this.refreshMessageList();
    }
  }
</script>

<style scoped>

</style>
