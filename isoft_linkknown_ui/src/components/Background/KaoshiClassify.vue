<template>
  <div>
    <Button type="success" size="small" style="margin: 10px 0;" @click="showClassifyEdit = true">新增分类</Button>
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
        <FormItem>
          <Button type="success" @click="handleSubmit('formInline')" :style="{'float':'right'}">确认</Button>
        </FormItem>
      </Form>
    </Modal>

    <Table stripe :columns="columns1" :data="kaoshi_classifys"></Table>
    <Page :total="total" :page-size="offset" show-total show-sizer :styles="{'text-align': 'center','margin-top': '10px'}"
          @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
  </div>
</template>

<script>
  import {QueryPageKaoshiClassify,EditKaoshiClassify} from "../../api"

  export default {
    name: "KaoshiClassify",
    data (){
      return {
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
