<template>
  <div style="width: 100%">
    <Row style="min-height: 150px;background-color: #ffffff;">
      <Col>
        <Card style="width:100%;background:linear-gradient(90deg,#ebd2ae,#e8b66e);">
          <p slot="title" style="height: 25px">
            <Icon class="VIPicon" @click="$router.push({path:'/vipcenter/vipIntroduction'})" type="md-ribbon"/>会员权益
          </p>
          <a href="#" slot="extra" @click.prevent="changeLimit">
            <Icon type="ios-loop-strong"></Icon>
            <span style="color: red;font-size: 15px" class="hvr-grow" @click="$router.push({path:'/vipcenter/vipIntroduction'})">
              <Icon type="md-arrow-round-forward" />开通会员
            </span>
          </a>
          <Carousel autoplay arrow="never" dots="outside" trigger="hover" radius-dot :autoplay-speed="4000">
            <CarouselItem>
              <div class="demo-carousel">
                <ul style="padding-left: 20px">
                  <li><div class="hvr-grow" style="cursor: pointer;color: blue" @click="$router.push({path:'/resource/resourceList'})">订阅本站优秀热门资源</div></li>
                  <li><div class="hvr-grow" style="cursor: pointer;color: blue" @click="$router.push({path:'/resource/resourceList'})">订阅本站优秀热门资源</div></li>
                  <li><div class="hvr-grow" style="cursor: pointer;color: blue" @click="$router.push({path:'/resource/resourceList'})">订阅本站优秀热门资源</div></li>
                </ul>
              </div>
            </CarouselItem>
            <CarouselItem>
              <div class="demo-carousel">
                <ul style="padding-left: 20px">
                  <li><div class="hvr-grow" style="cursor: pointer;color: blue" @click="$router.push({path:'/advertisement/advApply'})">本站广告推广</div></li>
                  <li><div class="hvr-grow" style="cursor: pointer;color: blue" @click="$router.push({path:'/advertisement/advApply'})">本站广告推广</div></li>
                  <li><div class="hvr-grow" style="cursor: pointer;color: blue" @click="$router.push({path:'/advertisement/advApply'})">本站广告推广</div></li>
                </ul>
              </div>
            </CarouselItem>
          </Carousel>
        </Card>
      </Col>
    </Row>
  </div>
</template>

<script>
	export default {
		name: "OpenVipCard"
	}
</script>

<style scoped>
  .VIPicon{
    color: #ff6900;
    font-size: 20px;
    cursor: pointer;

    position:relative;
    animation-name:myfirst;
    animation-duration:8s;
    animation-iteration-count:infinite;
    animation-play-state:running;
    /* Safari and Chrome: */
    -webkit-animation-name:myfirst;
    -webkit-animation-duration:8s;
    -webkit-animation-iteration-count:infinite;
    -webkit-animation-play-state:running;
  }


  @keyframes myfirst
  {
    0%   {color:red; left:0px; top:0px;}
    25%  {color:yellow; left:230px; top:0px;}
    50%  {color:blue; left:230px; top:10px;}
    75%  {color:green; left:0px; top:10px;}
    100% {color:red; left:0px; top:0px;}
  }

  @-webkit-keyframes myfirst /* Safari and Chrome */
  {
    0%   {color:red; left:0px; top:0px;}
    25%  {color:yellow; left:230px; top:0px;}
    50%  {color:blue; left:230px; top:10px;}
    75%  {color:green; left:0px; top:10px;}
    100% {color:red; left:0px; top:0px;}
  }
</style>
