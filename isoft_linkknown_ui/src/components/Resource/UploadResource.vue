<template>
  <div class="isoft_bg_white isoft_pd20">
    <Form ref="formInline" :model="formInline" :rules="ruleValidate" :label-width="100">
      <FormItem label="分类名称" prop="resource_type">
        <Input v-model.trim="formInline.resource_type" placeholder="请您选择分类名称"></Input>
      </FormItem>
      <FormItem label="文件路径" prop="resource_path">
        <Input type="text" readonly="readonly" v-model="formInline.resource_path" placeholder="请您选择文件路径"
               @on-focus="handleFocus"/>
        <IFileUpload ref="fileUpload" :show-button="false" @uploadComplete="uploadComplete"
                     :action="fileUploadUrl" uploadLabel="上传"/>
      </FormItem>
      <FormItem label="文件名称" prop="resource_name">
        <Input readonly="readonly" v-model.trim="formInline.resource_name" placeholder="请您选择文件名称"></Input>
      </FormItem>
      <FormItem label="描述信息" prop="resource_desc">
        <Input type="textarea" :rows="3" v-model.trim="formInline.resource_desc" placeholder="请您输入描述信息"></Input>
      </FormItem>
      <FormItem>
        <Button type="success" @click="handleSubmit('formInline')" style="margin-right: 6px">提交</Button>
      </FormItem>
    </Form>
  </div>
</template>

<script>
  import IFileUpload from "../Common/file/IFileUpload"
  import {EditResource, fileUploadUrl} from "../../api"

  export default {
    name: "UploadResource",
    components: {IFileUpload},
    data() {
      return {
        fileUploadUrl: fileUploadUrl,
        formInline: {
          id: -1,
          resource_type: '',
          resource_name: '',
          resource_desc: '',
          resource_path: '',
        },
        ruleValidate: {
          resource_type: [
            {required: true, message: '分类名称不能为空!', trigger: 'blur'},
          ],
          resource_name: [
            {required: true, message: '文件名称不能为空!', trigger: 'blur'},
          ],
          resource_desc: [
            {required: true, message: '描述信息不能为空!', trigger: 'blur'},
          ],
          resource_path: [
            {required: true, message: '文件路径不能为空!', trigger: 'blur'},
          ],
        }
      }
    },
    methods: {
      handleFocus: function () {
        this.$refs.fileUpload.showModal();
      },
      uploadComplete: function (result) {
        if (result.status == "SUCCESS") {
          this.formInline.resource_name = result.fileName;
          this.formInline.resource_path = result.fileServerPath;
        }
      },
      handleSubmit: async function (name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            const result = await EditResource({
              id: this.formInline.id,
              resource_name: this.formInline.resource_name,
              resource_desc: this.formInline.resource_desc,
              resource_path: this.formInline.resource_path,
            });
            if (result.status == "SUCCESS") {
              this.$router.push({path: '/resource/resourceList'});
            } else {
              this.$Message.error("保存失败!");
            }
          }
        })
      }
    }
  }
</script>

<style scoped>

</style>
