<template>
  <!-- 按钮触发模态框 -->
  <!-- ref 的作用是为了在其它地方方便的获取到当前子组件 -->
  <ISimpleBtnTriggerModal ref="triggerModal" btn-text="新增" modal-title="新增调度指令" :modal-width="600">
    <!-- 表单信息 -->
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100">
      <FormItem label="任务名称" prop="task_name">
        <Input v-model.trim="formValidate.task_name" placeholder="任务名称目前版本需要和流程名称保持一致"></Input>
      </FormItem>
      <FormItem label="任务类型" prop="task_type">
        <Select v-model="formValidate.task_type">
          <Option value="iwork_quartz">iwork_quartz</Option>
        </Select>
      </FormItem>
      <FormItem label="cron 表达式" prop="cron_str">
        <Input v-model.trim="formValidate.cron_str" placeholder="请输入 cron 表达式"></Input>
      </FormItem>
      <FormItem>
        <Button type="success" @click="handleSubmit('formValidate')" style="margin-right: 6px">提交</Button>
        <Button type="warning" @click="handleReset('formValidate')" style="margin-right: 6px">重置</Button>
      </FormItem>
    </Form>
  </ISimpleBtnTriggerModal>
</template>

<script>
  import ISimpleBtnTriggerModal from "../../Common/modal/ISimpleBtnTriggerModal"
  import {EditQuartz} from "../../../api/index"
  import {validateCron} from "../../../tools/index"

  export default {
    name: "QuartzEdit",
    components:{ISimpleBtnTriggerModal},
    data(){
      const _validateCron = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('cron表达式不能为空!'));
        } else if (!validateCron(value)) {
          callback(new Error('cron表达式格式错误!'));
        } else {
          callback();
        }
      };
      return {
        formValidate: {
          id: 0,
          task_name: '',
          task_type: '',
          cron_str: '',
        },
        ruleValidate: {
          task_name: [
            { required: true, message: '任务名称不能为空!', trigger: 'blur' }
          ],
          task_type: [
            { required: true, message: '任务类型不能为空!', trigger: 'blur' }
          ],
          cron_str: [
            { validator: _validateCron, trigger: 'blur' }
          ]
        },
      }
    },
    methods:{
      handleSubmit (name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            const result = await EditQuartz(this.formValidate);
            if (result.status === "SUCCESS") {
              this.$Message.success('提交成功!');
              // 调用子组件隐藏 modal (this.refs.xxx.子组件定义的方法())
              this.$refs.triggerModal.hideModal();
              // 通知父组件添加成功
              this.$emit('handleSuccess');
            }else{
              this.$Message.error('提交失败!');
            }
          }
        })
      },
      handleReset (name) {
        this.formValidate.id = 0;
        this.$refs[name].resetFields();
      },
      initData(formdata) {
        this.formValidate = formdata;
        this.$refs.triggerModal.triggerClick();
      }
    }
  }
</script>

<style scoped>

</style>
