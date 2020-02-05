<template>
  <div>

    <!--左页眉-->
    <div id="headLineId">
      <div style="float: left;width: 30%">
        <code class="headLineClass">收藏本站</code>
        <code class="BlackSpaceClass">&nbsp;&nbsp;|&nbsp;&nbsp;</code>
        <code class="headLineClass">手机APP</code>
        <code class="BlackSpaceClass">&nbsp;&nbsp;|&nbsp;&nbsp;</code>
        <code class="headLineClass">公众号</code>
      </div>
      <!--右页眉-->
      <div style="float: right;width: 45%">
        <code class="headLineClass" @click="login">你好,请登录</code>
        <code class="BlackSpaceClass">&nbsp;</code>
        <code class="headLineClass" style="color: #e33333" @click="regist">免费注册</code>
        <code class="BlackSpaceClass">&nbsp;&nbsp;|&nbsp;&nbsp;</code>
        <code class="headLineClass">消息</code>
        <code class="BlackSpaceClass">&nbsp;&nbsp;|&nbsp;&nbsp;</code>
        <code class="headLineClass">邮件</code>
        <code class="BlackSpaceClass">&nbsp;&nbsp;|&nbsp;&nbsp;</code>
        <code class="headLineClass">在线客服</code>
        <code class="BlackSpaceClass">&nbsp;&nbsp;|&nbsp;&nbsp;</code>
        <code class="headLineClass">充值</code>
        <code class="BlackSpaceClass">&nbsp;&nbsp;|&nbsp;&nbsp;</code>
        <code class="headLineClass">退出</code>
      </div>
      <div style="clear: both"></div>
    </div>

    <!--logo和搜索-->
    <Affix :offset-top="0">
      <div class="demo-affix" id="headShowId">
        <div id="tagPicture" style="width: 28%;float: left;cursor: pointer">
          <h1 style="color: #ff6900"><em>link</em> <em style="color: #ff537c">love</em></h1>
        </div>
        <div style="width: 72%;float: left;">
          <input id="searchId" placeholder="搜索..." v-model="searchUserId"/>
          <button id="searchButtonId" @click="searchUser">
            <code id="searchCodeId">搜索</code>
          </button>
        </div>
        <div style="clear: both;"></div>
      </div>
    </Affix>

    <!--横向菜单栏-->
    <div id="contentTitleId">
      <span class="indexMenuClass"><router-link to="/linklove/index" class="whiteColorClass">中央大厅</router-link></span>
      <span class="indexMenuClass"><router-link to="/linklove/myself" class="whiteColorClass">个人中心</router-link></span>
      <span class="indexMenuClass"><router-link to="/linklove/lover" class="whiteColorClass">lover</router-link></span>
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

    <!--左侧提示 + 中间router-view + 右侧提示-->
    <div style="margin: 20px 2% 0 2% ;height: 400px;">
      <div id="leftContentId">
        <!--左侧悬浮-->
        <Affix :offset-top="120">
          <div class="demo-affix" id="leftContentBoxId">
            <p class="leftContentClass">游客: 张三</p><hr>
            <p class="leftContentClass">游客: 张三</p><hr>
            <p class="leftContentClass">游客: 张三</p><hr>
            <p class="leftContentClass">游客: 张三</p><hr>
            <p class="leftContentClass">游客: 张三</p><hr>
            <p class="leftContentClass">游客: 张三</p><hr>
            <p class="leftContentClass">游客: 张三</p><hr>
            <p class="leftContentClass">游客: 张三</p><hr>
            <p class="leftContentClass">游客: 张三</p><hr>
            <p class="leftContentClass">游客: 张三</p><hr>
            <p class="leftContentClass">游客: 张三</p><hr>
            <p class="leftContentClass">游客: 张三</p><hr>
            <p class="leftContentClass">游客: 张三</p><hr>
            <p class="leftContentClass">游客: 张三</p><hr>
          </div>
        </Affix>
      </div>

      <!--中间router-view-->
      <div id="centerContentId">
        <router-view></router-view>
      </div>

      <div id="rightContentId">
        <!--右边悬浮-->
        <Affix :offset-top="120">
          <div class="demo-affix" id="rightContentBoxId">
            <p class="rightContentClass">恋爱手册</p><hr>
            <p class="rightContentClass">恋爱心得</p><hr>
            <p class="rightContentClass">备忘录</p><hr>
            <p class="rightContentClass">备忘录</p><hr>
            <p class="rightContentClass">备忘录</p><hr>
            <p class="rightContentClass">备忘录</p><hr>
            <p class="rightContentClass">备忘录</p><hr>
            <p class="rightContentClass">备忘录</p><hr>
            <p class="rightContentClass">备忘录</p><hr>
            <p class="rightContentClass">备忘录</p><hr>
            <p class="rightContentClass">备忘录</p><hr>
            <p class="rightContentClass">备忘录</p><hr>
            <p class="rightContentClass">备忘录</p><hr>
            <p class="rightContentClass">备忘录</p><hr>
          </div>
        </Affix>
      </div>

      <!--clear: both-->
      <div style="clear: both"></div>
    </div>

    <!--其他内容 暂时待定-->
    <div style="float: left;margin: 10px 12% 0 12% ;width:76%;height: 2000px;background-color: #f6f4f0">
      其他内容<br>
      其他内容<br>
      其他内容<br>
      其他内容<br>
      其他内容<br>
      其他内容<br>
    </div>

    <!--页脚-->
    <div style="float: left;margin: 1px 12% 0 12% ;width:76%;height: 150px;background-color: #ffcfce">
      footer<br>
      footer<br>
      footer<br>
      footer<br>
    </div>

    <div style="clear: both"></div>

    <BackTop :height="100" :bottom="50" >
      <div class="top"><Icon type="ios-arrow-up" /></div>
    </BackTop>
  </div>
</template>

<script>
  import {Login,Regist} from '../../api/api'
  export default {
    name: "baseLayout",
    data(){
      return{
        searchUserId:'',
      }
    },
    methods:{
      login:async function () {
        this.$Message.info("Login");
        let result = await Login({});
      },
      regist:async function () {
        this.$Message.info("Regist");
        let result = await Regist({});
      },
      toIndex:async function(){
        this.$router.push({path:'/linklove/index'});
      },
      searchUser:function () {
        this.$Message.info("searchUser");
        this.$router.push({path:'/linklove/index',query:{'userid':this.searchUserId}});
      }

    },

  }
</script>

<style scoped>
  #headLineId{
    background-color: #e3e4e5;padding: 5px 0 5px 30px
  }
  .headLineClass{
    color: #999;font-size: 13px;cursor: pointer;
  }
  .BlackSpaceClass{
    color: #999;font-size: 13px
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
    width: 10%;float: left;
  }
  #leftContentBoxId{
    background-color: #fae5db;border-radius: 10px;
  }
  #centerContentId{
    width: 78%;float: left;background-color: #f6f4f0;margin: 0 1% 0 1% ;
  }
  #rightContentId{
    width: 10%;float: right;
  }
  #rightContentBoxId{
    background-color: #fae5db;border-radius: 10px
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
  hr{
    background-color: #ffd5d6;border: none;height: 1px
  }
  .rightContentClass{
    font-size: 15px;text-align: center;color: #000;cursor: pointer
  }
  .leftContentClass{
    font-size: 15px;cursor: pointer;
  }
  .top{
    padding: 10px;
    background: #ff537c;
    color: #fff;
    text-align: center;
    border-radius: 2px;
    height: 60px;
    width: 100px
  }
</style>
