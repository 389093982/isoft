<template>
  <div style="width: 95%;position: relative;top: -10px">

    <div style="line-height: 30px;height: 30px;background-color: #eee;text-align: center;margin: 10px 0;cursor: pointer;"
      @click="$router.push({path:'/user/userGuide'})">
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
        <Input v-model.trim="formValidate.course_short_desc" type="textarea" :rows="2" placeholder="请输入课程描述"></Input>
      </FormItem>
      <FormItem label="收费情况" prop="isCharge">
        <RadioGroup v-model="formValidate.isCharge">
          <Radio label='free'>免费</Radio>
          <Radio label='charge'>收费</Radio>
        </RadioGroup>
      </FormItem>
      <div v-if="formValidate.isCharge==='charge'">
        <FormItem label="前" prop="preListFree">
          <Input v-model="formValidate.preListFree" clearable style="width: 50px" />集免费
        </FormItem>
        <FormItem label="价格" prop="price">
          <Input v-model="formValidate.price" placeholder="000.00" clearable style="width: 100px" />￥
        </FormItem>
      </div>
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
  import {checkEmpty,validatePatternForString} from "../../../tools";

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
        }else if(value.length>100){
          callback(new Error('课程描述不能超过100个字符!'));
        }else {
          callback();
        }
      };
      const checkPreListFree = (rule,value,callback) => {
        let patrn = /^([0-9]{1,3})$/;
        if(!validatePatternForString(patrn,value)){
          callback(new Error('集数必须是0-3位数字!'));
        }else {
          callback();
        }
      };
      const checkPrice = (rule,value,callback) => {
        let patrn = /^[0-9]{1,7}(.[0-9]{1,2})?$/;
        if (value === '') {
          callback(new Error('收费金额不能为空！'));
        }else if(!validatePatternForString(patrn,value)){
          callback(new Error('收费金额格式不正确!'));
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
          isCharge:'free',
          preListFree:'4',
          price:'',
        },
        ruleValidate: {
          course_name: [
            {required: true,validator: checkCourseName,  trigger: 'change'}
          ],
          course_type: [
            {required: true,validator:checkCourseType, trigger: 'change'}
          ],
          course_sub_type: [
            {required: true,validator:checkCourseSubType, trigger: 'change'}
          ],
          course_short_desc: [
            {required: true, validator:checkCourseShortDesc, trigger: 'change'}
          ],
          isCharge: [
            {required: true, message:"请选择是否收费", trigger: 'change'}
          ],
          preListFree: [
            {required: true, validator:checkPreListFree, trigger: 'change'}
          ],
          price: [
            {required: true,validator:checkPrice, trigger: 'change'}
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
