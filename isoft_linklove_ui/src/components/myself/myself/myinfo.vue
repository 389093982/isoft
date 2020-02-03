<template>
  <div>
    <a @click="showInfo = true">个人信息</a>
    <Drawer :closable="true" width="50%" v-model="showInfo">
      <Tabs value="name1">
        <TabPane label="基础信息" name="name1">基础信息</TabPane>
        <TabPane label="标签二" name="name2">标签二的内容</TabPane>
        <TabPane label="标签三" name="name3">标签三的内容</TabPane>
      </Tabs>
    </Drawer>
  </div>
</template>

<script>
	export default {
		name: "myinfo",
    props:{
		  showInfo:false,
    },
    data () {
      return {
        pStyle: {
          showInfo:false,
          fontSize: '16px',
          color: 'rgba(0,0,0,0.85)',
          lineHeight: '24px',
          display: 'block',
          marginBottom: '16px'
        }
      }
    }
	}
</script>

<style scoped>
  .demo-drawer-profile{
    font-size: 14px;
  }
  .demo-drawer-profile .ivu-col{
    margin-bottom: 12px;
  }
</style>
