<template>
  <div style="margin: 20px;">
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="70">
      <FormItem label="分类名称" prop="catalog_name">
        <Input v-model="formValidate.catalog_name" maxlength="50" show-word-limit placeholder="Enter catalog name..."/>
      </FormItem>
      <FormItem label="分类简介" prop="catalog_desc">
        <Input v-model="formValidate.catalog_desc" maxlength="200" show-word-limit type="textarea" :rows="4"
               placeholder="Enter catalog desc..."></Input>
      </FormItem>
      <FormItem>
        <Button type="success" size="small" @click="handleSubmit('formValidate')">提交</Button>
      </FormItem>
    </Form>
  </div>
</template>

<script>
  import {BlogCatalogEdit} from "../../api"

  export default {
    name: "BlogCatalogEdit",
    data() {
      const checkCatalogName = (rule,value,callback) => {
        if (value === '') {
          callback(new Error('博客分类不能为空！'));
        }else if (value.indexOf("官方")!=-1){
          callback(new Error('分类名称不能含有 ‘官方’ 二字 ！'));
        }else {
          callback();
        }
      };
      return {
        formValidate: {
          catalog_name: '',
          catalog_desc: '',
        },
        ruleValidate: {
          catalog_name: [
            {required: true, validator:checkCatalogName, trigger: 'blur'}
          ],
          catalog_desc: [
            {required: true, message: '分类描述不能为空', trigger: 'blur'}
          ],
        },
      }
    },
    methods: {
      handleSubmit(name) {
        var _this = this;
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            const result = await BlogCatalogEdit(_this.formValidate);
            if (result.status == "SUCCESS") {
              _this.$Message.success('提交成功!');
              this.$emit("handleSuccess");
            } else {
              _this.$Message.error('提交失败!');
            }
          } else {
            _this.$Message.error('验证失败!');
          }
        })
      },
      handleReset(name) {
        this.$refs[name].resetFields();
      }
    }
  }
</script>

<style scoped>

</style>
