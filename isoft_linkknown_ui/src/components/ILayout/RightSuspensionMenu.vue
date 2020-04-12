<template>
  <div class="rightSuspension isoft_bg_white">
    <div class="itemArea" @click="$router.push({path:'/ilearning/advise'})">
      <Icon type="ios-apps-outline" :size="28"/>
      <p>反馈</p>
      <span class="tipBox"><div class="tipInfo">提出您的意见或建议</div></span>
    </div>
    <div class="item-line"></div>

    <div class="itemArea" @click="$router.push({path:'/user/userGuide'})">
      <Icon type="logo-reddit" :size="28"/>
      <p>帮助</p>
      <span class="tipBox"><div class="tipInfo">是否遇到困难无法解决</div></span>
    </div>
    <div class="item-line"></div>

    <div class="itemArea" @click="$router.push({path:'/site/siteIndex'})">
      <Icon type="ios-radio-outline" :size="28"/>
      <p>导航</p>
      <span class="tipBox"><div class="tipInfo">查看本站更多功能</div></span>
    </div>
    <div class="item-line"></div>

    <div class="itemArea" @click="$router.push({path:'/advertisement/advApply'})">
      <Icon type="ios-cog-outline" :size="28"/>
      <p>广告</p>
      <span class="tipBox"><div class="tipInfo">可以推广您的产品奥</div></span>
    </div>
    <div class="item-line"></div>

    <div class="itemArea" @click="$router.push({path:'/contact/contactList'})">
      <Icon type="ios-people-outline" :size="28"/>
      <p>社交</p>
      <span class="tipBox"><div class="tipInfo">有空多与人联系联系</div></span>
    </div>
    <div class="item-line"></div>

    <div class="itemArea" @click="$router.push({path:'/user/userDetail'})">
      <Icon type="ios-person-add-outline" :size="28"/>
      <p>个人</p>
      <span class="tipBox"><div class="tipInfo">快来看看您都有哪些</div></span>
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

  .tipBox {
    position: relative;
    right: 150px;
    top: -48px;
    display: none;
    animation: moveToRight 0.2s infinite;
    animation-iteration-count: 1;
  }
  .itemArea:hover > .tipBox {
    display: block;
  }
  @keyframes moveToRight {
    0%   { right: 180px;}
    100% { right: 150px;}
  }
  .tipInfo {
    width: 150px;
    height: 53px;
    line-height: 53px;
    font-size: 14px;
    color: white;
    background-color: #ff6900;
    position: absolute;
  }
  .tipInfo:after {
    content: '';
    width: 0;
    height: 0;
    border-width: 10px;
    border-style: solid;
    border-color:transparent transparent transparent #ff6900;
    position: absolute;
    right: -20px;
    top: 16.5px;
  }
</style>
