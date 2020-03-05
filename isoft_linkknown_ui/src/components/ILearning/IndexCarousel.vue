<template>
  <div>
    <div style="width: 100%;height: 400px">
      <!--首页轮播图 图片尺寸尽量长宽2:1，长800，宽400比较合适-->
      <Carousel :autoplay="true" :autoplaySpeed="2000" dots="outside" trigger="hover" loop>
        <CarouselItem>
          <div class="demo-carousel"><img src="../../../static/images/indexCarousel/index.jpg" height="400" width="100%"/></div>
        </CarouselItem>
        <CarouselItem>
          <div class="demo-carousel"><img src="../../../static/images/indexCarousel/course.jpg" height="400" width="100%"/></div>
        </CarouselItem>
        <CarouselItem>
          <div class="demo-carousel"><img src="../../../static/images/indexCarousel/blog.jpg" height="400" width="100%"/></div>
        </CarouselItem>
        <CarouselItem>
          <div class="demo-carousel"><img src="../../../static/images/indexCarousel/book.jpg" height="400" width="100%"/></div>
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
