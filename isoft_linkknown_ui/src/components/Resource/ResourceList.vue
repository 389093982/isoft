<template>
  <div style="overflow-x: hidden;">
    <div class="isoft_bg_white">
      <div class="bg01" style="width: 100%;height: 80px;overflow: hidden;padding: 20px;">
        <Row>
          <Col span="16" class="animated faster bounceInRight">
            <p class="isoft_font_white" style="font-size: 24px;font-weight: 300;">热门资源</p>
            <p class="isoft_font_white" style="margin-top: 5px;">许多精品资源文件汇集，涵盖it、科技、办公等全部资源，为互联网、行政、设计等领域从业者打造。</p>
          </Col>
          <Col span="8">
            <Button type="default" ghost style="cursor:pointer;margin-top: 5px" @click="uploadResource">
              <Icon type="md-cloud-upload" style="font-size: 20px" />上传资源赚钱
            </Button>
          </Col>
        </Row>
      </div>

      <div style="padding: 10px 0 10px 30px ">
        <div class="isoft_top10">
          热门分类：
          <div class="isoft_tag2 mr5" @click="searchResource('')"  style="cursor: pointer">全部</div>
          <div class="isoft_tag2 mr5" v-for="(fl, index) in resource_fl" @click="searchResource(fl)"  style="cursor: pointer">{{fl}}</div>
        </div>
        <div class="isoft_border_bottom isoft_top10" style="padding-bottom: 10px;">
          文件格式：
          <div class="isoft_tag2 mr5" @click="searchResource('')"  style="cursor: pointer">全部</div>
          <div v-for="(filetype, index) in resource_filetypes" class="isoft_tag2 mr5" @click="searchResource(filetype)"  style="cursor: pointer">{{filetype}}
          </div>
        </div>
      </div>

      <div style="padding: 10px 0 0 30px ">
        <Row>
          <Col span="10">
            <span class="tip_hover">
              大家都在寻找适合自己的资源，我们为您<span style="color: red;">&nbsp;&nbsp;"精选"&nbsp;&nbsp;"推荐"&nbsp;&nbsp;</span>以下资源
            </span>
          </Col>
          <Col span="14">
            <ISearch @submitFunc="submitFunc" @searchDataHasChange="searchDataHasChange" style="position: relative;left: -330px;top:-8px"></ISearch>
          </Col>
        </Row>
      </div>

    </div>

    <div style="display: flex;">
      <div style="width: 65%;background-color: white;margin-top: 5px">
        <div class="isoft_bg_white isoft_pd20">
          <div v-for="(resource,index) in resources" style="padding: 15px 0 0 20px ;border-bottom: 1px solid #eee;">
            <Row style="margin-top: 15px">
              <Col span="12"><h4>{{resource.resource_name}}</h4></Col>
              <Col span="8" style="font-size: 15px;color: #777;"><span v-if="resource.resource_catalog">分类：{{resource.resource_catalog}}</span></Col>
              <!--进入下载-->
              <Col span="4">
                <a @click="downloadResource(resource)">进入下载</a>
              </Col>
            </Row>
            <p style="color: #9b9896;font-size: 12px">描述：{{resource.resource_desc | filterLimitFunc(50)}}</p>
            <p>
              <Row style="color: #adaaa8;font-size: 12px;margin-bottom: 15px">
                <Col span="4"><span>下载所需积分：{{resource.points}}</span></Col>
                <Col span="4">上传时间：<Time :time="resource.last_updated_time" :interval="1"/></Col>
                <Col span="4"><span>已下载：<span style="color:red;">{{resource.downloads}}</span> 次 </span></Col>
                <Col span="12">
                  <!--内容与描述不符-->
                  <IBeautifulLink @onclick="recommandResource(resource.id, 1, 0)">
                    <Icon type="ios-thumbs-down-outline" style="font-size: 15px" /> ({{resource.not_recommend}})
                  </IBeautifulLink>
                  <!--进度条-->
                  <Progress :stroke-width="5" :percent="calProgress(resource.recommend, resource.not_recommend)" style="width: 200px;"/>
                  <!--推荐-->
                  <IBeautifulLink @onclick="recommandResource(resource.id, 0, 1)">
                    <Icon type="ios-thumbs-up-outline" style="font-size: 15px" /> ({{resource.recommend}})
                  </IBeautifulLink>
                </Col>
              </Row>
            </p>
          </div>

          <Page :total="total" :page-size="offset" show-total show-sizer :styles="{'text-align': 'center','margin-top': '10px'}" @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
        </div>
      </div>
      <div style="width: 30.5%;margin: 5px 0 0 5px ;background-color: white">
        <WaitYourAnswer></WaitYourAnswer>
        <div style="margin-top: 5px">
          <img src="../../../static/images/common_img/linkknown_tip_share.jpg" height="480" width=100%/>
        </div>
        <ExpertWall style="margin-top: 5px"></ExpertWall>
      </div>
    </div>

  </div>
