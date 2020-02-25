<template>
  <div>
    <div class="isoft_bg_white isoft_pd10 clear" v-if="ask_expert">
      <h4>{{ask_expert.short_desc}}</h4>
      <IShowMarkdown :content="ask_expert.question"/>
      <div style="text-align: right;">
        <a @click="showEditanswer = !showEditanswer">我来回答</a>
        <a @click="$router.push({path:'/expert/ask_expert'})">返回问题列表</a>
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
          <h4 style="color: red;">专家回答({{ (current_page - 1) * offset + index + 1}} 楼)</h4>
          <p>{{as.answer}}</p>
          <Row>
            <Col span="6">
              <span class="isoft_font12">回答人：{{as.user_name}}</span>
            </Col>
            <Col span="6" class="isoft_font12">
              <span class="isoft_font12">提出时间:<Time :time="as.last_updated_time" :interval="1"/></span>
            </Col>
            <Col span="6" class="isoft_font12">
              <span style="cursor: pointer;" @click="modifyGoodBadNumber(as.id)">好评({{as.good_number}})</span>
            </Col>
          </Row>
        </li>
      </ul>

      <Page :total="total" :page-size="offset" show-total show-sizer
            :styles="{'text-align': 'center','margin-top': '10px'}"
            @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
    </div>
  </div>
</template>

<script>
  import {EditAnswerExpert, ModifyGoodNumber, QueryPageAnswerExpertList, ShowAskExpertDetail} from "../../api"
  import {checkEmpty} from "../../tools"
  import IShowMarkdown from "../Common/markdown/IShowMarkdown"

  export default {
    name: "AnswerExpert",
    components: {IShowMarkdown},
    data() {
      return {
        ask_expert: null,
        showEditanswer: false,
        answer: '',
        answer_experts: [],
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 10,
      }
    },
    methods: {
      handleChange(page) {
        this.current_page = page;
        this.refreshAskanswerList();
      },
      handlePageSizeChange(pageSize) {
        this.offset = pageSize;
        this.refreshAskanswerList();
      },
      modifyGoodBadNumber: async function (answerId) {
        if (localStorage.getItem(this.GLOBAL.currentSite + "anser_expert" + answerId)) {
          this.$Message.success("您已经点过好评了!");
          return;
        }
        const result = await ModifyGoodNumber({id: answerId});
        if (result.status == "SUCCESS") {
          this.refreshAskanswerList();
          localStorage.setItem(this.GLOBAL.currentSite + "anser_expert" + answerId, true);
        }
      },
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
        const result = await QueryPageAnswerExpertList({
          current_page: this.current_page,
          offset: this.offset,
          question_id: this.$route.query.id
        });
        if (result.status == "SUCCESS") {
          this.answer_experts = result.answer_experts;
          this.total = result.paginator.totalcount;
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
