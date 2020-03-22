<template>
  <div>
    <div v-if="showTip" style="text-align: center;background-color: #eee;padding: 5px 10px;">
      温馨提示：点击键盘左右箭头可切换上一篇和下一篇文章
      <Icon type="md-close" style="float: right;margin: 5px;cursor: pointer;" @click="showTip = false"/>
    </div>

    <Row style="min-height: 500px;background-color: white">
      <Col span="6" style="padding-right: 5px;">
        <div style="background-color: #fff;padding: 20px;">
          <Button @click="$router.push({path:'/ibook/bookList'})">全部书单</Button>
          <div>
            <dl>
              <dd>
                <span class="bookName">{{$route.query.book_name}}</span>
              </dd>
              <dt v-for="(bookCatalog, index) in bookCatalogs" class="isoft_hover_red isoft_inline_ellipsis">
                &nbsp;&nbsp;<span style="color: rgba(0,128,0,0.4);">{{index+1}}</span>&nbsp;&nbsp;
                <span @click="showDetail(bookCatalog.id, index)" :style="{color: viewIndex === index ? 'red':''}">{{bookCatalog.catalog_name}}</span>
              </dt>
            </dl>
          </div>
        </div>
      </Col>
      <Col span="16" style="background-color: #fff;border: 1px solid #e6e6e6;padding: 20px;">
        <div style="height: 125px;border-bottom: 1px solid #eee;">
          <h1 class="isoft_inline_ellipsis" style="font-size: 28px;word-wrap: break-word;color: #000;line-height: 80px;">{{viewCatalogName}}</h1>
          <span style="background-color: rgb(249, 236, 236);color: rgb(202, 12, 22);padding: 3px 5px;">原创</span>
          <a class="isoft_mr10" v-if="created_by" @click="$router.push({path:'/user/detail',query:{username:created_by}})">
            <span v-if="renderNickName(created_by)">{{renderNickName(created_by)}}</span>
            <span v-else>{{created_by}}</span>
          </a>
          <span class="isoft_mr10" style="color: #8a8a8a;">最后发布于: {{last_updated_time}}</span>
          <span class="isoft_mr10" style="color: #8a8a8a;">累计阅读次数: {{views}}</span>
        </div>

        <div ref="scrollArticleArea" style="min-height: 400px;padding: 15px 5px 60px 5px;">
          <IShowMarkdown v-if="bookArticle && bookArticle.content" :content="bookArticle.content"/>
        </div>

        <div style="border-top: 2px solid #e8e8e8;margin: 0 0 10px 0;padding: 10px 0;">
          <img src="../../../static/images/book/dianzan.gif" style="width: 50px;height: 50px;
             position: absolute;margin-top: -80px;"/>
          <img src="../../../static/images/book/learning.gif" style="width: 100px;height: 120px;
             position: absolute;margin-top: -80px;right: 30px;"/>

          <Row :gutter="20">
            <Col span="12">
              <div class="move_dh isoft_inline_ellipsis isoft_point_cursor" v-if="viewIndex > 0"
                   @click="readPrefixOrNextArticle(viewIndex - 1)">
                上一篇: {{prevCatalogName}}
              </div>
            </Col>
            <Col span="12">
              <div class="move_dh isoft_inline_ellipsis isoft_point_cursor"
                   v-if="bookCatalogs && viewIndex < bookCatalogs.length - 1"
                   @click="readPrefixOrNextArticle(viewIndex + 1)">
                下一篇: {{nextCatalogName}}
              </div>
            </Col>
          </Row>
        </div>

        <div style="margin-top: 20px;">
          <!-- 随机广告部分 -->
          <RandomAdmt/>
        </div>
      </Col>
    </Row>
  </div>
</template>

