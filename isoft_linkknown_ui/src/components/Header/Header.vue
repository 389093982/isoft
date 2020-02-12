<template>
  <div class="layout">
    <Menu mode="horizontal" :theme="theme1" active-name="1">
      <div class="layout-nav">
        <MenuItem name="1">
          <IBeautifulLink @onclick="$router.push({path:'/ilearning/index'})">精品课程</IBeautifulLink>
        </MenuItem>
        <MenuItem name="2">
          <IBeautifulLink @onclick="$router.push({path:'/iblog/blog_list'})">话题博客</IBeautifulLink>
        </MenuItem>
        <MenuItem name="3">
          <IBeautifulLink @onclick="$router.push({path:'/ibook/book_list'})">热门书单</IBeautifulLink>
        </MenuItem>
        <MenuItem name="4">
          <IBeautifulLink @onclick="$router.push({path:'/job/jobList'})">求职招聘</IBeautifulLink>
        </MenuItem>
        <MenuItem name="5">
          <IBeautifulLink @onclick="$router.push({path:'/resource/resourceList'})">热门资源</IBeautifulLink>
        </MenuItem>
        <Submenu name="7">
          <template slot="title">
            <span v-if="loginUserName">{{loginUserName}}</span>
            <span v-else>未登录</span>
          </template>
          <MenuGroup title="账号管理">
            <MenuItem name="8-1" @click.native="cancelUser">前往登录</MenuItem>
            <MenuItem name="8-3" @click.native="cancelUser">注销</MenuItem>
            <MenuItem name="8-4" @click.native="$router.push({path:'/user/mine/detail',query:{username:'mine'}})">个人中心
            </MenuItem>
          </MenuGroup>
        </Submenu>
        <Submenu name="9">
          <template slot="title">
            会员中心
          </template>
          <MenuGroup title="账号管理">
            <MenuItem name="10-1">
              <IBeautifulLink @onclick="$router.push({path:'/vipcenter/vipIntroduction'})">开通会员</IBeautifulLink>
            </MenuItem>
            <MenuItem name="10-2">
              <IBeautifulLink @onclick="$router.push({path:'/vipcenter/vipInterest'})">会员权益</IBeautifulLink>
            </MenuItem>
          </MenuGroup>
        </Submenu>
        <MenuItem name="11">
          <div class="message">
            <IBeautifulLink>消息
              <Badge dot style="position: relative;top: -12px;"></Badge>
            </IBeautifulLink>
            <div class="message_detail">
              <MessageList :show-detail="false"/>
            </div>
          </div>
        </MenuItem>
        <MenuItem name="12">
          <IBeautifulLink @onclick="$router.push({path:'/background/advise_list'})">管理控制台</IBeautifulLink>
        </MenuItem>
      </div>
    </Menu>
  </div>
</template>

<script>
  import {CheckHasLogin, delCookie, getCookie} from '../../tools/index'
  import {LoginAddr} from "../../api"
  import IBeautifulLink from "../Common/link/IBeautifulLink";
  import MessageList from "../Message/MessageList";

  export default {
    name: "Header",
    components: {MessageList, IBeautifulLink},
    data() {
      return {
        theme1: 'light',
        loginUserName: '',
      }
    },
    methods: {
      cancelUser() {
        delCookie("tokenString");
        delCookie("userName");
        delCookie("isLogin");
        this.loginUserName = "";
        window.location.href = LoginAddr + "?redirectUrl=" + window.location.href;
      }
    },
    mounted: function () {
      if (CheckHasLogin()) {
        this.loginUserName = getCookie("nickName");
      }
    },
  }
</script>

<style scoped>
  .layout-nav {
    float: right;
    width: 1100px;
  }

  .message_detail {
    display: none;
  }

  .message:hover > .message_detail {
    padding: 5px 15px;
    position: absolute;
    left: -100px;
    display: block;
    max-height: 300px;
    width: 300px;
    background-color: white;
    box-shadow: 0 0 5px red;
  }
</style>

