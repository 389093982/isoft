<template>
  <div>

    <div style="width: 40%;height: auto;margin: 20px 0 0 350px;">
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
        <FormItem label="昵称" prop="nick_name">
          <Input v-model="formValidate.nick_name" placeholder="请输入您的昵称"></Input>
        </FormItem>
        <FormItem label="性别" prop="gender">
          <RadioGroup v-model="formValidate.gender">
            <Radio label="male">男</Radio>
            <Radio label="female">女</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="生日">
          <Tooltip content="温馨提示:  点击  ‘年份’  可以快速选择哦！" placement="top-start">
            <FormItem>
              <DatePicker type="date" placeholder="请选择出生日期" v-model="formValidate.birthday"></DatePicker>
            </FormItem>
          </Tooltip>
        </FormItem>
        <FormItem label="角色" prop="role_name">
          &nbsp;{{formValidate.role_name==='admin'?'管理员':'链知用户'}}
        </FormItem>
        <FormItem label="积分" prop="user_points">
          &nbsp;{{formValidate.user_points}}
        </FormItem>
        <FormItem label="会员等级" prop="vip_level">
          &nbsp;{{formValidate.vip_level}}级&nbsp;&nbsp;&nbsp;{{formValidate.vip_level === 1?' ( 过期时间: '+formValidate.vip_expired_time+' ) ':''}}
        </FormItem>
        <FormItem label="现居住地" prop="current_residence">
          <Input v-model="formValidate.current_residence"  placeholder="请选择现居住地址" @on-focus="showCurrentResidenceAreaChooseModal()"></Input>
          <IAreaChooser ref="currentResidenceAreaChooser" title="现居住地址" @handleSubmit="handleCurrentResidenceAreaSubmit"/>
        </FormItem>
        <FormItem label="家乡" prop="hometown">
          <Input v-model="formValidate.hometown" placeholder="请选择家乡地址" @on-focus="showHometownAreaChooseModal()"></Input>
          <IAreaChooser ref="hometownAreaChooser" title="家乡" @handleSubmit="handleHometownAreaSubmit"/>
        </FormItem>

        <!--提交-->
        <FormItem>
          <Button type="primary" @click="handleSubmit('formValidate')">保存</Button>
        </FormItem>
      </Form>
    </div>

  </div>
</template>
<script>
  import IAreaChooser from "../Common/IAreaChooser";
  import {checkEmpty, GetLoginUserName} from "../../tools"
  import {GetUserDetail,UpdateUserDetail} from "../../api"

  export default {
    name: "UserInfo",
    components: { IAreaChooser},
    data () {
      const checkNickName = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('昵称不能为空!'));
        } else if (value.length>10) {
          callback(new Error('昵称? 请不要超过10个字符哦'));
        } else {
          callback();
        }
      };
      return {
        formValidate:{
          nick_name: '',
          gender: '',
          birthday: '',
          role_name: '',
          user_points: '',
          vip_level: 0,
          vip_expired_time: '',
          current_residence:'',
          hometown: '',
        },
        ruleValidate: {
          nick_name: [
            { required: true,validator:checkNickName, trigger: 'blur' }
          ],
          gender: [
            { required: true, message:'性别不能为空！',trigger: 'change' }
          ],
        }
      }
    },
    methods: {
      handleCurrentResidenceAreaSubmit: function (province, city, area) {
        if (checkEmpty(city)) {
            this.formValidate.current_residence = province;
        } else if (checkEmpty(area)) {
            this.formValidate.current_residence = province + '-' + city;
        } else {
            this.formValidate.current_residence = province + '-' + city + '-' + area;
        }
      },
      handleHometownAreaSubmit: function (province, city, area) {
        if (checkEmpty(city)) {
          this.formValidate.hometown = province;
        } else if (checkEmpty(area)) {
          this.formValidate.hometown = province + '-' + city;
        } else {
          this.formValidate.hometown = province + '-' + city + '-' + area;
        }
      },
      showCurrentResidenceAreaChooseModal: function () {
        this.$refs.currentResidenceAreaChooser.showModal();
      },
      showHometownAreaChooseModal: function () {
        this.$refs.hometownAreaChooser.showModal();
      },
      GetUserDetail:async function () {
        const result = await GetUserDetail(GetLoginUserName());
        if (result.status === "SUCCESS") {
          this.formValidate.nick_name = result.user.nick_name;
          this.formValidate.gender = result.user.gender;
          this.formValidate.birthday = result.user.birthday==='0000-00-00'?'':result.user.birthday;
          this.formValidate.role_name = result.user.role_name;
          this.formValidate.user_points = result.user.user_points;
          this.formValidate.vip_level = result.user.vip_level;
          this.formValidate.vip_expired_time = result.user.vip_expired_time;
          this.formValidate.current_residence = result.user.current_residence;
          this.formValidate.hometown = result.user.hometown;
        }
      },
      handleSubmit: function(name){
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            let params = {
              "user_name":GetLoginUserName(),
              "nick_name":this.formValidate.nick_name,
              "gender":this.formValidate.gender,
              "birthday":this.formValidate.birthday,
              "current_residence":this.formValidate.current_residence,
              "hometown":this.formValidate.hometown,
            };
            const result = await UpdateUserDetail(params);
            if (result.status === "SUCCESS") {
              this.$Message.success('保存成功!');
              this.$router.push({path:"/user/detail"});
            }
          }
        })
      },
    },
    mounted(){
      this.GetUserDetail();
    },
  }
</script>

<style scoped>

</style>
