<template>
  <div style="font-size: 14px;">
    <!-- 文字样式 -->
    <span class="isoft_font12">文字 font12</span>
    <span class="isoft_hover_bottom">hover 显示红色下边框</span>
    <span class="isoft_hover_red">hover 字体变成红色</span>
    <span class="isoft_hover_bottom isoft_hover_red">hover 字体变成红色,显示红色下边框</span>
    <span class="isoft_color_grey">灰色字体</span>
    <span class="isoft_color_grey2">灰色字体2</span>
    <span class="isoft_color_grey3">灰色字体3</span>
    <span class="isoft_color_green1 isoft_font16">绿色字体1</span>
    <span class="isoft_color_grey3 isoft_font16">灰色字体</span>
    <span class="isoft_hover_color_green">hover 字体绿色样式</span>
    <span class="isoft_hover_color_blue">hover 字体蓝色样式</span>
    <br/>
    <span class="isoft_hover_title">这里是标题</span>
    <span class="isoft_hover_desc">这里是描述</span>

    <!-- div 淡入淡出效果-->
    <div class="fadeToLeft">淡入淡出</div>
    <div class="fadeToRight">淡入淡出</div>
    <div class="fadeToTop">淡入淡出</div>
    <div class="fadeToBottom">淡入淡出</div>
    <div class="hoverItemClass">hover 从左移入</div>
    <br/>

    <!-- title 样式 -->
    <span class="isoft_font_header">文字样式</span>
    <div class="isoft_title">友情链接</div>
    <br/>

    <!-- 按钮样式 -->
    <span class="isoft_button_blue">按钮样式</span>
    <span class="isoft_button_green1">按钮样式</span>
    <span class="isoft_button_red1">按钮样式</span>
    <span class="isoft_button_red2">按钮样式</span>
    <span class="isoft_button_red_small">按钮样式 small</span>
    <span class="isoft_button_red3">按钮样式</span>
    <br/><br/><br/>

    <!-- 背景色 -->
    <div class="isoft_info_tip">灰度背景色</div>
    <div class="isoft_info_tip2">淡红色背景</div>
    <div class="isoft_info_tip3">淡红色背景</div>
    <br/>

    <!-- hover 偏移 -->
    <div class="isoft_hover_top10">hover 向上偏移 10</div>
    <br/>

    <!-- tag 标签清单 -->
    <div class="isoft_tag1">tag 标签样式1</div>
    <div class="isoft_tag1 isoft_hover_red">tag 标签样式1</div>
    <div class="isoft_tag2">tag 标签样式2</div>
    <div class="isoft_tag3">tag 标签样式3</div>
    <div class="isoft_tag4">tag 标签样式4</div>
    <div class="isoft_tag5">tag 标签样式5</div>
    <br/><br/>

    <!-- 三角形,实现参考 http://www.divcss5.com/rumen/r50847.shtml -->
    <div class="sjx1" style="display: inline-block;"></div> &nbsp;
    <div class="sjx2" style="display: inline-block;"></div> &nbsp;
    <div class="sjx3" style="display: inline-block;"></div> &nbsp;
    <div class="sjx4" style="display: inline-block;"></div> &nbsp;
    <div class="sjx5" style="display: inline-block;"></div> &nbsp;
    <div class="sjx6" style="display: inline-block;"></div> <br/>
    <div class="jt0" style="display: inline-block;"></div> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
    <div class="jt1" style="display: inline-block;"></div> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
    <div class="jt2" style="display: inline-block;"></div> &nbsp;

    <br/><br/><br/><br/>

    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <div class="introduce-box">
      <div class="introduce-btn">发布招标需求</div>
    </div>

    <!-- 图标 -->
    <div class="isoft_add"></div>
  </div>
</template>

<script>
  export default {
    name: "CssDemo"
  }
</script>

