<template>
  <div>

    <Row>
      <Col span="18" style="padding: 0 8px 0 0;">
        <div class="isoft_bg_white isoft_pd10">
          <!-- 内外边距：上右下左 -->
          <Row style="padding: 15px 10px 10px 25px;border-bottom: 1px solid #e6e6e6;height: 62px;">
            <Col span="3" offset="6">
              <IBeautifulLink @onclick="refreshBookList('_all', '')" :style="{color: pattern === '_all' ? 'red': ''}">
                全部书单
              </IBeautifulLink>
            </Col>
            <Col span="3">
              <IBeautifulLink @onclick="refreshBookList('_hot', '')" :style="{color: pattern === '_hot' ? 'red': ''}">
                热门书单
              </IBeautifulLink>
            </Col>
            <Col span="3">
              <IBeautifulLink @onclick="refreshMyBookList" :style="{color: pattern === 'mine' ? 'red': ''}">我的书单
              </IBeautifulLink>
            </Col>
            <Col span="3">
              <IBeautifulLink @onclick="showBookEditModal">新增书单</IBeautifulLink>
            </Col>
          </Row>

          <div style="text-align: center;background-color: #eee;padding: 5px 10px;">图书万千，一书难得，你有知识，我有平台。赶快发布书籍尝尝鲜吧，^_^
          </div>

          <div style="min-height: 450px;">
            <Row v-for="book in books" style="border-bottom: 1px solid #d7dde4;padding: 20px;" :gutter="20">
              <Col span="18">
                <div class="bookName" @click="$router.push({path:'/ibook/book_catalogs',query:{book_id:book.id}})">
                  {{book.book_name}}
                </div>
                <div>
                  <span>作者：
                    <span v-if="renderNickName(book.created_by)">{{renderNickName(book.created_by)}}</span>
                    <span v-else>{{book.created_by}}</span>
                  </span>
                  <span style="margin-left: 10px;">
                    创建时间：<span style="color: red;"><Time :time="book.created_time" type="relative"/></span>
                  </span>
                  <span style="margin-left: 10px;">
                    最后更新于:<span style="color: red;margin-left: 10px;"><Time :time="book.last_updated_time"
                                                                            type="relative"/></span>
                  </span>
                </div>
                <div style="font-size: 14px;color: #7d7d7d;">
                  {{book.book_desc}}
                </div>
                <div v-if="isLoginUserName(book.created_by)" style="margin: 10px;float: right;">
                  <IFileUpload class="isoft_mr10" size="small" :auto-hide-modal="true"
                               :extra-data="book" @uploadComplete="uploadComplete"
                               :action="fileUploadUrl" uploadLabel="换张图片"/>
                  <IBeautifulLink class="isoft_mr10" @onclick="deleteBook(book.id)">删除</IBeautifulLink>
                  <IBeautifulLink class="isoft_mr10" @onclick="showBookEditModal2(book)">修改信息</IBeautifulLink>
                  <IBeautifulLink class="isoft_mr10" @onclick="$router.push({path:'/ibook/book_edit',
                                 query:{book_id:book.id,book_name:book.book_name}})">编辑文章
                  </IBeautifulLink>
                </div>

              </Col>
              <Col span="4">
                <div class="bookImg">
                  <router-link :to="{path:'/ibook/book_catalogs',query:{book_id:book.id}}">
                    <img v-if="book.book_img" :src="book.book_img" height="160px" width="140px"/>
                    <img v-else src="../../../static/images/404.jpg" height="160px" width="140px"/>
                    <p style="font-size: 12px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                      <span class="book_label">精品</span>
                      <span>{{book.book_name}}</span>
                    </p>
                  </router-link>
                </div>
              </Col>
            </Row>

            <Page :total="total" :page-size="offset" show-total show-sizer
                  :styles="{'text-align': 'center','margin-top': '10px'}"
                  @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
          </div>

          <BookInfoEdit ref="bookEditModal" @handleSubmit="refreshMyBookList"></BookInfoEdit>
        </div>
      </Col>
      <Col span="6">
        <div class="isoft_bg_white isoft_pd10">
          <HotUser/>
        </div>

        <div class="isoft_bg_white isoft_pd10 isoft_top10">
          <RandomAdmt/>
          <RandomAdmt/>
        </div>
      </Col>
    </Row>
  </div>
