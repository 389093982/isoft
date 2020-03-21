<template>
  <div style="padding-top: 10px; min-height: 628px" >
    <h2 class="good_rank">热门书单</h2>
    <a v-for="(book, index) in books" @click="$router.push({path:'/ibook/bookCatalogs', query:{'book_id': book.id}})">
      <Row>
        <Col span="2" :class="index < 3 ? 'rank_red_index' : 'rank_grey_index'">{{index + 1}}</Col>
        <Col span="17">
          <span class="rank_name">{{book.book_name}}</span>
          <span class="rank_desc">{{book.book_desc}}</span>
        </Col>
        <Col span="5" class="rank_label">{{book.views}} 次阅读</Col>
      </Row>
    </a>
  </div>
</template>

<script>
  import {QueryCustomTagBook} from "../../api"

  export default {
    name: "BookRank",
    data() {
      return {
        books: [],
      }
    },
    methods: {
      refreshCustomTagBook: async function () {
        const result = await QueryCustomTagBook({custom_tag: 'hot'});
        if (result.status == "SUCCESS") {
          this.books = result.books;
        }
      },
    },
    mounted() {
      this.refreshCustomTagBook();
    }
  }
</script>

<style scoped>
  .good_rank {
    margin: 0 10px 10px 10px;
    position: relative;
    height: 40px;
    color: #111;
    font-size: 20px;
    font-weight: 400;
    line-height: 40px;
    white-space: nowrap;
  }

  a {
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
