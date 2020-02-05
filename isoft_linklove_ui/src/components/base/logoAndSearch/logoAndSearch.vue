<template>
  <div class="demo-affix" id="headShowId">
    <div id="tagPicture" style="width: 28%;float: left;">
      <h1 style="color: #ff6900;"><em style="cursor: pointer">link</em> <em style="color: #ff537c;cursor: pointer">love</em></h1>
    </div>
    <div style="width: 72%;float: left;">
      <input id="searchId" placeholder="搜索..." v-model="searchUserId"/>
      <button id="searchButtonId" @click="searchUser">
        <code id="searchCodeId">搜索</code>
      </button>
    </div>
    <div style="clear: both;"></div>
  </div>
</template>

<script>
	export default {
		name: "logoAndSearch",
    data(){
      return{
        searchUserId:'',
      }
    },
    methods:{
      searchUser:function () {
        this.$Message.info("searchUser");
        this.$router.push({path:'/linklove/index',query:{'userid':this.searchUserId}});
      }
    },
	}
</script>

<style scoped>
  #headShowId{
    background-color: #f6f6f6 ; padding: 20px 40px 20px 60px
  }
  #searchId{
    border-style:solid;border-color:#ff537c;width: 500px;height: 35px;padding: 2px 0 2px 2px
  }
  #searchButtonId{
    border-style:solid;border-color:#ff537c;width:60px;height: 35px;background-color: #ff537c; position: relative;left: -4px;top: 1px
  }
  #searchCodeId{
    color: white;cursor: pointer;font-size: 15px;
  }
</style>
