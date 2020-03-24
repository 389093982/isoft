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
            <div style="border: 1px solid #eee;height: 450px;overflow-y:scroll;padding: 20px;">
              <div style="text-align: center;margin-bottom: 5px;">
                <img src="../../assets/default.png" style="width: 30px;height: 30px;"/>
                <span class="isoft_tag1">智能小豆为您解答</span>
              </div>

              <div class="ask" v-for="(ask, index) in asks">
                <img src="../../assets/default.png" style="width: 30px;height: 30px;"/>
                <span class="contact">{{ask}}</span>
              </div>
              <div class="answer" v-for="(answer, index) in answers">
                <span class="contact">{{answer}}</span>
                <img src="../../assets/default.png" style="width: 30px;height: 30px;"/>
              </div>
            </div>
            <div style="margin-top:5px;">
              <input style="background-color: #ffffff;border-color: #eee;height: 50px;width: 100%;padding: 0 10px;"/>
            </div>
          </div>
        </div>
        <div style="width: 40%;">
          <div style="border: 1px solid #eee;margin: 25px 130px 0 10px;">
            <div class="isoft_info_tip"><span>自助服务</span>&nbsp;&nbsp;<span>我相关的联系</span></div>
            <div class="selfAssistent isoft_pd20 clear">
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

            <div class="isoft_info_tip">常见问题</div>
            <div class="selfAssistent isoft_pd20">
              <p>如何找回链知王账号密码？</p>
              <p>如何找回链知王账号密码？</p>
              <p>如何找回链知王账号密码？</p>
              <p>如何找回链知王账号密码？</p>
              <p>如何找回链知王账号密码？</p>
              <p>如何找回链知王账号密码？</p>
              <p>如何找回链知王账号密码？</p>
              <p>如何找回链知王账号密码？</p>
            </div>
            <div class="selfContact">

            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ContactList",
    data() {
      return {
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
</style>
