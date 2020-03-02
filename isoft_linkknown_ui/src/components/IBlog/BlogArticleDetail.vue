<template>
  <div>

    <div v-if="blog" style="float: left;width: 70%;">
      <div style="margin: 10px;padding: 20px;min-height: 800px;background: #ffffff;">
        <span style="margin-left: 8px;font-size: 15px;color: #777;">{{blog.catalog_name }}</span>
        <span style="font-size: 16px;margin-left: 30px"><b>{{blog.blog_title}}</b></span>
        <div style="border-bottom: 1px solid #eee;margin-top:20px;margin-bottom: 20px;">
          <Row style="margin: 10px;">
            <Col span="24">
              <!--作者:-->
              <span v-if="renderNickName(blog.author)" style="color: #797776;">{{renderNickName(blog.author)}}</span>
              <span v-else style="color: #797776;">{{blog.author}}</span>
              <span style="color: #adaaa8"> • 发布于:<Time :time="blog.created_time"/></span>
              <span style="color: #9b9896">, 更新于:<Time :time="blog.last_updated_time"/></span>
              <span style="margin-left: 200px"><span style="color: rgba(255,0,0,0.65)">{{blog.views}}</span> 次阅读 </span>
              <span style="margin-left: 20px"><span style="color: rgba(255,0,0,0.65)">{{blog.edits}}</span> 次编辑</span>
              <span>
                <Button type="success" size="small" v-if="editable" @click="$router.push({ path: '/iblog/blog_edit', query: { id: blog.id }})">继续编辑
              </Button>
              </span>
            </Col>
          </Row>
        </div>

        <div style="border-bottom: 1px solid #eee;min-height: 200px;">
          <IShowMarkdown v-if="blog.content" :content="blog.content"/>
          <span v-if="blog.link_href">分享链接：<a :href="blog.link_href" target="_blank">{{blog.link_href}}</a></span>
        </div>

        <!-- 评论模块 -->
        <IEasyComment :theme_pk="blog.id" theme_type="blog_theme_type" style="margin-top: 50px;"/>
      </div>
    </div>
    <div style="float: left;width: 21%;margin: 10px 0 0 20px;min-height: 350px;background-color: white">
      这一块待定...
    </div>
    <div style="clear: both;"></div>

  </div>
</template>

<script>
  import {ShowBlogArticleDetail} from "../../api"
  import IShowMarkdown from "../Common/markdown/IShowMarkdown"
  import IEasyComment from "../Comment/IEasyComment"
  import {CheckHasLogin, GetLoginUserName, RenderNickName, RenderUserInfoByName} from "../../tools"
  import MoveLine from "../Common/decorate/MoveLine";

  export default {
    name: "BlogArticleDetail",
    components: {MoveLine, IShowMarkdown, IEasyComment},
    data() {
      return {
        blog: null,
        userInfos: [],
      }
    },
    methods: {
      refreshArticleDetail: async function () {
        const result = await ShowBlogArticleDetail(this.$route.query.blog_id);
        if (result.status === "SUCCESS") {
          this.userInfos = await RenderUserInfoByName(result.blog.author);
          this.blog = result.blog;
        }
      },
      renderNickName: function (user_name) {
        return RenderNickName(this.userInfos, user_name);
      }
    },
    mounted: function () {
      this.refreshArticleDetail();
    },
    computed: {
      editable: function () {
        return CheckHasLogin() && this.blog != null && this.blog.author === GetLoginUserName();
      }
    }
  }
</script>

<style scoped>
</style>
