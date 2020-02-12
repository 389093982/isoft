<template>
  <div>
    <div class="isoft_bg_white isoft_pd10 clear" v-if="ask_expert">
      <h4>{{ask_expert.short_desc}}</h4>
      <p>{{ask_expert.question}}</p>
      <div style="text-align: right;">
        <a @click="showEditAnser = !showEditAnser">我来回答</a>
        <a @click="$router.push({path:'/communicate/ask_expert'})">返回问题列表</a>
      </div>

      <div v-if="showEditAnser">
        <Input type="textarea" :rows="10" v-model.trim="anser"/>
        <Button size="small" style="float: right;margin: 10px 0;" type="success"
                @click="editAnserExpert">提交
        </Button>
      </div>
    </div>

    <div class="isoft_bg_white isoft_top10 isoft_pd10">
      <ul>
        <li v-for="(as, index) in anser_experts"
            style="list-style:none;padding: 10px 10px;background: #fff;border-bottom: 1px solid #f4f4f4;">
          <h4 style="color: red;">专家回答({{index+1}} 楼)</h4>{{as.answer}}
          <p>{{as.anser}}</p>
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
  import {EditAnserExpert, QueryPageAskAnserList, ShowAskExpertDetail} from "../../api"
  import {checkEmpty} from "../../tools"

  export default {
    name: "AskAnswer",
    data() {
      return {
        ask_expert: null,
        showEditAnser: false,
        anser: '',
        anser_experts: [],
      }
    },
    methods: {
      refreshQuestionDetail: async function (id) {
        const result = await ShowAskExpertDetail({id: id});
        if (result.status == "SUCCESS") {
          this.ask_expert = result.ask_expert;
        }
      },
      editAnserExpert: async function () {
        if (!checkEmpty(this.anser)) {
          const result = await EditAnserExpert({question_id: this.ask_expert.id, anser: this.anser});
          if (result.status == "SUCCESS") {
            this.refreshAskAnserList();
          }
        }
      },
      refreshAskAnserList: async function () {
        const result = await QueryPageAskAnserList({question_id: this.$route.query.id});
        if (result.status == "SUCCESS") {
          this.anser_experts = result.anser_experts;
        }
      }
    },
    mounted() {
      if (this.$route.query.id > 0) {
        this.refreshAskAnserList();
        this.refreshQuestionDetail(this.$route.query.id);
      }
    }
  }
</script>

<style scoped>

</style>
