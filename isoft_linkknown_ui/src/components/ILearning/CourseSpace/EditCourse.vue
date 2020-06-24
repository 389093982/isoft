<template>
  <div style="width: 95%;position: relative;top: -10px">

    <Row>
      <Col span="21">
        <div style="line-height: 30px;height: 30px;background-color: #eee;text-align: center;margin: 10px 0;cursor: pointer;"
             @click="$router.push({path:'/user/userGuide'})">
          <Icon type="logo-buffer" style="color: red;" :size="18"/>
          查看课程发布说明
        </div>
      </Col>
    </Row>

    <Row>
      <Col span="5">
        <div class="img_box" @click="handleUpload" style="position: relative;top: 120px;">
          <div v-if="formValidate.small_image">
            <span class="animated faster fadeIn isoft_point_cursor isoft_close" @click="formValidate.small_image = ''" title="换张图片">
              <Icon type="md-close" size="20"/>
            </span>
            <img :src="formValidate.small_image" style="width: 180px;height: 120px;"/>
          </div>
          <div v-else>
            <div class="isoft_add isoft_point_cursor"></div>
            <div class="isoft_hover_desc" style="margin:5px 0 0 30px;">点击上传/更新封面</div>
            <IFileUpload ref="fileUpload" size="small" :show-button="false" :auto-hide-modal="true" @uploadComplete="uploadComplete" :action="fileUploadUrl" uploadLabel="上传课程封面"/>
          </div>
        </div>
      </Col>
      <Col span="16">
        <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100">
          <FormItem label="课程名称" prop="course_name">
            <Input v-if="this.$route.query.course_id != null" readonly v-model.trim="formValidate.course_name" placeholder="请输入课程名称"/>
            <Input v-else v-model.trim="formValidate.course_name" placeholder="请输入课程名称"/>
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
              <Input v-model="formValidate.preListFree" clearable style="width: 50px" />&nbsp;集免费
            </FormItem>
            <Row>
              <Col span="10">
                <FormItem label="价格" prop="price">
                  <Icon type="logo-yen" /><Input v-model="formValidate.price" placeholder="000.00" clearable style="width: 100px" />
                </FormItem>
              </Col>
              <Col span="10" v-if="this.$route.query.course_id != null">
                <FormItem label="老价格" prop="old_price">
                  <Icon type="logo-yen" />{{formValidate.old_price}}
                </FormItem>
              </Col>
            </Row>
            <Row v-if="this.$route.query.course_id != null">
              <Col span="24">
                <FormItem label="展示老价格" prop="is_show_old_price">
                  <RadioGroup v-model="formValidate.is_show_old_price">
                    <Radio label='Y'>展示</Radio>
                    <Radio label='N'>不展示</Radio>
                  </RadioGroup>
                </FormItem>
              </Col>
            </Row>
          </div>
          <FormItem label="自定义标签语" prop="course_label">
            <Input v-model.trim="formValidate.course_label" placeholder="多个标签语用 / 分割"></Input>
          </FormItem>
          <FormItem>
            <Button type="success" @click="handleSubmit('formValidate')">提交</Button>
            <Button style="margin-left: 8px" @click="handleReset('formValidate')">重置</Button>
          </FormItem>
        </Form>
      </Col>
    </Row>

  </div>
</template>

