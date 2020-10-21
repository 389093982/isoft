<template>
  <div style="width: 265px;height: 100%;">
    <a class="section01" :class="{hoverIndexSectionClass: hoverIndex === index, hoverIndexBorderClass: hoverIndex === index}"
       v-for="(sectionData, index) in sectionDatas" @mouseenter="handleMouseEnter(index)" @mouseleave="handleMouseLeave(index)">
      <p class="title" :class="hoverIndex === index ? 'hoverIndexTitleClass' : ''">{{sectionData.title}}</p>
      <p class="content">{{sectionData.content}}</p>
    </a>
  </div>
</template>

<script>
  export default {
    name: "HeavyRecommend",
    data (){
      return {
        hoverIndex: 0,
        timer: null,
        timerValid: true,
        sectionDatas: [
          {title: '编程语言', content: '汇集各种流行编程语言精华'},
          {title: '流行框架', content: '深度剖析多种流行框架源码'},
          {title: '名师荟萃', content: '重磅推荐各领域名家大师进行授课'},
          {title: '技术交流', content: '全网用户互动交流的集结地'},
          {title: '能力计划', content: '各领域多种多样的途径提升推广自己'},
        ]
      }
    },
    methods:{
      handleMouseEnter: function (index){
        this.hoverIndex = index;
        this.timerValid = false;
      },
      handleMouseLeave: function (index) {
        this.timerValid = true;
      },
    },
    mounted(){
      var _this = this;
      this.timer = setInterval(function () {
        if (_this.timerValid){
          _this.hoverIndex = _this.hoverIndex < 4 ? _this.hoverIndex + 1 : 0;
        }
      }, 2000);
    },
    beforeDestroy() {
      if (this.timer != null) {
        clearInterval(this.timer);
      }
    },
  }
</script>

<style scoped>
  .section01 {
    height: 20%;
    color: white;
    padding: 20px;
    background-color: rgba(0, 0, 0, 0.2);
    display: block;
    box-sizing: border-box;
  }

  .title {
    font-size: 18px;
    line-height: 50px;
  }

  .content {
    font-size: 14px;
  }

  .hoverIndexSectionClass {
    background-color: rgba(0, 0, 0, 0.6);
  }

  .hoverIndexTitleClass {
    color: yellow;
    font-weight: bold;
  }

  .section01::before {
    content: '';
    position: absolute;
    margin-left: -20px;
    margin-top: 10%;
    border-left: 3px solid yellow;
    height: 0;
  }
  .hoverIndexBorderClass::before {
    animation: hoverIndexBorderAnimation 0.5s infinite;
    animation-iteration-count: 1;
    animation-fill-mode: forwards;
  }
  @keyframes hoverIndexBorderAnimation {
    0%   {
      margin-top: 10%;
      height: 0;
    }
    100% {
      margin-top: -20px;
      height: 20%;
    }
  }
</style>
