<template>
  <div>
    <HeavyRecommend v-if="showRecommend" style="position: absolute;top: 76px;left: 110px;z-index: 1;"/>
    <div style="color: white;font-size: 32px;text-align: center;padding-top: 100px;">以最快的速度获取最想要的资源</div>

    <div style="margin-top: 40px;text-align: center;">
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

    <div style="text-align: center;margin-top: 155px;">
      <span class="isoft_font12 tagColor">热门推荐</span>
      <span class="isoft_font12 tagColor">高清图库</span>
      <span class="isoft_font12 tagColor">灵感创意</span>
      <span class="isoft_font12 tagColor">素材资源</span>
      <span class="isoft_font12 tagColor">素材资源</span>
      <span class="isoft_font12 tagColor">素材资源</span>
      <span class="isoft_font12 tagColor">素材资源</span>
      <span class="isoft_font12 tagColor">素材资源</span>
      <span class="isoft_font12 tagColor">素材资源</span>
      <span class="isoft_font12 tagColor">素材资源</span>
      <span class="isoft_font12 tagColor">素材资源</span>
      <span class="isoft_font12 tagColor" @mouseenter="showRecommend = true" @mouseleave="showRecommend = false">素材资源</span>
    </div>

    <div style="text-align: center;padding: 120px 0 20px 0;">
      <h1 style="color: #ffffff;" class="animated bounce infinite">链知科技</h1>
      <p style="margin: 50px 0 30px 0;color:#ffffff;font-size:18px;">致力于成为链接知识的桥梁</p>
      <p style="color: #ffffff;margin-top: 20px;">让知识不在局限于小众人群，人人可参与、可传播的的新平台</p>
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
    padding:8px 10px;
    border-radius:2px;
    background-color: rgb(235,235,235);
  }
</style>
