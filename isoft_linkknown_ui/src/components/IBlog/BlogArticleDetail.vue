<template>
  <div>

    <div v-if="blog" style="float: left;width: 72%;margin-left: 40px">
      <div style="margin: 10px;padding: 20px;width:100%;min-height: 800px;background: #ffffff;">
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
                <Button type="success" size="small" v-if="editable" @click="$router.push({ path: '/iblog/blog_edit', query: { id: blog.id }})">继续编辑</Button>
              </span>
              <span>
                <Button type="error" size="small" v-if="editable" @click="showDeleteModal">删除博客</Button>
              </span>
            </Col>
          </Row>
        </div>

        <!--是否确认删除博客-->
        <IsComfirmDelete ref="showModal"></IsComfirmDelete>

        <div style="border-bottom: 1px solid #eee;min-height: 150px;">
          <IShowMarkdown v-if="blog.content" :content="blog.content"/>
          <span v-if="blog.link_href">分享链接：<a :href="blog.link_href" target="_blank">{{blog.link_href}}</a></span>
        </div>

        <!-- 评论模块 -->
        <IEasyComment :theme_pk="blog.id" theme_type="blog_theme_type" style="margin-top: 50px;"/>
      </div>
    </div>

    <!--右侧点赞板-->
    <div style="float:right;width: 18%;position: fixed;top: 80px;right: 55px;background-color: white;border-radius: 5px;text-align: center">
      <div style="min-height: 355px;">
        <div style="width: 86%;margin: 18px 0 18px 18px;">
          <div style="height:auto;border: 1px solid gainsboro;border-radius: 5px;cursor: pointer">
            <Icon type="md-arrow-dropup" style="font-size: 25px;" />
          </div>
          <div>
            <Icon type="md-heart" style="font-size: 50px;margin-top: 30px ;cursor: pointer" />
            <div style="position: relative;top: -10px">168个赞</div>
          </div>
          <div style="margin: 20px 0 0 0 ;border-bottom: 1px solid gainsboro;">
            <div style="margin-bottom: 20px">
              <ButtonGroup>
                <Button><Icon type="ios-eye" style="font-size: 20px" />关注</Button>
                <Button><Icon type="md-bookmark" style="font-size: 20px" />收藏</Button>
              </ButtonGroup>
            </div>
          </div>
          <div style="margin: 10px 0 0 0 ;border-bottom: 1px solid gainsboro;">
            <div style="margin-bottom: 5px">
             <img src="../../../static/images/blogShare/sina.png" height="18" width="18" style="cursor: pointer"/>
             <img src="../../../static/images/blogShare/wechat.png" height="18" width="18" style="cursor: pointer"/>
            </div>
          </div>
          <div style="margin: 10px 0 0 0 ;border-bottom: 1px solid gainsboro;">
            <div style="margin-bottom: 10px">
              共 88 个回复
            </div>
          </div>
          <div style="height:auto;border: 1px solid gainsboro;border-radius: 5px;margin-top: 18px;cursor: pointer">
            <Icon type="md-arrow-dropdown" style="font-size: 25px;" />
          </div>
        </div>
      </div>
    </div>

    <div style="clear: both"></div>

  </div>
</template>

<script>
  import {ShowBlogArticleDetail,ArticleDelete} from "../../api"
  import IShowMarkdown from "../Common/markdown/IShowMarkdown"
  import IEasyComment from "../Comment/IEasyComment"
  import {CheckHasLogin, GetLoginUserName, RenderNickName, RenderUserInfoByName} from "../../tools"
  import MoveLine from "../Common/decorate/MoveLine";
  import IsComfirmDelete from "./IsComfirmDelete";

  export default {
    name: "BlogArticleDetail",
    components: {IsComfirmDelete, MoveLine, IShowMarkdown, IEasyComment},
    data() {
      return {
        blog: null,
        userInfos: [],
        isShow:false,
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
      },
      showDeleteModal:function(){
        this.isShow = true;
      },
      deleteBlog: async function () {
        const result = await ArticleDelete(this.$route.query.blog_id);
        if (result.status === "SUCCESS") {
          this.$router.push({path:'/iblog/blog_list'});
          this.$Message.info("删除成功！");
        }
      },
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
