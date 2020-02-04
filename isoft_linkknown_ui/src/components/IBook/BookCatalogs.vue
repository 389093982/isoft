<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <div>
        <a>首页</a>
        <a>图书</a>
      </div>
      <Row v-if="bookInfo" style="min-height: 400px;">
        <Col span="4" style="text-align: center;">
          <img :src="bookInfo.book_img" style="cursor: pointer;" height="160px" width="140px" @error="defImg()"/>

          <p class="isoft_font">推荐图书</p>
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
                <div>{{bookInfo.created_by}}（作者）</div>
              </Col>
              <Col span="8">
                <div>阅读xx 评论xx</div>
              </Col>
            </Row>
          </div>

          <div style="padding: 10px 0;">
            <Button size="small" style="float: right;"
                    @click="$router.push({path:'/ibook/book_detail', query:{book_id: $route.query.book_id}})">在线阅读
            </Button>

            <div style="border-bottom: 1px solid #d7dde4;padding-bottom: 10px;">
              <h3 style="margin:10px 0;">简介</h3>
              <p style="color: #333333;">{{bookInfo.book_desc}}</p>
            </div>

            <div style="padding: 10px 0;">
              <h3 style="margin:10px 0;">目录</h3>
              <p v-for="(bookCatalog, index) in bookCatalogs" style="color: #333333;font-size: 15px;">
                {{bookCatalog.catalog_name}}</p>
            </div>
          </div>
        </Col>
      </Row>
    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_top10">
      <!-- 评论模块 -->
      <IEasyComment :theme_pk="bookInfo.id" theme_type="bookInfo_theme_type" style="margin-top: 50px;"/>
    </div>
  </div>
</template>

<script>
  import {BookCatalogList} from "../../api"
  import IEasyComment from "../Comment/IEasyComment"

  export default {
    name: "BookCatalogs",
    components: {IEasyComment},
    data() {
      return {
        bookInfo: null,
        bookCatalogs: [],
        defaultImg: require('../../assets/default.png'),
      }
    },
    methods: {
      defImg() {
        let img = event.srcElement;
        img.src = this.defaultImg;
        img.onerror = null; //防止闪图
      },
      refreshBookCatalogList: async function (book_id) {
        const result = await BookCatalogList({book_id: book_id});
        if (result.status == "SUCCESS") {
          this.bookInfo = result.bookInfo;
          this.bookCatalogs = result.bookCatalogs;
        }
      },
    },
    mounted() {
      if (this.$route.query.book_id != undefined && this.$route.query.book_id != null) {
        this.refreshBookCatalogList(this.$route.query.book_id);
      }
    }
  }
</script>

<style scoped>

</style>
