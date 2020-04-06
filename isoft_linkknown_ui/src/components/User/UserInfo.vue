<template>
  <div>

    <!--帽子抽屉-->
    <Drawer title="我的帽子" placement="left" v-model="hatDrawer" width="14">
      <div style="text-align: center">
        <div class="hovered hvr-grow isoft_hover_red2" style="width: 100%;height: 35px;font-size: 20px;text-align: center;cursor: pointer;">
          <span @click="removeHat()">摘下帽子</span>
        </div><hr><br><br>
        <Tooltip max-width="200" content="我要再提醒你一次，金箍戴上之后你再也不是个凡人，人世间的情欲不能再沾半点。如果动心这个金箍就会在你头上越收越紧，苦不堪言！只有好好学习，来链知网多蹭热量才是王道哦^_^">
          <img @click="chooseHat('hat01')" class="hvr-grow" style="cursor: pointer" src="../../../static/images/vipHat/hat01.png" width="80" height="20" @error="defImg()"/>
        </Tooltip>
      </div>
     </Drawer>

    <!--帽子&头像-->
    <div style="float:left;width: 33%;">
      <div style="margin: 150px 0 0 248px ">
        <!--更换帽子-->
        <div v-if="formValidate.vip_level>0" style="position: relative;top: -50px;">
          <Button @click="hatDrawer=true"><i style="color: #E8B66E">vip</i> · 更换帽子</Button>
        </div>
        <div v-else style="position: relative;top: -50px;visibility:hidden">
          <Button>隐藏按钮</Button>
        </div>
        <!--帽子和头像-->
        <div>
          <HatAndFacePicture :src="formValidate.small_icon" :vip_level="formValidate.vip_level" :hat_in_use="formValidate.hat_in_use"></HatAndFacePicture>
        </div>
        <!--修改头像-->
        <div style="position: absolute;top: 380px;">
          <UploadHeadSculpture ref="fileUpload" @uploadComplete="uploadComplete" :action="fileUploadUrl" uploadLabel="修改头像"/>
        </div>
      </div>
    </div>

    <!--用户详细信息-->
    <div style="float:left;width: 40%;height: auto;margin-top: 20px;">
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
    <div style="clear: both"></div>

  </div>
</template>
<script>
  import IAreaChooser from "../Common/IAreaChooser";
  import {checkEmpty, copyObj, GetLoginUserName} from "../../tools"
  import {GetUserDetail, UpdateUserDetail,UpdateUserIcon,fileUploadUrl} from "../../api"
  import HatAndFacePicture from "../Common/HatAndFacePicture/HatAndFacePicture";
  import UploadHeadSculpture from "../Common/file/UploadHeadSculpture";

  export default {
    name: "UserInfo",
    components: {UploadHeadSculpture, HatAndFacePicture, IAreaChooser},
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
        hatDrawer:false,
        fileUploadUrl: fileUploadUrl + "?table_name=user&table_field=small_icon",
        formValidate:{
          hat:'',
          hat_in_use:'',
          small_icon:'',
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
      removeHat:function(){
        this.formValidate.hat_in_use = 'N';
      },
      chooseHat:function(hat){
        this.formValidate.hat_in_use = 'Y';
        this.formValidate.hat=hat;
      },
      uploadComplete: async function (data) {
        if (data.status === "SUCCESS") {
          this.$refs.fileUpload.hideModal();
          let uploadFilePath = data.fileServerPath;
          const result = await UpdateUserIcon(GetLoginUserName(), uploadFilePath);
          if (result.status === "SUCCESS") {
            this.GetUserDetail();
          }
        }
      },
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
          this.formValidate = result.user;
          this.formValidate.birthday = result.user.birthday==='0000-00-00'?'':result.user.birthday;
        }
      },
      handleSubmit: function(name){
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            let params = copyObj(this.formValidate);
            params.user_name = GetLoginUserName();
            params.birthday = new Date(params.birthday).getTime();
            const result = await UpdateUserDetail(params);
            if (result.status === "SUCCESS") {
              this.$Message.success('保存成功!');
              this.$router.push({path:"/user/userDetail"});
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
