<template>
  <div class="rightSuspension isoft_bg_white">
    <div class="itemArea">
      <Icon type="ios-apps-outline" :size="28" style="cursor: pointer;"
            @click="$router.push({path:'/ilearning/advise'})"/>
      <p>反馈</p>
    </div>
    <div class="item-line"></div>

    <div class="itemArea">
      <Icon type="logo-reddit" :size="28" style="cursor: pointer;"
            @click="$router.push({path:'/user/guide'})"/>
      <p>FAQ</p>
    </div>
    <div class="item-line"></div>

    <div class="itemArea">
      <Icon type="ios-people-outline" :size="28" style="cursor: pointer;"/>
      <p>社交</p>
    </div>
    <div class="item-line"></div>

    <div class="itemArea">
      <Icon type="ios-person-add-outline" :size="28" style="cursor: pointer;"
            @click="$router.push({path:'/user/mine/detail'})"/>
      <p>我的</p>
    </div>
  </div>
</template>

<script>
  name: "RightSuspensionMenu"
  export default {
  }
</script>

<style scoped>
  .rightSuspension {
    position: fixed;
    width: 40px;
    min-height: 100px;
    right: 0;
    top: 20%;
    z-index: 999;
  }

  .itemArea {
    text-align: center;
    font-size: 10px;
    padding: 5px 0;
  }

  .itemArea:hover {
    background-color: black;
    color: white;
  }

  .item-line {
    margin: 5px 10px;
    height: 1px;
    background: #ccc;
  }
</style>