</template>

<script>
  import {FilterPageResourceList, RecommendResource} from "../../api"
  import {checkFastClick, CheckHasLoginConfirmDialog} from "../../tools";
  import ISearch from "../Common/search/ISearch"
  import WaitYourAnswer from "../Expert/WaitYourAnswer";
  import ExpertWall from "../Expert/ExpertWall";

  export default {
    name: "ResourceList",
    components: {ExpertWall, WaitYourAnswer, ISearch},
    data() {
      return {
        search: '',
        resources: null,
        resource_fl: ["java", "python", "php", "c", "c++", "vue"],
        resource_filetypes: [".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".pdf"],
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 20,
      }
    },
    methods: {
      submitFunc:function(search_data){
        this.search = search_data;
        this.refreshResourceList();
      },
      searchResource: function (search) {
        this.search = search;
        this.refreshResourceList();
      },
      calProgress: function (recommendNum, not_recommendNum) {    // 计算推荐比例进度
        return recommendNum + not_recommendNum > 0 ? Math.ceil(100 * (recommendNum / (recommendNum + not_recommendNum))) : 100;
      },
      recommandResource: async function (resource_id, not_recommendNum, recommendNum) {
        var _this = this;
        if (checkFastClick()) {
          _this.$Message.error("点击过快,请稍后重试!");
          return;
        }
        var flag = false;
        if (recommendNum > 0 && localStorage.getItem(this.GLOBAL.currentSite + resource_id + "recommendNum") == null) {
          localStorage.setItem(this.GLOBAL.currentSite + resource_id + "recommendNum", recommendNum);
          flag = true;
        }
        if (not_recommendNum > 0 && localStorage.getItem(this.GLOBAL.currentSite + resource_id + "not_recommendNum") == null) {
          localStorage.setItem(this.GLOBAL.currentSite + resource_id + "not_recommendNum", not_recommendNum);
          flag = true;
        }
        if (flag) {
          const result = await RecommendResource({
            resource_id: resource_id,
            recommendNum: recommendNum,
            not_recommendNum: not_recommendNum
          });
          if (result.status === "SUCCESS") {
            this.$Message.success("感谢您的评价!");
            this.refreshResourceList();
          }
        } else {
          this.$Message.error("您已经点评过！");
        }
      },
      uploadResource: function () {
        CheckHasLoginConfirmDialog(this, {path: '/resource/resourceUpload'});
      },
      downloadResource: function (resource) {
        CheckHasLoginConfirmDialog(this, {path: '/resource/resourceDownload', query: {id: resource.id}});
      },
      refreshResourceList: async function () {
        const result = await FilterPageResourceList({
          offset: this.offset,
          current_page: this.current_page,
          search: this.search
        });
        if (result.status === "SUCCESS") {
          this.resources = result.resources;
          this.total = result.paginator.totalcount;
        }
      },
      handleChange(page) {
        this.current_page = page;
        this.refreshResourceList();
      },
      handlePageSizeChange(pageSize) {
        this.offset = pageSize;
        this.refreshResourceList();
      },
    },
    mounted() {
      this.refreshResourceList();
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
  .tip_hover {
    font-size: 15px;
    color: #555;
    font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  }
  .tip_hover:hover {
    font-size: 15px;
    color: red;
    font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  }

  .bg01 {
    animation: toggle_bg 30s infinite;
  }

  @keyframes toggle_bg {
    0% {
      background-image: url("../../../static/images/resourceList/banner1.jpg");
    }
    25% {
      background-image: url("../../../static/images/resourceList/banner2.jpg");
    }
    50% {
      background-image: url("../../../static/images/resourceList/banner3.jpg");
    }
    75% {
      background-image: url("../../../static/images/resourceList/banner2.jpg");
    }
    100% {
      background-image: url("../../../static/images/resourceList/banner1.jpg");
    }
  }
</style>
