<template>
  <div id="app">
    <router-view/>

    <AutoLogin/>
    <CheckHasLoginConfirmDialog/>
  </div>
</template>

<script>
  import CheckHasLoginConfirmDialog from "./components/SSO/Login/CheckHasLoginConfirmDialog";
  import AutoLogin from "./components/SSO/Login/AutoLogin";

  export default {
    name: 'App',
    components: {CheckHasLoginConfirmDialog, AutoLogin},
  }
</script>

<style scoped>

</style>
