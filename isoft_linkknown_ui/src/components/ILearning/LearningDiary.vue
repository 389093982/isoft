<template>
  <!-- 学习日记 -->
  <span>
    <div class="message_container isoft_point_cursor" style="position: relative;">
      <div class="message_box">
        <div class="message1">查看我的学习日记</div>
        <div class="message2">链知网官方交流群</div>
      </div>
      <img src="../../../static/images/common_img/holdon.png" style="width: 100%;height: 430px;"/>
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
    visibility: hidden;
    border-radius: 4px;
    transition: top 1s ease-in-out;
  }
  .message_container:hover .message_box {
    visibility: visible;
    top: 70px;
  }

  .message1 {
    background: rgba(255, 203, 60, 0.85);
    bottom: 35px;
    width: 80%;
    left: 10%;
    padding: 8px 0;
    margin: 10px 0;
    border-radius: 15px;
    font-size: 14px;
    color: #fff;
    text-align: center;
  }

  .message2 {
    background: rgba(255, 203, 60, 0.85);
    bottom: 35px;
    width: 80%;
    left: 10%;
    padding: 8px 0;
    margin: 10px 0;
    border-radius: 15px;
    font-size: 14px;
    color: #ffffff;
    text-align: center;
  }
</style>
