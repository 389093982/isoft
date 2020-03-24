<template>
  <div class="isoft_bg_white">
    <div class="header">
    <span style="color: #999;font-size: 14px;font-weight: bold;float: right;padding-right: 120px;">
      <img src="../../assets/default.png" style="width: 40px;height: 40px;"/>链知网智能小豆为您服务
    </span>
    </div>
    <div class="content">
      <div style="display: flex;">
        <div style="width: 60%;">
          <div style="min-height: 500px;margin: 25px 0 0 90px;">
            <div style="border: 1px solid #eee;height: 420px;overflow-y:scroll;padding: 20px;">
              <div style="text-align: center;margin-bottom: 5px;">
                <img src="../../assets/default.png" style="width: 30px;height: 30px;"/>
                <span class="isoft_tag1">智能小豆为您解答</span>
              </div>

              <div v-if="isLoading">
                <div style="text-align: center;"><span class="isoft_tag1">小豆正在为您拼命加载中,请耐心等待，^_^</span></div>
              </div>
              <div v-else>
                <div class="ask" v-for="(ask, index) in asks">
                  <img src="../../assets/default.png" style="width: 30px;height: 30px;"/>
                  <span class="contact">{{ask}}</span>
                </div>
                <div class="answer" v-for="(answer, index) in answers">
                  <span class="contact">{{answer}}</span>
                  <img src="../../assets/default.png" style="width: 30px;height: 30px;"/>
                </div>
              </div>
            </div>
            <div style="margin-top:5px;position: relative;">
              <textarea rows="3" placeholder="请简要描述您想咨询的问题,如：密码怎么修改？按 Enter 键发送"
                        style="background-color: #ffffff;border-color: #eee;height: 80px;width: 100%;padding: 0 10px;"
                        v-model.trim="sendMsgText"></textarea>
              <span class="isoft_tag1 isoft_point_cursor" style="position: absolute;bottom: 15px;right: 20px;"
                    @click="sendMsg">&nbsp;&nbsp;立刻发送&nbsp;&nbsp;</span>
            </div>
          </div>
        </div>
        <div style="width: 40%;">
          <div style="border: 1px solid #eee;margin: 25px 130px 0 10px;">
            <div class="isoft_info_tip isoft_font12">最近联系</div>
            <div class="selfContact isoft_pd20">
              AAAAAAAAAAAA
              AAAAAAAAAAAA
              AAAAAAAAAAAA
              AAAAAAAAAAAA
              AAAAAAAAAAAA
              AAAAAAAAAAAA
              AAAAAAAAAAAA
            </div>

            <div class="isoft_info_tip isoft_font12">
              <span class="isoft_point_cursor" :class="showHelpPattern === 1 ? 'isoft_color_red': ''"
                    @click="showHelpPattern = 1">自助服务</span>&nbsp;|&nbsp;
              <span class="isoft_point_cursor" :class="showHelpPattern === 2 ? 'isoft_color_red': ''"
                    @click="showHelpPattern = 2">常见问题</span>
            </div>
            <div class="selfAssistent isoft_pd20 clear" v-if="showHelpPattern === 1">
              <a>
                <img src="../../assets/default.png" style="width: 30px;height: 30px;"/>
                <p>友情链接</p>
              </a>
              <a>
                <img src="../../assets/default.png" style="width: 30px;height: 30px;"/>
                <p>问题反馈</p>
              </a>
              <a>
                <img src="../../assets/default.png" style="width: 30px;height: 30px;"/>
                <p>商业合作</p>
              </a>
              <a>
                <img src="../../assets/default.png" style="width: 30px;height: 30px;"/>
                <p>商业合作</p>
              </a>
              <a>
                <img src="../../assets/default.png" style="width: 30px;height: 30px;"/>
                <p>商业合作</p>
              </a>
            </div>
            <div class="selfAssistent isoft_pd20" v-if="showHelpPattern === 2">
              <p>如何找回链知王账号密码？</p>
              <p>如何找回链知王账号密码？</p>
              <p>如何找回链知王账号密码？</p>
              <p>如何找回链知王账号密码？</p>
              <p>如何找回链知王账号密码？</p>
              <p>如何找回链知王账号密码？</p>
              <p>如何找回链知王账号密码？</p>
              <p>如何找回链知王账号密码？</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {CheckHasLoginConfirmDialog2} from "../../tools";

  export default {
    name: "ContactList",
    data() {
      return {
        isLoading: false,
        sendMsgText: '',
        showHelpPattern: 1,  // 1、自助服务 2、常见问题
        asks: [
          '呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃',
          '呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃',
          '呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃',
        ],
        answers: [
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
          '请大大详细描述您遇到的问题，以便小豆更好的帮您解答哦。',
        ],
      }
    },
    methods: {
      sendMsg: function () {
        CheckHasLoginConfirmDialog2(this, function () {
          alert(this.sendMsgText);
        });
      },
      refreshContactData: function () {
        this.isLoading = true;      // 加载效果 2 s 后消失
        setTimeout(() => {
          this.isLoading = false;
        }, 2000);
      }
    },
    mounted() {
      this.refreshContactData();
    }
  }
</script>

<style scoped>
  .header {
    height: 90px;
    line-height: 90px;
    border-bottom: 5px solid #00b500;
  }

  .content {
    min-height: 500px;
    padding-bottom: 20px;
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

  .ask {
    text-align: left;
    margin-bottom: 15px;
  }

  .answer {
    text-align: right;
    margin-bottom: 15px;
  }

  .ask > .contact {
    background: #E5E4EA;
    color: black;
    padding: 10px;
    border-radius: 5px;
  }

  .answer > .contact {
    background: rgba(0, 128, 32, 0.5);
    color: white;
    padding: 10px;
    border-radius: 5px;
  }

  textarea::-webkit-input-placeholder {
    /* placeholder颜色  */
    color: #aab2bd;
    /* placeholder字体大小  */
    font-size: 12px;
  }
</style>
