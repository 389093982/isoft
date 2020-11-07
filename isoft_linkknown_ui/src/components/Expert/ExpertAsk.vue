<template>
  <div class="isoft_container" style="overflow-x: hidden;display: flex">
        <div style="width: 65%;background-color: white;min-height: 1000px">
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
                <a class="isoft_hover_color_blue" @click="iWillAsk">我要提问</a>
              </Col>
              <Col span="3" style="text-align: center;">
                <a class="isoft_hover_color_blue" @click="searchQuestion(5)" :style="{color: pattern === 5 ? 'red':''}">我的问题</a>
              </Col>
            </Row>

            <div v-for="(as, index) in asks" class="isoft_border_bottom" @mouseenter="hoverIndex = index"
                 :class="{'animated faster bounceInLeft': index === hoverIndex}" style="height: 82px;padding: 10px 30px;">
              <div style="display: flex;">
                <div style="width: 25%;display: flex;text-align: center;">
                  <div class="isoft_hover_color_green isoft_point_cursor mr5">
                    <p>{{as.answer_number}}</p>
                    <p>回答</p>
                  </div>

                  <div class="isoft_hover_desc">
                    <p>{{as.view_number}}</p>
                    <p>浏览</p>
                  </div>

                  <div style="margin: 10px 0 0 60px;" @click="$router.push({path:'/user/userDetail',query:{username:as.user_name}})">
                    <HatAndFacePicture :src="renderUserIcon(as.user_name)" :vip_level="renderVipLevel(as.user_name)" :hat_in_use="renderHatInUse(as.user_name)" :src_size="40" :hat_width="36" :hat_height="10" :hat_relative_left="2" :hat_relative_top="-56" ></HatAndFacePicture>
                  </div>
                </div>
                <div style="width: 75%;line-height: 30px;">
                  <div class="title_hover isoft_font16 isoft_point_cursor"
                       @click="$router.push({path:'/expert/expertAnswer', query:{id : as.id}})">{{as.short_desc | filterLimitFunc(30)}}</div>
                  <div class="isoft_font12">
                    <div style="display: flex;">
                      <div class="isoft_point_cursor isoft_hover_desc" style="width: 50%;">
                        <span @click="$router.push({path:'/user/userDetail',query:{username:as.user_name}})">{{renderNickName(as.user_name)}} · </span>
                        <span><Time :time="as.last_updated_time" :interval="1"/></span>
                      </div>
                      <div style="width: 50%;">
                        <span class="isoft_mr10">
                          <a v-if="showEdit(as.user_name)" @click="$router.push({path:'/expert/editQuestion', query: {id : as.id}})">编辑</a>
                        </span>
                          <span>
                          <a @click="$router.push({path:'/expert/expertAnswer', query:{id : as.id}})">
                            <span class="isoft_hover_color_green mr5">我来回答</span>
                            <span class="isoft_color_grey">回答问题可获得 2 积分</span>
                          </a>
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <Page :total="total" :page-size="offset" show-total show-sizer
                  :styles="{'text-align': 'center','margin-top': '10px'}"
                  @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
          </div>
        </div>
        <div style="width: 35%;margin-left: 5px">
          <WaitYourAnswer></WaitYourAnswer>
          <ExpertWall class="isoft_top5"></ExpertWall>
        </div>
      </div>
</template>

<script>
  import {QueryPageExpertAsk} from "../../api"
  import ExpertWall from "./ExpertWall";
  import {checkHasLogin} from "../../tools/sso"
  import {
    CheckHasLoginConfirmDialog2,
    GetLoginUserName,
    RenderNickName,
    RenderUserIcon,
    RenderVipLevel,
    RenderHatInUse,
    RenderUserInfoByNames
  } from "../../tools";
  import MoveLine from "../Common/decorate/MoveLine";
  import IShowMarkdown from "../Common/markdown/IShowMarkdown"
  import HatAndFacePicture from "../Common/HatAndFacePicture/HatAndFacePicture";
  import WaitYourAnswer from "./WaitYourAnswer";

  export default {
    name: "ExpertAsk",
    components: {WaitYourAnswer, HatAndFacePicture, MoveLine, ExpertWall, IShowMarkdown},
    data() {
      return {
        hoverIndex: 0,
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
        defaultImg: require('../../../static/images/common_img/default.png'),
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
        const result = await QueryPageExpertAsk({
          offset: this.offset,
          current_page: this.current_page,
          search_type: this.search_type,
          search_user_name: this.search_user_name
        });
        if (result.status === "SUCCESS") {
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
      },
      renderVipLevel: function (user_name) {
        return RenderVipLevel(this.userInfos, user_name);
      },
      renderHatInUse: function (user_name) {
        return RenderHatInUse(this.userInfos, user_name);
      },
      iWillAsk:function () {
        let _this = this;
        if (checkHasLogin()) {
          _this.$router.push({path:'/expert/editQuestion'})
        }else {
          CheckHasLoginConfirmDialog2(this, function () {
            _this.$router.push({path:'/expert/askExpert'})
          });
        }
      }
    },
    mounted() {
      this.refreshAskExperts();
    },
    filters: {
      // 内容超长则显示部分
      filterLimitFunc:function (value,limitLenth) {
        if (value.length > limitLenth) {
          return value.slice(0,limitLenth) + ' · · ·'
        }else {
          return value
        }
      }
    },
  }
</script>

<style scoped>
  .title_hover {
    font-size: 15px;
    color: #555;
    font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  }
  .title_hover:hover {
    font-size: 15px;
    color: red;
    font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  }
</style>
