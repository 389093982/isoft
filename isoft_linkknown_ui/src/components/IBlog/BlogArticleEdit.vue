<template>

  <div class="isoft_container" style="display: flex;min-height: 550px">
      <div style="width: 74%;margin-right:1%;background-color: white">
        <div style="background-color: white;">
          <Row>
            <div class="isoft_pd10">
              <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
                <Row>
                  <Col span="12">
                    <FormItem label="文章标题" prop="blog_title">
                      <Input v-if="this.$route.query.id != null" readonly v-model="formValidate.blog_title" :maxlength="200" show-word-limit placeholder="请输入文章标题"/>
                      <Input v-else v-model="formValidate.blog_title" :maxlength="200" show-word-limit placeholder="请输入文章标题"/>
                    </FormItem>
                  </Col>
                  <Col span="12">
                    <FormItem label="文章分类" prop="catalog_name">
                      <Select v-model="formValidate.catalog_name" filterable @on-open-change="refreshMyCatalogs()">
                        <!-- 热门分类 -->
                        <Option v-for="(hotCatalogItem,index) in hotCatalogItems" :value="hotCatalogItem.element_label"
                                :key="'__hot__' + index">热门分类： {{ hotCatalogItem.element_label }}</Option>
                        <!-- 我的分类 -->
                        <Option v-for="(mycatalog, index) in mycatalogs" :value="mycatalog.catalog_name"
                                :key="'__mine__' + index">我的分类：{{ mycatalog.catalog_name }}</Option>
                      </Select>
                    </FormItem>
                  </Col>
                </Row>
                <Row>
                  <Col span="12">
                    <FormItem label="检索词条" prop="key_words">
                      <Input v-model="formValidate.key_words" :maxlength="200" show-word-limit
                             placeholder="多个检索词条用 / 分割,方便检索"></Input>
                    </FormItem>
                  </Col>
                  <Col span="12">
                    <FormItem label="分享链接" prop="link_href">
                      <Input v-model="formValidate.link_href" :maxlength="200" show-word-limit
                             placeholder="请输入您要推广的链接"></Input>
                    </FormItem>
                  </Col>
                </Row>
                <FormItem label="文章内容" prop="content">
                  <mavon-editor ref="md" v-model="formValidate.content" @imgAdd="$imgAdd" :toolbars="toolbars" :ishljs="true"
                                :subfield="false" @fullScreen="handleFullScreen"
                                :style="{'z-index' : fullScreen ? '9999': '1'}"/>
                </FormItem>
                <FormItem>
                  <Button type="success" @click="handleSubmit('formValidate')">提交</Button>
                </FormItem>
              </Form>
            </div>
          </Row>
        </div>
      </div>

      <div style="width: 25%;background-color: white">
        <div>
          <div class="isoft_bg_white isoft_pd10" style="min-height: 200px;">
            <CatalogList/>
          </div>
          <div class="isoft_bg_white isoft_pd10 isoft_top10">
          </div>
        </div>
      </div>
    </div>

</template>

