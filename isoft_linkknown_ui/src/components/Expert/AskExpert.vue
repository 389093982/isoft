<template>
  <div>
    <div>
      <Row class="isoft_top10">
        <Col span="16" style="padding-right: 5px;">
          <div class="isoft_bg_white isoft_pd10">
            <Row class="search" style="border-bottom: 1px solid #e6e6e6;padding: 20px;height: 62px;">
              <Col span="3" offset="3" style="text-align: center;">
                <a class="isoft_hover_color_blue" @click="searchQuestion(1)" :style="{color: pattern === 1 ? 'red':''}">全部问题</a>
              </Col>
              <Col span="3" style="text-align: center;">
                <a class="isoft_hover_color_blue" @click="searchQuestion(2)" :style="{color: pattern === 2 ? 'red':''}">已有答复</a>
              </Col>
              <Col span="3" style="text-align: center;">
                <a class="isoft_hover_color_blue" @click="searchQuestion(3)" :style="{color: pattern === 3 ? 'red':''}">暂无答复</a>
              </Col>
              <Col span="3" style="text-align: center;">
                <a class="isoft_hover_color_blue" @click="searchQuestion(4)" :style="{color: pattern === 4 ? 'red':''}">热门问题</a>
              </Col>
              <Col span="3" style="text-align: center;">
                <a class="isoft_hover_color_blue" @click="$router.push({path:'/expert/edit_question'})">我要提问</a>
              </Col>
              <Col span="3" style="text-align: center;">
                <a class="isoft_hover_color_blue" @click="searchQuestion(5)" :style="{color: pattern === 5 ? 'red':''}">我的问题</a>
              </Col>
            </Row>

            <ul>
              <li class="isoft_hover_parent" v-for="(as, index) in asks" style="list-style:none;height: 82px;padding: 10px 30px;
                background: #fff;border-bottom: 1px solid #f4f4f4;">
                <Row>
                  <Col span="6" style="display: flex;vertical-align: middle;">
                    <div class="isoft_hover_color_green"
                         style="text-align: center;line-height: 16px;margin: 10px 10px 0 0;">
                      <p>{{as.answer_number}}</p>
                      <p>回答</p>
                    </div>

                    <div style="color: #696969;text-align: center;line-height: 16px;margin: 10px 10px 0 0;">
                      <p>{{as.view_number}}</p>
                      <p>浏览</p>
                    </div>

                    <div style="margin: 10px 0 0 0;">
                      <img style="cursor: pointer;border-radius: 50%;"
                           @click="$router.push({path:'/user/detail',query:{username:as.user_name}})"
                           width="35" height="35" :src="renderUserIcon(as.user_name)" @error="defImg()">
                    </div>
                  </Col>
                  <Col span="18" style="line-height: 30px;">
                    <h4 class="isoft_inline_ellipsis" style="font-size: 16px;cursor: pointer;"
                        @click="$router.push({path:'/expert/answer_expert', query:{id : as.id}})">{{as.short_desc}}</h4>

                    <div class="isoft_font12">
                      <span><Time :time="as.last_updated_time" :interval="1"/></span>
                      <span class="isoft_hover_item_show" style="float: right;">
                        <span class="isoft_mr10">
                          <a v-if="showEdit(as.user_name)"
                             @click="$router.push({path:'/expert/edit_question', query: {id : as.id}})">编辑</a>
                        </span>
                        <span>
                          <a @click="$router.push({path:'/expert/answer_expert', query:{id : as.id}})">
                            <span class="isoft_hover_color_green" style="margin-right: 5px;">我来回答</span>
                            <span class="isoft_color_grey">回答问题可获得 2 积分</span>
                          </a>
                        </span>
                      </span>
                    </div>
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
  import {
    CheckHasLoginConfirmDialog2,
    GetLoginUserName,
    RenderNickName,
    RenderUserIcon,
    RenderUserInfoByNames
  } from "../../tools";
  import MoveLine from "../Common/decorate/MoveLine";
  import IShowMarkdown from "../Common/markdown/IShowMarkdown"

  export default {
    name: "AskExpert",
    components: {MoveLine, ExpertWall, IShowMarkdown},
    data() {
      return {
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 10,
        asks: [],
        pattern: 1,           // 按钮选中的模式
        search_type: '',
        search_user_name: '',
        userInfos: [],
        defaultImg: require('../../assets/default.png'),
      }
    },
    methods: {
      defImg() {
        let img = event.srcElement;
        img.src = this.defaultImg;
        img.onerror = null; //防止闪图
      },
      searchQuestion: function (pattern) {
        this.pattern = pattern;
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
          this.userInfos = await RenderUserInfoByNames(result.asks, 'user_name');
          this.asks = result.asks;
          this.total = result.paginator.totalcount;
        }
      },
      renderUserIcon: function (user_name) {
        return RenderUserIcon(this.userInfos, user_name);
      },
      renderNickName: function (user_name) {
        return RenderNickName(this.userInfos, user_name);
      }
    },
    mounted() {
      this.refreshAskExperts();
    }
  }
</script>

<style scoped>

</style>
