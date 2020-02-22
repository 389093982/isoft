<template>
  <div>
    <div class="isoft_pd10">
      <div class="isoft_auto_with title">他/她收藏的图书</div>
      <div style="padding: 10px;border-top: 2px solid #edeff0;">
        <div v-for="(book, index) in books">
          <Row style="border:1px solid #eee;margin-bottom: 10px;">
            <Col span="4" style="text-align: center;padding: 10px;">
              <img :src="book.book_img" @error="defImg()" style="width: 80px; height: 110px;"/>
            </Col>
            <Col span="20" style="padding: 10px;">
              <h3>{{book.book_name}}</h3>
              <p>{{book.book_desc}}</p>
            </Col>
          </Row>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {GetUserFavoriteList, QueryBookListByIds} from "../../api"
  import {checkEmpty} from "../../tools"

  export default {
    name: "UserFavorite",
    props: {
      userName: {
        type: String,
        default: ''
      },
    },
    data() {
      return {
        defaultImg: require('../../assets/default.png'),
        book_collects: [],
        books: [],
      }
    },
    methods: {
      defImg() {
        let img = event.srcElement;
        img.src = this.defaultImg;
        img.onerror = null; //防止闪图
      },
      // 根据图书收藏列表 id 查询图书信息
      refreshBookList: async function () {
        let book_ids = this.book_collects.map(bc => bc.favorite_id).join(",");
        const result = await QueryBookListByIds({book_ids});
        if (result.status === "SUCCESS") {
          this.books = result.books;
        }
      },
      // 获取用户收藏列表
      refreshUserFavoriteList: async function () {
        // 获取图书收藏列表
        const result = await GetUserFavoriteList({favorite_type: 'book_collect', user_name: this.userName});
        if (result.status === "SUCCESS") {
          this.book_collects = result.favorites;
          this.refreshBookList();
        }
      }
    },
    mounted() {
      if (checkEmpty(this.user_name)) {
        this.refreshUserFavoriteList();
      }
    }
  }
</script>

<style scoped>
  .title {
    font-size: 18px;
    font-weight: normal;
    height: 35px;
    line-height: 35px;
    font-family: "微软雅黑";
  }

  .title::after {
    content: "";
    display: block;
    height: 3px;
    border-bottom: 3px solid red;
  }
</style>
