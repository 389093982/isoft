<template>
  <div class="isoft_bg_white">
    <div id="chat-messages" style="padding: 30px;" v-html="chatContent"></div>
    <!-- 未加入聊天室 -->
    <div v-if="!joined" class="isoft_top10" style="text-align: right;padding: 5px;">
      <Input type="text" style="width: 200px;" v-model.trim="username" placeholder="请输入用户名"/>
      <Button type="success" @click="join">加入</Button>
    </div>

    <!-- 已加入聊天室 -->
    <div v-if="joined" class="isoft_top10" style="text-align: right;padding: 5px;">
      <Input type="text" style="width: 200px;" v-model.trim="newMsg" @keyup.enter="send" placeholder="请输入发送的内容"/>
      <Button type="success" @click="send">发送</Button>
    </div>
  </div>
</template>

<script>
  import {checkEmpty} from "../../tools"

  export default {
    name: "ChatRom",
    data() {
      return {
        ws: null, // Our websocket
        newMsg: '', // Holds new messages to be sent to the server
        chatContent: '', // A running list of chat messages displayed on the screen
        email: null, // Email address used for grabbing an avatar
        username: null, // Our username
        joined: false // True if email and username have been filled in
      }
    },
    methods: {
      handleMessageEvent: function () {
        var _this = this;
        var common_questions = document.getElementsByClassName('common_question');
        if (common_questions != null && common_questions.length > 0) {
          for (var i = 0; i < common_questions.length; i++) {
            let common_questionNode = common_questions[i];
            let common_question = common_questionNode.attributes["data"].nodeValue;
            common_questionNode.onclick = function () {
              _this.newMsg = common_question;
              _this.send();
            }
          }
        }
      },
      send: function () {
        if (!checkEmpty(this.newMsg)) {
          this.ws.send(
            JSON.stringify({
                username: this.username,
                message: this.newMsg,
              }
            ));
          this.newMsg = '111111111'; // Reset newMsg
        }
      },
      join: function () {
        // 补充校验 username
        this.joined = true;
      },
    },
    created: function () {
      var _this = this;
      this.ws = new WebSocket('ws://127.0.0.1:8000/ws');
      this.ws.addEventListener('message', function (e) {
        var msg = JSON.parse(e.data);
        _this.chatContent += '<div class="chip">'
          + '<img style="border-radius: 15px;width:30px;height:30px;" src="https://upload.jianshu.io/users/upload_avatars/9988193/fc26c109-1ae6-4327-a298-2def343e9cd8.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96/format/webp">' // Avatar
          + msg.username
          + '</div>'
          + msg.message + '<br/>';
        // 自动滚动至底部
        var element = document.getElementById('chat-messages');
        element.scrollTop = element.scrollHeight;

        setTimeout(function () {
          _this.handleMessageEvent();
        }, 1000);
      });
    },
  }
</script>

<style scoped>
  #chat-messages {
    min-height: 10vh;
    height: 70vh;
    width: 100%;
    overflow-y: scroll;
  }
</style>
