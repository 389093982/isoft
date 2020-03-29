<template>
  <div class="isoft_bg_white isoft_pd10">
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100">
      <FormItem label="问题简述" prop="short_desc">
        <Input v-model.trim="formValidate.short_desc" placeholder="Enter short_desc..."></Input>
      </FormItem>
      <FormItem label="我的问题" prop="question">
        <mavon-editor ref="md" v-model="formValidate.question" @imgAdd="$imgAdd"
                      :toolbars="toolbars" :ishljs="true" @fullScreen="handleFullScreen"
                      :style="{'z-index' : fullScreen ? '9999': '1'}"/>
      </FormItem>
      <FormItem>
        <Button type="success" @click="handleSubmit('formValidate')">提交</Button>
      </FormItem>
    </Form>
  </div>
</template>

<script>
  import {EditQuestion, fileUploadUrl, ShowAskExpertDetail} from "../../api"
  import axios from 'axios'

  export default {
    name: "EditQuestion",
    data() {
      return {
        fullScreen: false,     // 默认没有全屏
        toolbars: {
          bold: true, // 粗体
          italic: true, // 斜体
          header: true, // 标题
          underline: true, // 下划线
          // mark: true, // 标记
          superscript: true, // 上角标
          quote: true, // 引用
          ol: true, // 有序列表
          link: true, // 链接
          imagelink: true, // 图片链接
          help: true, // 帮助
          code: true, // code
          subfield: true, // 是否需要分栏
          fullscreen: true, // 全屏编辑
          readmodel: true, // 沉浸式阅读
          undo: true, // 上一步
          trash: true, // 清空
          save: true, // 保存（触发events中的save事件）
          navigation: true // 导航目录
        },
        formValidate: {
          id: -1,
          short_desc: '',
          question: '',
        },
        ruleValidate: {
          short_desc: [
            {required: true, message: 'short_desc 不能为空!', trigger: 'blur'}
          ],
          question: [
            {required: true, message: 'question 不能为空!', trigger: 'blur'}
          ],
        },
      }
    },
    methods: {
      // 绑定@imgAdd event
      // 参考 https://github.com/hinesboy/mavonEditor/blob/master/doc/cn/upload-images.md
      $imgAdd(pos, $file) {
        // 第一步.将图片上传到服务器.
        var formdata = new FormData();
        formdata.append('file', $file);
        axios({
          url: fileUploadUrl + "?table_name=ask_expert&table_field=question",
          method: 'post',
          data: formdata,
          headers: {'Content-Type': 'multipart/form-data'},
        }).then((result) => {
          // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)
          this.$refs.md.$img2Url(pos, result.data.fileServerPath+'?width=500px&height=300px');
        })
      },
      handleSubmit(name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            const result = await EditQuestion(this.formValidate);
            if (result.status === "SUCCESS") {
              this.$router.push({path: '/expert/askExpert'});
            }
          }
        })
      },
      refreshQuestionDetail: async function (id) {
        const result = await ShowAskExpertDetail({id: id});
        if (result.status == "SUCCESS") {
          this.formValidate = result.ask_expert;
        }
      },
      handleFullScreen: function () {
        this.fullScreen = !this.fullScreen;
      },
    },
    mounted() {
      if (this.$route.query.id > 0) {
        this.refreshQuestionDetail(this.$route.query.id);
      }
    }
  }
</script>

<style scoped>

</style>
