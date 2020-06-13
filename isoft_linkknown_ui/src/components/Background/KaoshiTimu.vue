<template>
  <div>
    <Button type="success" size="small" style="margin: 10px 0;" @click="showTimuEdit = true">新增分类</Button>
    <Modal
      v-model="showTimuEdit"
      width="1150"
      title="新增题目"
      :mask-closable="false"
      :footer-hide="true"
      :styles="{top: '20px'}">
      <Form ref="formInline" :model="formInline" :rules="ruleInline" :label-width="100">
        <FormItem prop="timu_question" label="题目名称">
          <Input type="textarea" :rows="5" v-model.trim="formInline.timu_question" placeholder="timu_question"></Input>
        </FormItem>
        <FormItem prop="timu_answer_a" label="选项a">
          <Input type="textarea" :rows="5" v-model.trim="formInline.timu_answer_a" placeholder="timu_answer_a"></Input>
        </FormItem>
        <FormItem prop="timu_answer_b" label="选项b">
          <Input type="textarea" :rows="5" v-model.trim="formInline.timu_answer_b" placeholder="timu_answer_b"></Input>
        </FormItem>
        <FormItem prop="timu_answer_c" label="选项c">
          <Input type="textarea" :rows="5" v-model.trim="formInline.timu_answer_c" placeholder="timu_answer_c"></Input>
        </FormItem>
        <FormItem prop="timu_answer_d" label="选项d">
          <Input type="textarea" :rows="5" v-model.trim="formInline.timu_answer_d" placeholder="timu_answer_d"></Input>
        </FormItem>
        <FormItem prop="timu_answer_e" label="选项e">
          <Input type="textarea" :rows="5" v-model.trim="formInline.timu_answer_e" placeholder="timu_answer_e"></Input>
        </FormItem>
        <FormItem prop="timu_answer_f" label="选项f">
          <Input type="textarea" :rows="5" v-model.trim="formInline.timu_answer_f" placeholder="timu_answer_f"></Input>
        </FormItem>
        <FormItem prop="timu_answer" label="正确答案">
          <Input v-model.trim="formInline.timu_answer" placeholder="timu_answer"></Input>
        </FormItem>
        <FormItem prop="timu_score" label="题目分数">
          <Input v-model.trim="formInline.timu_score" placeholder="timu_score"></Input>
        </FormItem>
        <FormItem>
          <Button type="success" @click="handleSubmit('formInline')" :style="{'float':'right'}">确认</Button>
        </FormItem>
      </Form>
    </Modal>

    <Table stripe :columns="columns1" :data="kaoshi_timus"></Table>
    <Page :total="total" :page-size="offset" show-total show-sizer :styles="{'text-align': 'center','margin-top': '10px'}"
          @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
  </div>
</template>

<script>
  import {QueryPageKaoshiTimu, EditKaoshiTimu} from "../../api"

  export default {
    name: "KaoshiTimu",
    data (){
      return {
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 10,
        // 考试题目
        kaoshi_timus:[],
        showTimuEdit:false,
        columns1: [
          {
            title: '题目名称',
            key: 'timu_question'
          },
        ],
        formInline: {
          id: 0,
          timu_question: '',
          timu_answer_a: '',
          timu_answer_b: '',
          timu_answer_c: '',
          timu_answer_d: '',
          timu_answer_e: '',
          timu_answer_f: '',
          timu_answer: '',
          timu_score: 0,
        },
        ruleInline: {
          timu_question: [
            { required: true, message: '请输入分类名称', trigger: 'blur' },
          ],
        }
      }
    },
    methods:{
      handleSubmit(name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            const result = await EditKaoshiTimu(this.formInline);
            if (result.status === "SUCCESS") {
              this.$Message.success('提交成功!');
              this.$refs[name].resetFields();
              this.showTimuEdit = false;
              this.refreshKaoshiTimu();
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
        this.refreshKaoshiTimu();
      },
      handlePageSizeChange(pageSize){
        this.offset = pageSize;
        this.refreshKaoshiTimu();
      },
      refreshKaoshiTimu: async function () {
        const result = await QueryPageKaoshiTimu({"current_page":this.current_page, "offset":this.offset});
        if (result.status === "SUCCESS"){
          this.kaoshi_timus = result.kaoshi_timus;
          this.total = result.paginator.totalcount;
        }
      }
    },
    mounted() {
      this.refreshKaoshiTimu();
    }
  }
</script>

<style scoped>

</style>
