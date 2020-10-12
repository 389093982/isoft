<template>
  <div class="isoft_glint" :class="{bgImg1: bgImg1Show,bgImg2: bgImg2Show,bgImg3: bgImg3Show, }" style="position: relative;height: 640px;overflow-x: hidden;">
    <HeavyRecommend v-if="showRecommend" :class="{showRecommendClass: showRecommend}" style="position: absolute;top: 0;z-index: 1;"/>
    <span class="isoft_point_cursor"
          style="position: absolute;right: 10px;top: 10px;padding: 5px;background-color: #ededed;">
      <Icon :size="25" type="ios-shirt-outline" @click="handleChangeBg"/>
    </span>

    <div class="animated faster bounceInRight" style="color: white;font-size: 32px;text-align: center;padding-top: 200px;">一个专门学习编程的网站</div>

    <div style="margin-top: 60px;">
      <div style="text-align: center;margin: 5px 0;">
        <span style="position: relative;padding: 10px 0;">
          <div :style="activeBottomStyle"></div>
          <span style="width: 70px;display: inline-block;" v-for="(recommend, index) in recommend1"
                class="animated faster bounceInRight isoft_point_cursor isoft_hover_yellow"
                @click="handleSubmitFunc(recommend)" @mouseenter="handleMouseEnter(index)">{{recommend}}</span>
        </span>
      </div>
      <div style="width: 500px;margin: 0 auto;">
        <ISearch :longer="true" @submitFunc="handleSubmitFunc"></ISearch>
      </div>
      <div class="animated faster bounceInRight" style="text-align: center;color: white;">热门关键词：
        <span v-for="(recommend, index) in recommend2" class="isoft_point_cursor isoft_hover_yellow isoft_mr10"
              @click="handleSubmitFunc(recommend)">{{recommend}}</span>
      </div>
    </div>

    <div style="position:absolute;left:0;right:0;bottom:10px;text-align: center;">
      <span class="animated faster flash isoft_font12 tagColor isoft_point_cursor" @click="handleShow(1)">热门推荐</span>
      <span class="animated faster flash isoft_font12 tagColor isoft_point_cursor" @click="handleShow(2)">新闻公告</span>
      <span class="animated faster flash isoft_font12 tagColor isoft_point_cursor" @click="handleShow(3)">热门应用</span>
      <span class="animated faster flash isoft_font12 tagColor isoft_point_cursor" @click="handleShow(4)">热门产品</span>
      <span class="animated faster flash isoft_font12 tagColor isoft_point_cursor" @click="handleShow(5)">热门项目</span>
      <span class="animated faster flash isoft_font12 tagColor isoft_point_cursor" @click="showRecommend = !showRecommend">更多素材</span>
    </div>

  </div>
</template>

<script>
  import ISearch from "../Common/search/ISearch"
  import HeavyRecommend from "./HeavyRecommend";
  import vueCanvasNest from 'vue-canvas-nest'
  import {GenerateRandom} from "../../tools/index"

  export default {
    name: "LinkKnownQuickSearch",
    components:{ISearch, HeavyRecommend, vueCanvasNest},
    data (){
      return {
        showRecommend: false,
        recommend1: ["编程基础", "编程规范", "项目实践", "源码分析", "专家讲坛"],     // 一级分类
        activeBottomTranslateX: 7,                                                 // 一级分类 bottom 初始偏移量
        activeBottomTranslateXArr: [7, 77, 147, 217, 287, 357],                    // 一级分类 bottom 偏移量
        recommend2: ["前端教程", "java", "数据库", "中间件", "素材", "linux", "docker"],
        changeBgTimer: null,
        bgImgShowArray: ['bgImg1Show', 'bgImg2Show', 'bgImg3Show'],
        bgImg1Show:true,
        bgImg2Show:false,
        bgImg3Show:false,
      }
    },
    methods:{
      handleShow: function (index) {
        this.$emit('handleShow', index);
      },
      handleSubmitFunc: function (search_data) {
        this.$emit('submitFunc', search_data);
      },
      handleChangeBg: function () {

      },
      handleMouseEnter: function (index) {
        this.activeBottomTranslateX = this.activeBottomTranslateXArr[index];      // 动态修改偏移量
      },
    },
    computed:{
      activeBottomStyle: function () {
        return {
          width: '56px',
          transition: 'transform .3s cubic-bezier(.645,.045,.355,1)',             // 触发过渡效果
          transform: `translateX(${this.activeBottomTranslateX}px)`,
          backgroundColor: '#ffd100',
          position: 'absolute',
          bottom: '0',
          left: '0',
          height: '2px',
          zIndex: 1,
        }
      },
    },
    mounted:function () {
      let _this = this;
      if (_this.changeBgTimer != null){
        clearInterval(_this.changeBgTimer);
      }
      this.changeBgTimer = setInterval(function () {
        let imgShow = GenerateRandom(_this.bgImgShowArray)+"";
        _this.bgImg1Show = imgShow === 'bgImg1Show';
        _this.bgImg2Show = imgShow === 'bgImg2Show';
        _this.bgImg3Show = imgShow === 'bgImg3Show';
      }, 5000);
    }
  }
</script>

<style scoped>
  .bgImg1{
    background-image: url("../../../static/images/indexBg/bg1.jpg");
    background-size: 100% 100%;
    height: 400px;
  }
  .bgImg2{
    background-image: url("../../../static/images/indexBg/bg2.jpg");
    background-size: 100% 100%;
    height: 400px;
  }
  .bgImg3{
    background-image: url("../../../static/images/indexBg/bg3.jpg");
    background-size: 100% 100%;
    height: 400px;
  }
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

  .isoft_hover_yellow {
    color: white;
  }
  .isoft_hover_yellow:hover {
    color: yellow;
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
    100%{transform:translateX(1500px) skewX(-15deg);opacity:1}
  }

  /*.quickSearchBg {*/
    /*animation: gradientAnimation 7.5s infinite;*/
  /*}*/
  /*@keyframes gradientAnimation {*/
    /*0% {*/
      /*background-image: linear-gradient(43deg,#4158D0 0%,#C850C0 46%,#FFCC70 100%);*/
    /*}*/
    /*100% {*/
      /*background-image: linear-gradient(43deg, #60afd0 0%, #c83782 46%, #ffe668 100%);*/
    /*}*/
  /*}*/
</style>
