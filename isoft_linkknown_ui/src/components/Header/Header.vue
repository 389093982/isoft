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
        <MenuItem name="6">
          <IBeautifulLink @onclick="$router.push({path:'/ifound/found_list'})">发现频道</IBeautifulLink>
        </MenuItem>
        <MenuItem name="7">
          <IBeautifulLink @onclick="$router.push({path:'/ifound/found_list'})">热门活动</IBeautifulLink>
        </MenuItem>
        <Submenu name="8">
          <template slot="title">
            <span>更多内容</span>
          </template>
          <MenuGroup title="更多内容">
            <MenuItem name="8-1">全部菜单项</MenuItem>
            <MenuItem name="8-2">精选内容</MenuItem>
            <MenuItem name="8-3">更多内容</MenuItem>
            <MenuItem name="8-4" @click.native="$router.push({path:'/chat/chatRom'})">Chat 聊天功能</MenuItem>
          </MenuGroup>
        </Submenu>
        <Submenu name="9">
          <template slot="title">
            <span v-if="loginUserName">{{loginUserName}}</span>
            <span v-else>未登录</span>
          </template>
          <MenuGroup title="账号管理">
            <MenuItem name="9-1" @click.native="cancelUser">前往登录</MenuItem>
            <MenuItem name="9-2" @click.native="cancelUser">切换账号</MenuItem>
            <MenuItem name="9-3" @click.native="cancelUser">注销</MenuItem>
            <MenuItem name="9-4" @click.native="$router.push({path:'/user/mine/detail',query:{username:'mine'}})">个人中心
            </MenuItem>
          </MenuGroup>
        </Submenu>
        <Submenu name="10">
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
          <IBeautifulLink @onclick="$router.push({path:'/background/advise_list'})">管理控制台</IBeautifulLink>
        </MenuItem>
      </div>
    </Menu>

    <LevelTwoHeader/>
  </div>
</template>

<script>
  import {CheckHasLogin, delCookie, getCookie} from '../../tools/index'
  import {LoginAddr} from "../../api"
  import IBeautifulLink from "../Common/link/IBeautifulLink";
  import LevelTwoHeader from "./LevelTwoHeader";

  export default {
    name: "Header",
    components: {LevelTwoHeader, IBeautifulLink},
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
    width: 1200px;
  }
</style>

