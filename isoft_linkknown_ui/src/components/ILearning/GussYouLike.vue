<template>
  <div>
    <div v-show="showContent">
      <ToolBox :placement_name="GLOBAL.placement_host_app_recommand"/>
    </div>
  </div>
</template>

<script>
  import ToolBox from "../Background/CMS/ToolBox";

  export default {
    name: "GussYouLike",
    components: {ToolBox},
    data() {
      return {}
    },
    props: {
      showContent: {
        type: Boolean,
        default: true,
      }
    }
  }
</script>

<style scoped>

</style>
