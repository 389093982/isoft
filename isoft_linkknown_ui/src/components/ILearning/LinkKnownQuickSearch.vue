<template>
  <div id="quickSearch" class="section01_bg isoft_glint" style="position: relative;height: 400px;">
    <HeavyRecommend v-if="showRecommend" :class="{showRecommendClass: showRecommend}" style="position: absolute;top: 0;z-index: 1;"/>
    <span class="isoft_point_cursor"
          style="position: absolute;right: 10px;top: 10px;padding: 5px;background-color: #ededed;">
      <Icon :size="25" type="ios-shirt-outline"/>
    </span>

    <div style="color: white;font-size: 32px;text-align: center;padding-top: 100px;">以最快的速度获取最想要的资源</div>

    <div style="margin-top: 60px;">
      <div style="text-align: center;">
        <span v-for="(recommend, index) in recommend1" class="isoft_point_cursor isoft_hover_green isoft_mr10"
              @click="handleSubmitFunc(recommend)">{{recommend}}</span>
      </div>
      <div style="width: 500px;margin: 0 auto;">
        <ISearch :longer="true" @submitFunc="handleSubmitFunc"></ISearch>
      </div>
      <div style="text-align: center;color: white;">热门关键词：
        <span v-for="(recommend, index) in recommend2" class="isoft_point_cursor isoft_hover_green isoft_mr10"
              @click="handleSubmitFunc(recommend)">{{recommend}}</span>
      </div>
    </div>

    <div style="position:absolute;left:0;right:0;bottom:10px;text-align: center;">
      <span class="isoft_font12 tagColor isoft_point_cursor" @click="handleShow(1)">热门推荐</span>
      <span class="isoft_font12 tagColor isoft_point_cursor" @click="handleShow(2)">新闻公告</span>
      <span class="isoft_font12 tagColor isoft_point_cursor" @click="handleShow(3)">热门应用</span>
      <span class="isoft_font12 tagColor isoft_point_cursor" @click="handleShow(4)">热门产品</span>
      <span class="isoft_font12 tagColor isoft_point_cursor" @click="handleShow(5)">热门项目</span>
      <span class="isoft_font12 tagColor isoft_point_cursor" @click="showRecommend = !showRecommend">更多素材</span>
    </div>

    <!-- 参考 https://github.com/ZYSzys/vue-canvas-nest 蜘蛛网效果 -->
    <vue-canvas-nest :config="{color:'255,0,0', count: 88, zIndex: 999}" :el="'#quickSearch'"></vue-canvas-nest>
  </div>
</template>

<script>
  import ISearch from "../Common/search/ISearch"
  import HeavyRecommend from "./HeavyRecommend";
  import vueCanvasNest from 'vue-canvas-nest'

  export default {
    name: "LinkKnownQuickSearch",
    components:{ISearch, HeavyRecommend, vueCanvasNest},
    data (){
      return {
        showRecommend: false,
        recommend1: ["编程基础", "编程规范", "项目实践", "源码分析", "专家讲坛"],
        recommend2: ["前端教程", "java", "数据库", "中间件", "素材", "linux", "docker"],
      }
    },
    methods:{
      handleShow: function (index) {
        this.$emit('handleShow', index);
      },
      handleSubmitFunc: function (search_data) {
        this.$emit('submitFunc', search_data);
      }
    }
  }
</script>

<style scoped>
  .tagColor {
    margin: 0 10px;
    padding:8px 25px;
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

  .isoft_hover_green {
    color: white;
  }
  .isoft_hover_green:hover {
    color: yellow;
  }

  .section01_bg {
    height: 400px;
    background: url(../../assets/bg.jpg) no-repeat;
    background-size: 100% 100%;
  }

  .isoft_glint:hover:before {
    background: rgba(255, 255, 255, 0.05);
    position: absolute;
    display: block;
    width: 60px;
    height: 100%;
    content: '';
    left: 0;
    top: 0;
    transform: translateX(-50px) skewX(-15deg);
    filter: blur(20px);         /* CSS: filter: blur(); 实现高斯模糊效果,不可不知的细节优化 */
    opacity: .6;
    animation: glint 1.2s infinite;
    animation-iteration-count: 1;
  }
  @keyframes glint{
    0%{transform:translateX(-50px) skewX(-15deg);opacity:.6}
    100%{transform:translateX(1100px) skewX(-15deg);opacity:1}
  }
</style>
