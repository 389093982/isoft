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
        alert(result);
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
