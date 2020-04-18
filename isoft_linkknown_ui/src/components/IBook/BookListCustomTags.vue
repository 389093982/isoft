<template>
  <div style="display: flex;justify-content: space-between;">
    <div style="width: 25%;padding: 5px 10px;">
      <div class="isoft_font_header">官方推荐</div>
      <div style="display: flex;flex-wrap: wrap;justify-content: space-between;">
        <div class="isoft_point_cursor isoft_hover_red" v-for="(book, index) in top_location1_books" style="width: 48%;">
          <div class="img_box" style="height: 120px;" @click="$router.push({path:'/ibook/bookCatalogs',query:{book_id:book.id}})">
            <img v-if="book.book_img" :src="book.book_img" height="100%" width="100%"/>
            <img v-else src="../../assets/default.png" height="100%" width="100%"/>
          </div>
          <div class="isoft_inline_ellipsis isoft_font12" style="margin: 2px 0 5px 0;">{{book.book_name}}</div>
        </div>
      </div>
    </div>

    <div style="width: 48%;padding: 15px 10px;">
      <div style="margin-top: 30px;display: flex;flex-wrap: wrap;justify-content: space-between;">
        <div class="img_box isoft_point_cursor" style="width: 50%;height: 293px;"
             @click="$router.push({path:'/ibook/bookCatalogs',query:{book_id:top_location2_books[top_location2_bookIndex].id}})">
          <img v-if="top_location2_books[top_location2_bookIndex].book_img"
               :src="top_location2_books[top_location2_bookIndex].book_img" height="100%" width="100%"/>
          <img v-else src="../../assets/default.png" height="100%" width="100%"/>
        </div>
        <div style="width: 50%;position: relative;">
          <div style="padding-left: 10px;">
            <p class="isoft_inline_ellipsis isoft_hover_title">{{top_location2_books[top_location2_bookIndex].book_name}}</p>

            <p style="margin: 3px 0;">
              <span class="isoft_tag1" :title="famousTexts[0]">博观而约取</span>
              <span class="isoft_tag1" :title="famousTexts[1]">厚积而薄发</span>
              <span class="isoft_tag1" :title="famousTexts[2]">纸上得来终觉浅</span>
              <span class="isoft_tag1" :title="famousTexts[3]">绝知此事要躬行</span>
            </p>

            <p class="isoft_p5line isoft_hover_desc isoft_font12">{{top_location2_books[top_location2_bookIndex].book_desc}}</p>
          </div>
          <div style="position: absolute;bottom: 0;display: flex;justify-content: space-between;">
            <div v-for="(book, index) in top_location2_books" style="width: 30%;height:88px;margin-left: 5px;"
                 class="img_box isoft_point_cursor" :class="{img_box_current: top_location2_bookIndex === index}"
                 @mouseenter="top_location2_bookIndex = index"
                 @click="$router.push({path:'/ibook/bookCatalogs',query:{book_id:book.id}})">
              <img v-if="book.book_img" :src="book.book_img" height="100%" width="100%"/>
              <img v-else src="../../assets/default.png" height="100%" width="100%"/>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div style="width: 25%;padding: 5px 10px;">
      <div class="isoft_font_header">新番图书</div>
      <div style="display: flex;flex-wrap: wrap;justify-content: space-between;">
        <div class="isoft_point_cursor isoft_hover_red" v-for="(book, index) in top_location3_books" style="width: 48%;">
          <div class="img_box" style="height: 120px;"
               @click="$router.push({path:'/ibook/bookCatalogs',query:{book_id:book.id}})">
            <img v-if="book.book_img" :src="book.book_img" height="100%" width="100%"/>
            <img v-else src="../../assets/default.png" height="100%" width="100%"/>
          </div>
          <div class="isoft_inline_ellipsis isoft_font12" style="margin: 2px 0 5px 0;">{{book.book_name}}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {QueryCustomTagBook} from "../../api";

  export default {
    name: "BookListCustomTags",
    data () {
      return {
        top_location1_books: [],  // 图书列表页顶部位置推荐左侧图书
        top_location2_books: [],  // 图书列表页顶部位置推荐中间图书
        top_location3_books: [],  // 图书列表页顶部位置推荐右侧图书
        top_location2_bookIndex: 0,
        famousTexts: [
          "努力的最大好处，就在于你可以选择你想要的生活，而不是被迫随遇而安。",
          "预测未来最好的方式就是创造未来。",
          "不要效仿别人，做最优秀的自己。",
          "人生就像一盘棋，一旦落子就不能反悔。过去也许有过荣耀，或许经历挫折，但都已不再重要。我们唯一能做的，就是走好脚下的每一步。把每一个平凡的当下都努力过得精彩，才能给未来留下最美的回忆。",
        ],

      }
    },
    methods: {
      refreshCustomTagBookList: async function (custom_tag, offset, current_page, callback) {
        const result = await QueryCustomTagBook({custom_tag: custom_tag, offset: offset, current_page: current_page});
        if (result.status === "SUCCESS"){
          callback(result.books);
        }
      }
    },
    mounted (){
      // 查询 custom_tag 标识的图书列表
      this.refreshCustomTagBookList(this.GLOBAL.book_list_top_location1, 4, 1, books => this.top_location1_books = books);
      this.refreshCustomTagBookList(this.GLOBAL.book_list_top_location2, 3, 1, books => this.top_location2_books = books);
      this.refreshCustomTagBookList(this.GLOBAL.book_list_top_location3, 4, 1, books => this.top_location3_books = books);
    }
  }
</script>

<style scoped>
  .img_box:hover,.img_box_current {
    box-shadow: 0 0 2px 2px #00ff33;
  }
</style>
