<template>
  <div style="display: flex;width: 100%;">
    <div style="width: 100%;">
      <Menu mode="horizontal" :theme="theme1" active-name="1">
        <div>
          <MenuItem name="0">
              <img width="80" height="35" src="../../../static/images/common_img/linkknown_logo02.png" @error="defImg()"
                   style="cursor: pointer;position: relative;top: 14px;left: 14px">
          </MenuItem>
          <MenuItem name="1">
            <IBeautifulLink @onclick="$router.push({path:'/ilearning/index'})">首页</IBeautifulLink>
          </MenuItem>
          <MenuItem name="2">
            <IBeautifulLink @onclick="$router.push({path:'/ilearning/jingpinCourse'})">精品课程</IBeautifulLink>
          </MenuItem>
          <MenuItem name="3">
            <IBeautifulLink @onclick="$router.push({path:'/iblog/blogList'})">话题博客</IBeautifulLink>
          </MenuItem>
          <MenuItem name="4">
            <IBeautifulLink @onclick="$router.push({path:'/expert/expertAsk'})">求问专家</IBeautifulLink>
          </MenuItem>
          <MenuItem name="5">
            <IBeautifulLink @onclick="$router.push({path:'/ibook/bookList'})">热门书单</IBeautifulLink>
          </MenuItem>
          <MenuItem name="6">
            <IBeautifulLink @onclick="$router.push({path:'/resource/resourceList'})">热门资源</IBeautifulLink>
          </MenuItem>
          <MenuItem name="7">
            <IBeautifulLink @onclick="$router.push({path:'/found/list'})">发现</IBeautifulLink>
          </MenuItem>
          <MenuItem name="8" class="message_box">
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
          </MenuItem>
          <MenuItem name="9" class="zhuanlan_box">
            <IBeautifulLink>专栏</IBeautifulLink>
            <div class="zhuanlan_detail">
              <div>
                <div class="zhuanlan_label">
                  特色专栏
                </div>
                <div class="zhuanlan_items">
                  <div @click="$router.push({path:'/business/businessList'})">我要赚钱</div>
                  <div @click="$router.push({path:'/business/businessIntroduce'})">商业合作</div>
                  <div @click="">链知自研</div>

                  <br/>

                  <div @click="">链知网合肥教育基地</div>
                  <div @click="">链知网武汉教育基地</div>
                  <div @click="">链知网上海教育基地</div>

                  <div v-if="showJobFlag" @click="$router.push({path:'/job/jobList'})">求职招聘</div>
                </div>
              </div>
            </div>
          </MenuItem>
          <Submenu name="10">
            <template slot="title">
              <span v-if="loginUserNickName">
                <!--头像照片-->
                <img v-if="small_icon.length>0" class="isoft_hover_red" style="cursor: pointer;border: 1px solid grey;border-radius:50%; position: relative;top: 5px" width="20" height="20" :src="small_icon" @error="defImg()">
                <Avatar v-else icon="ios-person" size="small" style="padding: 0 0 0 5px ;margin: 0 2px 2px 0 " />
                {{loginUserNickName | filterLimitFunc(5)}}
              </span>
              <span v-else>
                <Avatar icon="ios-person" size="small" style="padding: 0 0 0 5px ;margin: 0 2px 2px 0 " />未登录
              </span>
            </template>
            <MenuGroup title="账户管理">
              <MenuItem name="10-1" @click.native="$router.push({path:'/user/userDetail'})" v-if="isLogin()">个人中心</MenuItem>
              <MenuItem name="10-2" @click.native="cancelUser">
                <span v-if="isLogin()">重新</span><span>登录</span>
              </MenuItem>
              <MenuItem name="10-3" @click.native="cancelUser" v-if="isLogin()">退出</MenuItem>
              <MenuItem name="10-4" @click.native="$router.push({path:'/sso/regist',query:{pattern:1}})" v-if="!isLogin()">注册</MenuItem>
              <MenuItem name="10-5" @click.native="$router.push({path:'/background/adviseList'})" v-if="isAdmin()">控制台</MenuItem>
            </MenuGroup>
          </Submenu>
        </div>
      </Menu>
    </div>
  </div>
</template>

<script>
  import {CheckAdminLogin, CheckHasLogin, GetLoginUserName} from '../../tools/index'
  import {deleteLoginInfo, getNickName} from "../../tools/sso"
  import {GetUserDetail, LoginAddr} from "../../api"
  import IBeautifulLink from "../Common/link/IBeautifulLink";
  import MessageList from "../Message/MessageList";
  import RechargeRight from "../VipCenter/RechargeRight";

  export default {
    name: "Header",
    components: {RechargeRight, MessageList, IBeautifulLink},
    data() {
      return {
        theme1: 'light',
        loginUserNickName: '',
        small_icon:'',
        defaultImg: require('../../../static/images/common_img/default.png'),
        showJobFlag:this.GLOBAL.showJobFlag,
      }
    },
    methods: {
      defImg() {
        let img = event.srcElement;
        img.src = this.defaultImg;
        img.onerror = null; //防止闪图
      },
      isAdmin: function () {
        return CheckAdminLogin();
      },
      isLogin:function(){
        return CheckHasLogin();
      },
      cancelUser() {
        deleteLoginInfo();
        this.loginUserNickName = "";
        this.small_icon = "";
        window.location.href = LoginAddr + "?redirectUrl=" + window.location.href;
      },
      querySamllIcon: async function () {
        let params = {
          'userName':GetLoginUserName()
        };
        const result = await GetUserDetail(params);
        if (result.status === "SUCCESS") {
          this.small_icon = result.user.small_icon;
        }
      }
    },
    mounted: function () {
      if (CheckHasLogin()) {
        this.loginUserNickName = getNickName();
        this.querySamllIcon();
      }
    },
    filters: {
      // 内容超长则显示部分
      filterLimitFunc:function (value,limitLenth) {
        if (value.length > limitLenth) {
          return value.slice(0,limitLenth) + '..'
        }else {
          return value
        }
      }
    },
  }
</script>

<style scoped>
  .message_box {
    position: relative;
  }
  .message_detail {
    padding: 5px 15px;
    position: absolute;
    bottom: 0;
    display: block;
    max-height: 300px;
    width: 360px;
    background-color: white;
    box-shadow: 0 0 5px red;

    transition: transform 0.1s linear;
    visibility: hidden;
    transform: translateX(-50%) translateY(102%);
  }
  .message_box:hover > .message_detail {
    visibility: visible;
    transform: translateX(-50%) translateY(100%);
  }

  .zhuanlan_box {
    position: relative;
    transition: transform 0.2s linear;
  }
  .zhuanlan_detail {
    position: absolute;
    bottom: 0;
    height: 300px;
    width: 360px;
    background-color: white;
    box-shadow: 0 0 0 1px lightgray;

    transition: transform 0.1s linear;
    visibility: hidden;
    transform: translateX(-50%) translateY(102%);
  }
  .zhuanlan_box:hover .zhuanlan_detail {
    visibility: visible;
    transform: translateX(-50%) translateY(100%);
  }

  .zhuanlan_label {
    height: 30px;
    line-height:30px;
    background-color: #e8e8e8;
    padding-left: 10px;
    font-size: 12px;
    color: green;
  }
  .zhuanlan_items {
    line-height: 30px;
  }
  .zhuanlan_items > div {
    display: inline-block;
    font-size: 12px;
    color: #999;
    padding: 0 5px;
  }
  .zhuanlan_items > div:hover {
    color: green;
  }
</style>

