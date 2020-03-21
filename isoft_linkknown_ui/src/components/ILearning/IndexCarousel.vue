<template>
  <div>
    <div style="width: 100%;height: 400px">
      <!--首页轮播图 图片尺寸尽量长宽2:1，长800，宽400比较合适-->
      <Carousel :autoplay="true" :autoplaySpeed="2000" dots="outside" trigger="hover" loop>
        <CarouselItem>
          <div style="cursor: pointer" @click="$router.push({path:'/ilearning/about'})"><img src="../../../static/images/indexCarousel/index.jpg" height="400" width="100%"/></div>
        </CarouselItem>
        <CarouselItem>
          <div style="cursor: pointer" @click="$router.push({path:'/ilearning/jingpinCourse'})"><img src="../../../static/images/indexCarousel/course.jpg" height="400" width="100%"/></div>
        </CarouselItem>
        <CarouselItem>
          <div style="cursor: pointer" @click="$router.push({path:'/iblog/blogList'})"><img src="../../../static/images/indexCarousel/blog.jpg" height="400" width="100%"/></div>
        </CarouselItem>
        <CarouselItem>
          <div style="cursor: pointer" @click="$router.push({path:'/ibook/bookList'})"><img src="../../../static/images/indexCarousel/book.jpg" height="400" width="100%"/></div>
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
