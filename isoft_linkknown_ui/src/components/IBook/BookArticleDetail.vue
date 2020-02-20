<template>
  <div>
    <Row>
      <Col span="6"
           style="background-color: #fff;border: 1px solid #e6e6e6;border-radius: 4px;padding: 20px;min-height: 500px;">
        <div>
          <Button @click="$router.push({path:'/ibook/book_list'})">全部书单</Button>
          <Scroll height="430" style="margin-top: 5px;">
            <div v-for="(bookCatalog, index) in bookCatalogs" class="isoft_hover_red isoft_inline_ellipsis">
              <Icon type="ios-paper-outline"/>
              <span @click="showDetail(bookCatalog.id, index)" :style="{color: viewIndex === index ? 'red':''}">{{bookCatalog.catalog_name}}</span>
            </div>
          </Scroll>
        </div>
      </Col>
      <Col span="18"
           style="background-color: #fff;border: 1px solid #e6e6e6;border-radius: 4px;padding: 20px;min-height: 500px;">
        <div style="border-bottom: 2px solid #bababa;margin: 0 0 10px 0;padding: 10px 0;">
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

        <div style="min-height: 400px;padding: 20px 0 60px 0;">
          <IShowMarkdown v-if="bookArticle && bookArticle.content" :content="bookArticle.content"/>
        </div>

        <div style="border-top: 2px solid #bababa;margin: 0 0 10px 0;padding: 10px 0;">
          <img src="../../../static/images/book/dianzan.gif" style="width: 50px;height: 50px;
             position: absolute;margin-top: -80px;"/>
          <img src="../../../static/images/book/learning.gif" style="width: 100px;height: 120px;
             position: absolute;margin-top: -80px;right: 30px;"/>

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

  export default {
    name: "BookArticleDetail",
    components: {RandomAdmt, HorizontalLinks, IBeautifulLink, IShowMarkdown},
    data() {
      return {
        bookArticles: [],
        bookArticle: null,
        bookCatalogs: [],
        viewIndex: 0,
        viewCatalogName: '',     // 当前阅读的文章标题
        prevCatalogName: '',     // 上一篇文章标题
        nextCatalogName: '',     // 下一篇文章标题
      }
    },
    methods: {
      showDetail: async function (book_catalog_id, index) {
        // 设置当前阅读的索引，上一篇、当前篇、下一篇标题
        this.viewIndex = index;
        this.viewCatalogName = this.bookCatalogs[index].catalog_name;
        this.prevCatalogName = index > 0 ? this.bookCatalogs[index - 1].catalog_name : '';
        this.nextCatalogName = index < this.bookCatalogs.length - 1 ? this.bookCatalogs[index + 1].catalog_name : '';

        const result = await ShowBookArticleDetail(book_catalog_id);
        if (result.status == "SUCCESS") {
          if (result.bookArticle != null) {
            this.bookArticle = result.bookArticle;
          }
        } else {
          this.$Message.error("加载失败!");
        }
      },
      refreshBookInfo: async function (book_id) {
        const result = await BookArticleList(book_id);
        if (result.status == "SUCCESS") {
          this.bookArticles = result.books;
          if (this.bookArticles.length > 0) {
            this.showDetail(this.bookArticles[0]);
          }
        }
      },
      refreshBookCatalogList: async function (book_id) {
        const result = await BookCatalogList({book_id: book_id});
        if (result.status == "SUCCESS") {
          this.bookCatalogs = result.bookCatalogs;
          if (this.bookCatalogs && this.bookCatalogs.length > 0) {
            this.showDetail(this.bookCatalogs[0].id, 0); // 默认显示第一条
          }
        }
      },
    },
    mounted() {
      if (this.$route.query.book_id != undefined && this.$route.query.book_id != null) {
        this.refreshBookCatalogList(this.$route.query.book_id);
      }
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
      transform: rotateY(5deg)
    }
    50% {
      transform: rotateY(10deg)
    }
    750% {
      transform: rotateY(5deg)
    }
    100% {
      transform: rotateY(0deg)
    }
  }
</style>
