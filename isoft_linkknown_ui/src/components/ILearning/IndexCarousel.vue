<template>
  <div>
    <div style="width: 100%;height: 400px">
      <!--首页轮播图 图片尺寸尽量长宽2:1，长800，宽400比较合适-->
      <Carousel autoplay="true" :autoplaySpeed="2000" dots="outside" trigger="hover" loop>
        <CarouselItem>
          <div class="demo-carousel"><img src="../../../static/images/indexCarousel/链接知识的未来.jpg" height="400" width="100%"/></div>
        </CarouselItem>
        <CarouselItem>
          <div class="demo-carousel"><img src="../../../static/images/indexCarousel/精品课程.jpg" height="400" width="100%"/></div>
        </CarouselItem>
        <CarouselItem>
          <div class="demo-carousel"><img src="../../../static/images/indexCarousel/链知博客.jpg" height="400" width="100%"/></div>
        </CarouselItem>
        <CarouselItem>
          <div class="demo-carousel"><img src="../../../static/images/indexCarousel/书本.jpg" height="400" width="100%"/></div>
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
