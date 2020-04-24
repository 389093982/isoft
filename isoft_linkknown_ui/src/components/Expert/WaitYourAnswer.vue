<template>
  <div class="isoft_bg_white isoft_pd10">
    <div class="isoft_font_header isoft_border_bottom">
      等你来答
      <span class="isoft_font12 isoft_point_cursor" style="float:right;margin-right: 30px;color: forestgreen;" @click="otherRefresh()"><Icon type="md-refresh" />&nbsp;换一批</span>
    </div>
    <Row v-for="(waitYourAnswer,index) in waitYourAnswerList" :style="{'margin-top': index===0 ? 5+'px':20+'px'}">
        <Row>
          <Col span="20" class="title_hover" style="cursor: pointer">
            <span @click="$router.push({path:'/expert/answerExpert', query:{id : waitYourAnswer.id}})">
              {{waitYourAnswer.short_desc | filterLimitFunc(15)}}
            </span>
          </Col>
          <Col span="4"><Icon type="ios-images-outline" :size="16" style="color:darkgrey;"/></Col>
        </Row>
        <Row style="font-size: 13px">
          <Col span="4">
            <span>
               <img style="border: 1px solid grey;border-radius:50%;" width="18" height="18" :src="getImgPath(index)">
            </span>
            <span style="position: relative;left: -10px;">
               <img style="border: 1px solid grey;border-radius:50%;" width="18" height="18" :src="getImgPath(index+1)">
            </span>
          </Col>
          <Col span="6" class="isoft_hover_desc isoft_font12" style="position: relative;left: -5px">
            等{{waitYourAnswer.also_want_ask_number}}人想问
          </Col>
          <Col span="6">
            <span>
              <span v-if="whetherAsked(waitYourAnswer.id)" style="color: #ff9e3c;cursor: pointer">
                我也问过^_^
              </span>
              <span v-else @click="IWantAskAlso(waitYourAnswer.id)" style="color: #34B458;cursor: pointer">
                <Icon type="md-add" :size="15"/>我也想问
              </span>
            </span>
          </Col>
          <Col span="7" offset="1" style="color: #34B458;cursor: pointer">
            <span @click="$router.push({path:'/expert/answerExpert', query:{id : waitYourAnswer.id}})">
              <Icon type="ios-create-outline" :size="20"/>我来回答
            </span>
          </Col>
        </Row>
      </Row>
  </div>
</template>

<script>
  import IBeautifulCard from "../Common/card/IBeautifulCard"
  import {QueryWaitYourAnswerList,IWantAskAlso} from "../../api"
  import HatAndFacePicture from "../Common/HatAndFacePicture/HatAndFacePicture";
  import {getLoginUserName} from "../../tools/sso"
  import {CheckHasLogin,CheckHasLoginConfirmDialog2} from "../../tools/index"

  export default {
    name: "WaitYourAnswer",
    components: {HatAndFacePicture, IBeautifulCard},
    data() {
      return {
        waitYourAnswerList: [],       // 等你来答
        alsoAskedIdList:[],
        pageNumber:1,
        pageSize:5,
      }
    },
    methods: {
      refreshWaitYourAnswerList: async function () {
        let params = {
          pageNumber:this.pageNumber,
          pageSize:this.pageSize,
          user_name:getLoginUserName()
        };
        const result = await QueryWaitYourAnswerList(params);
        if (result.status === "SUCCESS") {
          this.waitYourAnswerList = result.waitYourAnswerList;
          this.alsoAskedIdList = result.alsoAskedIdList;
        }
      },
      otherRefresh:function(pageNumber,pageSize){
        this.pageNumber = this.pageNumber===1?2:1;
        this.refreshWaitYourAnswerList();
      },
      IWantAskAlso:async function (id) {
        let _this = this;
        CheckHasLoginConfirmDialog2(this, async function () {
          let params = {
            id:id,
            favorite_type:'ask_expert',
            user_name:getLoginUserName()
          };
          const result = await IWantAskAlso(params);
          if (result.status === "SUCCESS") {
            _this.$Message.success("+ 1 成功！");
            _this.refreshWaitYourAnswerList();
          }
        });
      },
      whetherAsked:function (id) {
        return this.alsoAskedIdList.filter(ask => ask.id === id).length > 0;
      },
      getImgPath:function (id) {
        let num = Math.floor(Math.random() * 15) + 1; //1-15 之间的随机数， 因为静态文件准备了20张小图片
        return "../../../static/images/waitYourAnswer/default_"+(id + num)+".jpg";
      }
    },
    mounted() {
      this.refreshWaitYourAnswerList();
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

  .hover_color_green{
    color: #575757;
  }
  .hover_color_green:hover{
    color: #34B458;
  }
</style>
