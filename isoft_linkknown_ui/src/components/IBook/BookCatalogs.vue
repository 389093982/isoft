<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <Row v-if="bookInfo" style="min-height: 400px;">
        <Col span="4" style="text-align: center;">
          <img :src="bookInfo.book_img" style="cursor: pointer;" height="160px" width="140px" @error="defImg()"/>

          <div class="isoft_font isoft_font12"
               style="background-color: #eee;padding: 2px 10px;border-radius: 3px;display: inline-block;">推荐图书
          </div>
        </Col>
        <Col span="20">
          <div style="border-bottom: 1px solid #d7dde4;padding: 10px 0;">
            <h2>{{bookInfo.book_name}}</h2>
            <Row>
              <Col span="4">
                <div>
                  <Time :time="bookInfo.last_updated_time" :interval="1" type="datetime"/>
                </div>
              </Col>
              <Col span="12">
                <div>
                  <span v-if="renderNickName(bookInfo.created_by)">{{renderNickName(bookInfo.created_by)}}</span>
                  <span v-else>{{bookInfo.created_by}}</span>（作者）
                </div>
              </Col>
              <Col span="8">
                <div>阅读xx 评论xx</div>
              </Col>
            </Row>
          </div>

          <div style="padding: 10px 0;">
            <div style="text-align: right;">
              <span v-if="isDifferentLoginUserName(bookInfo.book_author)">
              <Button v-if="!isCollected"
                      @click="toggle_favorite($route.query.book_id,'book_collect', '收藏图书')">收藏</Button>
              <Button v-else @click="toggle_favorite($route.query.book_id,'book_collect', '取消收藏图书')">已收藏</Button>

              </span>

              <Button v-if="isLoginUserName(bookInfo.book_author)" @click="$router.push({path:'/ibook/book_edit',
                query:{book_id: $route.query.book_id, book_name: bookInfo.book_name}})">前去编辑
              </Button>
              <Button @click="$router.push({path:'/ibook/book_detail',
                query:{book_id: $route.query.book_id, book_name: bookInfo.book_name}})">在线阅读
              </Button>
            </div>

            <div style="border-bottom: 1px solid #d7dde4;padding-bottom: 10px;">
              <h3 style="margin:10px 0;">简介</h3>
              <p style="color: #333333;">{{bookInfo.book_desc}}</p>
            </div>

            <div style="padding: 10px 0;">
              <h3 style="margin:10px 0;">文章列表</h3>
              <p v-for="(bookCatalog, index) in bookCatalogs" style="color: #333333;font-size: 15px;">
                <span style="color: rgba(0,128,0,0.4);">{{index+1}}</span>&nbsp;&nbsp;{{bookCatalog.catalog_name}}
              </p>
            </div>
          </div>
        </Col>
      </Row>
    </div>

    <Row class="isoft_top10">
      <Col span="18">
        <div class="isoft_bg_white isoft_pd10" style="margin-right: 5px;">
          <!-- 评论模块 -->
          <IEasyComment v-if="bookInfo" :theme_pk="bookInfo.id" theme_type="bookInfo_theme_type"
                        style="margin-top: 50px;"/>
        </div>
      </Col>
      <Col span="6" class="isoft_bg_white isoft_pd10">
        <RandomAdmt/>
        <RandomAdmt/>
      </Col>
    </Row>

  </div>
</template>

<script>
  import {BookCatalogList, IsFavorite, ToggleFavorite} from "../../api"
  import IEasyComment from "../Comment/IEasyComment"
  import RandomAdmt from "../Advertisement/RandomAdmt";
  import {checkEmpty, GetLoginUserName, RenderNickName, RenderUserInfoByName} from "../../tools"

  export default {
    name: "BookCatalogs",
    components: {RandomAdmt, IEasyComment},
    data() {
      return {
        bookInfo: null,
        bookCatalogs: [],
        defaultImg: require('../../assets/default.png'),
        userInfos: null,
        isCollected: false,  // 是否收藏
      }
    },
    methods: {
      isFavorite: async function (favorite_id, favorite_type) {
        const result = await IsFavorite({favorite_id: favorite_id, favorite_type: favorite_type});
        if (result.status === "SUCCESS") {
          this.isCollected = result.isFavorite;
        }
      },
      toggle_favorite: async function (favorite_id, favorite_type, message) {
        const result = await ToggleFavorite({favorite_id, favorite_type});
        if (result.status === "SUCCESS") {
          this.$Message.success(message + "成功!");
          this.isFavorite(favorite_id, favorite_type);
        }
      },
      isDifferentLoginUserName: function (user_name) {
        return !checkEmpty(GetLoginUserName()) && user_name != GetLoginUserName();
      },
      isLoginUserName: function (user_name) {
        return user_name === GetLoginUserName();
      },
      defImg() {
        let img = event.srcElement;
        img.src = this.defaultImg;
        img.onerror = null; //防止闪图
      },
      refreshBookCatalogList: async function (book_id) {
        const result = await BookCatalogList({book_id: book_id});
        if (result.status === "SUCCESS") {
          this.userInfos = await RenderUserInfoByName(result.bookInfo.created_by);
          this.bookInfo = result.bookInfo;
          this.bookCatalogs = result.bookCatalogs;
        }
      },
      renderNickName: function (user_name) {
        return RenderNickName(this.userInfos, user_name);
      }
    },
    mounted() {
      if (this.$route.query.book_id != null) {
        this.refreshBookCatalogList(this.$route.query.book_id);
      }
    }
  }
</script>

<style scoped>

</style>
