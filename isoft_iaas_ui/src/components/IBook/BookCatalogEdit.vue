<template>
  <Row :gutter="10">
    <Col span="6">
      <div style="background-color: #fff;border: 1px solid #e6e6e6;padding: 20px;min-height: 500px;">
        <Button size="small" @click="editBookCatalog" long>新建目录</Button>

        <ISimpleConfirmModal ref="bookCatalogEditModal" modal-title="新增/编辑 目录" :modal-width="600" :footer-hide="true">
          <div style="margin: 15px 30px;">
            <p>目录命名规范示例：1-一级目录</p>
            <p>目录命名规范示例：1.1-二级目录</p>
            <p>目录命名规范示例：1.1.1-三级目录</p>
            <p>目录不能超过三级,目录级别不能以 0 开头</p>
          </div>
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
            <span style="font-size: 12px;color: green;">提示：双击目录可以进行编辑</span>
            <dl>
              <dt><span style="color: green;font-weight: bold;">{{$route.query.book_name}}</span></dt>
              <dd class="isoft_font isoft_inline_ellipsis" style="color: #333333;" v-for="bookCatalog in bookCatalogs">
                &nbsp;&nbsp;&nbsp;<Icon type="ios-paper-outline"/>
                <span class="isoft_hover_red" @click="editBookArticle(bookCatalog.id)"
                      @dblclick="editBookCatalog(bookCatalog.id)">
                  <span style="color: rgba(0,128,0,0.4);">{{bookCatalog.grades}}</span> &nbsp;&nbsp;&nbsp;
                  {{bookCatalog.catalog_name | filterCatalogName}}
                </span>
              </dd>
            </dl>
          </div>
          <div v-else>
            暂未创建目录,直接在右边创建奥
          </div>
        </div>
      </div>
    </Col>
    <Col span="18">
      <BookArticleEdit ref="bookArticleEdit" :success-emit="true" @successEmitFunc="refreshBookCatalogList"/>
    </Col>
  </Row>
</template>

<script>
  import BookArticleEdit from "./BookArticleEdit";
  import {BookCatalogEdit, BookCatalogList, ShowBookCatalogDetail} from "../../api";
  import IBeautifulCard from "../Common/card/IBeautifulCard"
  import IBeautifulLink from "../Common/link/IBeautifulLink"
  import ISimpleConfirmModal from "../Common/modal/ISimpleConfirmModal";
  import {strSplit, validatePatternForString} from "../../tools";

  export default {
    name: "BookCatalogEdit",
    components: {ISimpleConfirmModal, IBeautifulCard,IBeautifulLink,BookArticleEdit},
    data(){
      const _validateCatalogName = (rule, value, callback) => {
        if (!validatePatternForString(/^[1-9][0-9]{0,}(\.[1-9][0-9]{0,}){0,2}-.*$/, value)) {
          callback(new Error('目录名称命名不符合规范!'));
        } else {
          callback();
        }
      };
      return {
        timer: null,
        bookCatalogs:[],
        formValidate: {
          id:-1,
          catalogName: '',
        },
        ruleValidate: {
          catalogName: [
            {required: true, message: '目录名称不能为空!', trigger: 'blur'},
            {validator: _validateCatalogName, trigger: 'blur'}
          ],
        },
      }
    },
    methods:{
      handleSubmit (name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            // 分割得到所有的目录
            let grades = this.formValidate.catalogName.slice(0, this.formValidate.catalogName.indexOf("-"));
            let gradeArr = strSplit(grades, ".");
            var grade_1 = parseInt(gradeArr[0]);    // 一级目录
            var grade_2 = 0;                            // 二级目录
            var grade_3 = 0;                            // 三级目录
            if (gradeArr.length > 1) {
              grade_2 = parseInt(gradeArr[1]);
            }
            if (gradeArr.length > 2) {
              grade_3 = parseInt(gradeArr[2]);
            }
            const result = await BookCatalogEdit({
              book_id: parseInt(this.$route.query.book_id),
              id: this.formValidate.id,
              catalog_name: this.formValidate.catalogName,
              grades: grades,
              grade_1: grade_1,
              grade_2: grade_2,
              grade_3: grade_3,
            });
            if(result.status == "SUCCESS"){
              this.$Message.success("编辑成功!");
              this.$refs.bookCatalogEditModal.hideModal();
              this.refreshBookCatalogList();
              this.handleReset('formValidate');
            }
          }
        })
      },
      handleReset (name) {
        this.$refs[name].resetFields();
      },
      // vue中对同一元素添加单击和双击事件,并解决之间的冲突
      // 把单击事件写在延时器里面晚一点执行,并如果是双击的话清除这个延时器也就清除了单击事件
      editBookArticle: function (bookCatalogId) {
        var _this = this;
        var timers = this.timer;
        if (timers) {
          window.clearTimeout(timers);
          this.timer = null;
        }
        this.timer = window.setTimeout(function () {
          _this.$refs.bookArticleEdit.refreshBookArticleDetail(bookCatalogId);
        }, 300);
      },
      editBookCatalog: async function(bookCatalogId){
        var timers = this.timer;
        if (timers) {
          window.clearTimeout(timers);
          this.timer = null;
        }
        if(bookCatalogId > 0){
          const result = await ShowBookCatalogDetail(bookCatalogId);
          if(result.status == "SUCCESS"){
            this.formValidate.id = result.bookCatalog.id;
            this.formValidate.catalogName = result.bookCatalog.catalog_name;
            this.$refs.bookCatalogEditModal.showModal();
          }
        }else{
          this.$refs.bookCatalogEditModal.showModal();
        }

      },
      refreshBookCatalogList:async function(){
        const result = await BookCatalogList(this.$route.query.book_id);
        if(result.status == "SUCCESS"){
          this.bookCatalogs = result.bookCatalogs;
        }
      },
    },
    computed:{
      _book_id:function () {
        return parseInt(this.$route.query.book_id);
      },
    },
    mounted(){
      this.refreshBookCatalogList();
    },
    filters: {
      filterCatalogName: function (value) {
        return value.slice(value.indexOf("-") + 1);
      },
    }
  }
</script>

<style scoped>

</style>
