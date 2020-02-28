<template>
  <div>
    <GraphicLink :placement_name="GLOBAL.placement_found_news_list"/>
  </div>
</template>

<script>
  import GraphicLink from "../Background/CMS/GraphicLink";

  export default {
    name: "NewsList",
    components: {GraphicLink}
  }
</script>

<style scoped>

</style>
