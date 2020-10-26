<template>
  <div style="width:90%;margin: 0 auto;background-color: white;border-top: 3px solid darkorange;">
    <div style="width: 1200px;margin: 0 auto;" class="clear">
      <div class="left">

        <div class="bg_box" @click="$router.push({path:'/ilearning/jingpinCourse'})">
          <div class="item"><a>Java</a></div>
          <div class="item"><a>HTML</a></div>
          <div class="item"><a>CSS</a></div>
          <div class="item"><a>JS</a></div>
          <div class="item"><a>Tomcat</a></div>
          <div class="item"><a>Spring</a></div>
          <div class="item"><a>SpringBoot</a></div>
          <div class="item"><a>maven</a></div>
          <div class="item"><a>vue</a></div>
          <div class="item"><a>商城</a></div>
          <div class="decorate_rotate"></div>

          <img src="../../../static/images/index/planet.png"
               style="width: 60px;height: auto;max-height: 100%;position: absolute;left: 100px;bottom: 10px;"/>
        </div>

      </div>
      <div class="right">
        <h3>链知网课程体系全面开放</h3>
        <ul>
          <li>汇聚优秀从业名师</li>
          <li>各种类型课程应有尽有</li>
          <li>深层次讲解课程内容</li>
          <li>在线免费观看全部课程</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "CourseSystem",
    mounted(){
      //获取dom节点
      var itemNodes = document.querySelectorAll('.item');
      itemNodes.forEach(function (itemNode, index) {
        var delay = index * 30;
        setTimeout(function () {

          //页面载入时，给它执行一次
          itemNode.classList.add('animate_zoomIn');
          //监听动画是否结束
          itemNode.addEventListener('animationend',function(){
            //动画结束，移除动画的样式类
            itemNode.classList.remove('animate_zoomIn');
            //延时1秒，再将动画加入
            setTimeout(function(){
              itemNode.classList.add('animate_zoomIn');
            },1000);
          });

        }, delay);
      });


    }
  }
</script>

<style scoped>
  .left {
    width: 50%;
    float: left;
    min-height: 500px;
    padding: 50px 20px 20px 20px;
  }
  .bg_box {
    width: 308px;
    height: 308px;
    border-radius: 50%;
    border: 20px solid #fafafa;
    position: relative;
    left: 132px;
    top: 50px;
  }
  .decorate_rotate {
    position: absolute;
    left: 15px;
    bottom: 10px;
    width: 26px;
    height: 26px;
    border-radius: 50%;
    background: #a66ade;
    /* transform-origin 设置旋转元素的基点位置 */
    transform-origin: 120px -98px;

    animation: decorate-rotate 20s linear infinite;
  }
  @keyframes decorate-rotate {
    0% {
      transform: rotateZ(0deg);
    }
    100% {
      transform: rotateZ(360deg);
    }
  }

  .bg_box:before {
    content: '';
    position: absolute;
    top: -21px;
    left: -21px;
    right: -21px;
    bottom: -21px;
    border-radius: 50%;
    border: 2px solid #e1e1e1;
  }

  .item {
    position: absolute;
    text-align: center;
    border-radius: 50%;
    z-index: 2;

    font-weight: 500;
    white-space: nowrap;
  }
  .item a {
    text-decoration: underline;
  }

  .item:nth-child(1) {
    width: 172px;
    height: 172px;
    top: 30px;
    left: -35px;

    line-height: 172px;
    font-size: 32px;

    background: rgba(250,244,255,0.8);
    border: 1px solid #dac8e9;
  }
  .item:nth-child(2) {
    width: 100px;
    height: 100px;
    top: 20px;
    left: 205px;

    line-height: 100px;
    font-size: 20px;

    background: rgba(153, 233, 156, 0.3);
    border: 1px solid #99e99c;
  }
  .item:nth-child(3) {
    width: 100px;
    height: 100px;
    top: -70px;
    left: 80px;

    line-height: 100px;
    font-size: 20px;

    background: rgba(134, 143, 233, 0.3);
    border: 1px solid #c6c4e9;
  }
  .item:nth-child(4) {
    width: 92px;
    height: 92px;
    top: 190px;
    left: 185px;

    line-height: 92px;
    font-size: 14px;

    background: rgba(227, 227, 227, 0.3);
    border: 1px solid #e3e3e3;
  }
  .item:nth-child(5) {
    width: 40px;
    height: 40px;
    top: 140px;
    left: 185px;

    line-height: 40px;
    font-size: 12px;

    background: rgba(250,244,255,0.3);
    border: 1px solid #dac8e9;
  }

  .item:nth-child(6) {
    width: 40px;
    height: 40px;
    top: 100px;
    left: 155px;

    line-height: 40px;
    font-size: 12px;

    background: rgba(250, 255, 172, 0.3);
    border: 1px solid #f7ff79;
  }
  .item:nth-child(7) {
    width: 40px;
    height: 40px;
    top: 220px;
    left: 20px;

    line-height: 40px;
    font-size: 12px;

    background: rgba(255, 164, 66, 0.3);
    border: 1px solid #ffa442;
  }
  .item:nth-child(8) {
    width: 40px;
    height: 40px;
    top: -20px;
    left: 8px;

    line-height: 40px;
    font-size: 12px;

    background: rgba(255, 116, 119, 0.3);
    border: 1px solid #ff7477;
  }
  .item:nth-child(9) {
    width: 40px;
    height: 40px;
    top: -40px;
    left: 224px;

    line-height: 40px;
    font-size: 12px;

    background: rgba(233, 94, 219, 0.3);
    border: 1px solid #e95edb;
  }
  .item:nth-child(10) {
    width: 40px;
    height: 40px;
    top: 150px;
    left: 135px;

    line-height: 40px;
    font-size: 12px;

    background: rgba(153, 213, 233, 0.3);
    border: 1px solid #99d5e9;
  }

  .animate_zoomIn {
    animation-duration: 1s;
    animation-delay: 0.2s;
    animation-name: zoomIn;
  }

  @keyframes zoomIn {
    0% {
      opacity: 0;
      transform: scale3d(0.3, 0.3, 0.3);
    }
    50% {
      opacity: 1;
    }
  }

  .right {
    width: 50%;
    float: left;
    min-height: 500px;
    padding: 130px 100px;
  }
  .right li {
    list-style: none;
    line-height: 30px;
  }
  .right h3 {
    font-size: 26px;
    line-height: 60px;

    font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  }
  .right li:before {
    width: 10px;
    content: '\2022';
    color: #feab66;
    font-size: 24px;
    margin-right: 8px;
    position: relative;
    top: 3px;
  }
</style>
