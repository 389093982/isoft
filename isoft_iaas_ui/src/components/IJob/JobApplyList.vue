<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <!--{{job_apply1}}-->
      <a>个人板块</a>

      <p v-for="(job_apply,index) in job_apply1">
        <Row>
          <Col span="8">岗位名称：{{job_apply.job_name}}</Col>
          <Col span="8">岗位地址：{{job_apply.job_address}}</Col>
          <Col span="8">投递时间：
            <Time :time="job_apply.last_updated_time" :interval="1"/>
          </Col>
        </Row>
      </p>
    </div>

    <div class="isoft_bg_white isoft_top10 isoft_pd10">
      <!--{{job_apply2}}-->
      <a>企业板块</a>

      <p v-for="(job_apply,index) in job_apply2">
        <Row>
          <Col span="8">岗位名称：{{job_apply.job_name}}</Col>
          <Col span="8">投递人：{{job_apply.user_name}}</Col>
          <Col span="8">收到时间：
            <Time :time="job_apply.last_updated_time" :interval="1"/>
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
