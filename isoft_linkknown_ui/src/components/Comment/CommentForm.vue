<template>
  <div>
    <Row>
      <!-- 评论表单 -->
      <Col span="14" style="padding-right: 10px;">
        <Input v-model.trim="content" type="textarea" :rows="6" placeholder="发表你的评论信息！"/>
        <Button size="small" type="success" style="margin: 5px;float: right;" @click="submitComment('comment')">发表评论</Button>
      </Col>
      <Col span="10" style="border: 1px solid #e9e9e9;font-size:12px;padding: 10px;">
        <p>发表评论需知：</p>
        <p>1、请勿在评论中发表违法违规信息</p>
        <p>2、谢绝人身攻击、地域歧视、刷屏、广告等恶性言论</p>
        <p>3、所有评论均代表玩家本人意见，不代表官方立场</p>
        <p>4、如果您有任何疑问，请在此以评论方式留言给我们</p>
      </Col>
    </Row>
  </div>
</template>

<script>
  import {AddComment} from "../../api/index"
  import {checkFastClick, CheckHasLoginConfirmDialog2} from "../../tools"

  export default {
    name: "CommentForm",
    // 父组件传递给子组件的字段
    props: ["org_parent_id","parent_id", "theme_pk", "theme_type", "refer_user_name"],
    data() {
      return {
        content: "",
      }
    },
    methods: {
      submitComment: function (comment_type) {
        var _this = this;
        if (checkFastClick()) {
          _this.$Message.error("点击过快,请稍后重试!");
          return;
        }
        CheckHasLoginConfirmDialog2(_this, async function () {
          if (_this.content == undefined || _this.content.length < 1) {
            _this.$Notice.error({
              title: '温馨提示',
              desc: "评论信息过短,需要1个字符以上！"
            });
          }else if (_this.content == undefined || _this.content.length > 255){
            _this.$Notice.error({
              title: '温馨提示',
              desc: "评论信息过长,不能超过255个字符！"
            });
          }  else {
            // 修改换行符，等取出来的时候，用v-html展示
            _this.content=  _this.content.replace(/\r|\n|\r\n/g,"<br/>");
            const result = await AddComment(_this.org_parent_id,_this.parent_id, _this.content, _this.theme_pk,_this.theme_type, comment_type, _this.refer_user_name);
            if (result.status == "SUCCESS") {
              _this.$Message.success("发表成功!");
              _this.content = '';
              // 调用父组件的 refreshComment 方法
              _this.$emit('refreshComment', "all");
            }
          }
        });
      }
    }
  }
</script>

<style scoped>

</style>
