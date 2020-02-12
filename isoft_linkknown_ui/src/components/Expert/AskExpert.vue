<template>
  <div>
    <div>
      <Row class="isoft_top10">
        <Col span="16" style="padding-right: 5px;">
          <div class="isoft_bg_white isoft_pd10">
            <Row style="border-bottom: 1px solid #e6e6e6;padding: 20px;height: 62px;">
              <Col span="3" offset="3" style="text-align: center;"><a @click="searchQuestion(1)">全部问题</a></Col>
              <Col span="3" style="text-align: center;"><a @click="searchQuestion(2)">已有答复</a></Col>
              <Col span="3" style="text-align: center;"><a @click="searchQuestion(3)">暂无答复</a></Col>
              <Col span="3" style="text-align: center;"><a @click="searchQuestion(4)">热门问题</a></Col>
              <Col span="3" style="text-align: center;">
                <router-link to="/expert/edit_question">我要提问</router-link>
              </Col>
              <Col span="3" style="text-align: center;"><a @click="searchQuestion(5)">我的问题</a></Col>
            </Row>

            <ul>
              <li v-for="(as, index) in asks"
                  style="list-style:none;padding: 10px 10px;background: #fff;border-bottom: 1px solid #f4f4f4;">
                <h4>{{as.short_desc}}</h4>
                <p>{{as.question}}</p>
                <Row>
                  <Col span="8">
                    <span class="isoft_font12">提出时间:<Time :time="as.last_updated_time" :interval="1"/></span>
                  </Col>
                  <Col span="8">
                    <span class="isoft_font12">提出人:{{as.user_name}}</span>
                  </Col>
                  <Col span="8" style="text-align: right;">
                    <span class="isoft_font12 mr5">
                      <a v-if="showEdit(as.user_name)"
                         @click="$router.push({path:'/expert/edit_question', query: {id : as.id}})">编辑</a>
                    </span>
                    <span class="isoft_font12 mr5">
                      <a @click="$router.push({path:'/expert/answer_expert', query:{id : as.id}})">回答数({{as.answer_number}})</a>
                    </span>
                  </Col>
                </Row>
              </li>
            </ul>

            <Page :total="total" :page-size="offset" show-total show-sizer
                  :styles="{'text-align': 'center','margin-top': '10px'}"
                  @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
          </div>
        </Col>
        <Col span="8">
          <ExpertWall/>
        </Col>
      </Row>
    </div>

  </div>
</template>

<script>
  import {QueryPageAskExpert} from "../../api"
  import ExpertWall from "./ExpertWall";
  import {CheckHasLoginConfirmDialog2, GetLoginUserName} from "../../tools";

  export default {
    name: "AskExpert",
    components: {ExpertWall},
    data() {
      return {
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 10,
        asks: [],
        search_type: '',
        search_user_name: '',
      }
    },
    methods: {
      searchQuestion: function (pattern) {
        // 置空参数
        this.search_user_name = '';
        this.search_type = '';
        if (pattern === 1) {
          this.search_type = '_all';
          this.refreshAskExperts();
        } else if (pattern === 2) {
          this.search_type = '_response';
          this.refreshAskExperts();
        } else if (pattern === 3) {
          this.search_type = '_noresponse';
          this.refreshAskExperts();
        } else if (pattern === 4) {
          this.search_type = '_hot';
          this.refreshAskExperts();
        } else if (pattern === 5) {
          this.searchMyQuestion();
        }
      },
      searchMyQuestion: function () {
        var _this = this;
        CheckHasLoginConfirmDialog2(this, function () {
          _this.search_user_name = GetLoginUserName();
          _this.refreshAskExperts();
        });
      },
      handleChange(page) {
        this.current_page = page;
        this.refreshAskExperts();
      },
      handlePageSizeChange(pageSize) {
        this.offset = pageSize;
        this.refreshAskExperts();
      },
      showEdit: function (user_name) {
        return user_name === GetLoginUserName();
      },
      refreshAskExperts: async function () {
        const result = await QueryPageAskExpert({
          offset: this.offset,
          current_page: this.current_page,
          search_type: this.search_type,
          search_user_name: this.search_user_name
        });
        if (result.status == "SUCCESS") {
          this.asks = result.asks;
          this.total = result.paginator.totalcount;
        }
      }
    },
    mounted() {
      this.refreshAskExperts();
    }
  }
</script>

<style scoped>

</style>
