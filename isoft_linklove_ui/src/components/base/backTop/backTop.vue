<template>
  <div>
    <BackTop :height="100" :bottom="50" >
      <div class="top"><Icon type="ios-arrow-up" /></div>
    </BackTop>
  </div>
</template>

<script>
	export default {
		name: "backTop"
	}
</script>

<style scoped>
  .top{
    padding: 10px;
    background: #ff537c;
    color: #fff;
    text-align: center;
    border-radius: 2px;
    height: 60px;
    width: 100px
  }
</style>
