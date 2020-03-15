<template>
  <div>
    <div class="isoft_bg_white">
      <div class="bg01" style="width: 100%;height: 200px;overflow: hidden;padding: 20px;">
        <p class="isoft_font_white" style="font-size: 24px;font-weight: 300;">热门资源</p>
        <p class="isoft_font_white" style="margin-top: 5px;">许多精品资源文件汇集，涵盖it、科技、办公等全部资源，为互联网、行政、设计等领域从业者打造。</p>
        <p class="isoft_font_white" style="cursor:pointer;text-align: center;margin-left: 660px" @click="uploadResource">上传资源赚钱</p>
      </div>

      <div class="isoft_pd10">
        <div class="isoft_top10">
          热门分类：
          <div class="isoft_tag2 mr5" @click="searchResource('')">全部</div>
          <div class="isoft_tag2 mr5" v-for="(fl, index) in resource_fl" @click="searchResource(fl)">{{fl}}</div>
        </div>
        <div class="isoft_border_bottom isoft_top10" style="padding-bottom: 10px;">
          文件格式：
          <div class="isoft_tag2 mr5" @click="searchResource('')">全部</div>
          <div v-for="(filetype, index) in resource_filetypes"
               class="isoft_tag2 mr5" @click="searchResource(filetype)">{{filetype}}
          </div>
        </div>
      </div>

      <p class="isoft_font14" style="padding:15px;font-weight: 600;">
        大家都在寻找适合自己的资源，我们为您
        <span style="color: red;">&nbsp;&nbsp;"精选"&nbsp;&nbsp;"推荐"&nbsp;&nbsp;</span>
        以下资源：
      </p>
    </div>

    <Row>
      <Col span="18">
        <div class="isoft_bg_white isoft_top10 isoft_pd20" style="margin-right: 10px;">
          <div v-for="(resource,index) in resources" style="padding: 10px;border-bottom: 1px solid #eee;">
            <Row>
              <Col span="6"><h4>{{resource.resource_name}}</h4></Col>
              <Col span="18"><span v-if="resource.resource_catalog">分类：{{resource.resource_catalog}}</span></Col>
            </Row>
            <p>介绍：{{resource.resource_desc}}</p>
            <p>
              <Row class="isoft_font12">
                <Col span="4"><span>下载所需积分：<span style="color:red;">{{resource.points}}</span> </span></Col>
                <Col span="4">上传时间：<span style="color:red;"><Time :time="resource.last_updated_time"
                                                                  :interval="1"/></span>
                </Col>
                <Col span="4"><span>已下载：<span style="color:red;">{{resource.downloads}}</span> 次 </span></Col>
                <Col span="4"><a @click="downloadResource(resource)">立刻下载</a></Col>
                <Col span="8">
                  <IBeautifulLink @onclick="recommandResource(resource.id, 1, 0)">推荐 ({{resource.recommend}})
                  </IBeautifulLink>&nbsp;&nbsp;&nbsp;
                  <IBeautifulLink @onclick="recommandResource(resource.id, 0, 1)">内容与描述不符 ({{resource.not_recommend}})
                  </IBeautifulLink>
                  <Progress :percent="calProgress(resource.recommend, resource.not_recommend)" style="width: 200px;"/>
                </Col>
              </Row>
            </p>
          </div>

          <Page :total="total" :page-size="offset" show-total show-sizer
                :styles="{'text-align': 'center','margin-top': '10px'}"
                @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
        </div>
      </Col>
      <Col span="6" class="isoft_bg_white isoft_top10 isoft_pd10">
        <RandomAdmt/>
        <RandomAdmt/>
      </Col>
    </Row>

  </div>
</template>

<script>
  import {FilterPageResourceList, RecommendResource} from "../../api"
  import {checkFastClick, CheckHasLoginConfirmDialog} from "../../tools";
  import RandomAdmt from "../Advertisement/RandomAdmt";

  export default {
    name: "ResourceList",
    components: {RandomAdmt},
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
      searchResource: function (search) {
        this.search = search;
        this.refreshResourceList();
      },
      calProgress: function (recommendNum, not_recommendNum) {    // 计算推荐比例进度
        return recommendNum + not_recommendNum > 0 ? Math.ceil(100 * (recommendNum / (recommendNum + not_recommendNum))) : 100;
      },
      recommandResource: async function (resource_id, recommendNum, not_recommendNum) {
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
          this.$Message.error("您已经评价过！");
        }
      },
      uploadResource: function () {
        CheckHasLoginConfirmDialog(this, {path: '/resource/upload'});
      },
      downloadResource: function (resource) {
        CheckHasLoginConfirmDialog(this, {path: '/resource/download', query: {id: resource.id}});
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
    }
  }
</script>

<style scoped>
  .bg01 {
    animation: toggle_bg 30s infinite;
  }

  @keyframes toggle_bg {
    0% {
      background-image: url("../../../static/images/banner1.jpg");
    }
    25% {
      background-image: url("../../../static/images/banner2.jpg");
    }
    50% {
      background-image: url("../../../static/images/banner3.jpg");
    }
    75% {
      background-image: url("../../../static/images/banner2.jpg");
    }
    100% {
      background-image: url("../../../static/images/banner1.jpg");
    }
  }
</style>
