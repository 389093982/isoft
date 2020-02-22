<template>
  <div>
    <div>
      <p>他/她收藏的图书</p>
      <div v-for="(book, index) in books">
        <div>
          <p>图书名称 {{book.book_name}}</p>
          <p>描述 {{book.book_desc}}</p>
          <img :src="book.book_img" @error="defImg()" style="width: 120px; height: 150px;"/>
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

</style>
