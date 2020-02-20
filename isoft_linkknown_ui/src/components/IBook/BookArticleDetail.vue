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
        <div style="text-align: right;border-bottom: 2px solid #bababa;margin: 0 0 10px 0;padding: 10px 0;">
          <IBeautifulLink v-if="viewIndex > 0">上一篇</IBeautifulLink>
          <IBeautifulLink v-if="bookCatalogs && viewIndex < bookCatalogs.length - 1">下一篇</IBeautifulLink>
        </div>

        <div style="min-height: 400px;">
          <IShowMarkdown v-if="bookArticle && bookArticle.content" :content="bookArticle.content"/>
        </div>

        <div style="text-align: right;border-top: 2px solid #bababa;margin: 0 0 10px 0;padding: 10px 0;">
          <IBeautifulLink v-if="viewIndex > 0">上一篇</IBeautifulLink>
          <IBeautifulLink v-if="bookCatalogs && viewIndex < bookCatalogs.length - 1">下一篇</IBeautifulLink>
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
      }
    },
    methods: {
      showDetail: async function (book_catalog_id, index) {
        this.viewIndex = index;
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

</style>
