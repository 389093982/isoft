<template>
  <div>
    <IBeautifulCard title="用户排行榜">
      <div slot="content" style="padding: 10px;">
        <Row v-for="(user,index) in users" :gutter="10">
          <Col span="3">
            <span @click="$router.push({path:'/user/detail',query:{username:user.user_name}})" >
              <HatAndFacePicture :src="user.small_icon" :vip_level="user.vip_level" :hat_in_use="user.hat_in_use" :src_size="30" :hat_width="30" :hat_height="10" hat_relative_left="0" hat_relative_top="-45" ></HatAndFacePicture>
            </span>
          </Col>
          <Col span="13" class="isoft_inline_ellipsis" style="font-size: 12px;margin: 4px 0 0 0 ">
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
  import HatAndFacePicture from "../Common/HatAndFacePicture/HatAndFacePicture";

  export default {
    name: "HotUser",
    components: {HatAndFacePicture, IBeautifulLink, IBeautifulCard},
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


  .small_font_size {
    font-size: 12px;
  }
</style>
