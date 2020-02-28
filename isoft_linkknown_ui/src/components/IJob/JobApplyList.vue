<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <Alert banner closable type="warning">您已投递以下岗位，请耐心等待招聘人员的联系！谢谢 ^_^</Alert>
      <!--{{job_apply1}}-->
      <a>个人板块(已投递)</a>
      <p v-for="(job_apply,index) in job_apply1">
        <Row style="padding: 10px;border-bottom: 1px solid #f4f4f4;">
          <Col span="8">公司名称：{{job_apply.corporate_name}}</Col>
          <Col span="8">招聘联系人：{{job_apply.last_updated_by}}</Col>
          <Col span="8">岗位名称：{{job_apply.job_name}}</Col>
          <Col span="8">岗位地址：{{job_apply.job_address}}</Col>
          <Col span="8">投递时间：
            <Time :time="job_apply.last_updated_time" :interval="1"/>
          </Col>
        </Row>
      </p>
    </div>

    <div class="isoft_bg_white isoft_top10 isoft_pd10">
      <Alert banner closable type="warning">您已收到以下简历，请及时联系求职人员，以免错过优质资源！谢谢 ^_^</Alert>

      <!--{{job_apply2}}-->
      <a>企业板块(已收到)</a>

      <p v-for="(job_apply,index) in job_apply2">
        <Row style="padding: 10px;border-bottom: 1px solid #f4f4f4;">
          <Col span="7">岗位名称：{{job_apply.job_name}}</Col>
          <Col span="7">投递人：{{job_apply.user_name}}</Col>
          <Col span="5">收到时间：
            <Time :time="job_apply.last_updated_time" :interval="1"/>
          </Col>
          <Col span="5">
            <div class="isoft_button_green1" style="width: 200px;margin: 0 auto;"
                 @click="$router.push({path:'/job/resume_manage', query:{'user_name': job_apply.user_name}})">查看简历
            </div>
          </Col>
        </Row>
      </p>
    </div>
  </div>
</template>

<script>
  import {GetJobApplyList} from "../../api"

  export default {
    name: "JobApplyList",
    data() {
      return {
        job_apply1: [],
        job_apply2: [],
      }
    },
    methods: {
      refreshJobAllyList: async function () {
        const result = await GetJobApplyList();
        if (result.status == "SUCCESS") {
          this.job_apply1 = result.job_apply1;
          this.job_apply2 = result.job_apply2;
        }
      }
    },
    mounted() {
      this.refreshJobAllyList();
    }
  }
</script>

<style scoped>

</style>
