<template>
  <div>
    <Icon class="dh" :size="30" type="ios-planet-outline"/>
  </div>
</template>

<script>
  export default {
    name: "MoveLine"
  }
</script>

<style scoped>
  .dh {
    position: relative;
    animation: dh_fly 20s linear infinite;
  }

  @keyframes dh_fly {
    0% {
      transform: rotate(0deg);
      left: 0;
    }
    25% {
      transform: rotate(180deg);
      color: green;
      left: 50%;
    }
    50% {
      transform: rotate(360deg);
      color: blue;
      left: 100%;
    }
    75% {
      transform: rotate(180deg);
      color: red;
      left: 50%;
    }
    100% {
      transform: rotate(0);
      left: 0;
    }
  }
</style>
