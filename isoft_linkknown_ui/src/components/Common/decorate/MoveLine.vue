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
    animation: dh_fly 8s linear infinite;
  }

  @-webkit-keyframes dh_fly {
    0%   {color:red; transform: rotate(0deg);left:5%; top:0;}
    25%  {color:yellow;transform: rotate(180deg); left:100%; top:0;}
    50%  {color:blue;transform: rotate(360deg); left:100%; top:450%;}
    75%  {color:green;transform: rotate(180deg); left:5%; top:450%;}
    100% {color:red;transform: rotate(0deg); left:5%; top:0;}
  }
</style>
