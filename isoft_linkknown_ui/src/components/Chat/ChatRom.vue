<template>
  <div class="isoft_bg_white">
    <div style="padding: 5px;">
      <div id="chat-messages" style="padding: 20px;border: 1px solid #d7dde4;" v-html="chatContent"></div>
    </div>
    <!-- 未加入聊天室 -->
    <div v-if="!joined" class="isoft_top10" style="text-align: right;padding: 5px;">
      <Input type="text" style="width: 200px;" v-model.trim="username" placeholder="请输入用户名"/>
      <Button type="success" @click="join">加入</Button>
    </div>

    <!-- 已加入聊天室 -->
    <div v-if="joined" class="isoft_top10" style="text-align: right;padding: 5px;">
      <Input type="text" style="width: 500px;" v-model.trim="new_msg" @keyup.enter="send" placeholder="请输入发送的内容"/>
      <Dropdown>
        <Button type="default">
          更多
          <Icon type="ios-arrow-down"></Icon>
        </Button>
        <DropdownMenu slot="list" @on-click="translate">
          <DropdownItem @click.native="translate('ZH_CN2EN')">翻译英文</DropdownItem>
          <DropdownItem @click.native="translate('EN2ZH_CN')">翻译中文</DropdownItem>
        </DropdownMenu>
      </Dropdown>
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
        new_msg: '', // Holds new messages to be sent to the server
        message_extra: '',
        message_type: 0,
        chatContent: '', // A running list of chat messages displayed on the screen
        email: null, // Email address used for grabbing an avatar
        username: null, // Our username
        joined: false // True if email and username have been filled in
      }
    },
    methods: {
      translate: function (translate_type) {
        this.message_type = 3;
        this.message_extra = "type=" + translate_type;
        this.send();
      },
      handleMessageEvent: function () {
        var _this = this;
        var common_questions = document.getElementsByClassName('common_question');
        if (common_questions != null && common_questions.length > 0) {
          for (var i = 0; i < common_questions.length; i++) {
            let common_questionNode = common_questions[i];
            let common_question = common_questionNode.attributes["data"].nodeValue;
            common_questionNode.onclick = function () {
              _this.new_msg = common_question;
              _this.message_type = 1;
              _this.send();
            }
          }
        }
      },
      send: function () {
        if (!checkEmpty(this.new_msg)) {
          this.ws.send(
            JSON.stringify({
                username: this.username,
                message: this.new_msg,
                message_type: this.message_type,
                message_extra: this.message_extra,
              }
            ));
          // 重置
          this.message_type = 0;
          this.new_msg = "";
          this.message_extra = "";
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
