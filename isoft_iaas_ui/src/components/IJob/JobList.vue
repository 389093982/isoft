<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <Row :gutter="10">
        <Col span="9"><Input v-model="jobInfoSearch" placeholder="输入职位或者公司关键词搜索您感兴趣的职位"/></Col>
        <Col span="6"><Input v-model="jobPlaceSearch" placeholder="北京、上海、广州、深圳"/></Col>
        <Col span="6"><Input v-model="jobSalaySearch" placeholder="薪酬范围"/></Col>
        <Col span="3">
          <Button type="success" @click="refreshJobList">搜索职位</Button>
        </Col>
      </Row>
    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_top10">
      <Row>
        <Col span="12">搜索职位:{{jobInfoSearch}} {{jobPlaceSearch}} {{jobSalaySearch}}</Col>
        <Col span="12" style="text-align: right;padding-right: 40px;">
          <Button @click="$router.push({path:'/job/resume_manage'})">简历管理</Button>
          <Button @click="$router.push({path:'/job/corporate_detail'})">发布招聘</Button>
        </Col>
      </Row>

      <Row :gutter="10" style="margin-top: 20px;">
        <Col span="5">公司名称</Col>
        <Col span="5">岗位名称</Col>
        <Col span="4">工作年限</Col>
        <Col span="4">工作地点</Col>
        <Col span="4">薪酬范围</Col>
        <Col span="2">操作</Col>
      </Row>
      <Row v-for="(job,index) in jobs" :gutter="10" style="margin-top: 20px;"
           v-if="job.corporate_name && job.job_name">
        <Col span="5" class="isoft_inline_ellipsis">{{job.corporate_name}}</Col>
        <Col span="5" class="isoft_inline_ellipsis">{{job.job_name}}</Col>
        <Col span="4" class="isoft_inline_ellipsis">{{job.job_age}}</Col>
        <Col span="4" class="isoft_inline_ellipsis">{{job.job_address}}</Col>
        <Col span="4" class="isoft_inline_ellipsis">{{job.salary_range}}</Col>
        <Col span="2">我要应聘</Col>
      </Row>

      <Page :total="total" :page-size="offset" show-total show-sizer
            :styles="{'text-align': 'center','margin-top': '10px'}"
            @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
    </div>
  </div>
</template>

<script>
  import {FilterPageJobList} from "../../api"

  export default {
    name: "JobList",
    data(){
      return {
        jobInfoSearch: '',
        jobPlaceSearch:'',
        jobSalaySearch:'',
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
          offset: this.offset,
          current_page: this.current_page
        });
        if (result.status == "SUCCESS") {
          this.jobs = result.jobs;
          this.total = result.paginator.totalcount;
        }
      }
    },
    mounted() {
      this.refreshJobList();
    }
  }
</script>

<style scoped>

</style>
