<template>
  <div id="app">
    <router-view/>

    <HoverTip/>
    <AutoLogin/>
    <CheckHasLoginConfirmDialog/>


  </div>
</template>

<script>
  import CheckHasLoginConfirmDialog from "./components/SSO/Login/CheckHasLoginConfirmDialog";
  import AutoLogin from "./components/SSO/Login/AutoLogin";
  import HoverTip from "./components/Common/HoverTip";

  export default {
    name: 'App',
    components: {HoverTip, CheckHasLoginConfirmDialog, AutoLogin},
  }
</script>

<style scoped>

</style>
