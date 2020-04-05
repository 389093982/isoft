<template>
  <div>

    <!--左侧博客内容-->
    <div v-if="blog" style="float: left;width: 72%;margin-left: 40px">
      <div style="margin: 10px;padding: 20px;width:100%;min-height: 800px;background: #ffffff;">

        <Row>
          <!--第一列：博主头像-->
          <Col span="2" style="position: relative;left: 20px;">
            <router-link :to="{path:'/user/userDetail',query:{username:blog.author}}" style="float: left;">
              <HatAndFacePicture :src="renderUserIcon(blog.author)" :vip_level="renderVipLevel(blog.author)" :hat_in_use="renderHatInUse(blog.author)" :src_size="40" :hat_width="36" :hat_height="10" :hat_relative_left="2" :hat_relative_top="-56" ></HatAndFacePicture>
            </router-link>
          </Col>
          <!--第二列：博客信息-->
          <Col span="22">
            <!--第一行-->
            <Row>
              <span style="font-size: 15px;color: #777;">{{blog.catalog_name }}</span>
              <span style="font-size: 16px;margin-left: 10px"><b>{{blog.blog_title}}</b></span>
            </Row>
            <!--第二行-->
            <Row>
              <Col span="24">
                <!--作者:-->
                <span v-if="renderNickName(blog.author)" style="color: #797776;">{{renderNickName(blog.author)}}</span>
                <span v-else style="color: #797776;">{{blog.author}}</span>
                <span style="color: #adaaa8"> • 发布于:<Time :time="blog.created_time"/></span>
                <span style="color: #9b9896">, 更新于:<Time :time="blog.last_updated_time"/></span>
              </Col>
            </Row>
            <!--第三行-->
            <Row>
              <span style="margin-left: 500px"><span style="color: rgba(255,0,0,0.65)">{{blog.views}}</span> 次阅读 </span>
              <span style="margin-left: 20px"><span style="color: rgba(255,0,0,0.65)">{{blog.edits}}</span> 次编辑</span>
              <span>
                  <Button type="success" size="small" v-if="editable" @click="$router.push({ path: '/iblog/blogArticleEdit', query: { id: blog.id }})">继续编辑</Button>
                </span>
              <span>
                  <Button type="error" size="small" v-if="editable" @click="showDeleteModal">删除博客</Button>
                </span>
            </Row>
          </Col>
        </Row>

        <!--灰色分割线-->
        <div style="border-bottom: 1px solid #eee;margin-bottom: 10px;"></div>

        <!--是否确认删除博客-->
        <IsComfirmDelete ref="comfirmDelete" @confirmDelete="deleteBlog" content="博客一经删除，无法复原，确认删除?"></IsComfirmDelete>

        <!--博客内容-->
        <div style="border-bottom: 1px solid #eee;min-height: 150px;margin-left: 10px">
          <IShowMarkdown v-if="blog.content" :content="blog.content"/>
          <span v-if="blog.link_href">分享链接：<a :href="blog.link_href" target="_blank">{{blog.link_href}}</a></span>
        </div>

        <p>博友印象：</p>
        <VoteTags v-if="blog.id > 0" referer_type="vote_blog" :referer_id="blog.id"/>

        <!-- 评论模块 -->
        <IEasyComment :theme_pk="blog.id" theme_type="blog_theme_type" style="margin-top: 50px;"/>
      </div>
    </div>

    <!--右侧点赞板-->
    <div style="float:right;width: 18%;position: fixed;top: 80px;right: 65px;background-color: white;border-radius: 5px;text-align: center">
      <div style="min-height: 355px;">
        <div style="width: 86%;margin: 18px 0 18px 18px;">
          <div class="TOTOP" @click="toTop()">
            <Icon type="md-arrow-dropup" style="font-size: 25px;"/>
          </div>
          <div>
            <Icon v-if="blog_praise===true" @click="toggle_favorite(blog.id,'blog_praise', '取消点赞')" class="hvr-grow" type="md-heart" style="font-size: 50px;margin-top: 30px ;cursor: pointer;color: #cc0000" />
            <Icon v-else type="md-heart" @click="toggle_favorite(blog.id,'blog_praise', '点赞')" class="hvr-grow" style="font-size: 50px;margin-top: 30px ;cursor: pointer" />
            <div style="position: relative;top: -10px">{{blog_praise_counts}}个赞</div>
          </div>
          <div style="margin: 20px 0 0 0 ;border-bottom: 1px solid gainsboro;">
            <div style="margin-bottom: 20px">
              <ButtonGroup>
                <Button><Icon type="ios-eye" style="font-size: 20px" />关注</Button>
                <Button>
                  <span v-if="blog_collect===true" style="color: #ff6900" @click="toggle_favorite(blog.id,'blog_collect', '取消收藏')">
                    <Icon type="md-bookmark" style="font-size: 20px" />已收藏
                  </span>
                  <span v-else @click="toggle_favorite(blog.id,'blog_collect', '收藏')">
                    <Icon type="md-bookmark" style="font-size: 20px" />收藏
                  </span>
                </Button>
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
              共 {{blog.comments}} 条评论
            </div>
          </div>
          <div class="TOBOTTOM" @click="toBottom()">
            <Icon type="md-arrow-dropdown" style="font-size: 25px;" />
          </div>
        </div>
      </div>
    </div>

    <div style="clear: both"></div>

  </div>
