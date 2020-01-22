<template>
  <div style="padding: 50px;">
    <Row :gutter="10">
      <Col span="3">
        <div class="box" @click="$router.push({path:'/oa/requireList'})">需求管理</div>
      </Col>
      <Col span="3">
        <div class="box">版本管理</div>
      </Col>
      <Col span="3">
        <div class="box">权限管理</div>
      </Col>
      <Col span="3">
        <div class="box">问题单管理</div>
      </Col>
    </Row>
  </div>
</template>

<script>
  export default {
    name: "OAItem"
  }
</script>

<style scoped>
  .box {
    background-color: rgba(255, 255, 0, 0.2);
    padding: 5px;
    min-height: 150px;
    font-size: 18px;
    cursor: pointer;
  }
</style>
