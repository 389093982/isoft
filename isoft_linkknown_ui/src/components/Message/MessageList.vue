<template>
  <div class="isoft_bg_white isoft_pd10">
    <div v-for="(message, index) in messages">
      {{message.message_text}}
    </div>
  </div>
</template>

<script>
  import {QueryPageMessageList} from "../../api"

  export default {
    name: "MessageList",
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
