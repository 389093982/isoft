<template>
  <div v-if="books.length>0">
    <div class="isoft_pd10">
      <div class="isoft_title">{{userName===loginUserName()?'我收藏的图书':'作者收藏的图书'}}</div>
      <div style="padding: 10px;border-top: 2px solid #edeff0;">
          <Row style="border:1px solid #eee;margin-bottom: 10px;">
            <div v-for="(book, index) in books">
            <Col span="6" style="margin-bottom: 10px">
              <div class="bookImg isoft_hover_top10">
                <router-link :to="{path:'/ibook/bookCatalogs',query:{book_id:book.id}}">
                  <img v-if="book.book_img" :src="book.book_img" height="160px" width="140px"/>
                  <img v-else src="../../assets/default.png" height="160px" width="140px"/>
                  <p style="font-size: 12px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                    <span class="book_label">精品</span>
                    <span style="color: grey">{{book.book_name | filterLimitFunc(7)}}</span>
                  </p>
                </router-link>
              </div>
            </Col>
            </div>
          </Row>
      </div>
    </div>
  </div>
</template>

<script>
  import {GetUserFavoriteList, QueryBookListByIds} from "../../api"
  import {checkEmpty} from "../../tools"
  import {GetLoginUserName} from "../../tools";

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
      },
      loginUserName:function(){
        return GetLoginUserName()
      },
    },
    mounted() {
      if (checkEmpty(this.user_name)) {
        this.refreshUserFavoriteList();
      }
    },
    filters: {
      // 内容超长则显示部分
      filterLimitFunc:function (value,limitLenth) {
        if (value.length > limitLenth) {
          return value.slice(0,limitLenth) + ' · · ·'
        }else {
          return value
        }
      }
    },
  }
</script>

<style scoped>

  .bookImg {
    padding: 10px 9px 4px;
    width: 160px;
    background-color: rgba(234, 234, 234, 0.5);
    border: 1px solid #FFFFFF;
    overflow: hidden;
    position: relative;
  }

  .bookImg:hover {
    background-color: rgba(214, 214, 214, 0.5);
    border: 1px solid #d0cdd2;
  }

  .bookImg .book_label {
    color: #FF9628;
    border: 1px solid #FF9628;
    border-radius: 1px;
    padding: 1px 4px;
  }

  .bookImg:hover .book_label {
    color: #FF9628;
    border: 1px solid #ffffff;
    background-color: white;
    border-radius: 1px;
    padding: 1px 4px;
  }


</style>
