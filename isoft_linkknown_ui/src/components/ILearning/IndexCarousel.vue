<template>
  <div style="height: 330px">
    <div style="width: 100%">
      <!--首页轮播图-->
      <Carousel autoplay="true" autoplaySpeed="2000" dots="outside" trigger="hover" loop>
        <CarouselItem>
          <div class="demo-carousel"><img src="../../../static/images/indexCarousel/1.jpg" height="400" width="100%"/></div>
        </CarouselItem>
        <CarouselItem>
          <div class="demo-carousel"><img src="../../../static/images/indexCarousel/3.jpg" height="400" width="100%"/></div>
        </CarouselItem>
        <CarouselItem>
          <div class="demo-carousel"><img src="../../../static/images/indexCarousel/4.jpg" height="400" width="100%"/></div>
        </CarouselItem>
        <CarouselItem>
          <div class="demo-carousel"><img src="../../../static/images/indexCarousel/5.jpg" height="400" width="100%"/></div>
        </CarouselItem>
      </Carousel>
    </div>
  </div>
</template>
<script>
  export default {
    name:'IndexCarousel',
  }
</script>

<style scoped>

</style>
