<template>
  <div>
    <Alert closable type="error" style="cursor: pointer;color: red;text-align: center;">提示：编辑文章可以获得用户积分，还有概率赠送免费会员资格！
    </Alert>
    <Row :gutter="10" style="min-height: 500px;background-color: white">
      <Col span="6">
        <div style="background-color: #fff;padding: 20px;border: 1px rgba(140,137,135,0.29) solid;">
          <Button @click="addBookCatalog" long>新建文章</Button>
          <ISimpleConfirmModal ref="bookCatalogEditModal" modal-title="新增/编辑 文章标题" :modal-width="600" :footer-hide="true">
            <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100">
              <FormItem v-if="modalTarget==='edit'" label="序号" prop="catalogOrder">
                <Input type="number" v-model.trim="formValidate.catalogOrder" placeholder="请输入序号"></Input>
              </FormItem>
              <FormItem label="文章名称" prop="catalogName">
                <Input v-model.trim="formValidate.catalogName" placeholder="文章名称"></Input>
              </FormItem>
              <FormItem>
                <Button type="success" @click="handleSubmit('formValidate')" style="margin-right: 6px">提交</Button>
                <Button type="warning" @click="handleReset('formValidate')" style="margin-right: 6px">重置</Button>
              </FormItem>
            </Form>
          </ISimpleConfirmModal>

          <div style="margin-top: 5px;min-height: 480px;">
            <div v-if="bookCatalogs && bookCatalogs.length > 0">
              <dl>
                <dt><span style="color: green;font-weight: bold;">书名：{{$route.query.book_name}}</span></dt>
                <dd class="bookCatalogs isoft_font isoft_inline_ellipsis" style="color: #333333;" v-for="(bookCatalog, index) in bookCatalogs">
                  <span class="isoft_hover_red isoft_inline_ellipsis" @click="editBookArticle(bookCatalog.id)" style="padding-left: 10px;">
                    <span style="color: rgba(115,179,137,0.91);">{{bookCatalog.catalog_order}} -</span>
                    <span :style="{color: curEditCatalogId === bookCatalog.id ? 'red': ''}">{{bookCatalog.catalog_name}}</span>
                  </span>
                  <span class="bookCatalogIcon" style="position: absolute;right: -60px;z-index: 999;
                        padding: 3px 10px;background-color: #eee;border-radius: 5px;">
                    <Icon type="md-close" style="cursor: pointer" @click="deleteBookCatalog(bookCatalog)"/>
                    <Icon type="md-arrow-down" style="cursor: pointer" @click="toggleLocation(bookCatalog, 'down')"/>
                    <Icon type="md-arrow-up" style="cursor: pointer" @click="toggleLocation(bookCatalog, 'up')"/>
                    <Icon type="ios-brush-outline" style="cursor: pointer" @click="editBookCatalog(bookCatalog)"/>
                  </span>
                </dd>
              </dl>
            </div>
            <div v-else>
              暂未创建文章,点击上面按钮创建
            </div>
          </div>
        </div>
      </Col>
      <Col span="18">
        <BookArticleEdit ref="bookArticleEdit" :success-emit="true" @successEmitFunc="refreshBookCatalogListWithRefresh"/>
      </Col>
    </Row>
  </div>
</template>