<script>
  import {BlogArticleEdit, fileUploadUrl, FilterElementByPlacement, GetMyCatalogs, ShowBlogArticleDetail} from "../../api"
  import axios from 'axios'
  import CatalogList from "./CatalogList";
  import {CheckAdminLogin} from "../../tools/index"
  import {checkEmpty, markdownAdapter} from "../../tools";

  export default {
    name: "BlogArticleEdit",
    components: {CatalogList},
    props: {
      successEmit: {
        type: Boolean,
        default: false,
      },
      bookId: {
        type: Number,
        default: -1,
      }
    },
    data() {
      const checkBlogTitle = (rule,value,callback) => {
        if (value === '') {
          callback(new Error('文章标题不能为空！'));
        }else if(value.length>40){
          callback(new Error('文章标题不能超过40个字符!'));
        }else {
          callback();
        }
      };
      const checkKeyWords = (rule,value,callback) => {
       if(value.length>20){
          callback(new Error('检索词条不能超过20个字符!'));
        }else {
          callback();
        }
      };

      const checkContent = (rule,value,callback) => {
        if (value === '') {
          callback(new Error('文章内容不能为空！'));
        }else if(value.length>20000){
          callback(new Error('文章内容不能超过20000个字符!'));
        }else {
          callback();
        }
      };
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
        blog: null,
        // 我的所有文章分类
        mycatalogs: [],
        hotCatalogItems: [],
        formValidate: {
          article_id: -1,
          blog_title: '',
          key_words: '',
          catalog_name: '',
          content: "",
          link_href: "",
          first_img: "",
        },
        ruleValidate: {
          blog_title: [
            {required: true,validator: checkBlogTitle, trigger: 'change'}
          ],
          key_words: [
            { required: false, validator: checkKeyWords, trigger: 'change' }
          ],
          catalog_name: [
            { required: true, message: '文章分类不能为空', trigger: 'change' }
          ],
          content: [
            {required: true, validator:checkContent, trigger: 'change'}
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
          url: fileUploadUrl + "?table_name=blog_article&table_field=content",
          method: 'post',
          data: formdata,
          headers: {'Content-Type': 'multipart/form-data'},
        }).then((result) => {
          if (checkEmpty(this.formValidate.first_img)) {
            this.formValidate.first_img = result.data.fileServerPath;
          }
          // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)
          this.$refs.md.$img2Url(pos, result.data.fileServerPath+'?width=500px&height=300px');
        })
      },
      handleSubmit: function (name) {
        var _this = this;
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            _this.formValidate.content = markdownAdapter(_this.formValidate.content);
            const result = await BlogArticleEdit(_this.formValidate);
            if (result.status === "SUCCESS") {
              _this.$Message.success('提交成功!');
              if (this.successEmit) {
                this.$emit("successEmitFunc");
              } else {
                if (_this.formValidate.article_id > 0){
                  this.$router.push({path: '/iblog/blogArticleDetail', query:{blog_id:_this.formValidate.article_id}});
                } else {
                  this.$router.push({path: '/iblog/blogList'});
                }
              }
            } else {
              _this.$Message.error('提交失败!');
            }
          } else {
            _this.$Message.error('验证失败!');
          }
        })
      },
      refreshArticleDetail: async function (article_id) {
        var articleId = article_id > 0 ? article_id : this.$route.query.id;
        this.formValidate.article_id = articleId;
        let params = {
          'id':articleId
        };
        const result = await ShowBlogArticleDetail(params);
        if (result.status === "SUCCESS") {
          if (result.blog != null) {
            this.blog = result.blog;
            this.formValidate = result.blog;
            this.formValidate.article_id = result.blog.id;
          }
        }
      },
      refreshHotCatalogItems: async function () {
        let params = {
          'placement':this.GLOBAL.placement_host_recommend_blog_tpyes
        };
        const result = await FilterElementByPlacement(params);
        if (result.status === "SUCCESS") {
          //判断是否有admin权限，如果没有那么剔除“官方博客”这一类别
          if (this.isAdmin()) {
            this.hotCatalogItems = result.elements;
          }else {
            for(let j = 0; j<result.elements.length; j++) {
              let element = result.elements[j];
              if (element.element_label.toString().indexOf("官方博客")!=-1 || element.element_label.toString().indexOf("热门博客")!=-1){
                continue;
              } else{
                this.hotCatalogItems.push(element)
              }
            }
          }
        }
      },
      refreshMyCatalogs: async function () {
        const result = await GetMyCatalogs();
        if (result.status === "SUCCESS") {
          this.mycatalogs = result.catalogs;
        }
      },
      isAdmin: function () {
        return CheckAdminLogin();
      },
      handleFullScreen: function () {
        this.fullScreen = !this.fullScreen;
      },
    },
    mounted: async function () {
      // 加载热门分类
      this.refreshHotCatalogItems();
      // 数据回显
      if (this.$route.query.id != null) {
        this.refreshArticleDetail();
      }
      const result = await GetMyCatalogs();
      if (result.status === "SUCCESS") {
        this.mycatalogs = result.catalogs;
      }
    }
  }
</script>

<style scoped>

</style>
