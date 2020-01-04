<template>
  <div>
    <Row>
      <Col span="18" style="padding: 0 8px 0 0;">
        <div class="isoft_bg_white isoft_pd10">
          <!-- 内外边距：上右下左 -->
          <Row style="padding: 15px 10px 10px 25px;border-bottom: 1px solid #e6e6e6;height: 62px;">
            <Col span="2">
              <IBeautifulLink @onclick="refreshBookList('_all')">全部书单</IBeautifulLink>
            </Col>
            <Col span="2">
              <IBeautifulLink @onclick="refreshBookList('_hot')">热门书单</IBeautifulLink>
            </Col>
            <Col span="2">
              <IBeautifulLink @onclick="refreshBookList('mine')">我的书单</IBeautifulLink>
            </Col>
            <Col span="2">
              <IBeautifulLink @onclick="showBookEditModal">新增书单</IBeautifulLink>
            </Col>
          </Row>

          <div style="min-height: 450px;">
            <Row v-for="book in books" style="border-bottom: 1px solid #d7dde4;padding: 20px;" :gutter="20">
              <Col span="18">
                <div class="bookName" @click="$router.push({path:'/iblog/book_detail',query:{book_id:book.id}})">
                  {{book.book_name}}
                </div>
                <div>
                  作者：{{book.created_by}}
                  创建时间：
                  <Time :time="book.created_time" type="date"/>
                  修改时间：
                  <Time :time="book.last_updated_time" type="date"/>
                  修改次数：???
                </div>
                <div style="font-size: 14px;color: #333333;">
                  {{book.book_desc}}
                </div>
                <div v-if="mine" style="margin: 10px;float: right;">
                  <IFileUpload size="small" :auto-hide-modal="true"
                               :extra-data="book.id" @uploadComplete="uploadComplete"
                               action="/api/iwork/httpservice/fileUpload" uploadLabel="换张图片"/>
                  <IBeautifulLink @onclick="deleteBook(book.id)">删除</IBeautifulLink>
                  <IBeautifulLink @onclick="showBookEditModal2(book)">修改信息</IBeautifulLink>
                  <IBeautifulLink @onclick="$router.push({path:'/iblog/book_edit',
                                 query:{book_id:book.id,book_name:book.book_name}})">编辑
                  </IBeautifulLink>
                  <IBeautifulLink @onclick="refreshBookList('mine')">
                    我的书单
                  </IBeautifulLink>
                </div>

              </Col>
              <Col span="4">
                <div class="bookImg">
                  <router-link :to="{path:'/iblog/book_detail',query:{book_id:book.id}}">
                    <img v-if="book.book_img" :src="book.book_img" height="160px" width="140px"/>
                    <img v-else src="../../assets/default.png" height="160px" width="140px"/>
                    <p style="font-size: 12px;">{{book.book_name | filterLimitFunc}}</p>
                  </router-link>
                </div>
              </Col>
            </Row>
          </div>

          <ISimpleConfirmModal ref="bookEditModal" modal-title="新增/编辑 Book" :modal-width="600" :footer-hide="true">
            <IKeyValueForm ref="bookEditForm" form-key-label="图书名称" form-value-label="图书描述"
                           form-key-placeholder="请输入书名" form-value-placeholder="请输入描述"
                           @handleSubmit="editBook">
            </IKeyValueForm>
          </ISimpleConfirmModal>
        </div>
      </Col>
      <Col span="6" class="isoft_bg_white isoft_pd10">
        <HotUser/>
        <RandomAdmt2/>
      </Col>
    </Row>
  </div>
</template>

<script>
  import {BookEdit, BookList, DeleteBookById, UpdateBookIcon} from "../../api"
  import IBeautifulCard from "../Common/card/IBeautifulCard"
  import IKeyValueForm from "../Common/form/IKeyValueForm";
  import ISimpleConfirmModal from "../Common/modal/ISimpleConfirmModal"
  import IBeautifulLink from "../Common/link/IBeautifulLink"
  import IFileUpload from "../Common/file/IFileUpload"
  import HotUser from "../User/HotUser";
  import IndexCarousel from "../ILearning/IndexCarousel";
  import RandomAdmt2 from "../Advertisement/RandomAdmt2";
  import {GetLoginUserName} from "../../tools";

  export default {
    name: "BookList",
    components: {
      RandomAdmt2,
      IndexCarousel, HotUser, IBeautifulLink, IKeyValueForm, IBeautifulCard,ISimpleConfirmModal,IFileUpload},
    data(){
      return {
        books:[],
        mine:false,
      }
    },
    methods:{
      deleteBook:async function(book_id){
        const result = await DeleteBookById(book_id);
        if(result.status == "SUCCESS"){
          this.refreshBookList();
        }
      },
      uploadComplete: async function (data) {
        if(data.status == "SUCCESS"){
          if(data.status == "SUCCESS"){
            let uploadFilePath = data.fileServerPath;
            let bookId = data.extraData;
            const result = await UpdateBookIcon(bookId, uploadFilePath);
            if(result.status == "SUCCESS"){
              this.refreshBookList();
            }
          }
        }
      },
      showBookEditModal2:function (book) {
        this.$refs.bookEditModal.showModal();
        this.$refs.bookEditForm.initFormData(book.id, book.book_name, book.book_desc);
      },
      editBook:async function (book_id, book_name, book_desc) {
        const result = await BookEdit(book_id, book_name, book_desc);
        if(result.status == "SUCCESS"){
          this.$refs.bookEditModal.hideModal();
          this.$refs.bookEditForm.handleSubmitSuccess("提交成功!");
          this.refreshBookList();
        }else{
          this.$refs.bookEditForm.handleSubmitError("提交失败!");
        }
      },
      showBookEditModal:function () {
        this.$refs.bookEditForm.handleReset('formValidate');
        this.$refs.bookEditModal.showModal();
      },
      refreshBookList: async function (type) {
        var search_type = type;
        var search_user_name = '';
        if (type == 'mine') {
          search_user_name = GetLoginUserName();
          this.mine = 'mine';
        } else {
          this.mine = '';
        }

        const result = await BookList({
          search_type: search_type,
          search_user_name: search_user_name,
        });
        if(result.status == "SUCCESS"){
          this.books = result.books;
        }
      }
    },
    mounted(){
      this.refreshBookList('_all');
    },
    watch:{
      // 监听路由是否变化
      '$route':'refreshBookList'
    },
    filters:{
      // 内容超长则显示部分
      filterLimitFunc:function (value) {
        if(value && value.length > 15) {
          value= value.substring(0,15) + '...';
        }
        return value;
      },
    }
  }
</script>

<style scoped>
  /* 引入公共样式库 */

  .bookName {
    cursor: pointer;
    color: #474747;
    font-size: 19px;
    font-weight: 700;
  }

  .bookName:hover {
    color: #ffae02;
  }

  a{
    color: black;
  }
  .bookImg{
    padding: 10px 9px 0;
    width: 160px;
    border: 1px solid #FFFFFF;
    overflow: hidden;
    text-align: center;
    position: relative;
  }
  .bookImg:hover{
    background-color: #f4f4f4;
    border: 1px solid #d0cdd2;
  }
  li a:hover {
    color:red;
  }
</style>
