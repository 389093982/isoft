<template>
  <span>
    您还未登录奥,前去 <IBeautifulLink @onclick="cancelUser" style="color: rgba(44,91,255,0.8)">登录</IBeautifulLink>
  </span>
</template>

<script>
  import IBeautifulLink from "../Common/link/IBeautifulLink";
  import {LoginAddr} from "../../api"
  import {deleteLoginInfo} from '../../tools/sso'

  export default {
    name: "ForwardLogin",
    components: {IBeautifulLink},
    methods: {
      cancelUser() {
        deleteLoginInfo();
        window.location.href = LoginAddr + "?redirectUrl=" + window.location.href;
      }
    }
  }
</script>

<style scoped>

</style>
