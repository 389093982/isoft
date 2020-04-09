<template>
  <div style="padding-top: 10px; min-height: 550px" >
    <div class="isoft_font_header" v-if="isSearchFlag">图书搜索结果</div>
    <div class="isoft_font_header" v-else>{{custom_label}}</div>
    <div class="bookItem hoverItemClass"
         v-for="(book, index) in books" @click="$router.push({path:'/ibook/bookCatalogs', query:{'book_id': book.id}})">
      <Row>
        <Col span="2" :class="index < 3 ? 'rank_red_index' : 'rank_grey_index'">{{index + 1}}</Col>
        <Col span="17">
          <span class="rank_name">{{book.book_name}}</span>
          <span class="rank_desc">{{book.book_desc}}</span>
        </Col>
        <Col span="5" class="rank_label">{{book.views}} 次阅读</Col>
      </Row>
    </div>


    <div v-if="isSearchFlag && !(books && books.length > 0)" style="text-align: center;border-top: 1px solid #eee;padding-top: 10px;">
      <p>未搜索到匹配的图书</p>
      <p class="isoft_hover_red2" @click="refreshBookList">给我推荐一些</p>
      <p class="isoft_hover_red2" @click="handleReSearch">重新搜索</p>
    </div>
  </div>
</template>

<script>
  import {QueryCustomTagBook, QueryPageBookList} from "../../api"

  export default {
    name: "RankBook",
    props:{
      custom_tag: {
        type: String,
        default: 'hot',
      },
      custom_label: {
        type: String,
        default: '热门图书',
      }
    },
    data() {
      return {
        books: [],
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 8,
        isSearchFlag: false,    // 是否是搜索模式
      }
    },
    methods: {
      handleReSearch: function (){
        this.$emit("research");
      },
      refreshCustomTagBook: async function () {
        const result = await QueryCustomTagBook({custom_tag: this.custom_tag, offset: this.offset, current_page: this.current_page});
        if (result.status === "SUCCESS") {
          this.books = result.books;
          this.total = result.paginator.totalcount;
        }
      },
      search: async function (search_data) {
        this.isSearchFlag = true;
        const result = await QueryPageBookList({
          search_text: search_data,
          offset: this.offset,
          current_page: this.current_page,
        });
        if (result.status === "SUCCESS") {
          this.books = result.books;
          this.total = result.paginator.totalcount;
        }
      },
      refreshBookList: function () {
        this.isSearchFlag = false;
        this.refreshCustomTagBook();
      }
    },
    mounted() {
      this.refreshBookList();
    }
  }
</script>

<style scoped>
  .bookItem {
    margin: 0 20px;
    padding: 10px 10px 5px 10px;
    border-top: 1px solid #eee;
    display: block;
    position: relative;
    cursor: pointer;
  }

  .rank_red_index {
    color: #ff183e;
    font-size: 20px;
  }

  .rank_grey_index {
    color: #999;
    font-size: 20px;
  }

  .rank_name {
    color: #111;
    font-size: 16px;
    font-weight: 500;
    line-height: 20px;
    display: block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .rank_name:hover {
    color: red;
  }

  .rank_desc {
    color: #999;
    font-size: 13px;
    display: block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .rank_label {
    color: #999;
    font-size: 13px;
    white-space: nowrap;
  }
</style>
