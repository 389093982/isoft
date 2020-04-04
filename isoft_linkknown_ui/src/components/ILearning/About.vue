<template>
  <div style="background: #FFFFFF;padding: 10px;">
    <div class="section01_bg">
      <LinkKnownAbout/>
    </div>

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

  import LinkKnownAbout from "./LinkKnownAbout";
  import ShowUserExperience from "./ShowUserExperience";
  import BlogRank2 from "./BlogRank2";

  export default {
    name: "About",
    components: {LinkKnownAbout, ShowUserExperience, BlogRank2},
  }
</script>

<style scoped>
  /* 引入公共样式库 */

  .section01_bg {
    height: 400px;
    background: url(../../assets/bg.jpg) no-repeat;
    background-size: 100% 100%;
  }
</style>
