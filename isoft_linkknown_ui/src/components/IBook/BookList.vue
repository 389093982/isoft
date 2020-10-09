<template>
  <div>
    <Row>
      <Col span="17" style="padding: 0 8px 0 0;">
        <div class="isoft_bg_white isoft_pd10">
          <!-- 内外边距：上右下左 -->
          <Row style="padding: 20px 8px 8px 25px;border-bottom: 1px solid #e6e6e6;height: 62px;">
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

          <div class="isoft_info_tip isoft_font12 isoft_color_grey isoft_point_cursor" @click="showBookEditModal">
            图书万千，一书难得，你有知识，我有平台。赶快发布书籍尝尝鲜吧，^_^
          </div>

          <div v-if="pattern === '_all'">
            <BookListCustomTags/>
            <SepLine/>
          </div>

          <div style="min-height: 450px;">
            <Row v-for="(book, index) in books" style="border-bottom: 1px solid #d7dde4;padding: 20px;" :gutter="20">
              <Col span="4" offset="2">
                <div class="bookImg isoft_hover_top5" :title="book.book_name">
                  <span @click="$router.push({path:'/ibook/bookCatalogs',query:{book_id:book.id}})">
                    <img v-if="book.book_img" :src="book.book_img" height="160px" width="140px"/>
                    <img v-else src="../../../static/images/common_img/default.png" height="160px" width="140px"/>
                    <p style="font-size: 12px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                      <span class="book_label">精品</span>
                      <span style="position: relative;">
                        <span class="book_label1">
                          <Icon type="ios-eye" style="color: #b4b4b4;font-size: 20px;"/> {{book.views}}
                          <Icon type="ios-chatboxes-outline" style="color: #b4b4b4;font-size: 20px;"/> {{book.comments}}
                        </span>
                        <span class="book_label2">{{book.book_name}}</span>
                      </span>
                    </p>
                  </span>

                </div>
              </Col>
              <Col span="12" offset="1" style="margin-top: 10px">
                <div class="bookName" @click="$router.push({path:'/ibook/bookCatalogs',query:{book_id:book.id}})">
                  << {{book.book_name}} >>
                </div>
                <div>
                  <Row>
                    <span style="margin-left: 10px;">
                      <span style="color: #777">作者:</span>
                      <span class="isoft_hover_desc" v-if="renderNickName(book.created_by)">{{renderNickName(book.created_by)}}</span>
                      <span class="isoft_hover_desc" v-else>{{book.created_by}}</span>
                    </span>
                  </Row>
                  <Row>
                    <span style="margin-left: 10px;">
                      <span style="color: #777">创建于:</span>
                      <span class="isoft_hover_desc"><Time :time="book.created_time" type="relative"/></span>
                    </span>
                  </Row>
                  <Row>
                    <span style="margin-left: 10px;">
                      <span style="color: #777">更新于:</span>
                      <span class="isoft_hover_desc"><Time :time="book.last_updated_time" type="relative"/></span>
                    </span>
                  </Row>
                </div>
                <div class="isoft_word_break" style="font-size: 14px;color: #7d7d7d;margin-left: 10px;">
                  <span style="color: #777">本书简介:</span>
                  <span class="isoft_hover_desc" style="font-size: 12px">{{book.book_desc}}</span>
                </div>
                <div v-if="isLoginUserName(book.created_by)" style="margin: 10px;float: right;">
                  <IFileUpload class="isoft_mr10" size="small" :auto-hide-modal="true" :extra-data="book" @uploadComplete="uploadComplete" :action="fileUploadUrl" uploadLabel="修改图书封面"/>
                  <Button size="small" @click="showDeleteModal(book.id)">删除</Button>
                  <Button size="small" @click="showBookEditModal2(book)">修改信息</Button>
                  <Button size="small" @click="$router.push({path:'/ibook/bookCatalogs',query:{book_id:book.id}})">编辑书本</Button>
                </div>
              </Col>
            </Row>

            <IsComfirmDelete ref="comfirmDelete" @confirmDelete="toDelBook()" content="书本一经删除，无法复原，确认删除?"></IsComfirmDelete>

            <Page :total="total" :page-size="offset" show-total show-sizer :styles="{'text-align': 'center','margin-top': '10px'}" @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
          </div>

          <BookInfoEdit ref="bookEditModal" @handleSubmit="refreshMyBookList"></BookInfoEdit>
        </div>
      </Col>
      <Col span="6">
        <HotUser/>
        <div style="margin-top: 5px">
          <LearningDiary/>
        </div>
        <WaitYourAnswer></WaitYourAnswer>
        <ExpertWall style="margin-top: 5px"></ExpertWall>
        <div style="margin-top: 5px">
          <img src="../../../static/images/common_img/linkknown_to_lovely_you.jpg" height="590" width=100%/>
        </div>
        <div class="isoft_bg_white isoft_pd10 isoft_top10">
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
  import {
    checkEmpty,
    checkFastClick,
    CheckHasLoginConfirmDialog2,
    GetLoginUserName,
    RenderNickName,
    RenderUserInfoByNames
  } from "../../tools";
  import BookInfoEdit from "./BookInfoEdit";
  import IsComfirmDelete from "../IBlog/IsComfirmDelete";
  import LearningDiary from "../ILearning/LearningDiary";
  import BookListCustomTags from "./BookListCustomTags";
  import SepLine from "../Common/SepLine";
  import WaitYourAnswer from "../Expert/WaitYourAnswer";
  import ExpertWall from "../Expert/ExpertWall";

  export default {
    name: "BookList",
    components: {
      ExpertWall,
      WaitYourAnswer,
      BookListCustomTags,
      LearningDiary,
      IsComfirmDelete,
      BookInfoEdit,
      SepLine,
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
        delBookId:'',
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
      showDeleteModal:function(book_id){
        this.delBookId = book_id;
        this.$refs.comfirmDelete.showModal();
      },
      toDelBook:function(){
        this.deleteBook(this.delBookId)
      },
      deleteBook: async function (book_id) {
        if (checkFastClick()) {
          this.$Message.error("点击过快,请稍后重试!");
          return;
        }
        let params = {
          'id':book_id,
        };
        const result = await DeleteBookById(params);
        if (result.status === "SUCCESS") {
          this.refreshBookList();
        }
      },
      uploadComplete: async function (data) {
        if (data.status === "SUCCESS") {
          if (data.status === "SUCCESS") {
            let params = {
              'book_id':data.extraData.id,
              'book_img':data.fileServerPath,
            };
            const result = await UpdateBookIcon(params);
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
          this.userInfos = await RenderUserInfoByNames(result.books, 'book_author');
          this.books = result.books;
          this.total = result.paginator.totalcount;

        }
      },
      renderNickName: function (user_name) {
        return RenderNickName(this.userInfos, user_name);
      },
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
    font-size: 17px;
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

  .isoft_hover_desc{
    color: #9b9896;
    cursor: pointer;
  }
  .isoft_hover_desc:hover{
    color: #777;
    cursor: pointer;
  }

  .book_label1 {
    position: absolute;
    visibility: hidden;
  }
  .bookImg:hover .book_label1 {
    visibility: visible;
  }
  .book_label2 {
    position: absolute;
    visibility: visible;
  }
  .bookImg:hover .book_label2 {
    visibility: hidden;
  }
</style>
