<template>
  <div class="layout">
    <div style="float: left;width: 14%;background-color: white;">
      <div style="padding: 5px 0 0 60px ">
        <img src="../../../static/images/linkknown_logo02.png" style="width: 100px;height: 49px;cursor: pointer" @click="$router.push({path:'/ilearning/index'})"/>
      </div>
    </div>
    <div style="float: left;width: 86%;">
      <Menu mode="horizontal" :theme="theme1" active-name="1">
        <div class="layout-nav">
          <MenuItem name="1">
            <IBeautifulLink @onclick="$router.push({path:'/ilearning/index'})">链知首页</IBeautifulLink>
          </MenuItem>
          <MenuItem name="2">
            <IBeautifulLink @onclick="$router.push({path:'/ilearning/boutiqueCourse'})">精品课程</IBeautifulLink>
          </MenuItem>
          <MenuItem name="3">
            <IBeautifulLink @onclick="$router.push({path:'/iblog/blog_list'})">话题博客</IBeautifulLink>
          </MenuItem>
          <MenuItem name="4">
            <IBeautifulLink @onclick="$router.push({path:'/ibook/book_list'})">热门书单</IBeautifulLink>
          </MenuItem>
          <MenuItem name="5">
            <IBeautifulLink @onclick="$router.push({path:'/resource/list'})">热门资源</IBeautifulLink>
          </MenuItem>
          <MenuItem name="6">
            <IBeautifulLink @onclick="$router.push({path:'/job/jobList'})">求职招聘</IBeautifulLink>
          </MenuItem>
          <Submenu name="7">
            <template slot="title">
              <span v-if="loginUserName">{{loginUserName}}</span>
              <span v-else>未登录</span>
            </template>
            <MenuGroup title="账号管理">
              <MenuItem name="7-1" @click.native="$router.push({path:'/user/detail'})">个人中心</MenuItem>
              <MenuItem name="7-2" @click.native="cancelUser">
                <span v-if="isLogin()">重新</span><span>登录</span>
              </MenuItem>
              <MenuItem name="7-3" @click.native="cancelUser" v-if="isLogin()">退出</MenuItem>
              <MenuItem name="7-4" @onclick="$router.push({path:'/background/advise_list'})" v-if="isAdmin()">控制台</MenuItem>
            </MenuGroup>
          </Submenu>
          <MenuItem name="8">
            <div class="message">
              <IBeautifulLink>消息
                <Badge dot style="position: relative;top: -12px;"></Badge>
              </IBeautifulLink>
              <div class="message_detail">
                <MessageList :show-detail="false"/>
              </div>
              <div>
                <!-- 会员权益 -->
                <RechargeRight/>
              </div>
            </div>
          </MenuItem>
          <MenuItem name="9">
            <IBeautifulLink @onclick="$router.push({path:'/found/list'})">发现</IBeautifulLink>
          </MenuItem>
          <MenuItem name="10">
            <IBeautifulLink @onclick="$router.push({path:'/ilearning/boutiqueCourse'})">我要赚钱</IBeautifulLink>
          </MenuItem>
          <MenuItem name="11">
            <IBeautifulLink @onclick="$router.push({path:'/business/introduce'})">商业合作</IBeautifulLink>
          </MenuItem>
        </div>
      </Menu>
    </div>
    <div style="clear: both"></div>
  </div>
</template>

<script>
  import {CheckAdminLogin, CheckHasLogin} from '../../tools/index'
  import {deleteLoginInfo, getNickName} from "../../tools/sso"
  import {LoginAddr} from "../../api"
  import IBeautifulLink from "../Common/link/IBeautifulLink";
  import MessageList from "../Message/MessageList";
  import RechargeRight from "../VipCenter/RechargeRight";

  export default {
    name: "Header",
    components: {RechargeRight, MessageList, IBeautifulLink},
    data() {
      return {
        theme1: 'light',
        loginUserName: '',
      }
    },
    methods: {
      isAdmin: function () {
        return CheckAdminLogin();
      },
      isLogin:function(){
        return CheckHasLogin();
      },
      cancelUser() {
        deleteLoginInfo();
        this.loginUserName = "";
        window.location.href = LoginAddr + "?redirectUrl=" + window.location.href;
      }
    },
    mounted: function () {
      if (CheckHasLogin()) {
        this.loginUserName = getNickName();
      }
    },
  }
</script>

<style scoped>
  .layout-nav {
    float: left;
    width: 100%;
  }

  .message_detail {
    display: none;
  }

  .message:hover > .message_detail {
    padding: 5px 15px;
    position: absolute;
    left: -130px;
    display: block;
    max-height: 300px;
    width: 360px;
    background-color: white;
    box-shadow: 0 0 5px red;
  }
</style>

