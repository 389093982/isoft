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
            <FormItem prop="date">
              <DatePicker type="date" placeholder="请选择出生日期" v-model="formValidate.birthday"></DatePicker>
            </FormItem>
          </Tooltip>
        </FormItem>
        <FormItem label="角色" prop="role">
          &nbsp;{{formValidate.role}}
        </FormItem>
        <FormItem label="积分" prop="user_points">
          &nbsp;{{formValidate.user_points}}
        </FormItem>
        <FormItem label="会员等级" prop="vip_level">
          &nbsp;{{formValidate.vip_level}}级&nbsp;&nbsp;&nbsp;{{formValidate.vip_level === 1?'过期时间: '+formValidate.vip_expired_time:''}}
        </FormItem>
        <FormItem label="现居住地" prop="current_residence">
          <Input v-model="formValidate.current_residence" placeholder="请输入现居住地址"></Input>
        </FormItem>
        <FormItem label="家乡" prop="hometown">
          <Input v-model="formValidate.hometown" placeholder="请输入家乡地址"></Input>
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
  export default {
    name: "UserInfo",
    components: {},
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
          birthday: '2020-10-03',
          role: '链知用户',
          user_points: '1856',
          vip_level: 1,
          vip_expired_time: '2020-10-02',
          current_residence: '安徽省合肥市1',
          hometown: '安徽省合肥市2',
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
      handleSubmit (name) {
        this.$refs[name].validate((valid) => {
          if (valid) {
            this.$Message.success('保存成功!');
          }
        })
      },
    }
  }
</script>

<style scoped>

</style>