</template>

<script>
  import {DeleteBookById, fileUploadUrl, QueryPageBookList, UpdateBookIcon} from "../../api"
  import IBeautifulCard from "../Common/card/IBeautifulCard"
  import ISimpleConfirmModal from "../Common/modal/ISimpleConfirmModal"
  import IBeautifulLink from "../Common/link/IBeautifulLink"
  import IFileUpload from "../Common/file/IFileUpload"
  import HotUser from "../User/HotUser";
  import IndexCarousel from "../ILearning/IndexCarousel";
  import RandomAdmt from "../Advertisement/RandomAdmt";
  import {
    checkEmpty,
    checkFastClick,
    CheckHasLoginConfirmDialog2,
    GetLoginUserName,
    RenderNickName,
    renderUserInfoByNames
  } from "../../tools";
  import BookInfoEdit from "./BookInfoEdit";

  export default {
    name: "BookList",
    components: {
      BookInfoEdit,
      RandomAdmt,
      IndexCarousel, HotUser, IBeautifulLink, IBeautifulCard, ISimpleConfirmModal, IFileUpload
    },
    data() {
      return {
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 10,
        fileUploadUrl: fileUploadUrl + "?table_name=book&table_field=book_img",
        books: [],
        userInfos: [],
        pattern: '_all',
      }
    },
    methods: {
      isLoginUserName: function (user_name) {
        return user_name === GetLoginUserName();
      },
      handleChange(page) {
        this.current_page = page;
        this.refreshBookList();
      },
      handlePageSizeChange(pageSize) {
        this.offset = pageSize;
        this.refreshBookList();
      },
      deleteBook: async function (book_id) {
        if (checkFastClick()) {
          this.$Message.error("点击过快,请稍后重试!");
          return;
        }
        const result = await DeleteBookById(book_id);
        if (result.status === "SUCCESS") {
          this.refreshBookList();
        }
      },
      uploadComplete: async function (data) {
        if (data.status === "SUCCESS") {
          if (data.status === "SUCCESS") {
            let uploadFilePath = data.fileServerPath;
            let bookId = data.extraData.id;
            const result = await UpdateBookIcon(bookId, uploadFilePath);
            if (result.status === "SUCCESS") {
              this.refreshBookList();
            }
          }
        }
      },
      showBookEditModal2: function (book) {
        var _this = this;
        CheckHasLoginConfirmDialog2(this, function () {
          _this.$refs.bookEditModal.initFormData(book);
          _this.$refs.bookEditModal.showModal();
        });
      },
      showBookEditModal: function () {
        var _this = this;
        CheckHasLoginConfirmDialog2(this, function () {
          _this.$refs.bookEditModal.showModal();
        });
      },
      refreshMyBookList: function () {
        var _this = this;
        CheckHasLoginConfirmDialog2(this, function () {
          _this.refreshBookList('', GetLoginUserName());
          _this.pattern = 'mine';
        });
      },
      refreshBookList: async function (search_type, search_user_name) {
        this.pattern = search_type;
        const result = await QueryPageBookList({
          search_type: search_type,
          search_user_name: search_user_name,
          offset: this.offset,
          current_page: this.current_page,
        });
        if (result.status === "SUCCESS") {
          this.userInfos = await renderUserInfoByNames(result.books, 'book_author');
          this.books = result.books;
          this.total = result.paginator.totalcount;

        }
      },
      renderNickName: function (user_name) {
        return RenderNickName(this.userInfos, user_name);
      }
    },
    mounted() {
      let search_type = !checkEmpty(this.$route.query.type) ? this.$route.query.type : '_all';
      this.refreshBookList(search_type, '');
    },
    watch: {
      // 监听路由是否变化
      '$route': 'refreshBookList'
    },
  }
</script>

<style scoped>
  .bookName {
    cursor: pointer;
    color: #474747;
    font-size: 19px;
    font-weight: 700;
  }

  .bookName:hover {
    color: #ffae02;
  }

  a {
    color: black;
  }

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
    top: -10px;
  }

  li a:hover {
    color: red;
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
