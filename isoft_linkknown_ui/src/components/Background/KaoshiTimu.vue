<template>
  <div>
    <Button type="success" size="small" style="margin: 10px 0;" @click="showTimuEdit = true">新增题目</Button>
    <Modal
      v-model="showTimuEdit"
      width="1150"
      title="新增题目"
      :mask-closable="false"
      :footer-hide="true"
      :styles="{top: '20px'}">
      <Form ref="formInline" :model="formInline" :rules="ruleInline" :label-width="100">
        <Tabs value="name0">
          <TabPane label="题目" name="name0">
            <FormItem prop="timu_question" label="题目名称">
              <Input type="textarea" :rows="5" v-model.trim="formInline.timu_question" placeholder="timu_question"></Input>
            </FormItem>
          </TabPane>
          <TabPane label="选项a" name="name1">
            <FormItem prop="timu_answer_a" label="选项a">
              <Input type="textarea" :rows="5" v-model.trim="formInline.timu_answer_a" placeholder="timu_answer_a"></Input>
            </FormItem>
          </TabPane>
          <TabPane label="选项b" name="name2">
            <FormItem prop="timu_answer_b" label="选项b">
              <Input type="textarea" :rows="5" v-model.trim="formInline.timu_answer_b" placeholder="timu_answer_b"></Input>
            </FormItem>
          </TabPane>
          <TabPane label="选项c" name="name3">
            <FormItem prop="timu_answer_c" label="选项c">
              <Input type="textarea" :rows="5" v-model.trim="formInline.timu_answer_c" placeholder="timu_answer_c"></Input>
            </FormItem>
          </TabPane>
          <TabPane label="选项d" name="name4">

            <FormItem prop="timu_answer_d" label="选项d">
              <Input type="textarea" :rows="5" v-model.trim="formInline.timu_answer_d" placeholder="timu_answer_d"></Input>
            </FormItem>
          </TabPane>
          <TabPane label="选项e" name="name5">
            <FormItem prop="timu_answer_e" label="选项e">
              <Input type="textarea" :rows="5" v-model.trim="formInline.timu_answer_e" placeholder="timu_answer_e"></Input>
            </FormItem>
          </TabPane>
          <TabPane label="选项f" name="name6">
            <FormItem prop="timu_answer_f" label="选项f">
              <Input type="textarea" :rows="5" v-model.trim="formInline.timu_answer_f" placeholder="timu_answer_f"></Input>
            </FormItem>
          </TabPane>
        </Tabs>
        <FormItem prop="timu_answer" label="正确答案">
          <Select v-model="formInline.timu_answer">
            <Option value="a" key="a">a</Option>
            <Option value="b" key="b">b</Option>
            <Option value="c" key="c">c</Option>
            <Option value="d" key="d">d</Option>
            <Option value="e" key="e">e</Option>
            <Option value="f" key="f">f</Option>
          </Select>
        </FormItem>
        <FormItem prop="timu_score" label="题目分数">
          <Select v-model="formInline.timu_score">
            <Option v-for="(item,index) in 10" :value="item" :key="item">{{item}}</Option>
          </Select>
        </FormItem>
        <FormItem prop="kaoshi_classify" label="题目分类">
          <Select v-model="formInline.kaoshi_classify">
            <Option v-for="(kaoshi_classify,index) in kaoshi_classifys" :value="kaoshi_classify" :key="kaoshi_classify">{{kaoshi_classify.classify_name}}</Option>
          </Select>
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
  import {QueryPageKaoshiTimu, QueryAllKaoshiClassify, EditKaoshiTimu} from "../../api"

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
        // 考试分类
        kaoshi_classifys:[],
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
          kaoshi_classify:'',
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
      },
      refreshAllKaoshiClassify: async function () {
        const result = await QueryAllKaoshiClassify();
        if (result.status === "SUCCESS"){
          this.kaoshi_classifys = result.kaoshi_classifys;
        }
      }
    },
    mounted() {
      this.refreshKaoshiTimu();
      this.refreshAllKaoshiClassify();
    }
  }
</script>

<style scoped>

</style>