<script>
  import BookArticleEdit from "./BookArticleEdit";
  import {
    BookCatalogEdit,
    BookCatalogList,
    ChangeCatalogOrder,
    DeleteBookCatalog,
    ShowBookCatalogDetail
  } from "../../api";
  import IBeautifulCard from "../Common/card/IBeautifulCard"
  import IBeautifulLink from "../Common/link/IBeautifulLink"
  import ISimpleConfirmModal from "../Common/modal/ISimpleConfirmModal";

  export default {
    name: "BookCatalogEdit",
    components: {ISimpleConfirmModal, IBeautifulCard, IBeautifulLink, BookArticleEdit},
    data() {
      const checkEditOrder = (rule, value, callback) => {
        if (value <= 0) {
          callback(new Error('序号从 1 开始!'));
        } else if (value > this.bookCatalogs.length) {
          callback(new Error('序号越界!'));
        } else {
          callback();
        }
      };
      return {
        curEditCatalogId: -1,
        tempOrgCatalogOrder:-1,
        modalTarget:'',
        bookCatalogs: [],
        formValidate: {
          id: -1,               // book_catalog表里的id
          catalogName: '',      //书本名称
          catalogOrder: -1,     // 当前编辑的文章索引
        },
        ruleValidate: {
          catalogOrder: [
            {required: true, validator:checkEditOrder, trigger: 'blur'},
          ],
          catalogName: [
            {required: true, message: '文章名称不能为空!', trigger: 'blur'},
          ],
        },
      }
    },
    methods: {
      toggleLocation: async function (bookCatalog,operate) {
        if ((bookCatalog.catalog_order === 1 && operate === "up") || (bookCatalog.catalog_order === this.bookCatalogs.length && operate === "down")) {
          return;
        }
        let catalog_order_base = bookCatalog.catalog_order;
        let catalog_order_target = operate === "up" ? bookCatalog.catalog_order-1 : bookCatalog.catalog_order+1;
        const result = await ChangeCatalogOrder({book_id:bookCatalog.book_id, catalog_order_base: catalog_order_base, catalog_order_target: catalog_order_target});
        if (result.status === "SUCCESS") {
          this.refreshBookCatalogList();
          this.$Message.success("移动成功!");
        } else {
          this.$Message.error("移动失败!");
        }
      },
      deleteBookCatalog: async function (bookCatalog) {
        const result = await DeleteBookCatalog({id: bookCatalog.id,book_id:bookCatalog.book_id,catalog_order:bookCatalog.catalog_order});
        if (result.status === "SUCCESS") {
          this.refreshBookCatalogList();
          this.$Message.success("删除成功!");
        } else {
          this.$Message.error("删除失败!");
        }
      },
      handleSubmit(name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            //修改名称
            const editResult = await BookCatalogEdit({
              book_id: parseInt(this.$route.query.book_id),
              id: this.formValidate.id,
              catalog_name: this.formValidate.catalogName,
            });

            //修改序号
            if (this.tempOrgCatalogOrder !== this.formValidate.catalogOrder) {
              const changeOrderResult = await ChangeCatalogOrder({book_id:parseInt(this.$route.query.book_id), catalog_order_base: this.tempOrgCatalogOrder, catalog_order_target: this.formValidate.catalogOrder});
            }

            if (editResult.status === "SUCCESS") {
              this.$Message.success("编辑成功!");
              this.$refs.bookCatalogEditModal.hideModal();
              this.refreshBookCatalogList();
              this.handleReset('formValidate');
            } else {
              this.$Message.error("编辑失败!");
            }
          }
        })
      },
      handleReset(name) {
        this.$refs[name].resetFields();
      },
      editBookArticle: function (bookCatalogId) {
        this.curEditCatalogId = bookCatalogId;
        this.catalogOrder = bookCatalogId.catalogOrder;
        this.$refs.bookArticleEdit.refreshBookArticleDetail(bookCatalogId);
      },
      addBookCatalog: async function (bookCatalogId) {
        this.handleReset('formValidate');
        this.modalTarget = 'add';
        this.$refs.bookCatalogEditModal.showModal();
      },
      editBookCatalog: async function (bookCatalog) {
        this.modalTarget = 'edit';
        this.formValidate.catalogOrder = bookCatalog.catalog_order;
        this.tempOrgCatalogOrder = bookCatalog.catalog_order;
        const result = await ShowBookCatalogDetail(bookCatalog.id);
        if (result.status == "SUCCESS") {
          this.formValidate.id = result.bookCatalog.id;
          this.formValidate.catalogName = result.bookCatalog.catalog_name;
          this.$refs.bookCatalogEditModal.showModal();
        }
      },
      refreshBookCatalogListWithRefresh: function () {
        // 刷新右侧文章
        this.editBookArticle(this.bookCatalogs[this.catalogOrder].id, this.catalogOrder);
        this.refreshBookCatalogList();
      },
      refreshBookCatalogList: async function () {
        const result = await BookCatalogList({book_id: this.$route.query.book_id});
        if (result.status === "SUCCESS") {
          this.bookCatalogs = result.bookCatalogs;
        }
      },
    },
    computed: {
      _book_id: function () {
        return parseInt(this.$route.query.book_id);
      },
    },
    mounted() {
      this.refreshBookCatalogList();
    },
  }
</script>

<style scoped>
  .bookCatalogs .bookCatalogIcon {
    display: none;
  }

  .bookCatalogs:hover .bookCatalogIcon {
    display: inline-block;
  }
</style>