<script>
  import {EditCourse, ShowCourseDetail,fileUploadUrl} from "../../../api"
  import ChooseHotCourseType from "../CourseType/ChooseHotCourseType"
  import IBeautifulCard from "../../Common/card/IBeautifulCard"
  import {checkEmpty,validatePatternForString,handleSpecial} from "../../../tools";
  import IFileUpload from "../../Common/file/IFileUpload"

  export default {
    name: "EditCourse",
    components: {IBeautifulCard, ChooseHotCourseType,IFileUpload},
    data() {
      const checkCourseName = (rule,value,callback) => {
        if (value === '') {
          callback(new Error('课程名称不能为空'));
        }else if(value.length>20){
          callback(new Error('课程名称不能超过20个字符'));
        }else {
          callback();
        }
      };
      const checkCourseType = (rule,value,callback) => {
        if (value === '') {
          callback(new Error('课程类型不能为空'));
        }else if(value.length>20){
          callback(new Error('课程类型不能超过20个字符'));
        }else {
          callback();
        }
      };
      const checkCourseSubType = (rule,value,callback) => {
        if (value === '') {
          callback(new Error('课程子类型不能为空'));
        }else if(value.length>20){
          callback(new Error('课程子类型不能超过20个字符'));
        }else {
          callback();
        }
      };
      const checkCourseShortDesc = (rule,value,callback) => {
        if (value === '') {
          callback(new Error('课程描述不能为空！'));
        }else if(value.length>100){
          callback(new Error('课程描述不能超过100个字符'));
        }else {
          callback();
        }
      };
      const checkPreListFree = (rule,value,callback) => {
        let patrn = /^([0-9]{1,3})$/;
        if(!validatePatternForString(patrn,value)){
          callback(new Error('集数必须是0-3位正整数'));
        }else {
          callback();
        }
      };
      const checkPrice = (rule,value,callback) => {
        let patrn = /^[0-9]{1,7}(.[0-9]{1,2})?$/;
        if (value === '') {
          callback(new Error('金额不能为空'));
        }else if(!validatePatternForString(patrn,value)){
          callback(new Error('金额格式不正确'));
        }else if(value<=0){
          callback(new Error('金额必须大于0'));
        }else {
          callback();
        }
      };
      return {
        fileUploadUrl: fileUploadUrl + "?table_name=course&table_field=small_image",
        formValidate: {
          id: -1,
          small_image:"",
          course_name: "",
          course_type: "",
          course_sub_type: "",
          course_label: "",
          course_short_desc: "",
          isCharge:'free',
          preListFree:'',
          price:'',
          old_price:'0.00',
          is_show_old_price:'N'
        },
        ruleValidate: {
          small_image: [
            {required: true, message:"请上传课程封面图片", trigger: 'change'}
          ],
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
      handleUpload: function () {
        this.$refs.fileUpload.showModal();
      },
      uploadComplete: async function (data) {
        if (data.status === "SUCCESS") {
          if (data.status === "SUCCESS") {
            this.formValidate.small_image = data.fileServerPath;
          }
        }
      },
      handleSubmit(name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            if (checkEmpty(this.formValidate.small_image)) {
              this.$Message.warning("请先上传课程封面！");
              return;
            }
            // 在真正修改前做个处理
            this.formValidate.small_image = handleSpecial(this.formValidate.small_image);
            //将价格保留2位小数
            this.formValidate.price = (this.formValidate.price*1).toFixed(2);
            this.formValidate.old_price = (this.formValidate.old_price*1).toFixed(2);
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
  .isoft_add {
    border: 2px solid #afafaf;
    width: 180px;
    height: 120px;
    color: #ccc;
    position: relative;
  }
  .isoft_add::before{
    content: '';
    position: absolute;
    left: 50%;
    top: 50%;
    width: 60px;
    margin-left: -30px;
    margin-top: -5px;
    /*利用伪类before和其 border-top 来设置一个“横”：*/
    border-top: 10px solid;
  }
  .isoft_add::after {
    content: '';
    position: absolute;
    left: 50%;
    top: 50%;
    height: 60px;
    margin-left: -5px;
    margin-top: -30px;
    /*使用after伪类和 border-left 设置一个“竖”：*/
    border-left: 10px solid;
  }
  .isoft_add:hover {
    color: #a5a5a5;
  }

  .isoft_close {
    position: absolute;
    display: none;
    right: 2px;
    top: -12px;
    padding: 1px;
    color: #959595;
    background: rgba(248, 248, 248, 1);
    border: 1px solid #959595;
    border-radius: 50%;
    transition: transform 0.5s ease-in;
  }
  .isoft_close:hover {
    color: #000000;
    border: 1px solid #000000;
    transform: rotateZ(360deg);
  }

  .img_box:hover .isoft_close {
    display: block;
  }
</style>
