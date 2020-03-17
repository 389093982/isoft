<template>
  <div id="app">
    <router-view/>

    <CheckHasLoginConfirmDialog/>
  </div>
</template>

<script>
  import CheckHasLoginConfirmDialog from "./components/SSO/Login/CheckHasLoginConfirmDialog";

  export default {
    name: 'App',
    components: {CheckHasLoginConfirmDialog},
  }
</script>

<style scoped>

</style>
