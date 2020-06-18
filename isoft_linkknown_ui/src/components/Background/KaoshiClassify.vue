<template>
  <div>
    <Button type="success" size="small" style="margin: 10px 0;" @click="addClassify">新增分类</Button>
    <Modal
      v-model="showClassifyEdit"
      title="新增分类"
      :mask-closable="false"
      :footer-hide="true">
      <Form ref="formInline" :model="formInline" :rules="ruleInline" :label-width="100">
        <FormItem prop="classify_name" label="分类名称">
          <Input type="text" v-model.trim="formInline.classify_name" placeholder="classify_name"></Input>
        </FormItem>
        <FormItem prop="classify_desc" label="分类描述">
          <Input type="textarea" :rows="5" v-model.trim="formInline.classify_desc" placeholder="classify_desc"></Input>
        </FormItem>
        <FormItem prop="classify_image" label="分类图片">
          <Input v-model.trim="formInline.classify_image" placeholder="请上传分类图片"
                 readonly="readonly" @on-focus="editKaoshiClassifyImage"></Input>
          <IFileUpload ref="fileUpload" size="small" :show-button="false" :auto-hide-modal="true" :multiple="false" :format="['jpg','png','gif']"
                       @uploadComplete="uploadComplete" :action="fileUploadUrl" uploadLabel="上传分类图片"/>
        </FormItem>
        <FormItem>
          <Button type="success" @click="handleSubmit('formInline')">确认</Button>
        </FormItem>
      </Form>
    </Modal>
    <Table stripe :columns="columns1" :data="kaoshi_classifys"></Table>
    <Page :total="total" :page-size="offset" show-total show-sizer :styles="{'text-align': 'center','margin-top': '10px'}"
          @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
  </div>
</template>

<script>
  import {QueryPageKaoshiClassify,EditKaoshiClassify, fileUploadUrl} from "../../api"
  import IFileUpload from "../Common/file/IFileUpload";
  import {handleSpecial, copyObj} from "../../tools";

  export default {
    name: "KaoshiClassify",
    components:{IFileUpload},
    data (){
      return {
        fileUploadUrl: fileUploadUrl + "?table_name=kaoshi_classify&table_field=classify_image",
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 10,
        // 考试分类表
        kaoshi_classifys:[],
        columns1: [
          {
            title: '分类名称',
            key: 'classify_name'
          },
          {
            title: '分类描述',
            key: 'classify_desc'
          },
          {
            title: '操作',
            key: 'operate',
            width: 100,
            render: (h, params) => {
              return h('div', [
                h('Button', {
                  props: {
                    type: 'info',
                    size: 'small'
                  },
                  style: {
                    marginRight: '5px',
                  },
                  on: {
                    click: () => {
                      this.formInline = copyObj(this.kaoshi_classifys[params.index]);
                      this.showClassifyEdit = true;
                    }
                  }
                }, '编辑'),
              ])
            }
          }
        ],
        showClassifyEdit:false,
        formInline: {
          id: 0,
          classify_name: '',
          classify_desc: ''
        },
        ruleInline: {
          classify_name: [
            { required: true, message: '请输入分类名称', trigger: 'blur' },
          ],
          classify_desc: [
            { required: true, message: '请输入分类描述', trigger: 'blur' },
          ]
        }
      }
    },
    methods:{
      addClassify : function () {
        this.formInline.id = 0;
        this.$refs['formInline'].resetFields();
        this.showClassifyEdit = true;
      },
      editKaoshiClassifyImage: function () {
        this.$refs.fileUpload.showModal();
      },
      uploadComplete: async function (data) {
        if (data.status === "SUCCESS") {
          let uploadFilePath = data.fileServerPath;     // uploadFilePath 使用 hash 值时含有特殊字符 + 等需要转义
          let filename = data.fileName;      // 上传文件名称
          this.$set(this.formInline, 'classify_image', handleSpecial(uploadFilePath));
        }
      },
      handleSubmit(name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            const result = await EditKaoshiClassify(this.formInline);
            if (result.status === "SUCCESS") {
              this.$Message.success('提交成功!');
              this.$refs[name].resetFields();
              this.showClassifyEdit = false;
              this.refreshKaoshiClassify();
            }else{
              this.$Message.error('提交失败!');
            }
          } else {
            this.$Message.error('校验失败!');
          }
        })
      },
      handleChange(page){
        this.current_page = page;
        this.refreshKaoshiClassify();
      },
      handlePageSizeChange(pageSize){
        this.offset = pageSize;
        this.refreshKaoshiClassify();
      },
      refreshKaoshiClassify: async function () {
        const result = await QueryPageKaoshiClassify({"current_page":this.current_page, "offset":this.offset});
        if (result.status === "SUCCESS"){
          this.kaoshi_classifys = result.kaoshi_classifys;
          this.total = result.paginator.totalcount;
        }
      }
    },
    mounted (){
      this.refreshKaoshiClassify();
    }
  }
</script>

<style scoped>

</style>