<style scoped>
  .sjx1 {
    width: 0;
    height: 0;
    border-width: 20px;
    border-style: solid;
    border-color:#FFCCCC #0099CC #996699 #339933;
  }
  .sjx2 {
    width: 0;
    height: 0;
    border-width: 20px;
    border-style: solid;
    border-color:#FFCCCC transparent transparent transparent;   /* transparent 属性用来指定全透明色彩 */
  }
  .sjx3 {
    width: 0;
    height: 0;
    border-width: 20px;
    border-style: solid;
    border-color:transparent #0099CC transparent transparent;
  }
  .sjx4 {
    width: 0;
    height: 0;
    border-width: 20px;
    border-style: solid;
    border-color:transparent transparent #996699 transparent;
  }
  .sjx5 {
    width: 0;
    height: 0;
    border-width: 20px;
    border-style: solid;
    border-color:transparent transparent transparent #339933;
  }
  .sjx5 {
    width: 0;
    height: 0;
    border-width: 20px;
    border-style: solid;
    border-color:transparent transparent transparent #339933;
    transform: rotate(45deg); /*顺时针旋转*/
  }
  .sjx6 {
    width: 100px;
    height: 40px;
    background-color: yellow;
    position: absolute;
  }
  .sjx6:after {
    content: '';
    width: 0;
    height: 0;
    border-width: 10px;
    border-style: solid;
    border-color:transparent transparent transparent yellow;
    position: absolute;
    right: -20px;
    top: 10px;
  }
  .jt0{
    position: absolute;
  }
  .jt0:before,.jt0:after{
    position: absolute;
    content: '';
    width: 0;
    height: 0;
    border-top: 10px transparent dashed;
    border-left: 10px transparent dashed;
    border-bottom: 10px transparent dashed;
    border-right: 10px #fff solid;      /* 上左下透明,右边白色 */
  }
  .jt0:before{
    border-right: 10px #0099CC solid;   /* before 前右边颜色,覆盖之前的白色 */
  }
  .jt0:after{
    left: 5px;                          /* 覆盖并错开 5 px */
    border-right: 10px #fff solid;      /* after 后右边颜色,覆盖之前的白色,但还是白色 */
  }
  .jt1{
    position: absolute;
  }
  .jt1:before,.jt1:after{
    position: absolute;
    content: '';
    width: 0;
    height: 0;
    border-top: 10px transparent dashed;
    border-left: 10px transparent dashed;
    border-bottom: 10px transparent dashed;
    border-right: 10px #fff solid;      /* 上左下透明,右边白色 */
  }
  .jt1:before{
    border-right: 10px #0099CC solid;   /* before 前右边颜色,覆盖之前的白色 */
  }
  .jt1:after{
    left: 1px;                          /* 覆盖并错开1px */
    border-right: 10px #fff solid;      /* after 后右边颜色,覆盖之前的白色,但还是白色 */
  }
  .jt2{
    position: absolute;
  }
  .jt2:before,.jt2:after{
    position: absolute;
    content: '';
    width: 0;
    height: 0;
    border-top: 10px transparent dashed;
    border-left: 10px transparent dashed;
    border-right: 10px transparent dashed;
    border-bottom: 10px #fff solid;
  }
  .jt2:before{
    border-bottom: 10px #0099CC solid;
  }
  .jt2:after{
    top: 1px; /*覆盖并错开1px*/
    border-bottom: 10px #fff solid;
  }
  .introduce-box {
    position: relative;
    padding: 10px;
    border: 1px solid #f6f6f6;
    background: #ff6900;
    color: white;
    display: inline-block;
  }
  .introduce-box:hover {
    background: #f6290e;
  }
  .introduce-btn:hover:before {
    background: rgba(255,255,255,.5);
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
  }
  @keyframes glint{
    0%{transform:translateX(-50px) skewX(-15deg);opacity:.6}
    100%{transform:translateX(300px) skewX(-15deg);opacity:1}
  }

  .isoft_add {
    width: 100px;
    height: 100px;
    color: #ccc;
    position: relative;
  }
  .isoft_add::before{
    content: '';
    position: absolute;
    left: 50%;
    top: 50%;
    width: 80px;
    margin-left: -40px;
    margin-top: -5px;
    /*利用伪类before和其 border-top 来设置一个“横”：*/
    border-top: 10px solid;
  }
  .isoft_add::after {
    content: '';
    position: absolute;
    left: 50%;
    top: 50%;
    height: 80px;
    margin-left: -5px;
    margin-top: -40px;
    /*使用after伪类和 border-left 设置一个“竖”：*/
    border-left: 10px solid;
  }
  .isoft_add:hover {
    color: #a5a5a5;
  }
</style>
