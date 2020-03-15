<template>
  <Row>
    <Col span="18">
      <div class="isoft_bg_white isoft_top10 isoft_pd20 isoft_mr10">

        <div class="isoft_info_tip" style="margin: 5px 0 40px 0;">每个用户每日可上传资料文件三次，请不要频繁的上传呢，^_^，在此感谢您无私的奉献！</div>

        <Form ref="formInline" :model="formInline" :rules="ruleValidate" :label-width="100">
          <FormItem label="分类名称" prop="resource_catalog">
            <Input v-model.trim="formInline.resource_catalog" placeholder="请您输入分类名称"></Input>
          </FormItem>
          <FormItem label="文件路径" prop="resource_path">
            <Input type="text" readonly="readonly" v-model="formInline.resource_path" placeholder="请您选择文件路径" @on-focus="handleFocus"/>
            <IFileUpload ref="fileUpload" :show-button="false" @uploadComplete="uploadComplete" :action="fileUploadUrl" uploadLabel="上传"/>
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
    </Col>
    <Col span="6" class="isoft_bg_white isoft_top10 isoft_pd10">
      <RandomAdmt/>
      <RandomAdmt/>
    </Col>
  </Row>

</template>

<script>
  import IFileUpload from "../Common/file/IFileUpload"
  import {EditResource, fileUploadUrl} from "../../api"
  import RandomAdmt from "../Advertisement/RandomAdmt";

  export default {
    name: "UploadResource",
    components: {RandomAdmt, IFileUpload},
    data() {
      const checkResourceCatalog = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('分类名称不能为空!'));
        } else if (value.length>10) {
          callback(new Error('分类名称请不要超过10个字符'));
        } else {
          callback();
        }
      };
      const checkResourceName = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('文件名称不能为空!'));
        } else if (value.length>30) {
          callback(new Error('文件名称请不要超过30个字符'));
        } else {
          callback();
        }
      };
      const checkResourceDesc = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('描述信息不能为空!'));
        } else if (value.length>50) {
          callback(new Error('描述信息请不要超过50个字符'));
        } else {
          callback();
        }
      };
      return {
        fileUploadUrl: fileUploadUrl + "?table_name=resource&table_field=resource_path",
        formInline: {
          resource_catalog: '',
          resource_name: '',
          resource_desc: '',
          resource_path: '',
        },
        ruleValidate: {
          resource_catalog: [
            {required: true, validator:checkResourceCatalog, trigger: 'change'},
          ],
          resource_path: [
            {required: true, message: '文件路径不能为空!', trigger: 'change'},
          ],
          resource_name: [
            {required: true, validator:checkResourceName, trigger: 'change'},
          ],
          resource_desc: [
            {required: true, validator:checkResourceDesc, trigger: 'change'},
          ],
        }
      }
    },
    methods: {
      handleFocus: function () {
        this.$refs.fileUpload.showModal();
      },
      uploadComplete: function (result) {
        if (result.status === "SUCCESS") {
          this.formInline.resource_name = result.fileName;
          this.formInline.resource_path = result.fileServerPath;
        }
      },
      handleSubmit: async function (name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            const result = await EditResource(this.formInline);
            if (result.status === "SUCCESS") {
              this.$router.push({path: '/resource/list'});
            } else {
              this.$Message.error(result.errorMsg);
            }
          }
        })
      }
    }
  }
</script>

<style scoped>

</style>
