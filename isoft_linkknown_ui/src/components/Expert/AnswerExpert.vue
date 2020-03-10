<template>
  <div>

    <div style="float: left;width: 70%">
      <div style="width: 90%;margin:0 0 0 30px;background-color: white ">
        <div style="margin: 0 0 0 50px ">
          <div class="isoft_bg_white isoft_pd20 clear" v-if="ask_expert">
            <span style="font-size: 14px"><b>{{ask_expert.short_desc}}</b></span>
            <code style="margin-left: 30px;color: #adaaa8">[{{ask_expert.answer_number}}次回答,  {{ask_expert.view_number}}次浏览]</code>
            <IShowMarkdown :content="ask_expert.question"/>
            <div style="text-align: left;">
              <a @click="$router.push({path:'/expert/ask_expert'})">返回问题列表</a>&nbsp;&nbsp;
              <span class="showTousu">
            <a @click="showEditanswer = !showEditanswer">我来回答</a>&nbsp;&nbsp;
            <a @click="$router.push({path:'/ilearning/advise',query:{user_name:ask_expert.user_name,ask_id:ask_expert.id,short_desc:ask_expert.short_desc}})" class="willComplaint">我要投诉</a>
          </span>
            </div>
            <div v-if="showEditanswer" style="width: 80%">
              <Input type="textarea" :rows="5" v-model.trim="answer"/>
              <Button size="small" style="float: right;margin: 10px 0;" type="success" @click="EditAnswerExpert">提交</Button>
            </div>
          </div>
          <div class="isoft_bg_white isoft_top10 isoft_pd10">
            <ul>
              <li v-for="(as, index) in answer_experts"
                  style="list-style:none;padding: 10px 10px;background: #fff;border-bottom: 1px solid #f4f4f4;">
                <!--<h4 style="color: red;">专家回答({{ (current_page - 1) * offset + index + 1}} 楼)</h4>-->
                <h4 style="color: red;">专家回答( # {{total - (current_page - 1) * offset - index}}楼 )</h4>
                <Row>
                  <Col span="2" style="position: relative;top: 6px">
                    <span style="cursor: pointer" @click="$router.push({path:'/user/detail',query:{username:as.user_name}})" class="isoft_font12">
                      <span><img class="isoft_hover_red" style="cursor: pointer;border: 1px solid grey;border-radius:50%;" width=30px height=30px :src=as.small_icon @error="defImg()"></span>
                    </span>
                  </Col>
                  <Col span="22" style="position: relative;left: -20px">
                    <Row>
                      <span style="font-size: 13px;color: #777">{{as.nick_name}}</span><span> : " {{as.answer}} "</span>
                    </Row>
                    <Row>
                      <Col span="20"><span class="isoft_font12" style="color: #adaaa8">
                        回答于 : <Time :time="as.last_updated_time" :interval="1"/></span>
                      </Col>
                      <Col span="4"><span style="cursor: pointer;" @click="modifyGoodBadNumber(as.id)">
                        <Icon type="md-heart-outline"  style="font-size: 20px;cursor: pointer;color: rgb(173, 170, 168)"/>&nbsp;
                        好评({{as.good_number}})</span>
                      </Col>
                    </Row>
                  </Col>
                </Row>
              </li>
            </ul>

            <Page :total="total" :page-size="offset" show-total show-sizer :styles="{'text-align': 'center','margin-top': '10px'}" @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
          </div>
        </div>
      </div>
    </div>

    <div style="float: left;width: 30%">
      <div style="position: relative;left: -50px;">
        <ExpertWall></ExpertWall>
      </div>
    </div>

    <div style="clear: both"></div>

  </div>
</template>

<script>
  import {EditAnswerExpert, ModifyGoodNumber, QueryPageAnswerExpertList, ShowAskExpertDetail} from "../../api"
  import {checkEmpty} from "../../tools"
  import IShowMarkdown from "../Common/markdown/IShowMarkdown"
  import ExpertWall from "./ExpertWall";

  export default {
    name: "AnswerExpert",
    components: {ExpertWall, IShowMarkdown},
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
        if (result.status === "SUCCESS") {
          this.refreshAskanswerList();
          localStorage.setItem(this.GLOBAL.currentSite + "anser_expert" + answerId, true);
        }
      },
      refreshQuestionDetail: async function (id) {
        const result = await ShowAskExpertDetail({id: id});
        if (result.status === "SUCCESS") {
          this.ask_expert = result.ask_expert;
        }
      },
      EditAnswerExpert: async function () {
        if (!checkEmpty(this.answer)) {
          const result = await EditAnswerExpert({question_id: this.ask_expert.id, answer: this.answer});
          if (result.status === "SUCCESS") {
            this.$Message.success("提交成功");
            this.answer = '';
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
        if (result.status === "SUCCESS") {
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
  .showTousu:hover .willComplaint{
    display: inline;
  }
  .willComplaint{
    display: none;
  }

</style>
