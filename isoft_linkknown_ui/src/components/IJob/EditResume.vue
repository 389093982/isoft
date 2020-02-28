<template>
  <div>
    <div class="isoft_bg_white" style="padding: 50px;">
      <Form ref="formInline" :model="formInline" :rules="ruleValidate" :label-width="100">
        <Row>
          <Col span="12">
            <FormItem label="姓名" prop="user_name">
              <Input v-model.trim="formInline.user_name" placeholder="请您输入姓名"></Input>
            </FormItem>
            <FormItem label="出生年月" prop="birthday">
              <Input v-model.trim="formInline.birthday" placeholder="请您输入出生年月"></Input>
            </FormItem>
            <FormItem label="毕业学校" prop="graduate_school">
              <Input v-model.trim="formInline.graduate_school" placeholder="请您输入毕业学校"></Input>
            </FormItem>
            <FormItem label="参加工作时间" prop="job_start_time">
              <DatePicker v-model="formInline.job_start_time" type="date" placeholder="请选择参加工作时间"
                          format="yyyy-MM-dd" style="width: 100%;"></DatePicker>
            </FormItem>
            <FormItem label="期望薪资" prop="expectant_salary">
              <Input v-model.trim="formInline.expectant_salary" placeholder="请您输入期望薪资"></Input>
            </FormItem>
            <FormItem label="手机号" prop="contact">
              <Input v-model.trim="formInline.contact" placeholder="请您输入手机号"></Input>
            </FormItem>
            <FormItem label="头像" prop="head_img">
              <Input v-model.trim="formInline.head_img" placeholder="请上传个人头像"></Input>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="性别" prop="sex">
              <Select v-model="formInline.sex">
                <Option value="男" key="1">男</Option>
                <Option value="女" key="2">女</Option>
              </Select>
            </FormItem>
            <FormItem label="年龄" prop="age">
              <InputNumber :max="100" :min="1" v-model="formInline.age" style="width: 100%;"></InputNumber>
            </FormItem>
            <FormItem label="学历" prop="education">
              <Input v-model.trim="formInline.education" placeholder="请您输入学历"></Input>
            </FormItem>
            <FormItem label="就业状态" prop="employment_status">
              <Input v-model.trim="formInline.employment_status" placeholder="请您输入就业状态"></Input>
            </FormItem>
            <FormItem label="期望地点" prop="job_area">
              <Input readonly="readonly" v-model.trim="formInline.job_area" placeholder="请您输入期望地点"
                     @on-focus="handleFocus('areaChooser')"></Input>
              <IAreaChooser ref="areaChooser" title="地区选择" @handleSubmit="handleAreaSubmit"/>
            </FormItem>
            <FormItem label="邮箱" prop="email">
              <Input v-model.trim="formInline.email" type="email" placeholder="请您输入邮箱"></Input>
            </FormItem>
            <FormItem label="当前状况" prop="current_situation">
              <Input v-model.trim="formInline.current_situation" placeholder="请您输入当前状况"></Input>
            </FormItem>
          </Col>
        </Row>
        <FormItem label="个人技能" prop="personal_skills">
          <Input type="textarea" :rows="8" v-model.trim="formInline.personal_skills" placeholder="请您输入个人技能"></Input>
        </FormItem>
        <FormItem label="项目经验" prop="project_experiences">
          <Input type="textarea" :rows="8" v-model.trim="formInline.project_experiences" placeholder="请您输入项目经验"></Input>
        </FormItem>
        <FormItem label="其它优势" prop="other_characters">
          <Input type="textarea" :rows="8" v-model.trim="formInline.other_characters" placeholder="请您输入其它优势"></Input>
        </FormItem>
        <FormItem label="个人爱好" prop="personal_hobbies">
          <Input type="textarea" :rows="8" v-model.trim="formInline.personal_hobbies" placeholder="请您输入个人爱好"></Input>
        </FormItem>
        <FormItem>
          <div class="isoft_button_green1" style="width: 250px;margin: 0 auto;"
               @click="handleSubmit('formInline')">保存简历
          </div>
        </FormItem>
      </Form>
    </div>
  </div>
</template>

<script>
  import {EditResume, QueryResume} from "../../api"
  import IAreaChooser from "../Common/IAreaChooser"
  import {checkEmpty, GetLoginUserName, strSplit} from "../../tools";
  import {genValidator, validateEmail, validatePhone} from "../../tools/regex";

  export default {
    name: "EditResume",
    components: {IAreaChooser},
    data() {
      return {
        formInline: {
          id: -1,
          head_img: '',
          user_name: '',
          age: '',
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
        ruleValidate: {
          user_name: [
            {required: true, message: '姓名不能为空!', trigger: 'blur'}
          ],
          contact: [
            {required: true, message: '手机号不能为空!', trigger: 'blur'},
            {validator: genValidator(validatePhone, "手机号不正确!"), trigger: 'blur'}
          ],
          sex: [
            {required: true, message: '性别不能为空!', trigger: 'blur'}
          ],
          birthday: [
            {required: true, message: '出生年月不能为空!', trigger: 'blur'}
          ],
          education: [
            {required: true, message: '学历不能为空!', trigger: 'blur'}
          ],
          graduate_school: [
            {required: true, message: '毕业学校不能为空!', trigger: 'blur'}
          ],
          age: [
            {required: true, message: '年龄不能为空!', trigger: 'blur'}
          ],
          email: [
            {required: true, message: '邮箱不能为空!', trigger: 'blur'},
            {validator: genValidator(validateEmail, "邮箱格式不正确!"), trigger: 'blur'}
          ],
        },
      }
    },
    methods: {
      handleFocus: function (name) {
        if (name === "areaChooser") {
          let arr = strSplit(this.formInline.job_area, "-");
          // 暂时不做回显
          this.$refs.areaChooser.showModal();
        }
      },
      handleSubmit(name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            const result = await EditResume(this.formInline);
            if (result.status === "SUCCESS") {
              this.$Message.success("保存成功！");
              this.$router.push({path: '/job/resume_manage', query: {'user_name': GetLoginUserName()}});
            } else {
              this.$Message.error("保存失败!");
            }
          }
        })
      },
      refreshQueryResume: async function () {
        if (!checkEmpty(GetLoginUserName())) {
          const result = await QueryResume({user_name: GetLoginUserName()});
          if (result.status === "SUCCESS") {
            this.formInline = result.resume;
          }
        }
      },
      handleAreaSubmit: function (province, city, area) {
        if (checkEmpty(city)) {
          this.formInline.job_area = province;
        } else if (checkEmpty(area)) {
          this.formInline.job_area = province + '-' + city;
        } else {
          this.formInline.job_area = province + '-' + city + '-' + area;
        }
      }
    },
    mounted() {
      this.refreshQueryResume();
    }
  }
</script>

<style scoped>

</style>
