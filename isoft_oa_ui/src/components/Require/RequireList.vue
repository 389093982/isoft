<template>
  <div style="padding: 50px;">
    <Button @click="$router.push({path:'/oa/requireEdit'})">新增需求</Button>

    <Row style="padding: 15px 0px;border-bottom: 1px solid #d7dde4;">
      <Col span="4">需求名称</Col>
      <Col span="4">需求详情</Col>
      <Col span="4">迭代周期(迭代时间)</Col>
      <Col span="4">需求状态</Col>
      <Col span="2">需求 owner</Col>
      <Col span="2">需求分配人员</Col>
      <Col span="4">操作</Col>
    </Row>
    <Row v-for="(require, index) in requires" style="padding: 15px 0px;border-bottom: 1px solid #d7dde4;">
      <Col span="4">{{require.require_name}}</Col>
      <Col span="4">{{require.require_detail}}</Col>
      <Col span="4">{{require.require_time}}</Col>
      <Col span="4">{{require.require_status}}</Col>
      <Col span="2">{{require.require_owner}}</Col>
      <Col span="2">{{require.require_user}}</Col>
      <Col span="4">操作xxx</Col>
    </Row>
  </div>
</template>

<script>
  export default {
    name: "RequireList",
    data() {
      return {
        requires: [
          {
            'require_name': 'iwork 框架1.0迭代',
            'require_detail': 'iwork 框架1.0迭代',
            'require_time': '一月份需求 1.1~1.7',
            'require_status': '已完成',
            'require_owner': 'z00000',
            'require_user': 'z00000',
          },
          {
            'require_name': 'iwork 框架1.0迭代',
            'require_detail': 'iwork 框架1.0迭代',
            'require_time': '一月份需求 1.1~1.7',
            'require_status': '已完成',
            'require_owner': 'z00000',
            'require_user': 'z00000',
          }
        ]
      }
    }
  }
</script>

<style scoped>

</style>
