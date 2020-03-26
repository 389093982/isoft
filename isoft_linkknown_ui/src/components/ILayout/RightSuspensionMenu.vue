<template>
  <div class="rightSuspension isoft_bg_white">
    <div class="itemArea" @click="$router.push({path:'/ilearning/advise'})">
      <Icon type="ios-apps-outline" :size="28"/>
      <p>反馈</p>
    </div>
    <div class="item-line"></div>

    <div class="itemArea" @click="$router.push({path:'/user/userGuide'})">
      <Icon type="logo-reddit" :size="28"/>
      <p>帮助中心</p>
    </div>
    <div class="item-line"></div>

    <div class="itemArea" @click="$router.push({path:'/site/siteIndex'})">
      <Icon type="ios-radio-outline" :size="28"/>
      <p>站点导航</p>
    </div>
    <div class="item-line"></div>

    <div class="itemArea" @click="$router.push({path:'/advertisement/advApply'})">
      <Icon type="ios-cog-outline" :size="28"/>
      <p>广告</p>
    </div>
    <div class="item-line"></div>

    <div class="itemArea" @click="$router.push({path:'/contact/contactList'})">
      <Icon type="ios-people-outline" :size="28"/>
      <p>社交</p>
    </div>
    <div class="item-line"></div>

    <div class="itemArea" @click="$router.push({path:'/user/userDetail'})">
      <Icon type="ios-person-add-outline" :size="28"/>
      <p>个人中心</p>
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
    right: 7px;
    top: 20%;
    z-index: 999;
  }

  .itemArea {
    text-align: center;
    font-size: 10px;
    padding: 5px 0;
    cursor: pointer;
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
