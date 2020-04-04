<template>
  <div style="position: relative;height: 400px;">
    <HeavyRecommend v-if="showRecommend" style="position: absolute;top: 0;left: 88px;z-index: 1;"/>
    <Icon type="ios-checkmark" style="position: absolute;right: 10px;top: 10px;"/>

    <div style="color: white;font-size: 32px;text-align: center;padding-top: 100px;">以最快的速度获取最想要的资源</div>

    <div style="margin-top: 60px;text-align: center;">
      <p style="color: white;">
        <span style="margin: 0 5px;">站点</span>
        <span style="margin: 0 5px;">常用</span>
        <span style="margin: 0 5px;">灵感</span>
        <span style="margin: 0 5px;">图片</span>
        <span style="margin: 0 5px;">素材</span>
        <span style="margin: 0 5px;">图标</span>
      </p>
      <div style="width: 500px;margin: 0 auto;"><ISearch :longer="true"></ISearch></div>
      <p style="color: white;">热门关键词：
        <span style="margin: 0 5px;">站点</span>
        <span style="margin: 0 5px;">常用</span>
        <span style="margin: 0 5px;">灵感</span>
        <span style="margin: 0 5px;">图片</span>
        <span style="margin: 0 5px;">素材</span>
        <span style="margin: 0 5px;">图标</span>
        <span style="margin: 0 5px;">图标</span>
        <span style="margin: 0 5px;">图标</span>
        <span style="margin: 0 5px;">图标</span>
      </p>
    </div>

    <div style="position:absolute;left:0;right:0;bottom:10px;text-align: center;">
      <span class="isoft_font12 tagColor">热门推荐</span>
      <span class="isoft_font12 tagColor">高清图库</span>
      <span class="isoft_font12 tagColor">灵感创意</span>
      <span class="isoft_font12 tagColor">素材资源</span>
      <span class="isoft_font12 tagColor">素材资源</span>
      <span class="isoft_font12 tagColor">素材资源</span>
      <span class="isoft_font12 tagColor">素材资源</span>
      <span class="isoft_font12 tagColor">素材资源</span>
      <span class="isoft_font12 tagColor">素材资源</span>
      <span class="isoft_font12 tagColor" @click="showRecommend = !showRecommend">素材资源</span>
    </div>
  </div>
</template>

<script>
  import ISearch from "../Common/search/ISearch"
  import HeavyRecommend from "./HeavyRecommend";

  export default {
    name: "LinkKnownAbout",
    components:{ISearch, HeavyRecommend},
    data (){
      return {
        showRecommend: false,
      }
    }
  }
</script>

<style scoped>
  .tagColor {
    margin: 0 10px;
    padding:8px 15px;
    border-radius:2px;
    background-color: rgb(235,235,235);
  }
  .showIntroduce {
    position:absolute;
    height: 300px;
    width:550px;
    left:50%;
    margin-left:-275px;
    top: 50px;
    background-color: rgb(235,235,235);
  }
</style>
