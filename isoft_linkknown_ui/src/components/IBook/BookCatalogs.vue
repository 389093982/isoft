<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <Row v-if="bookInfo" style="min-height: 400px;">
        <!--左侧图书图片-->
        <Col span="4" style="text-align: center;">
          <img :src="bookInfo.book_img" style="cursor: pointer;" height="160px" width="140px" @error="defImg()"/>
          <div class="isoft_font isoft_font12" style="background-color: #eee;padding: 2px 10px;border-radius: 3px;display: inline-block;">
            推荐图书
          </div>
        </Col>
        <!--右侧描述-->
        <Col span="14">
          <div style="border-bottom: 1px solid #d7dde4;padding: 10px 0;">
            <!--第一行书名、阅读和评论次数-->
            <Row>
              <Col span="16">
                <span class="bookName"><< {{bookInfo.book_name}} >></span>
              </Col>
              <Col span="8">
                <div style="margin-left: 5px">{{bookInfo.views}}&nbsp;&nbsp;次阅读 ,&nbsp;&nbsp;&nbsp;&nbsp;{{bookInfo.comments}}&nbsp;&nbsp;次评论</div>
              </Col>
            </Row>
            <!--第二行：最后更新时间、作者-->
            <Row>
              <Col span="6">
                <div style="font-size: 12px">
                  作者:
                  <span v-if="bookInfo.created_by">{{renderNickName(bookInfo.created_by)}}</span>
                  <span v-else>{{bookInfo.created_by}}</span>
                </div>
              </Col>
              <Col span="10">
                创建于:<Time :time="bookInfo.created_time" :interval="1" type="datetime" style="font-size: 12px"/>
              </Col>
              <Col span="8">
                <div>
                  <Button v-if="isLoginUserName(bookInfo.book_author)" @click="$router.push({path:'/ibook/bookEdit', query:{book_id: $route.query.book_id, book_name: bookInfo.book_name}})">前去编辑</Button>
                  <Button @click="$router.push({path:'/ibook/bookDetail',query:{book_id: $route.query.book_id, book_name: bookInfo.book_name}})">在线阅读</Button>
                  <span v-if="isDifferentLoginUserName(bookInfo.book_author)">
                    <Button v-if="!isCollected" @click="toggle_favorite($route.query.book_id,'book_collect', '收藏图书')" style="color: grey">
                      <Icon type="md-bookmark" style="font-size: 20px;"/>
                      收藏
                    </Button>
                    <Button v-else @click="toggle_favorite($route.query.book_id,'book_collect', '取消收藏图书')" style="color: #ff6900">
                      <Icon type="md-bookmark" style="font-size: 20px;"/>
                      已收藏
                    </Button>
                  </span>
                </div>
              </Col>
            </Row>
          </div>

          <div style="padding: 10px 0;">
            <div style="border-bottom: 1px solid #d7dde4;padding-bottom: 10px;">
              <h3 style="margin:10px 0;">简介</h3>
              <p class="isoft_word_break" style="color: #333333;">{{bookInfo.book_desc}}</p>
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

          <p>读者印象：</p>
          <VoteTags v-if="bookInfo.id > 0" referer_type="book_theme_type" :referer_id="bookInfo.id"/>

          <!-- 评论模块 -->
          <IEasyComment v-if="bookInfo" :theme_pk="bookInfo.id" theme_type="bookInfo_theme_type" style="margin-top: 50px;"/>
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
  import VoteTags from "../Decorate/VoteTags";

  export default {
    name: "BookCatalogs",
    components: {VoteTags, RandomAdmt, IEasyComment},
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
          this.isFavorite(this.bookInfo.id,"book_collect");
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
  .bookName {
    cursor: pointer;
    color: #474747;
    font-size: 17px;
  }
</style>
