<template>
  <div class="isoft_container" style="padding: 10px 0;">
    <IHotRecommand :placement_name="GLOBAL.placement_frind_link"/>
  </div>
</template>

<script>
  import IHotRecommand from "../Common/recommend/IHotRecommand"

  export default {
    name: "FrindLink",
    components: {IHotRecommand},
  }
</script>

<style scoped>

</style>
