<template>
  <div>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100">
      <FormItem label="问题简述" prop="short_desc">
        <Input v-model.trim="formValidate.short_desc" placeholder="Enter short_desc..."></Input>
      </FormItem>
      <FormItem label="我的问题" prop="question">
        <Input type="textarea" :rows="10" v-model.trim="formValidate.question" placeholder="Enter question..."></Input>
      </FormItem>
      <FormItem>
        <Button type="success" @click="handleSubmit('formValidate')">提交</Button>
      </FormItem>
    </Form>
  </div>
</template>

<script>
  import {EditQuestion, ShowAskExpertDetail} from "../../api"

  export default {
    name: "EditQuestion",
    data() {
      return {
        formValidate: {
          id: -1,
          short_desc: '',
          question: '',
        },
        ruleValidate: {
          short_desc: [
            {required: true, message: 'short_desc 不能为空!', trigger: 'blur'}
          ],
          question: [
            {required: true, message: 'question 不能为空!', trigger: 'blur'}
          ],
        },
      }
    },
    methods: {
      handleSubmit(name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            const result = await EditQuestion(this.formValidate);
            if (result.status == "SUCCESS") {
              this.$router.push({path: '/communicate/ask_expert'});
            }
          }
        })
      },
      refreshQuestionDetail: async function (id) {
        const result = await ShowAskExpertDetail({id: id});
        if (result.status == "SUCCESS") {
          this.formValidate = result.ask_expert;
        }
      }
    },
    mounted() {
      if (this.$route.query.id > 0) {
        this.refreshQuestionDetail(this.$route.query.id);
      }
    }
  }
</script>

<style scoped>

</style>
