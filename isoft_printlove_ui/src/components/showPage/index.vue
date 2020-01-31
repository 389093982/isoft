<template>
  <div>

    <div id="headLineId">
      <div style="float: left;width: 30%">
        <span class="headLineClass">收藏本站</span>
        <span class="headLineClass">手机APP</span>
        <span class="headLineClass">公众号</span>
      </div>
      <div style="float: right;width: 30%">
        <span class="headLineClass">消息</span>
        <span class="headLineClass">邮件</span>
        <span class="headLineClass">在线客服</span>
        <span class="headLineClass">充值</span>
        <span class="headLineClass">注销</span>
      </div>
      <div style="clear: both"></div>
    </div>

    <div id="headShowId">
      <div id="tagPicture" style="width: 30%;float: left;">
        <h1 style="color: #ff6900">print love</h1>
      </div>
      <div style="width: 70%;float: left;">
        <input id="searchId" placeholder="搜索..."/>
        <button id="searchButtonId">
          <code id="searchCodeId">搜索</code>
        </button>
      </div>
      <div style="clear: both;"></div>
    </div>

    <div id="contentTitleId">
      <span>中央大厅</span>
      <span>
        <Dropdown>
        <a href="javascript:void(0)"><p style="color: white">未登录<Icon type="ios-arrow-down"></Icon></p></a>
        <DropdownMenu slot="list">
          <DropdownItem>前往登陆</DropdownItem>
          <DropdownItem>切换账号</DropdownItem>
          <DropdownItem>注销</DropdownItem>
        </DropdownMenu>
      </Dropdown>
      </span>
      <span>个人中心</span>
      <span>lover</span>
      <span>
        <Dropdown>
        <a href="javascript:void(0)"><p style="color: white">礼物<Icon type="ios-arrow-down"></Icon></p></a>
        <DropdownMenu slot="list">
          <DropdownItem>礼物商城</DropdownItem>
          <DropdownItem>我的礼物</DropdownItem>
        </DropdownMenu>
      </Dropdown>
      </span>
      <span>
        <Dropdown>
        <a href="javascript:void(0)"><p style="color: white">礼金<Icon type="ios-arrow-down"></Icon></p></a>
        <DropdownMenu slot="list">
          <DropdownItem>我的红包</DropdownItem>
          <DropdownItem>我的优惠券</DropdownItem>
        </DropdownMenu>
      </Dropdown>
      </span>
      <span>
        <Dropdown>
        <a href="javascript:void(0)"><p style="color: white">安排<Icon type="ios-arrow-down"></Icon></p></a>
        <DropdownMenu slot="list">
          <DropdownItem>结婚</DropdownItem>
          <DropdownItem>设置定时可见</DropdownItem>
        </DropdownMenu>
      </Dropdown>
      </span>
      <span>
        <Dropdown>
        <a href="javascript:void(0)"><p style="color: white">家族<Icon type="ios-arrow-down"></Icon></p></a>
        <DropdownMenu slot="list">
          <DropdownItem>关系图谱</DropdownItem>
          <DropdownItem>聊天会话</DropdownItem>
        </DropdownMenu>
      </Dropdown>
      </span>
    </div>

    <div style="margin: 20px 40px 0 20px ;height: 400px">
      <div id="leftContentId">
        <div>
          <p class="leftContentClass">游客</p><hr>
          <p class="leftContentClass">游客</p><hr>
          <p class="leftContentClass">游客</p><hr>
          <p class="leftContentClass">游客</p><hr>
          <p class="leftContentClass">游客</p><hr>
          <p class="leftContentClass">游客</p><hr>
          <p class="leftContentClass">游客</p><hr>
          <p class="leftContentClass">游客</p><hr>
          <p class="leftContentClass">游客</p><hr>
        </div>
      </div>
      <div id="centerContentId">
        这里展示子页面
        <router-view></router-view>
      </div>
      <div id="rightContentId">
        <p class="rightContentClass">恋爱手册</p><hr>
        <p class="rightContentClass">恋爱手册</p><hr>
        <p class="rightContentClass">恋爱手册</p><hr>
        <p class="rightContentClass">恋爱手册</p><hr>
        <p class="rightContentClass">恋爱手册</p><hr>
        <p class="rightContentClass">恋爱手册</p><hr>
        <p class="rightContentClass">恋爱手册</p><hr>
        <p class="rightContentClass">恋爱手册</p><hr>
      </div>
      <div style="clear: both"></div>
    </div>

  </div>
</template>

<script>
	export default {
		name: "index"
	}
</script>

<style scoped>
  #headLineId{
    background-color: #e3e4e5;padding: 5px 0 5px 30px
  }
  .headLineClass{
    color: #ff6900;font-size: 13px
  }
  #headShowId{
    background-color: #f6f6f6 ; padding: 20px 40px 20px 60px
  }
  #searchId{
    border-style:solid;border-color:#ff537c;width: 500px;height: 35px;padding: 2px 0 2px 2px
  }
  #searchButtonId{
    border-style:solid;border-color:#ff537c;width:60px;height: 35px;background-color: #ff537c; position: relative;left: -4px;top: 1px
  }
  #searchCodeId{
    color: white;cursor: pointer;font-size: 15px;
  }
  #contentTitleId{
    background-color: #ff537c; padding: 5px 10px 5px 40px
  }
  #leftContentId{
    width: 10%;float: left;background-color: #f6f4f0;border-radius: 10px;
  }
  #centerContentId{
    width: 75%;float: left;background-color: #fff;margin: 0 1% 0 1% ;
  }
  #rightContentId{
    width: 13%;float: left;background-color: #f6f4f0;border-radius: 10px
  }
  span{
    font-size: 15px;
    margin: 0 0 0 2%;
    cursor: pointer;
    color: white
  }
  hr{
    background-color: rgba(242,21,255,0.05);border: none;height: 1px
  }
  .rightContentClass{
    font-size: 15px;text-align: center;color: #000;cursor: pointer
  }
  .leftContentClass{
    font-size: 15px;cursor: pointer;
  }
</style>
