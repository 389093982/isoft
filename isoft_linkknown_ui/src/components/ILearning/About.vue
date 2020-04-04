<template>
  <div style="background: #FFFFFF;padding: 10px;">
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

  export default {
    name: "About",
    components: {ShowUserExperience, BlogRank2},
  }
</script>

<style scoped>

</style>
