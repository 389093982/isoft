<template>
  <div style="text-align: center;margin-top: 250px;">
    <h2>抱歉，您输入的地址没有找到</h2>
    <h3>o(╥﹏╥)o</h3>
    <button @click="$router.push({path:'/'})">点击返回主页</button>
  </div>
</template>

<script>
  export default {
    name: "NotFound"
  }
</script>

<style scoped>

</style>
