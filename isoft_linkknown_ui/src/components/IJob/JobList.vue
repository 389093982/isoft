<template>
  <div>
    <div class="isoft_bg_white isoft_pd10" style="padding-top: 20px;">
      <Row :gutter="10">
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
    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_top10" style="border-top: 2px solid rgba(255,8,0,0.3);">
      <Row>
        <Col span="12">搜索职位:{{jobInfoSearch}} {{jobPlaceSearch}} {{jobSalaySearch}}</Col>
        <Col span="12" style="text-align: right;padding-right: 25px;">
          <Button @click="forwardResumeManage">简历管理</Button>
          <Button @click="toEditCorporateDetail">发布招聘</Button>
          <Button @click="toToudiDetail">投递清单</Button>
        </Col>
      </Row>

      <div style="padding: 10px;">
        <Row v-for="(job,index) in jobs" :gutter="10" v-if="job.corporate_name && job.job_name"
             style="padding: 15px;border-bottom: 1px solid #d7dde4;">
          <div style="cursor: pointer;" @click="showJobDetail(job)">
            <Col span="4" class="isoft_inline_ellipsis" style="font-size: 16px;color: #656565;">{{job.corporate_name}}
            </Col>
            <Col span="4" class="isoft_inline_ellipsis" style="font-size: 16px;color: #656565;">{{job.job_name}}</Col>
            <Col span="4" class="isoft_inline_ellipsis">{{job.job_age}}</Col>
            <Col span="6" class="isoft_inline_ellipsis">{{job.job_address}}</Col>
            <Col span="6" class="isoft_inline_ellipsis">
              <span style="font-size: 16px;color: #393;">{{job.salary_range}}</span>
              <span style="font-size: 14px;color: rgba(0,0,0,0.5);float: right;"><Time
                :time="job.last_updated_time"/></span>
            </Col>
          </div>
        </Row>

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

  export default {
    name: "JobList",
    components: {IAreaChooser},
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
        CheckHasLoginConfirmDialog(this, {path: '/job/resume_manage', query: {'user_name': GetLoginUserName()}});
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
        this.$router.push({path: '/job/corporate_detail', query: {'corporate_id': job.corporate_id}});
      },
      toToudiDetail: function () {
        CheckHasLoginConfirmDialog(this, {path: '/job/apply_list'});
      },
      toEditCorporateDetail: function () {
        CheckHasLoginConfirmDialog(this, {path: '/job/corporate_detail'});
      }
    },
    mounted() {
      this.refreshJobList();
    }
  }
</script>

<style scoped>

</style>
