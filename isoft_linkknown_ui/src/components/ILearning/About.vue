<template>
  <div style="background: #FFFFFF;padding: 10px;">

    <!--首页轮播图-->
    <IndexCarousel></IndexCarousel>

    <!--展示模块  目前是4个展示-->
    <show-modulars></show-modulars>

    <Row :gutter="10">
      <Col span="16">
        <ShowUserExperience/>
      </Col>
      <Col span="8">
        <BlogRank2/>
      </Col>
    </Row>
  </div>
</template>

<script>

  import ShowUserExperience from "./ShowUserExperience";
  import BlogRank2 from "./BlogRank2";
  const IndexCarousel = () => import("@/components/ILearning/IndexCarousel");
  const ShowModulars = () => import("@/components/ILearning/ShowModulars");

  export default {
    name: "About",
    components: {ShowUserExperience, BlogRank2,IndexCarousel,ShowModulars,},
  }
</script>

<style scoped>

</style>
