<template>
  <ISimpleConfirmModal ref="bookEditModal" modal-title="新增/编辑 Book" :modal-width="800" :footer-hide="true">
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
  </ISimpleConfirmModal>
</template>

<script>
  import ISimpleConfirmModal from "../Common/modal/ISimpleConfirmModal"
  import {BookEdit} from "../../api"
  import {copyObj} from "../../tools"

  export default {
    name: "BookInfoEdit",
    components: {ISimpleConfirmModal},
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
        formValidate: {
          id: 0,     // 图书 id,为 0 是表示新增图书
          book_name: '',
          book_desc: '',
        },
        ruleValidate: {
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
      initFormData: function (book) {
        this.formValidate = copyObj(book);    // 拷贝对象,操作新对象不影响原对象
      },
      handleSubmit(name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
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

</style>
