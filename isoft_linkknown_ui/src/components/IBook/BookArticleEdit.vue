<template>
  <div>
    <div v-if="this.formValidate.book_catalog_id > 0">
      <Spin fix size="large" v-if="isLoading">
        <div class="isoft_loading"></div>
      </Spin>
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate">
        <FormItem prop="content">
          <mavon-editor ref="md" v-model="formValidate.content" @imgAdd="$imgAdd"
                        :editable="formValidate.book_catalog_id > 0"
                        :toolbars="toolbars" :ishljs="true" style="min-height: 500px;"
                        :subfield="false" @fullScreen="handleFullScreen"
                        :style="{'z-index' : fullScreen ? '9999': '1'}"/>
        </FormItem>
        <FormItem>
          <Button type="success" @click="handleSubmit('formValidate')">提交</Button>
        </FormItem>
      </Form>
    </div>

    <div v-else class="isoft_bg_white isoft_hover_desc" style="min-height: 500px;text-align: center;padding: 50px 0;cursor: pointer;">
      <span class="book_icon">
        <img src="../../../static/images/book/book.gif" width="150px" height="150px"/>
        <p class="book_icon_text1">多读书使人聪明</p>
        <p class="book_icon_text2">选择左侧文章进行编辑</p>
      </span>
    </div>
  </div>
</template>

<script>
  import {BookArticleEdit, fileUploadUrl, ShowBookArticleDetail} from "../../api"
  import axios from 'axios'
  import {checkNotEmpty, markdownAdapter} from "../../tools";

  export default {
    name: "BookArticleEdit",
    data() {
      return {
        isLoading: false,
        bookArticle: null,
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
          book_catalog_id: -1,
          content: "",
        },
        ruleValidate: {
          content: [
            {required: true, message: '文章内容不能为空', trigger: 'blur'}
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
          url: fileUploadUrl + "?table_name=book_article&table_field=content",
          method: 'post',
          data: formdata,
          headers: {'Content-Type': 'multipart/form-data'},
        }).then((result) => {
          // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)
          this.$refs.md.$img2Url(pos, result.data.fileServerPath+'?width=500px&height=300px');
        })
      },
      refreshBookArticleDetail: async function (book_catalog_id) {
        this.isLoading = true;
        try {
          this.formValidate.book_catalog_id = book_catalog_id;
          let params = {
            'book_catalog_id':book_catalog_id,
          };
          const result = await ShowBookArticleDetail(params);
          if (result.status === "SUCCESS") {
            if (result.bookArticle != null) {
              this.bookArticle = result.bookArticle;
              this.formValidate.id = result.bookArticle.id;
              this.formValidate.content = result.bookArticle.content;
            } else {
              this.bookArticle = null;
              this.formValidate.id = -1;
              this.formValidate.content = "";
            }
          } else {
            this.$Message.error("加载失败!");
          }
        } finally {
          this.isLoading = false;
        }
      },
      handleSubmit: function (name) {
        var _this = this;
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            // 内容优化
            const result = await BookArticleEdit({
              id:_this.formValidate.id,
              book_catalog_id:_this.formValidate.book_catalog_id,
              content:markdownAdapter(_this.formValidate.content),
            });
            if (result.status === "SUCCESS") {
              _this.$Message.success('提交成功!');
              if (this.successEmit) {
                this.$emit("successEmitFunc");
              }
            } else {
              _this.$Message.error('提交失败!');
            }
          } else {
            _this.$Message.error('验证失败!');
          }
        })
      },
      handleFullScreen: function () {
        this.fullScreen = !this.fullScreen;
      },
    },
  }
</script>

<style scoped>
  .book_icon .book_icon_text1 {
    display: none;
  }

  .book_icon:hover .book_icon_text1 {
    display: block;
  }

  .book_icon:hover .book_icon_text2 {
    display: none;
  }
</style>
