<template>
  <div>
    <LoginHeader/>
    <LoginBg/>
    <LoginFooter/>
  </div>
</template>

<script>
  import LoginHeader from "./LoginHeader"
  import LoginBg from "./LoginBg"
  import LoginFooter from "./LoginFooter"

  export default {
    name: "Login",
    components: {LoginHeader, LoginBg, LoginFooter}
  }
</script>

<style scoped>

</style>
