<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <p class="clear">
        <label for="head_img">头像：</label>
        <input id="head_img" class="input" v-model="formInline.head_img" placeholder="请上传个人头像"/>
      </p>
      <p class="clear">
        <label for="sex">公司名称：</label>
        <input id="sex" class="input" v-model="formInline.sex" placeholder="请选择性别"/>
      </p>
      <p class="clear">
        <label for="job_start_time">参加工作时间：</label>
        <input id="job_start_time" class="input" v-model="formInline.job_start_time" placeholder="请您输入参加工作时间"/>
      </p>
      <p class="clear">
        <label for="contact">联系方式：</label>
        <input id="contact" class="input" v-model="formInline.contact" placeholder="请您输入联系方式"/>
      </p>
      <p class="clear">
        <label for="email">邮箱：</label>
        <input id="email" class="input" v-model="formInline.email" placeholder="请您输入邮箱"/>
      </p>
      <p class="clear">
        <label for="birthday">出生年月：</label>
        <input id="birthday" class="input" v-model="formInline.birthday" placeholder="请您输入出生年月"/>
      </p>
      <p class="clear">
        <label for="personal_skills">个人技能：</label>
        <textarea rows="8" id="personal_skills" class="input" v-model="formInline.personal_skills"
                  placeholder="请您输入个人技能"></textarea>
      </p>
      <p class="clear">
        <label for="project_experiences">项目经验：</label>
        <textarea rows="8" id="project_experiences" class="input" v-model="formInline.personal_skills"
                  placeholder="请您输入项目经验"></textarea>
      </p>
      <p class="clear">
        <label for="other_characters">其它优势：</label>
        <textarea rows="8" id="other_characters" class="input" v-model="formInline.personal_skills"
                  placeholder="请您输入其它优势"></textarea>
      </p>
      <p class="clear">
        <label for="education">学历：</label>
        <input id="education" class="input" v-model="formInline.education" placeholder="请您输入学历"/>
      </p>
      <p class="clear">
        <label for="employment_status">就业状态：</label>
        <input id="employment_status" class="input" v-model="formInline.employment_status" placeholder="请您输入就业状态"/>
      </p>
      <p class="clear">
        <label for="graduate_school">毕业学校：</label>
        <input id="graduate_school" class="input" v-model="formInline.graduate_school" placeholder="请您输入毕业学校"/>
      </p>
      <p class="clear">
        <label for="expectant_salary">期望薪资：</label>
        <input id="expectant_salary" class="input" v-model="formInline.expectant_salary" placeholder="请您输入期望薪资"/>
      </p>
      <p class="clear">
        <label for="job_area">期望地点：</label>
        <input id="job_area" class="input" v-model="formInline.job_area" placeholder="请您输入期望地点"/>
      </p>
      <p class="clear">
        <label for="current_situation">当前状况：</label>
        <input id="current_situation" class="input" v-model="formInline.current_situation" placeholder="请您输入当前状况"/>
      </p>
      <p class="clear">
        <label for="personal_hobbies">个人爱好：</label>
        <input id="personal_hobbies" class="input" v-model="formInline.personal_hobbies" placeholder="请您输入个人爱好"/>
      </p>

      <p class="isoft_top10" style="text-align: center;">
        <Button type="success" @click="handleSubmit">提交</Button>
      </p>
    </div>
  </div>
</template>

<script>
  import {EditResume} from "../../api"

  export default {
    name: "EditResume",
    data() {
      return {
        formInline: {
          id: -1,
          head_img: '',
          sex: '',
          job_start_time: '',
          contact: '',
          email: '',
          birthday: '',
          personal_skills: '',
          project_experiences: '',
          other_characters: '',
          education: '',
          employment_status: '',
          graduate_school: '',
          expectant_salary: '',
          job_area: '',
          current_situation: '',
          personal_hobbies: '',
        },
      }
    },
    methods: {
      handleSubmit: async function () {
        const result = await EditResume(this.formInline);
        if (result.status == "SUCCESS") {
          this.$Message.success("保存成功！");
          this.$router.push({path: '/job/resume_manage'});
        } else {
          this.$Message.error("保存失败!");
        }
      }
    }
  }
</script>

<style scoped>
  p {
    margin-top: 5px;
  }

  label {
    width: 100px;
    float: left;
  }

  input, textarea {
    outline-style: none;
    border: 1px solid #ccc;
    border-radius: 3px;
    padding: 3px 3px;
    width: 850px;
    font-size: 14px;
    font-family: 'Microsoft Yahei', 'PingFangSC', sans-serif;
  }

  /* 设置输入框点击发光效果 */
  input:focus, textarea:focus {
    border-color: #66afe9;
    outline: 0;
    -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px rgba(102, 175, 233, .6);
    box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px rgba(102, 175, 233, .6);
  }
</style>
