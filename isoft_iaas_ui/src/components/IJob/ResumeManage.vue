<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <IBeautifulLink>简历管理</IBeautifulLink>
      <IBeautifulLink>个人特色</IBeautifulLink>
      <IBeautifulLink>简历下载</IBeautifulLink>
      <IBeautifulLink style="float: right;" @onclick="$router.push({path:'/job/resume_edit'})">编辑简历</IBeautifulLink>
    </div>

    <div v-if="resume" class="isoft_bg_white isoft_top10" style="padding: 50px;">
      <span style="position: absolute;right: 160px;top:180px;">
        <img v-if="resume.head_img" :src="resume.head_img" height="160px" width="140px"/>
        <img v-else src="../../assets/default.png" height="160px" width="140px"/>
      </span>
      <Row>
        <Col span="12">
          <p>姓名:{{resume.sex}}</p>
          <p>年龄:{{resume.sex}}</p>
          <p>性别:{{resume.sex}}</p>
          <p>学历:{{resume.education}}</p>
          <p>就业状态:{{resume.employment_status}}</p>
          <p>毕业学校:{{resume.graduate_school}}</p>
          <p>目前状况:{{resume.current_situation}}</p>
        </Col>
        <Col span="12">
          <p>参加工作时间:{{resume.job_start_time}}</p>
          <p>联系方式：QQ 微信xxx {{resume.contact}}</p>
          <p>邮箱:{{resume.email}}</p>
          <p>出生年月:{{resume.birthday}}</p>
          <p>期望薪资:{{resume.expectant_salary}}</p>
          <p>期望地点:{{resume.job_area}}</p>
        </Col>
      </Row>
      <p class="isoft_text_rows">个人技能:{{resume.personal_skills}}</p>
      <p class="isoft_text_rows">项目经验:{{resume.project_experiences}}</p>
      <p class="isoft_text_rows">其它优势:{{resume.other_characters}}</p>
      <p class="isoft_text_rows">个人爱好:{{resume.personal_hobbies}}</p>
    </div>
  </div>
</template>

<script>
  import {QueryResume} from "../../api"

  export default {
    name: "ResumeManage",
    data() {
      return {
        resume: null,
      }
    },
    methods: {
      refreshQueryResume: async function () {
        const result = await QueryResume();
        if (result.status == "SUCCESS") {
          this.resume = result.resume;
        }
      }
    },
    mounted() {
      this.refreshQueryResume();
    }
  }
</script>

<style scoped>
  p {
    margin-top: 10px;
  }
</style>
