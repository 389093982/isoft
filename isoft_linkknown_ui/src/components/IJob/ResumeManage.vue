<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <div id="resumeInfo" v-if="resume" class="isoft_bg_white isoft_top10" style="padding: 50px;">
        <div style="display: flex;">
          <div style="width: 20%;">
            <span class="isoft_hover_top10 isoft_point_cursor">
              <img v-if="resume.head_img" :src="resume.head_img" style="width: 160px;height: 160px;border-radius: 50%;"
                   @error="defImg()"/>
              <img v-else src="../../assets/default.png" style="width: 160px;height: 160px;border-radius: 50%;"/>
            </span>
          </div>
          <div style="width: 80%;">
            <Row>
              <Col span="12">
                <p>姓名:{{resume.user_name}}</p>
                <p>年龄:{{resume.age}}</p>
                <p>性别:{{resume.sex}}</p>
                <p>学历:{{resume.education}}</p>
                <p>就业状态:{{resume.employment_status}}</p>
                <p>毕业学校:{{resume.graduate_school}}</p>
                <p>目前状况:{{resume.current_situation}}</p>
              </Col>
              <Col span="12">
                <p>参加工作时间:{{resume.job_start_time}}</p>
                <p>手机号：{{resume.contact}}</p>
                <p>邮箱:{{resume.email}}</p>
                <p>出生年月:{{resume.birthday}}</p>
                <p>期望薪资:{{resume.expectant_salary}}</p>
                <p>期望地点:{{resume.job_area}}</p>
              </Col>
            </Row>
          </div>
        </div>

        <p class="isoft_text_rows">个人技能:{{resume.personal_skills}}</p>
        <p class="isoft_text_rows">项目经验:{{resume.project_experiences}}</p>
        <p class="isoft_text_rows">其它优势:{{resume.other_characters}}</p>
        <p class="isoft_text_rows">个人爱好:{{resume.personal_hobbies}}</p>

        <div class="isoft_button_green1" style="width: 200px;margin: 0 auto;margin-top: 50px;"
             @click="$router.push({path:'/job/resume_edit'})">编辑简历
        </div>
      </div>

      <div v-else class="isoft_bg_white isoft_pd20" style="min-height: 400px;">
        <Spin fix size="large" v-if="isLoading">
          <div class="isoft_loading"></div>
        </Spin>
        <div v-else-if="isLoginUserName" class="isoft_button_green1" style="width: 500px;margin: 0 auto;"
             @click="$router.push({path:'/job/resume_edit'})">您还未创建简历，前去创建
        </div>
        <div v-else style="text-align: center;margin-top: 50px;"><h3>该用户太懒，一封简历也没留下</h3></div>
      </div>
    </div>
  </div>
</template>
<script>
  import {QueryResume} from "../../api"
  import {checkNotEmpty, GetLoginUserName} from "../../tools";

  export default {
    name: "ResumeManage",
    data() {
      return {
        isLoading: true,
        resume: null,
        defaultImg: require('../../assets/default.png'),
      }
    },
    methods: {
      defImg() {
        let img = event.srcElement;
        img.src = this.defaultImg;
        img.onerror = null; //防止闪图
      },
      refreshQueryResume: async function (user_name) {
        this.isLoading = true;
        try {
          const result = await QueryResume({user_name: user_name});
          if (result.status === "SUCCESS") {
            this.resume = result.resume;
          }
        } finally {
          this.isLoading = false;
        }
      },
    },
    computed: {
      isLoginUserName: function () {
        return checkNotEmpty(this.$route.query.user_name) && this.$route.query.user_name === GetLoginUserName();
      },
    },
    mounted() {
      if (checkNotEmpty(this.$route.query.user_name)) {
        this.refreshQueryResume(this.$route.query.user_name);
      }
    }
  }
</script>


<style scoped>
  p {
    margin-top: 10px;
  }
</style>
