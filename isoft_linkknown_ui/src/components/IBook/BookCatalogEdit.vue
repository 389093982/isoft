<template>
  <div>
    <Alert closable type="error" style="cursor: pointer;color: red;">提示：单击目录可以编辑文章，双击目录可以编辑目录，编辑文章前请选择目录</Alert>
    <Row :gutter="10">
      <Col span="6">
        <div style="background-color: #fff;border: 1px solid #e6e6e6;padding: 20px;min-height: 500px;">
          <Button size="small" @click="editBookCatalog" long>新建目录</Button>

          <ISimpleConfirmModal ref="bookCatalogEditModal" modal-title="新增/编辑 目录" :modal-width="600" :footer-hide="true">
            <!-- 表单信息 -->
            <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100">
              <FormItem label="目录名称" prop="catalogName">
                <Input v-model.trim="formValidate.catalogName" placeholder="请输入目录名称"></Input>
              </FormItem>
              <FormItem>
                <Button type="success" @click="handleSubmit('formValidate')" style="margin-right: 6px">提交</Button>
                <Button type="warning" @click="handleReset('formValidate')" style="margin-right: 6px">重置</Button>
              </FormItem>
            </Form>
          </ISimpleConfirmModal>

          <div style="margin-top: 5px;min-height: 250px;">
            <div v-if="bookCatalogs && bookCatalogs.length > 0">
              <dl>
                <dt><span style="color: green;font-weight: bold;">书名：{{$route.query.book_name}}</span></dt>
                <dd class="isoft_font isoft_inline_ellipsis" style="color: #333333;"
                    v-for="(bookCatalog, index) in bookCatalogs">
                  <Icon type="md-close" style="cursor: pointer" @click="deleteBookCatalog(bookCatalog.id)"/>
                  <Icon type="md-arrow-down" style="cursor: pointer" @click="toggleLocation(index, 'down')"/>
                  <Icon type="md-arrow-up" style="cursor: pointer" @click="toggleLocation(index, 'up')"/>
                  <Icon type="ios-brush-outline" style="cursor: pointer" @click="editBookCatalog(bookCatalog.id)"/>
                  <span class="isoft_hover_red isoft_inline_ellipsis" @click="editBookArticle(bookCatalog.id)">
                    {{bookCatalog.catalog_name}}
                    <input style="width: 300px;" :value="bookCatalog.catalog_name"/>
                </span>
                </dd>
              </dl>
            </div>
            <div v-else>
              暂未创建目录,点击上面按钮创建
            </div>
          </div>
        </div>
      </Col>
      <Col span="18">
        <BookArticleEdit ref="bookArticleEdit" :success-emit="true" @successEmitFunc="refreshBookCatalogList"/>
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
      return {
        bookCatalogs: [],
        formValidate: {
          id: -1,
          catalogName: '',
        },
        ruleValidate: {
          catalogName: [
            {required: true, message: '目录名称不能为空!', trigger: 'blur'},
          ],
        },
      }
    },
    methods: {
      toggleLocation: async function (index, operate) {
        if ((index == 0 && operate == "up") || (index == this.bookCatalogs.length - 1 && operate == "down")) {
          return;
        }
        let catalog_id1 = this.bookCatalogs[index].id;
        let catalog_id2 = operate == "up" ? this.bookCatalogs[index - 1].id : this.bookCatalogs[index + 1].id;
        const result = await ChangeCatalogOrder({catalog_id1: catalog_id1, catalog_id2: catalog_id2});
        if (result.status == "SUCCESS") {
          this.refreshBookCatalogList();
          this.$Message.success("移动成功!");
        } else {
          this.$Message.error("移动失败!");
        }
      },
      deleteBookCatalog: async function (catalog_id) {
        const result = await DeleteBookCatalog({catalog_id: catalog_id});
        if (result.status == "SUCCESS") {
          this.refreshBookCatalogList();
          this.$Message.success("删除成功!");
        } else {
          this.$Message.error("删除失败!");
        }
      },
      handleSubmit(name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            const result = await BookCatalogEdit({
              book_id: parseInt(this.$route.query.book_id),
              id: this.formValidate.id,
              catalog_name: this.formValidate.catalogName,
            });
            if (result.status == "SUCCESS") {
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
        this.$refs.bookArticleEdit.refreshBookArticleDetail(bookCatalogId);
      },
      editBookCatalog: async function (bookCatalogId) {
        if (bookCatalogId > 0) {
          const result = await ShowBookCatalogDetail(bookCatalogId);
          if (result.status == "SUCCESS") {
            this.formValidate.id = result.bookCatalog.id;
            this.formValidate.catalogName = result.bookCatalog.catalog_name;
            this.$refs.bookCatalogEditModal.showModal();
          }
        } else {
          this.$refs.bookCatalogEditModal.showModal();
        }

      },
      refreshBookCatalogList: async function () {
        const result = await BookCatalogList({book_id: this.$route.query.book_id});
        if (result.status == "SUCCESS") {
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

</style>
