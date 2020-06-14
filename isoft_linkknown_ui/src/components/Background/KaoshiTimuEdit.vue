<template>
  <div>
    <Form ref="formInline" :model="formInline" :rules="ruleInline" :label-width="100">
      <Tabs value="name0">
        <TabPane label="题目" name="name0">
          <FormItem prop="timu_question" label="题目内容">
            <Input type="textarea" :rows="10" v-model.trim="formInline.timu_question" placeholder="timu_question"></Input>
          </FormItem>
        </TabPane>
        <TabPane label="选项A" name="name1">
          <FormItem prop="timu_answer_a" label="选项A">
            <Input type="textarea" :rows="10" v-model.trim="formInline.timu_answer_a" placeholder="timu_answer_a"></Input>
          </FormItem>
        </TabPane>
        <TabPane label="选项B" name="name2">
          <FormItem prop="timu_answer_b" label="选项B">
            <Input type="textarea" :rows="10" v-model.trim="formInline.timu_answer_b" placeholder="timu_answer_b"></Input>
          </FormItem>
        </TabPane>
        <TabPane label="选项C" name="name3">
          <FormItem prop="timu_answer_c" label="选项C">
            <Input type="textarea" :rows="10" v-model.trim="formInline.timu_answer_c" placeholder="timu_answer_c"></Input>
          </FormItem>
        </TabPane>
        <TabPane label="选项D" name="name4">

          <FormItem prop="timu_answer_d" label="选项D">
            <Input type="textarea" :rows="10" v-model.trim="formInline.timu_answer_d" placeholder="timu_answer_d"></Input>
          </FormItem>
        </TabPane>
        <TabPane label="选项E" name="name5">
          <FormItem prop="timu_answer_e" label="选项E">
            <Input type="textarea" :rows="10" v-model.trim="formInline.timu_answer_e" placeholder="timu_answer_e"></Input>
          </FormItem>
        </TabPane>
        <TabPane label="选项F" name="name6">
          <FormItem prop="timu_answer_f" label="选项F">
            <Input type="textarea" :rows="10" v-model.trim="formInline.timu_answer_f" placeholder="timu_answer_f"></Input>
          </FormItem>
        </TabPane>
      </Tabs>
      <FormItem prop="timu_answer" label="正确答案">
        <Select v-model="formInline.timu_answer">
          <Option value="A" key="A">A</Option>
          <Option value="B" key="B">B</Option>
          <Option value="C" key="C">C</Option>
          <Option value="D" key="D">D</Option>
          <Option value="E" key="E">E</Option>
          <Option value="F" key="F">F</Option>
        </Select>
      </FormItem>
      <FormItem prop="timu_score" label="题目分数">
        <Tag :color="current_timu_score === 1 ? 'success': 'default'"><span @click="current_timu_score = 1">1</span></Tag>
        <Tag :color="current_timu_score === 2 ? 'success': 'default'"><span @click="current_timu_score = 2">2</span></Tag>
        <Tag :color="current_timu_score === 3 ? 'success': 'default'"><span @click="current_timu_score = 3">3</span></Tag>
        <Tag :color="current_timu_score === 5 ? 'success': 'default'"><span @click="current_timu_score = 5">5</span></Tag>
        <Tag :color="current_timu_score === 10 ? 'success': 'default'"><span @click="current_timu_score = 10">10</span></Tag>
      </FormItem>
      <FormItem prop="kaoshi_classify" label="题目分类">
        <Tag v-for="(kaoshi_classify, index) in kaoshi_classifys"
             :color="current_kaoshi_classify_ids.indexOf(kaoshi_classify.id) >= 0 ? 'success': 'default'">
          <span @click="chooseKaoshiClassify(kaoshi_classify)">{{kaoshi_classify.classify_name}}</span>
        </Tag>
      </FormItem>
      <FormItem>
        <Button type="success" @click="handleSubmit('formInline')" :style="{'float':'left'}">确认</Button>
      </FormItem>
    </Form>
  </div>
</template>

<script>
  import {QueryAllKaoshiClassify, EditKaoshiTimu, QueryKaoshiTimuById} from "../../api"

  export default {
    name: "KaoshiTimuEdit",
    data () {
      return {
        // 当前选择的分数
        current_timu_score:-1,
        // 当前选择的分类 id 列表
        current_kaoshi_classify_ids:[],
        // 考试分类
        kaoshi_classifys:[],
        visible: false,
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
          // 至少校验一个选项
          timu_answer_a: [
            { required: true, message: '请输入选项A', trigger: 'blur' },
          ],
          timu_answer: [
            { required: true, message: '请选择正确选项', trigger: 'blur' },
          ],
        },
      }
    },
    methods:{
      // 选择考试分类,一共可选择多个分类
      chooseKaoshiClassify (kaoshi_classify) {
        // 没有包含择添加
        if (this.current_kaoshi_classify_ids.indexOf(kaoshi_classify.id) === -1) {
          this.current_kaoshi_classify_ids.push(kaoshi_classify.id);
        } else {
          // 包含择去除
          this.current_kaoshi_classify_ids.splice(this.current_kaoshi_classify_ids.indexOf(kaoshi_classify.id), 1);
        }
      },
      handleSubmit(name) {
        if (this.current_timu_score <= 0) {
          this.$Message.error('请选择题目分数');
          return;
        }
        if (this.current_kaoshi_classify_ids.length === 0) {
          this.$Message.error('请选择题目分类');
          return;
        }

        this.formInline.timu_score = this.current_timu_score;
        this.formInline.kaoshi_classify = this.current_kaoshi_classify_ids.join(",");

        this.$refs[name].validate(async (valid) => {
          if (valid) {
            const result = await EditKaoshiTimu(this.formInline);
            if (result.status === "SUCCESS") {
              this.$Message.success('提交成功!');
              this.$refs[name].resetFields();
              this.$router.push({path:'/background/kaoshiTimu'});
            }else{
              this.$Message.error('提交失败!');
            }
          } else {
            this.$Message.error('校验失败!');
          }
        })
      },
      refreshAllKaoshiClassify: async function () {
        const result = await QueryAllKaoshiClassify();
        if (result.status === "SUCCESS"){
          this.kaoshi_classifys = result.kaoshi_classifys;
        }
      },
      refreshKaoshiTimu: async function (timuId) {
        const result = await QueryKaoshiTimuById({id:timuId});
        if (result.status === "SUCCESS"){
          this.formInline = result.kaoshi_timu;
          this.current_timu_score = result.kaoshi_timu.timu_score;
          this.current_kaoshi_classify_ids = result.kaoshi_timu_classifys.map(classify => classify.classify_id);
        }
      }
    },
    mounted(){
      if (this.$route.query.timuId > 0) {
        this.refreshKaoshiTimu(this.$route.query.timuId);
      }
      this.refreshAllKaoshiClassify();
    }
  }
</script>

<style scoped>

</style>
