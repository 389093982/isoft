<template>
  <div>
    <div class="isoft_bg_white isoft_pd10 clear" v-if="ask_expert">
      <h4>{{ask_expert.short_desc}}</h4>
      <p>{{ask_expert.question}}</p>
      <div style="text-align: right;">
        <a @click="showEditanswer = !showEditanswer">我来回答</a>
        <a @click="$router.push({path:'/communicate/ask_expert'})">返回问题列表</a>
      </div>

      <div v-if="showEditanswer">
        <Input type="textarea" :rows="10" v-model.trim="answer"/>
        <Button size="small" style="float: right;margin: 10px 0;" type="success"
                @click="EditAnswerExpert">提交
        </Button>
      </div>
    </div>

    <div class="isoft_bg_white isoft_top10 isoft_pd10">
      <ul>
        <li v-for="(as, index) in answer_experts"
            style="list-style:none;padding: 10px 10px;background: #fff;border-bottom: 1px solid #f4f4f4;">
          <h4 style="color: red;">专家回答({{index+1}} 楼)</h4>{{as.answer}}
          <p>{{as.answer}}</p>
          <Row>
            <Col span="6"></Col>
            <Col span="6">
              <span class="isoft_font12">回答人：{{as.user_name}}</span>
            </Col>
            <Col span="6" class="isoft_font12">
              <span class="isoft_font12">提出时间:<Time :time="as.last_updated_time" :interval="1"/></span>
            </Col>
            <Col span="6" class="isoft_font12">好评(100) 差评(50)</Col>
          </Row>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
  import {EditAnswerExpert, QueryPageAnswerExpertList, ShowAskExpertDetail} from "../../api"
  import {checkEmpty} from "../../tools"

  export default {
    name: "AnswerExpert",
    data() {
      return {
        ask_expert: null,
        showEditanswer: false,
        answer: '',
        answer_experts: [],
      }
    },
    methods: {
      refreshQuestionDetail: async function (id) {
        const result = await ShowAskExpertDetail({id: id});
        if (result.status == "SUCCESS") {
          this.ask_expert = result.ask_expert;
        }
      },
      EditAnswerExpert: async function () {
        if (!checkEmpty(this.answer)) {
          const result = await EditAnswerExpert({question_id: this.ask_expert.id, answer: this.answer});
          if (result.status == "SUCCESS") {
            this.refreshAskanswerList();
          }
        }
      },
      refreshAskanswerList: async function () {
        const result = await QueryPageAnswerExpertList({question_id: this.$route.query.id});
        if (result.status == "SUCCESS") {
          this.answer_experts = result.answer_experts;
        }
      }
    },
    mounted() {
      if (this.$route.query.id > 0) {
        this.refreshAskanswerList();
        this.refreshQuestionDetail(this.$route.query.id);
      }
    }
  }
</script>

<style scoped>

</style>
