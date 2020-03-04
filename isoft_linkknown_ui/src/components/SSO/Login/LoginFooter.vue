<template>
  <div id="footer"
       style="background-color: #ffffff;text-align: center;clear: both;padding: 30px;height: 60px;font-size: 12px;padding-top: 10px">
    <p style="line-height: 25px;">
      <a href="#">关于我们</a> |
      <a href="#">联系我们</a> |
      <a href="#">人才招聘</a> |
      <a href="#">商家入驻</a> |
      <a href="#">广告服务</a> |
      <a href="#">友情链接</a> |
      <a href="#" @click="$router.push({path:'/iblog/blog_list'})">链知博客</a> |
      <a href="#">更多</a>
    </p>
    <p style="line-height: 25px;">Copyright © 2019-2020 LinkKnown 版权所有</p>
  </div>
</template>

<script>
  export default {
    name: "LoginFooter"
  }
</script>

<style scoped>
  #footer a {
    color: #666;
    text-decoration: none;
  }

  #footer a:hover {
    color: red;
    text-decoration: none;
  }
</style>
