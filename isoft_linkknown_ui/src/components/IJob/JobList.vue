<template>
  <div>
    <div class="isoft_bg_white isoft_pd20">
      <div style="text-align: right;">
        <span class="isoft_button_theme1"
              @click="forwardResumeManage">我是应聘者 - 管理我的简历</span>
        <span class="isoft_button_theme2"
              @click="toEditCorporateDetail">我是招聘者 - 我来发起招聘</span>
      </div>
    </div>

    <div class="isoft_bg_white isoft_pd20 isoft_top10">
      <Row :gutter="10" style="margin-bottom: 10px;">
        <Col span="8"><Input v-model="jobInfoSearch" placeholder="输入职位或者公司关键词搜索您感兴趣的职位"/></Col>
        <Col span="6">
          <Input v-model="jobPlaceSearch" placeholder="北京、上海、广州、深圳" @on-focus="handleFocus('areaChooser')"/>
          <IAreaChooser ref="areaChooser" title="地区选择" @handleSubmit="handleAreaSubmit"/>
        </Col>
        <Col span="6">
          <Select v-model="jobSalaySearch">
            <Option v-for="(salaryRange, index) in salaryRanges" :value="salaryRange" :key="salaryRange">
              {{salaryRange}}
            </Option>
          </Select>
        </Col>
        <Col span="4">
          <Button type="success" @click="refreshJobList">搜索职位</Button>
          <Button type="default" @click="clearSearch">清空</Button>
        </Col>
      </Row>

      <Row>
        <Col span="12" style="padding: 0 0 0 10px;">
          搜索职位:{{jobInfoSearch}} &nbsp;&nbsp;&nbsp; {{jobPlaceSearch}} &nbsp;&nbsp;&nbsp; {{jobSalaySearch}}
        </Col>
        <Col span="12" style="text-align: right;padding-right: 56px;">
          <Button style="float: right;" @click="toToudiDetail">投递清单</Button>
        </Col>
      </Row>

      <div style="padding: 10px;">

        <div v-for="(job,index) in jobs">
          <JobItem :job-detail="job"/>
        </div>

        <Page :total="total" :page-size="offset" show-total show-sizer
              :styles="{'text-align': 'center','margin-top': '10px'}"
              @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
      </div>

    </div>
  </div>
</template>

<script>
  import {FilterPageJobList} from "../../api"
  import {checkEmpty, CheckHasLoginConfirmDialog, GetLoginUserName, strSplit} from "../../tools";
  import IAreaChooser from "../Common/IAreaChooser";
  import JobItem from "./JobItem";

  export default {
    name: "JobList",
    components: {JobItem, IAreaChooser},
    data() {
      return {
        salaryRanges: this.GLOBAL.salaryRanges,
        jobInfoSearch: '',
        jobPlaceSearch: '',
        jobSalaySearch: '',
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 20,
        jobs: [],
      }
    },
    methods: {
      forwardResumeManage: function () {
        CheckHasLoginConfirmDialog(this, {path: '/job/resumeManage', query: {'user_name': GetLoginUserName()}});
      },
      handleAreaSubmit: function (province, city, area) {
        if (checkEmpty(city)) {
          this.jobPlaceSearch = province;
        } else if (checkEmpty(area)) {
          this.jobPlaceSearch = province + '-' + city;
        } else {
          this.jobPlaceSearch = province + '-' + city + '-' + area;
        }
      },
      handleFocus: function (name) {
        if (name === "areaChooser") {
          let arr = strSplit(this.jobPlaceSearch, "-");
          // 暂时不做回显
          this.$refs.areaChooser.showModal();
        }
      },
      handleChange(page) {
        this.current_page = page;
        this.refreshJobList();
      },
      handlePageSizeChange(pageSize) {
        this.offset = pageSize;
        this.refreshJobList();
      },
      refreshJobList: async function () {
        const result = await FilterPageJobList({
          jobInfoSearch: this.jobInfoSearch,
          jobPlaceSearch: this.jobPlaceSearch,
          jobSalaySearch: this.jobSalaySearch,
          offset: this.offset,
          current_page: this.current_page
        });
        if (result.status == "SUCCESS") {
          this.jobs = result.jobs;
          this.total = result.paginator.totalcount;
        }
      },
      clearSearch: function () {
        this.jobInfoSearch = "";
        this.jobPlaceSearch = "";
        this.jobSalaySearch = "";
      },
      showJobDetail: function (job) {
        this.$router.push({path: '/job/corporateDetail', query: {'corporate_id': job.corporate_id}});
      },
      toToudiDetail: function () {
        CheckHasLoginConfirmDialog(this, {path: '/job/jobApplyList'});
      },
      toEditCorporateDetail: function () {
        CheckHasLoginConfirmDialog(this, {path: '/job/corporateDetail'});
      }
    },
    mounted() {
      this.refreshJobList();
    }
  }
</script>

<style scoped>
  .box {
    padding: 2px 5px;
    margin: 2px 10px 2px 0px;
    border: 1px solid rgba(191, 191, 191, 0.34);
  }

  .isoft_button_theme1 {
    padding: 10px 20px;
    border: 1px solid #ff6c38;
    border-radius: 3px;
    font-size: 14px;
    color: #ff6c38;
    cursor: pointer;
  }

  .isoft_button_theme1:hover {
    color: white;
    background-color: #ff6c38;
  }

  .isoft_button_theme2 {
    padding: 10px 20px;
    border: 1px solid #ff9d4b;
    border-radius: 3px;
    font-size: 14px;
    color: #ff9d4b;
    cursor: pointer;
  }

  .isoft_button_theme2:hover {
    color: white;
    background-color: #ff9d4b;
  }
</style>
