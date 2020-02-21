<template>
  <div>
    <Row>
      <Col span="6" style="padding-right: 5px;">
        <div style="background-color: #fff;border: 1px solid #e6e6e6;padding: 20px;min-height: 600px;">
          <Button @click="$router.push({path:'/ibook/book_list'})">全部书单</Button>
          <div v-for="(bookCatalog, index) in bookCatalogs" class="isoft_hover_red isoft_inline_ellipsis">
            <Icon type="ios-paper-outline"/>
            <span @click="showDetail(bookCatalog.id, index)" :style="{color: viewIndex === index ? 'red':''}">{{bookCatalog.catalog_name}}</span>
          </div>
        </div>
      </Col>
      <Col span="18" style="background-color: #fff;border: 1px solid #e6e6e6;padding: 20px;min-height: 500px;">
        <div style="height: 125px;border-bottom: 1px solid #eee;">
          <h1 class="isoft_inline_ellipsis" style="font-size: 28px;word-wrap: break-word;
            color: #000;line-height: 80px;">{{viewCatalogName}}</h1>
          <span style="background-color: rgb(249, 236, 236);color: rgb(202, 12, 22);padding: 3px 5px;">原创</span>
          <a class="isoft_mr10" v-if="created_by">
            <span v-if="renderNickName(created_by)">{{renderNickName(created_by)}}</span>
            <span v-else>{{created_by}}</span>
          </a>
          <span class="isoft_mr10" style="color: #8a8a8a;">最后发布于{{last_updated_time}}</span>
          <span class="isoft_mr10" style="color: #8a8a8a;">累计阅读次数 {{views}}</span>
          <a class="isoft_mr10">收藏</a>
        </div>

        <div ref="scrollArticleArea" style="min-height: 400px;padding: 15px 5px 60px 5px;">
          <IShowMarkdown v-if="bookArticle && bookArticle.content" :content="bookArticle.content"/>
        </div>

        <div style="border-top: 2px solid #bababa;margin: 0 0 10px 0;padding: 10px 0;">
          <img src="../../../static/images/book/dianzan.gif" style="width: 50px;height: 50px;
             position: absolute;margin-top: -80px;"/>
          <img src="../../../static/images/book/learning.gif" style="width: 100px;height: 120px;
             position: absolute;margin-top: -80px;right: 30px;"/>

          <div v-if="viewIndex >= 0 && viewIndex === bookCatalogs.length - 1 && readToEnd"
               style="background-color: #a0a0a0;text-align: center;position: fixed;width: 400px;bottom: 100px;
               right:200px;z-index: 999;cursor: pointer;height: 40px;line-height: 40px;font-size: 20px;color: white;">
            您太棒了，一口气读完全部内容
          </div>

          <Row :gutter="20">
            <Col span="12">
              <div class="move_dh isoft_inline_ellipsis isoft_point_cursor" v-if="viewIndex > 0">
                上一篇 {{prevCatalogName}}
              </div>
            </Col>
            <Col span="12">
              <div class="move_dh isoft_inline_ellipsis isoft_point_cursor"
                   v-if="bookCatalogs && viewIndex < bookCatalogs.length - 1">
                下一篇 {{nextCatalogName}}
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

    <HorizontalLinks :placement_name="GLOBAL.placement_want_to_find"/>
  </div>
</template>

<script>
  import {BookArticleList, BookCatalogList, ShowBookArticleDetail} from "../../api";
  import IBeautifulLink from "../Common/link/IBeautifulLink";
  import IShowMarkdown from "../Common/markdown/IShowMarkdown"
  import HorizontalLinks from "../Elementviewers/HorizontalLinks";
  import RandomAdmt from "../Advertisement/RandomAdmt";
  import {RenderNickName, RenderUserInfoByName} from "../../tools"

  export default {
    name: "BookArticleDetail",
    components: {RandomAdmt, HorizontalLinks, IBeautifulLink, IShowMarkdown},
    data() {
      return {
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
        readToEnd: false,
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
      scrollHandle: function (e) {
        // 最后一篇文章滚动到底部
        if (this.viewIndex >= 0 && this.viewIndex === this.bookCatalogs.length - 1) {
          // 网页可见区域高: document.body.clientHeight
          // 网页正文全文高: document.body.scrollHeight
          // 网页可见区域高(包括边线的高): document.body.offsetHeight
          // 网页被卷去的高: document.body.scrollTop
          // 屏幕分辨率高: window.screen.height
          let scrollTop = e.srcElement.scrollingElement.scrollTop;    // 获取页面滚动高度
          let clientHeight = e.srcElement.scrollingElement.clientHeight;
          let scrollHeight = this.$refs.scrollArticleArea.scrollHeight;
          let height = 250; //根据项目实际定义
          // 网页被卷去的高 + 网页可见区域高 >= 文章高度 + 误差(文章距顶部高度)
          if (scrollTop + clientHeight >= scrollHeight + height) {
            this.readToEnd = true;
          } else {
            this.readToEnd = false;
          }
        }
      }
    },
    mounted() {
      if (this.$route.query.book_id != null) {
        this.refreshBookCatalogList(this.$route.query.book_id);
      }
      // 绑定页面的滚动事件
      window.addEventListener('scroll', this.scrollHandle);
    },
  }
</script>

<style scoped>
  .move_dh {
    animation: move_dh_1 5s infinite;
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
