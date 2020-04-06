<template>
  <div style="position: relative;height: 400px;">
    <HeavyRecommend v-if="showRecommend" :class="{showRecommendClass: showRecommend}" style="position: absolute;top: 0;z-index: 1;"/>
    <span class="isoft_point_cursor"
          style="position: absolute;right: 10px;top: 10px;padding: 5px;background-color: #ededed;">
      <Icon :size="25" type="ios-shirt-outline"/>
    </span>

    <div style="color: white;font-size: 32px;text-align: center;padding-top: 100px;">以最快的速度获取最想要的资源</div>

    <div style="margin-top: 60px;">
      <div style="text-align: center;color: white;">
        <span style="margin: 0 5px;">编程基础</span>
        <span style="margin: 0 5px;">编程规范</span>
        <span style="margin: 0 5px;">项目实践</span>
        <span style="margin: 0 5px;">源码分析</span>
        <span style="margin: 0 5px;">专家讲坛</span>
      </div>
      <div style="width: 500px;margin: 0 auto;"><ISearch :longer="true"></ISearch></div>
      <div style="text-align: center;color: white;">热门关键词：
        <span style="margin: 0 5px;">前端教程</span>
        <span style="margin: 0 5px;">java</span>
        <span style="margin: 0 5px;">mysql</span>
        <span style="margin: 0 5px;">中间件</span>
        <span style="margin: 0 5px;">素材</span>
        <span style="margin: 0 5px;">docker</span>
        <span style="margin: 0 5px;">linux</span>
      </div>
    </div>

    <div style="position:absolute;left:0;right:0;bottom:10px;text-align: center;">
      <span class="isoft_font12 tagColor" @click="handleShow(1)">热门推荐</span>
      <span class="isoft_font12 tagColor" @click="handleShow(2)">新闻公告</span>
      <span class="isoft_font12 tagColor" @click="handleShow(3)">热门应用</span>
      <span class="isoft_font12 tagColor" @click="handleShow(4)">热门产品</span>
      <span class="isoft_font12 tagColor" @click="handleShow(5)">热门项目</span>
      <span class="isoft_font12 tagColor" @click="handleShow(6)">素材资源</span>
      <span class="isoft_font12 tagColor" @click="handleShow(7)">素材资源</span>
      <span class="isoft_font12 tagColor" @click="handleShow(8)">素材资源</span>
      <span class="isoft_font12 tagColor" @click="handleShow(9)">素材资源</span>
      <span class="isoft_font12 tagColor" @click="showRecommend = !showRecommend">更多素材</span>
    </div>
  </div>
</template>

<script>
  import ISearch from "../Common/search/ISearch"
  import HeavyRecommend from "./HeavyRecommend";

  export default {
    name: "LinkKnownQuickSearch",
    components:{ISearch, HeavyRecommend},
    data (){
      return {
        showRecommend: false,
      }
    },
    methods:{
      handleShow: function (index) {
        this.$emit('handleShow', index);
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
  .showRecommendClass {
    animation: showRecommendAnimation 0.2s infinite;
    animation-iteration-count: 1;
    animation-fill-mode: forwards;
  }
  @keyframes showRecommendAnimation {
    0%   {
      left: 58px;
    }
    100% {
      left: 88px;
    }
  }
</style>
