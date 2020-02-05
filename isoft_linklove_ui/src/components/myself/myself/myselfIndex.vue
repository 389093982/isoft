<template>
	<div>

    <!--第一行-->
    <div>
      <!--两张头像-->
      <div style="float: left;width: 40%">
        <Card style="float: left;width:50%;height: 100%">
          <div style="text-align:center">
            <img src="../../../../static/img/personImg/lizhien/9.png" style="width:100%;height: 150px">
            <em><myinfo></myinfo></em>
          </div>
        </Card>
        <Card style="float: left;width:50%;height: 100%">
          <div style="text-align:center">
            <img src="../../../../static/img/personImg/lizhien/12.png" style="width:100%;height: 150px">
            <em style="color: #ff537c;cursor: pointer">我的女友</em>
          </div>
        </Card>
      </div>
      <!--头像右侧应用-->
      <div style="float: right;width: 59%;">
          <div>
            <Badge :count="3" type="lime"><Tag color="magenta">女友消息</Tag></Badge>
            <Badge :count="3" type="info"><Tag color="blue">家族消息</Tag></Badge>
            <Badge :count="5" type="success"><Tag color="cyan">朋友消息</Tag></Badge>
            <Badge :count="2" type="warning"><Tag color="lime">大厅消息</Tag></Badge>
          </div>
      </div>
      <div style="clear: both"></div>
    </div>

    <!--个人信息&修改-->
    <div style="margin: 10px 0 0 0 ">
      <div style="float: left;width: 40%;">
        <div>
          <Badge :count="3" type="lime"><Tag color="magenta">女友消息</Tag></Badge>
          <Badge :count="3" type="info"><Tag color="blue">家族消息</Tag></Badge>
          <Badge :count="5" type="success"><Tag color="cyan">朋友消息</Tag></Badge>
          <Badge :count="2" type="warning"><Tag color="lime">大厅消息</Tag></Badge>
        </div>
      </div>
      <div style="float: right;width: 60%">
        资料展示。。。
      </div>
    </div>


  </div>
</template>

<script>
	import myinfo from "./myinfo";
  export default {
		name: "myselfIndex",
    components: {myinfo},
    data(){
		  return{

      }
    }

	}
</script>

<style scoped>

</style>
