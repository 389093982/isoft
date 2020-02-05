<template>
  <!--横向菜单栏-->
  <div id="contentTitleId">
    <span class="indexMenuClass"><router-link to="/linklove/index" class="whiteColorClass">中央大厅</router-link></span>
    <span class="indexMenuClass"><router-link to="/myself/index" class="whiteColorClass">个人中心</router-link></span>
    <span class="indexMenuClass"><router-link to="/lover/index" class="whiteColorClass">lover</router-link></span>
    <span class="indexMenuClass">
        <Dropdown>
        <a href="javascript:void(0)"><p class="whiteColorClass">礼物<Icon type="ios-arrow-down"></Icon></p></a>
        <DropdownMenu slot="list">
          <DropdownItem><router-link to="/linklove/index">礼物商城</router-link></DropdownItem>
          <DropdownItem><router-link to="/linklove/index">我的礼物</router-link></DropdownItem>
        </DropdownMenu>
      </Dropdown>
      </span>
    <span class="indexMenuClass">
        <Dropdown>
        <a href="javascript:void(0)"><p class="whiteColorClass">礼金<Icon type="ios-arrow-down"></Icon></p></a>
        <DropdownMenu slot="list">
          <DropdownItem><router-link to="/linklove/index">我的红包</router-link></DropdownItem>
          <DropdownItem><router-link to="/linklove/index">我的优惠券</router-link></DropdownItem>
        </DropdownMenu>
      </Dropdown>
      </span>
    <span class="indexMenuClass">
        <Dropdown>
        <a href="javascript:void(0)"><p class="whiteColorClass">安排<Icon type="ios-arrow-down"></Icon></p></a>
        <DropdownMenu slot="list">
          <DropdownItem><router-link to="/linklove/index">我的婚礼</router-link></DropdownItem>
          <DropdownItem><router-link to="/linklove/index">定时计划</router-link></DropdownItem>
        </DropdownMenu>
      </Dropdown>
      </span>
    <span class="indexMenuClass">
        <Dropdown>
        <a href="javascript:void(0)"><p style="color: white">家族<Icon type="ios-arrow-down"></Icon></p></a>
        <DropdownMenu slot="list">
          <DropdownItem><router-link to="/linklove/index">关系图谱</router-link></DropdownItem>
          <DropdownItem><router-link to="/linklove/index">聊天会话</router-link></DropdownItem>
        </DropdownMenu>
      </Dropdown>
      </span>
  </div>
</template>

<script>
	export default {
		name: "pinkMenuTitle"
	}
</script>

<style scoped>
  #contentTitleId{
    background-color: #ff537c; padding: 5px 10px 5px 40px
  }
  .indexMenuClass{
    font-size: 15px;
    margin: 0 0 0 2%;
    cursor: pointer;
    color: white
  }
  .whiteColorClass{
    color: white;
  }
</style>
