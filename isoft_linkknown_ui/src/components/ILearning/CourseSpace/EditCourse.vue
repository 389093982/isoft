<template>
  <div>

    <div
      style="line-height: 30px;height: 30px;background-color: #eee;text-align: center;margin: 10px 0;cursor: pointer;"
      @click="$router.push({path:'/user/guide'})">
      <Icon type="logo-buffer" style="color: red;" :size="18"/>
      查看课程发布说明
    </div>

    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100">
      <FormItem label="课程名称" prop="course_name">
        <Input v-model.trim="formValidate.course_name" placeholder="请输入课程名称"/>
      </FormItem>
      <FormItem label="课程类型" prop="course_type">
        <Row>
          <Col span="20"><Input v-model="formValidate.course_type" placeholder="请输入课程类型"></Input></Col>
          <Col span="4">
            <ChooseHotCourseType @chooseCourseType="chooseCourseType"/>
          </Col>
        </Row>
      </FormItem>
      <FormItem label="课程子类型" prop="course_sub_type">
        <Input v-model.trim="formValidate.course_sub_type" placeholder="请输入课程子类型"></Input>
      </FormItem>
      <FormItem label="课程描述" prop="course_short_desc">
        <Input v-model.trim="formValidate.course_short_desc" type="textarea" :rows="6"
               placeholder="请输入课程描述"></Input>
      </FormItem>
      <FormItem label="自定义标签语" prop="course_label">
        <Input v-model.trim="formValidate.course_label" placeholder="多个标签语用 / 分割"></Input>
      </FormItem>
      <FormItem>
        <Button type="success" @click="handleSubmit('formValidate')">提交</Button>
        <Button style="margin-left: 8px" @click="handleReset('formValidate')">重置</Button>
      </FormItem>
    </Form>
  </div>
</template>

<script>
  import {EditCourse, ShowCourseDetail} from "../../../api"
  import ChooseHotCourseType from "../CourseType/ChooseHotCourseType"
  import IBeautifulCard from "../../Common/card/IBeautifulCard"
  import {checkEmpty} from "../../../tools";

  export default {
    name: "EditCourse",
    components: {IBeautifulCard, ChooseHotCourseType},
    data() {
      const checkCourseName = (rule,value,callback) => {
        if (value === '') {
          callback(new Error('课程名称不能为空！'));
        }else if(value.length>20){
          callback(new Error('课程名称不能超过20个字符!'));
        }else {
          callback();
        }
      };
      const checkCourseType = (rule,value,callback) => {
        if (value === '') {
          callback(new Error('课程类型不能为空！'));
        }else if(value.length>20){
          callback(new Error('课程类型不能超过20个字符!'));
        }else {
          callback();
        }
      };
      const checkCourseSubType = (rule,value,callback) => {
        if (value === '') {
          callback(new Error('课程子类型不能为空！'));
        }else if(value.length>20){
          callback(new Error('课程子类型不能超过20个字符!'));
        }else {
          callback();
        }
      };
      const checkCourseShortDesc = (rule,value,callback) => {
        if (value === '') {
          callback(new Error('课程描述不能为空！'));
        }else if(value.length>200){
          callback(new Error('课程描述不能超过200个字符!'));
        }else {
          callback();
        }
      };
      return {
        formValidate: {
          id: -1,
          course_name: "",
          course_type: "",
          course_sub_type: "",
          course_label: "",
          course_short_desc: "",
        },
        ruleValidate: {
          course_name: [
            {required: true,validator: checkCourseName,  trigger: 'blur'}
          ],
          course_type: [
            {required: true,validator:checkCourseType, trigger: 'blur'}
          ],
          course_sub_type: [
            {required: true,validator:checkCourseSubType, trigger: 'blur'}
          ],
          course_short_desc: [
            {required: true, validator:checkCourseShortDesc, trigger: 'blur'}
          ],
        },
      }
    },
    methods: {
      handleSubmit(name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            const result = await EditCourse(this.formValidate);
            if (result.status === "SUCCESS") {
              this.$Message.success('提交成功!');
              this.$router.push({path: "/ilearning/courseSpace/myCourseList"});
            } else {
              this.$Message.error('提交失败!');
            }
          } else {
            this.$Message.error('验证失败!');
          }
        })
      },
      handleReset(name) {
        this.$refs[name].resetFields();
      },
      chooseCourseType: function (course_type, course_sub_type) {
        this.formValidate.course_type = course_type;
        this.formValidate.course_sub_type = course_sub_type;
      },
      refreshCourseInfo: async function (course_id) {
        let params = {
          "course_id":course_id,
        };
        const result = await ShowCourseDetail(params);
        if (result.status == "SUCCESS") {
          this.formValidate = result.course;
        }
      }
    },
    mounted() {
      if (!checkEmpty(this.$route.query.course_id) && this.$route.query.course_id > 0) {
        this.refreshCourseInfo(this.$route.query.course_id);
      }
    }
  }
</script>

<style scoped>

</style>
