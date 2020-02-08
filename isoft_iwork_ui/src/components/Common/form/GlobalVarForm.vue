<template>
  <!-- 表单信息 -->
  <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="150">
    <FormItem :label="formKeyLabel" prop="formkey">
      <Input v-model.trim="formValidate.formkey" :placeholder="formKeyPlaceholder"></Input>
    </FormItem>
    <FormItem :label="formValueLabel01" prop="formvalue01">
      <Select v-model.trim="formValidate.formvalue01" :placeholder="formValuePlaceholder01" style="width:100%">
        <Option v-for="item in EnvNameList" :value="item" :key="item">{{ item }}</Option>
      </Select>
    </FormItem>
    <FormItem :label="formValueLabel02" prop="formvalue02">
      <Input v-model.trim="formValidate.formvalue02" :placeholder="formValuePlaceholder02"></Input>
    </FormItem>
    <FormItem>
      <Button type="success" @click="handleSubmit('formValidate')" style="margin-right: 6px">提交</Button>
      <Button type="warning" @click="handleReset('formValidate')" style="margin-right: 6px">重置</Button>
    </FormItem>
  </Form>
</template>

<script>
  export default {
    name: "GlobalVarForm",
    props:{
      formKeyLabel:{
        type:String,
        default:'formKeyLabel',
      },
      formValueLabel01:{
        type:String,
        default:'formValueLabel01',
      },
      formValueLabel02:{
        type:String,
        default:'formValueLabel02',
      },
      formKeyPlaceholder:{
        type:String,
        default:'请输入 formKey',
      },
      formValuePlaceholder01:{
        type:String,
        default:'请输入 formValue01',
      },
      formValuePlaceholder02:{
        type:String,
        default:'请输入 formValue02',
      },
      formkeyValidator:{
        type: Function,
      },
      formvalueValidator:{
        type: Function,
      },
      EnvNameList:{
        type:Array,
        required:true,
      }
    },
    data(){
      return {
        formValidate: {
          formmode: null,    // 用户回显使用
          formkey: '',
          formvalue01: '',
          formvalue02: '',
        },
        ruleValidate: {
          formkey: [
            { validator: this.formkeyValidator, trigger: 'blur' },
            { required: true, message: 'formkey 不能为空!', trigger: 'blur' }
          ],
          formvalue01: [
            { validator: this.formvalueValidator, trigger: 'blur' },
            { required: true, message: '请选择!', trigger: 'change' }
          ],
          formvalue02: [
            { validator: this.formvalueValidator, trigger: 'blur' },
            { required: true, message: 'formvalue02 不能为空!', trigger: 'blur' }
          ],
        },
      }
    },
    methods:{
      handleSubmit (name) {
        this.$refs[name].validate((valid) => {
          if (valid) {
            this.$emit("handleSubmit", this.formValidate.formmode, this.formValidate.formkey, this.formValidate.formvalue01, this.formValidate.formvalue02);
          }
        })
      },
      handleSubmitSuccess (msg){
        this.formValidate.formmode = null;
        this.handleReset('formValidate');
        this.$Message.success(msg);
      },
      handleSubmitError (msg){
        this.$Message.error(msg);
      },
      initFormData (formmode, formkey, formvalue01,formvalue02){
        this.formValidate.formmode = formmode;
        this.formValidate.formkey = formkey;
        this.formValidate.formvalue01 = formvalue01;
        this.formValidate.formvalue02 = formvalue02;
      },
      handleReset (name) {
        this.$refs[name].resetFields();
      },
    }
  }
</script>

<style scoped>

</style>