<script>
  import {BookArticleList, BookCatalogList, ShowBookArticleDetail} from "../../api";
  import IBeautifulLink from "../Common/link/IBeautifulLink";
  import IShowMarkdown from "../Common/markdown/IShowMarkdown"
  import HorizontalLinks from "../Elementviewers/HorizontalLinks";
  import RandomAdmt from "../Advertisement/RandomAdmt";
  import {RenderNickName, RenderUserInfoByName} from "../../tools"
  import {scrollTop} from "iview/src/utils/assist"

  export default {
    name: "BookArticleDetail",
    components: {RandomAdmt, HorizontalLinks, IBeautifulLink, IShowMarkdown},
    data() {
      return {
        showTip: true,
        bookArticles: [],
        bookArticle: null,
        bookCatalogs: [],
        viewIndex: -1,
        viewCatalogName: '',     // 当前阅读的文章标题
        prevCatalogName: '',     // 上一篇文章标题
        nextCatalogName: '',     // 下一篇文章标题
        created_by: '',
        last_updated_time: null,
        userInfos: [],
        views: 0,
      }
    },
    methods: {
      renderNickName: function (user_name) {
        return RenderNickName(this.userInfos, user_name);
      },
      showDetail: async function (book_catalog_id, index) {
        if (this.viewIndex === index) {   // 点击的是同一篇文章,不重复加载
          return;
        }
        // 设置当前阅读的索引，上一篇、当前篇、下一篇标题
        this.viewIndex = index;
        this.viewCatalogName = this.bookCatalogs[index].catalog_name;
        this.prevCatalogName = index > 0 ? this.bookCatalogs[index - 1].catalog_name : '';
        this.nextCatalogName = index < this.bookCatalogs.length - 1 ? this.bookCatalogs[index + 1].catalog_name : '';
        const result = await ShowBookArticleDetail(book_catalog_id);
        if (result.status === "SUCCESS") {
          if (result.bookArticle != null) {
            this.bookArticle = result.bookArticle;

            this.created_by = this.bookArticle.last_updated_by;
            this.last_updated_time = this.bookArticle.last_updated_time;
            this.views = this.bookArticle.views;
            this.userInfos = await RenderUserInfoByName(this.created_by);
          }
        } else {
          this.$Message.error("加载失败!");
        }
      },
      refreshBookInfo: async function (book_id) {
        const result = await BookArticleList(book_id);
        if (result.status === "SUCCESS") {
          this.bookArticles = result.books;
          if (this.bookArticles.length > 0) {
            this.showDetail(this.bookArticles[0]);
          }
        }
      },
      refreshBookCatalogList: async function (book_id) {
        const result = await BookCatalogList({book_id: book_id});
        if (result.status === "SUCCESS") {
          this.bookCatalogs = result.bookCatalogs;
          if (this.bookCatalogs && this.bookCatalogs.length > 0) {
            this.showDetail(this.bookCatalogs[0].id, 0); // 默认显示第一条
          }
        }
      },
      readPrefixOrNextArticle: function (index) {
        if (index < 0 || index >= this.bookCatalogs.length) {
          return;
        }
        const sTop = document.documentElement.scrollTop || document.body.scrollTop;
        // 滚动到顶部 50px
        scrollTop(window, sTop, 50, 1000);
        this.showDetail(this.bookCatalogs[index].id, index);
      },
      registOnKeyDown: function () {
        var _this = this;
        //当前页面监视键盘输入
        document.onkeydown = function (e) {
          //事件对象兼容
          let e1 = e || event || window.event || arguments.callee.caller.arguments[0];
          //键盘按键判断:左箭头-37;上箭头-38；右箭头-39;下箭头-40
          if (e1 && e1.keyCode === 37) {
            _this.readPrefixOrNextArticle(_this.viewIndex - 1);
          } else if (e1 && e1.keyCode === 39) {
            _this.readPrefixOrNextArticle(_this.viewIndex + 1);
          }
        }
      }
    },
    mounted() {
      if (this.$route.query.book_id != null) {
        this.refreshBookCatalogList(this.$route.query.book_id);
      }
      this.registOnKeyDown();
    },
  }
</script>

<style scoped>
  .move_dh {
    animation: move_dh_1 5s infinite;
  }

  .bookName {
    cursor: pointer;
    color: #474747;
    font-size: 14px;
  }

  @keyframes move_dh_1 {
    0% {
      transform: rotateY(0deg)
    }
    25% {
      transform: rotateY(10deg)
    }
    50% {
      transform: rotateY(20deg)
    }
    75% {
      transform: rotateY(10deg)
    }
    100% {
      transform: rotateY(0deg)
    }
  }
</style>
