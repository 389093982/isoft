<template>
  <div>
    <h3>您还不是会员，
      <slot><span v-if="tip">{{tip}}，</span></slot>
      马上去<a @click="$router.push({path:'/vipcenter/vipIntroduction'})">开通!</a></h3>
  </div>
</template>

<script>
  export default {
    name: "OpenVipTip",
    props: {
      tip: {
        type: String,
        default: '',
      }
    }
  }
</script>

<style scoped>

</style>
