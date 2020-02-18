<template>
  <div>
    <IBeautifulCard title="用户排行榜">
      <div slot="content" style="padding: 10px;">
        <Row v-for="(user,index) in users" :gutter="10">
          <Col span="4">
            <img style="cursor: pointer;border-radius: 50%;"
                 @click="$router.push({path:'/user/detail',query:{username:user.user_name}})"
                 width="30" height="30" :src="user.small_icon" @error="defImg()" :title="'邮箱：' +user.user_name">
          </Col>
          <Col span="12" class="isoft_inline_ellipsis" style="font-size: 12px;">
            <IBeautifulLink @onclick="$router.push({path:'/user/detail',query:{username:user.user_name}})">
              {{user.nick_name}}
            </IBeautifulLink>
          </Col>
          <Col span="8" class="small_font_size">
            积分：{{user.user_points}}
          </Col>
        </Row>
      </div>
    </IBeautifulCard>
  </div>
</template>

<script>
  import {GetHotUsers} from "../../api"
  import IBeautifulCard from "../../components/Common/card/IBeautifulCard";
  import IBeautifulLink from "../Common/link/IBeautifulLink";

  export default {
    name: "HotUser",
    components: {IBeautifulLink, IBeautifulCard},
    data() {
      return {
        users: [],
        defaultImg: require('../../assets/default.png'),
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
        if (result.status == "SUCCESS") {
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


  .small_font_size {
    font-size: 12px;
  }
</style>
