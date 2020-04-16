<template>
  <!-- 学习日记 -->
  <span>
    <div class="message_container isoft_point_cursor" style="position: relative;">
      <div class="message_box">查看我的学习日记  ~~~~  链知网官方交流群</div>
      <img src="../../../static/images/holdon.png" style="width: 100%;height: 430px;"/>
    </div>
  </span>
</template>

<script>
  export default {
    name: "LearningDiary",
    data (){
      return {
      }
    },
    methods:{
    }
  }
</script>

<style scoped>
  .message_box {
    position: absolute;
    width:80%;
    left:15%;
    top: 200px;
    min-height: 200px;
    padding: 10px 8px;
    background-color: #eff3f6;
    color: #28334B;
    visibility: hidden;
    border-radius: 4px;
    transition: top 1s ease-in;
  }
  .message_container:hover .message_box {
    visibility: visible;
    top: 70px;
  }
</style>