</template>

<script>
  import {ShowBlogArticleDetail,ArticleDelete,IsFavorite,ToggleFavorite,queryFavoriteCount} from "../../api"
  import IShowMarkdown from "../Common/markdown/IShowMarkdown"
  import IEasyComment from "../Comment/IEasyComment"
  import {CheckHasLogin, GetLoginUserName, RenderNickName,RenderUserIcon,RenderVipLevel,RenderHatInUse, RenderUserInfoByName,CheckHasLoginConfirmDialog} from "../../tools"
  import MoveLine from "../Common/decorate/MoveLine";
  import IsComfirmDelete from "./IsComfirmDelete";
  import VoteTags from "../Decorate/VoteTags";
  import HatAndFacePicture from "../Common/HatAndFacePicture/HatAndFacePicture";

  export default {
    name: "BlogArticleDetail",
    components: {HatAndFacePicture, VoteTags, IsComfirmDelete, MoveLine, IShowMarkdown, IEasyComment},
    data() {
      return {
        blog: '',
        userInfos: [],
        // 博客点赞
        blog_praise: false,
        // 收藏博客
        blog_collect: false,
        //点赞数量
        blog_praise_counts:0,
        //右侧-上下速度控制
        speed:40,
      }
    },
    methods: {
      refreshArticleDetail: async function () {
        const result = await ShowBlogArticleDetail(this.$route.query.blog_id);
        if (result.status === "SUCCESS") {
          this.userInfos = await RenderUserInfoByName(result.blog.author);
          this.blog = result.blog;
          this.refreshFavoriteStatus();
        }
      },
      refreshFavoriteStatus: async function () {
        let result = await queryFavoriteCount({favorite_id:this.blog.id, favorite_type:'blog_praise'});
        if (result.status === "SUCCESS") {
          this.blog_praise_counts = result.counts;
        }

        if (CheckHasLogin() && this.blog) {
          let result = await IsFavorite({favorite_id: this.blog.id, favorite_type: "blog_praise"});
          if (result.status === "SUCCESS") {
            this.blog_praise = result.isFavorite;
          }

          result = await IsFavorite({favorite_id: this.blog.id, favorite_type: "blog_collect"});
          if (result.status === "SUCCESS") {
            this.blog_collect = result.isFavorite;
          }
        }
      },
      toggle_favorite: async function (favorite_id, favorite_type, message) {
        if (CheckHasLogin()) {
          const result = await ToggleFavorite({favorite_id, favorite_type});
          if (result.status === "SUCCESS") {
            this.refreshFavoriteStatus();
            this.$Message.success(message + "成功!");
          }
        }else {
          CheckHasLoginConfirmDialog(this, {path: "/iblog/blogArticleDetail?blog_id="+this.$route.query.blog_id});
        }
      },
      renderNickName: function (user_name) {
        return RenderNickName(this.userInfos, user_name);
      },
      renderUserIcon: function (user_name) {
        return RenderUserIcon(this.userInfos, user_name);
      },
      renderVipLevel: function (user_name) {
        return RenderVipLevel(this.userInfos, user_name);
      },
      renderHatInUse: function (user_name) {
        return RenderHatInUse(this.userInfos, user_name);
      },
      showDeleteModal:function(){
        this.$refs.comfirmDelete.showModal();
      },
      deleteBlog: async function () {
        const result = await ArticleDelete(this.$route.query.blog_id);
        if (result.status === "SUCCESS") {
          this.$router.push({path:'/iblog/blogList'});
          this.$Message.info("删除成功！");
        }
      },
      toTop:function () {
        //参数speed表示间隔的幅度大小，以此来控制速度
        document.documentElement.scrollTop -= this.speed;
        if (document.documentElement.scrollTop > 0) {
          var c = setTimeout(()=>this.toTop(this.speed),16);
        }else {
          clearTimeout(c);
        }
      },
    toBottom:function () {
      let clientHeight = document.documentElement.clientHeight || document.body.clientHeight;
      let scrollHeight = document.documentElement.scrollHeight;
      let height = scrollHeight - clientHeight; //超出窗口上界的值就是底部的scrolTop的值
      document.documentElement.scrollTop += this.speed;
      if (document.documentElement.scrollTop < height-320) {//这里减320，是为了不用到底部
        var c = setTimeout(()=>this.toBottom(this.speed),16);
      }else {
        clearTimeout(c);
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
  .TOTOP{
    height:auto;border: 1px solid gainsboro;color: #5e5e5e;border-radius: 5px;cursor: pointer
  }
  .TOTOP:hover{
    height:auto;border: 1px solid #ff730e;color: #ff6900;border-radius: 5px;cursor: pointer
  }
  .TOBOTTOM{
    height:auto;border: 1px solid gainsboro;color: #5e5e5e;border-radius: 5px;margin-top: 18px;cursor: pointer
  }
  .TOBOTTOM:hover{
    height:auto;border: 1px solid #ff730e;color: #ff6900;border-radius: 5px;margin-top: 18px;cursor: pointer
  }
</style>
