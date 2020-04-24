<template>
  <ISimpleConfirmModal ref="bookEditModal" modal-title="新增/编辑 Book" :modal-width="800" :footer-hide="true">
    <div style="display: flex;">
      <div class="img_box isoft_point_cursor" style="width: 18%;" @click="handleUpload">
        <div v-if="formValidate.book_img" style="position: relative;">
          <span class="animated faster fadeIn isoft_point_cursor isoft_close" @click="formValidate.book_img = ''"
            title="换张图片"><Icon type="md-close" size="20"/></span>
          <img :src="formValidate.book_img" style="width: 120px;height: 160px;"/>
        </div>
        <div v-else>
          <div class="isoft_add isoft_point_cursor"></div>
          <div class="isoft_hover_desc" style="margin:5px 0 0 10px;">点击上传/更新封面</div>
          <IFileUpload ref="fileUpload" class="isoft_mr10" size="small" :show-button="false" :auto-hide-modal="true" @uploadComplete="uploadComplete" :action="fileUploadUrl" uploadLabel="上传图书封面"/>
        </div>
      </div>
      <div style="width: 82%;">
        <!-- 表单信息 -->
        <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100">
          <FormItem label="图书名称" prop="book_name">
            <Input v-model.trim="formValidate.book_name" placeholder="请输入书名"></Input>
          </FormItem>
          <FormItem label="图书描述" prop="book_desc">
            <Input v-model.trim="formValidate.book_desc" type="textarea" :rows="4" placeholder="请输入描述"></Input>
          </FormItem>
          <FormItem>
            <Button type="success" @click="handleSubmit('formValidate')" style="margin-right: 6px">提交</Button>
            <Button type="warning" @click="handleReset('formValidate')" style="margin-right: 6px">重置</Button>
          </FormItem>
        </Form>
      </div>
    </div>
  </ISimpleConfirmModal>
</template>

<script>
  import ISimpleConfirmModal from "../Common/modal/ISimpleConfirmModal"
  import IFileUpload from "../Common/file/IFileUpload"
  import {BookEdit, fileUploadUrl} from "../../api"
  import {checkEmpty, copyObj} from "../../tools"

  export default {
    name: "BookInfoEdit",
    components: {ISimpleConfirmModal, IFileUpload},
    data() {
      const checkBookName = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('图书名称不能为空!'));
        } else if (value.length>15) {
          callback(new Error('图书名称不要超过15个字符哦'));
        } else {
          callback();
        }
      };
      const checkBookDesc = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('图书描述不能为空!'));
        } else if (value.length<20 || value.length>120) {
          callback(new Error('图书描述需20-120个字符'));
        } else {
          callback();
        }
      };
      return {
        fileUploadUrl: fileUploadUrl + "?table_name=book&table_field=book_img",
        formValidate: {
          id: 0,     // 图书 id,为 0 是表示新增图书
          book_img: '',
          book_name: '',
          book_desc: '',
        },
        ruleValidate: {
          book_img: [
            {required: true, validator:checkBookName, trigger: 'change'}
          ],
          book_name: [
            {required: true, validator:checkBookName, trigger: 'change'}
          ],
          book_desc: [
            {required: true, validator:checkBookDesc, trigger: 'change'}
          ],
        },
      }
    },
    methods: {
      handleUpload: function () {
        this.$refs.fileUpload.showModal();
      },
      uploadComplete: async function (data) {
        if (data.status === "SUCCESS") {
          if (data.status === "SUCCESS") {
            this.formValidate.book_img = data.fileServerPath;
          }
        }
      },
      initFormData: function (book) {
        this.formValidate = copyObj(book);    // 拷贝对象,操作新对象不影响原对象
      },
      handleSubmit(name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            if (checkEmpty(this.formValidate.book_img)) {
              this.$Message.warning("请先上传图书封面！");
              return;
            }
            const result = await BookEdit(this.formValidate);
            if (result.status === "SUCCESS") {
              this.hideModal();
              this.handleReset('formValidate');
              this.$emit("handleSubmit");
            } else {
              this.$Message.error("保存失败!");
            }
          }
        })
      },
      handleReset(name) {
        this.$refs[name].resetFields();
        this.formValidate.book_img = '';
      },
      showModal: function () {
        this.$refs.bookEditModal.showModal();
      },
      hideModal: function () {
        this.$refs.bookEditModal.hideModal();
      },
    }
  }
</script>

<style scoped>
  .isoft_add {
    border: 2px solid #afafaf;
    width: 120px;
    height: 160px;
    color: #ccc;
    position: relative;
  }
  .isoft_add::before{
    content: '';
    position: absolute;
    left: 50%;
    top: 50%;
    width: 60px;
    margin-left: -30px;
    margin-top: -5px;
    /*利用伪类before和其 border-top 来设置一个“横”：*/
    border-top: 10px solid;
  }
  .isoft_add::after {
    content: '';
    position: absolute;
    left: 50%;
    top: 50%;
    height: 60px;
    margin-left: -5px;
    margin-top: -30px;
    /*使用after伪类和 border-left 设置一个“竖”：*/
    border-left: 10px solid;
  }
  .isoft_add:hover {
    color: #a5a5a5;
  }


  .isoft_close {
    position: absolute;
    display: none;
    right: 2px;
    top: -12px;
    padding: 2px;
    color: #959595;
    background: rgba(248, 248, 248, 1);
    border: 1px solid #959595;
    border-radius: 50%;
    transition: transform 0.5s ease-in;
  }
  .isoft_close:hover {
    color: #000000;
    border: 1px solid #000000;
    transform: rotateZ(360deg);
  }

  .img_box:hover .isoft_close {
    display: block;
  }
</style>
