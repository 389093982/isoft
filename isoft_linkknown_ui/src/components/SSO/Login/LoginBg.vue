<template>
  <div style="width: 100%;">
    <div class="login_bg">
      <LoginForm/>
    </div>
  </div>
</template>

<script>
  import LoginForm from "./LoginForm"

  export default {
    name: "LoginBg",
    components: {LoginForm}
  }
</script>

<style scoped>
  .login_bg {
    height: 450px;
    width: 100%;
    background: url(../../../assets/sso/login_bg03.jpg) 0px 0px no-repeat;
    background-size: 100%;
  }
</style>
