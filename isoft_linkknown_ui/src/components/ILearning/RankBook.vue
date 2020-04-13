<template>
  <div style="padding-top: 10px; min-height: 550px">
    <div class="isoft_font_header" v-if="isSearchFlag">图书搜索结果</div>
    <div class="isoft_font_header" v-else>{{custom_label}}</div>
    <div class="bookItem hoverItemClass" @mouseenter="handleMouseEvent(index, true)" @mouseleave="handleMouseEvent(index, false)"
         v-for="(book, index) in books" @click="$router.push({path:'/ibook/bookCatalogs', query:{'book_id': book.id}})">
      <Row>
        <Col span="2" :class="index < 3 ? 'rank_red_index' : 'rank_grey_index'">{{index + 1}}</Col>
        <Col span="17">
          <span class="rank_name">{{book.book_name}}</span>
          <span class="rank_desc">{{book.book_desc}}</span>
        </Col>
        <Col span="5" class="rank_label">
          <div v-if="book.ishover" style="position: relative;" title="如果您有多余的空闲时间，可以写写文章发布发布博客">
            <Icon type="ios-eye-outline" size="20" style="position: relative;top: -2px;"/>
            {{book.views}}
            <Icon v-if="book.ishover" style="color: #6a6a6a;top: 8px;" size="30" type="md-arrow-forward" />
          </div>
          <div v-else>{{book.views}} 次阅读</div>
        </Col>
      </Row>
    </div>


    <div class="search_result" v-if="isSearchFlag && !(books && books.length > 0)" style="text-align: center;border-top: 1px solid #eee;padding-top: 10px;">
      <p>未搜索到和 "{{search_data}}" 相关的图书</p>
      <p class="tag isoft_hover_red2" @click="handleReSearch">重新搜索</p>
      <p class="tag hidden isoft_hover_red2" @click="refreshBookList">给我推荐一些</p>
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
        ishover: false,
        books: [],
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 8,
        isSearchFlag: false,    // 是否是搜索模式
        search_data: '',
      }
    },
    methods: {
      handleMouseEvent: function (index, flag) {
        let book = this.books[index];
        book.ishover = flag;
        this.$set(this.books, index, book);
      },
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
        this.search_data = search_data;
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

  .hidden {
    display: none;
  }
  .search_result:hover .hidden {
    display: block;
  }
  .tag {
    background-color: rgba(80, 73, 255, 0.21);
    border-radius: 3px;
    width: 80%;
    margin-left: 10%;
    margin-top: 10px;
    padding: 5px 10px;
  }
</style>
