<template>
  <div>

    <Carousel autoplay v-model="carousel" loop autoplay-speed="3000">
      <CarouselItem><div class="demo-carousel"><indexshow01></indexshow01></div></CarouselItem>
      <CarouselItem><div class="demo-carousel"><indexshow02></indexshow02></div></CarouselItem>
    </Carousel>

  </div>
</template>

<script>
  import indexshow01 from "./indexshow01";
  import indexshow02 from "./indexshow02";
  import indexshow03 from "./indexshow03";
  import indexshow04 from "./indexshow04";
  import indexshow05 from "./indexshow05";
  export default {
    name: "index",
    components: {indexshow01, indexshow02, indexshow03, indexshow04, indexshow05},
    data(){
      return{
        carousel:0,
      }
    }
  }
</script>

<style scoped>

</style>
