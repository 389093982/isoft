<template>
  <div>
    <div class="isoft_bg_white">
      <div style="width: 100%;height: 100px;overflow: hidden;background-color: rgba(0,21,228,0.5);padding: 20px;">
        <p class="isoft_font_white" style="font-size: 24px;font-weight: 300;">共享资源</p>
        <p class="isoft_font_white" style="margin-top: 5px;">
          许多精品资源文件汇集，涵盖it、科技、办公等全部资源，为互联网、行政、设计等领域从业者打造。
        </p>
        <p class="isoft_font_white" style="cursor:pointer;text-align: right;"
           @click="uploadResource">
          上传资源赚钱
        </p>
      </div>

      <div class="isoft_pd10">
        <p class="isoft_top10">
          热门分类：
          <Tag><span @click="searchResource('')">全部</span></Tag>
          <Tag v-for="(fl, index) in resource_fl"><span @click="searchResource(fl)">{{fl}}</span></Tag>
        </p>
        <p class="isoft_border_bottom isoft_top10" style="padding-bottom: 10px;">
          文件格式：
          <Tag><span @click="searchResource('')">全部</span></Tag>
          <Tag v-for="(filetype, index) in resource_filetypes"><span
            @click="searchResource(filetype)">{{filetype}}</span></Tag>
        </p>
      </div>

      <p class="isoft_font14" style="padding:15px;font-weight: 600;">
        大家都在寻找适合自己的资源，我们为您
        <span style="color: red;">&nbsp;&nbsp;"精选"&nbsp;&nbsp;"推荐"&nbsp;&nbsp;</span>
        以下资源：
      </p>
    </div>

    <div class="isoft_bg_white isoft_top10 isoft_pd20">
      <div v-for="(resource,index) in resources" style="padding: 10px;border-bottom: 1px solid #eee;">
        <Row>
          <Col span="6"><h4>{{resource.resource_name}}</h4></Col>
          <Col span="18"><span v-if="resource.resource_catalog">分类：{{resource.resource_catalog}}</span></Col>
        </Row>
        <p>介绍：{{resource.resource_desc}}</p>
        <p>
          <Row class="isoft_font12">
            <Col span="4"><span>下载所需积分：<span style="color:red;">{{resource.points}}</span> </span></Col>
            <Col span="4">上传时间：<span style="color:red;"><Time :time="resource.last_updated_time" :interval="1"/></span>
            </Col>
            <Col span="4"><span>已下载：<span style="color:red;">{{resource.downloads}}</span> 次 </span></Col>
            <Col span="4"><a @click="downloadResource(resource)">立刻下载</a></Col>
            <Col span="8">
              <IBeautifulLink @onclick="recommandResource(resource.id, 1, 0)">推荐 ({{resource.recommend}})
              </IBeautifulLink>&nbsp;&nbsp;&nbsp;
              <IBeautifulLink @onclick="recommandResource(resource.id, 0, 1)">不推荐 ({{resource.not_recommend}})
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
  </div>
</template>

<script>
  import {FilterPageResourceList, RecommendResource} from "../../api"
  import {checkFastClick, CheckHasLoginConfirmDialog} from "../../tools";

  export default {
    name: "ResourceList",
    data() {
      return {
        search: '',
        resources: null,
        resource_fl: ["java", "python", "php", "c", "c++", "vue"],
        resource_filetypes: [".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".zip", ".rar"],
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 10,
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
          if (result.status == "SUCCESS") {
            this.$Message.success("感谢您的评价!");
            this.refreshResourceList();
          }
        } else {
          this.$Message.error("您已经评价过！");
        }
      },
      uploadResource: function () {
        CheckHasLoginConfirmDialog(this, {path: '/resource/uploadResource'});
      },
      downloadResource: function (resource) {
        CheckHasLoginConfirmDialog(this, {path: '/resource/downloadResource', query: {id: resource.id}});
      },
      refreshResourceList: async function () {
        const result = await FilterPageResourceList({
          offset: this.offset,
          current_page: this.current_page,
          search: this.search
        });
        if (result.status == "SUCCESS") {
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

</style>
