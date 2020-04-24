<template>
  <div class="isoft_bg_white isoft_pd5">
    <h2 class="user_rank" style="border-bottom: 2px solid #eee;">用户排行榜</h2>
    <div style="position: relative;">
      <div style="text-align: center;" class="animated bounce infinite isoft_color_grey2 isoft_font12">奉献自己的力量，让自己榜上有名吧~ </div>
    </div>
    <div style="position: relative;">
      <div v-for="(user,index) in users" style="display: flex;">
        <div style="width: 12%;">
        <span @click="$router.push({path:'/user/userDetail',query:{username:user.user_name}})" >
          <HatAndFacePicture :src="user.small_icon" :vip_level="user.vip_level" :hat_in_use="user.hat_in_use" :src_size="30"
                             :hat_width="30" :hat_height="10" :hat_relative_left="0" :hat_relative_top="-46" ></HatAndFacePicture>
        </span>
        </div>
        <div style="width: 55%;margin: 4px 0 0 0;color: #009a61;" class="isoft_inline_ellipsis">
        <span style="position: relative;" class="isoft_point_cursor isoft_hover_red nickName">
          {{user.nick_name}}
          <span class="nickNameTip" @click="$router.push({path:'/user/userDetail',query:{username:user.user_name}})">无力吐槽</span>
        </span>
        </div>
        <div style="width: 33%;" class="isoft_hover_desc isoft_font12">
          积分:{{user.user_points}}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {GetHotUsers} from "../../api"
  import IBeautifulCard from "../../components/Common/card/IBeautifulCard";
  import IBeautifulLink from "../Common/link/IBeautifulLink";
  import HatAndFacePicture from "../Common/HatAndFacePicture/HatAndFacePicture";

  export default {
    name: "HotUser",
    components: {HatAndFacePicture, IBeautifulLink, IBeautifulCard},
    data() {
      return {
        users: [],
        defaultImg: require('../../../static/images/common_img/default.png'),
      }
    },
    methods: {
      defImg() {
        let img = event.srcElement;
        img.src = this.defaultImg;
        img.onerror = null; //防止闪图
      },
      refreshHotUsers: async function () {
        const result = await GetHotUsers();
        if (result.status === "SUCCESS") {
          this.users = result.users;
        }
      }
    },
    mounted() {
      this.refreshHotUsers();
    }
  }
</script>

<style scoped>
  .user_rank {
    margin: 0 10px 10px 10px;
    position: relative;
    height: 40px;
    color: #111;
    font-size: 20px;
    font-weight: 400;
    line-height: 40px;
    white-space: nowrap;
  }

  .nickNameTip {
    position: relative;
    left: 2px;
    top: -6px;
    width: 65px;
    height: 18px;
    background: url(../../assets/tip.svg) no-repeat;
    color: #fff;
    text-align: center;
    font-size: 12px;
    line-height: 18px;
    display: none;
  }
  .nickName:hover > .nickNameTip {
    display: inline-block;
  }
</style>
