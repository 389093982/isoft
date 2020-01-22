<template>
  <div style="padding: 50px;">
    <Form ref="formInline" :model="formInline" :rules="ruleInline" :label-width="100">
      <FormItem prop="require_name" label="需求名称">
        <Input type="text" v-model.trim="formInline.require_name" placeholder="请输入需求名称">
        </Input>
      </FormItem>
      <FormItem prop="require_detail" label="需求详情">
        <Input type="textarea" :rows="5" v-model.trim="formInline.require_detail" placeholder="请输入需求详情">
        </Input>
      </FormItem>
      <Row>
        <Col span="12">
          <FormItem prop="require_start_time" label="需求开始时间">
            <DatePicker v-model="formInline.require_start_time" type="date" placeholder="请选择需求开始时间"
                        @on-change="getDatePicker1" style="width: 100%"></DatePicker>
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem prop="require_end_time" label="需求结束时间">
            <DatePicker v-model="formInline.require_end_time" type="date" placeholder="请选择需求结束时间"
                        @on-change="getDatePicker2" style="width: 100%"></DatePicker>
          </FormItem>
        </Col>
      </Row>
      <FormItem label="需求状态">
        <Select v-model="formInline.require_status">
          <Option value="状态1" key="1">状态1</Option>
          <Option value="状态2" key="2">状态2</Option>
          <Option value="状态3" key="3">状态3</Option>
          <Option value="状态4" key="4">状态4</Option>
        </Select>
      </FormItem>
      <FormItem label="需求责任人">
        <Select v-model="formInline.require_owner">
          <Option value="状态1" key="1">状态1</Option>
          <Option value="状态2" key="2">状态2</Option>
          <Option value="状态3" key="3">状态3</Option>
          <Option value="状态4" key="4">状态4</Option>
        </Select>
      </FormItem>
      <FormItem label="需求开发人">
        <Select v-model="formInline.require_user">
          <Option value="状态1" key="1">状态1</Option>
          <Option value="状态2" key="2">状态2</Option>
          <Option value="状态3" key="3">状态3</Option>
          <Option value="状态4" key="4">状态4</Option>
        </Select>
      </FormItem>
      <FormItem prop="require_mark" label="需求备注">
        <Input type="textarea" :rows="5" v-model.trim="formInline.require_mark" placeholder="请输入需求备注">
        </Input>
      </FormItem>
      <FormItem>
        <Button type="success" @click="handleSubmit('formInline')" :style="{'float':'right'}">确认</Button>
      </FormItem>
    </Form>
  </div>
</template>

<script>
  import {EditRequire} from "../../api"

  export default {
    name: "RequireEdit",
    data() {
      return {
        formInline: {
          id: 0,
          require_name: '',
          require_detail: '',
          require_start_time: null,
          require_end_time: null,
          require_status: '',
          require_owner: '',
          require_user: '',
          require_mark: '',
        },
        ruleInline: {
          require_name: [
            {required: true, message: '请输入需求名称', trigger: 'blur'},
          ],
        }
      }
    },
    methods: {
      getDatePicker1: function (dateTime) {
        this.formInline.require_start_time = dateTime;
      },
      getDatePicker2: function (dateTime) {
        this.formInline.require_end_time = dateTime;
      },
      handleSubmit: async function (name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            const result = await EditRequire(this.formInline);
            if (result.status == "SUCCESS") {
              this.$router.push({path: '/oa/requireList'});
            } else {
              this.$Message.error("保存失败!");
            }
          }
        })
      },
    }
  }
</script>

<style scoped>

</style>
