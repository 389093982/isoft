<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <IBeautifulCard title="等你来答">
        <div slot="content" style="padding: 10px;">
          <Row v-for="(user,index) in users2" :gutter="10">
            <Col span="3">
              <span @click="$router.push({path:'/user/detail',query:{username:user.user_name}})">
                <HatAndFacePicture :src="user.small_icon" :vip_level="user.vip_level" :hat_in_use="user.hat_in_use" :src_size="30" :hat_width="30" :hat_height="10" :hat_relative_left="0" :hat_relative_top="-46" ></HatAndFacePicture>
              </span>
            </Col>
            <Col span="11" style="position: relative;left: -5px">
              <span @click="$router.push({path:'/user/detail',query:{username:user.user_name}})" style="cursor: pointer">
                <span v-if="renderNickName(user.user_name)">{{renderNickName(user.user_name)}}</span>
                <span v-else>{{user.user_name}}</span>
              </span>
            </Col>
            <Col span="10">
              累计好评次数：{{user.good_numbers}}
            </Col>
          </Row>
        </div>
      </IBeautifulCard>
    </div>

    <div class="isoft_bg_white isoft_pd10">
      <IBeautifulCard title="专家墙">
        <div slot="content" style="padding: 10px;">
          <Row v-for="(user,index) in users2" :gutter="10">
            <Col span="3">
              <span @click="$router.push({path:'/user/detail',query:{username:user.user_name}})">
                <HatAndFacePicture :src="user.small_icon" :vip_level="user.vip_level" :hat_in_use="user.hat_in_use" :src_size="30" :hat_width="30" :hat_height="10" :hat_relative_left="0" :hat_relative_top="-46" ></HatAndFacePicture>
              </span>
            </Col>
            <Col span="11" style="position: relative;left: -5px">
              <span @click="$router.push({path:'/user/detail',query:{username:user.user_name}})" style="cursor: pointer">
                <span v-if="renderNickName(user.user_name)">{{renderNickName(user.user_name)}}</span>
                <span v-else>{{user.user_name}}</span>
              </span>
            </Col>
            <Col span="10">
              累计好评次数：{{user.good_numbers}}
            </Col>
          </Row>
        </div>
      </IBeautifulCard>
    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_top5">
      <IBeautifulCard title="答疑勤兵">
        <div slot="content" style="padding: 10px;">
          <Row v-for="(user,index) in users" :gutter="10">
            <Col span="3">
              <span @click="$router.push({path:'/user/detail',query:{username:user.user_name}})">
                <HatAndFacePicture :src="user.small_icon" :vip_level="user.vip_level" :hat_in_use="user.hat_in_use" :src_size="30" :hat_width="30" :hat_height="10" :hat_relative_left="0" :hat_relative_top="-46" ></HatAndFacePicture>
              </span>
            </Col>
            <Col span="11" style="position: relative;left: -5px">
              <span @click="$router.push({path:'/user/detail',query:{username:user.user_name}})" style="cursor: pointer">
                <span v-if="renderNickName(user.user_name)">{{renderNickName(user.user_name)}}</span>
                <span v-else>{{user.user_name}}</span>
              </span>
            </Col>
            <Col span="10">
              累计解答次数：{{user.count}}
            </Col>
          </Row>
        </div>
      </IBeautifulCard>
    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_top5">
      <IBeautifulCard title="提问达人">
        <div slot="content" style="padding: 10px;">
          <Row v-for="(user,index) in users3" :gutter="10">
            <Col span="3">
              <span @click="$router.push({path:'/user/detail',query:{username:user.user_name}})">
                <HatAndFacePicture :src="user.small_icon" :vip_level="user.vip_level" :hat_in_use="user.hat_in_use" :src_size="30" :hat_width="30" :hat_height="10" :hat_relative_left="0" :hat_relative_top="-46" ></HatAndFacePicture>
              </span>
             </Col>
            <Col span="11" style="position: relative;left: -5px">
              <span @click="$router.push({path:'/user/detail',query:{username:user.user_name}})" style="cursor: pointer">
                <span v-if="renderNickName(user.user_name)">{{renderNickName(user.user_name)}}</span>
                <span v-else>{{user.user_name}}</span>
              </span>
            </Col>
            <Col span="10">
              累计提问次数：{{user.question_numbers}}
            </Col>
          </Row>
        </div>
      </IBeautifulCard>
    </div>
  </div>
</template>

<script>
  import IBeautifulCard from "../Common/card/IBeautifulCard"
  import {QueryExpertWallList} from "../../api"
  import {RenderNickName, RenderUserInfoByNames} from "../../tools";
  import HatAndFacePicture from "../Common/HatAndFacePicture/HatAndFacePicture";

  export default {
    name: "ExpertWall",
    components: {HatAndFacePicture, IBeautifulCard},
    data() {
      return {
        users: [],        // 回答总数排行榜
        users2: [],       // 好评排行榜
        users3: [],       // 提问达人排行榜
        userInfos: [],     // 用户名和用户昵称等信息
      }
    },
    methods: {
      refreshExpertWallList: async function () {
        const result = await QueryExpertWallList({});
        if (result.status === "SUCCESS") {
          this.userInfos = await RenderUserInfoByNames(result.users.concat(result.users2).concat(result.users3), "user_name");
          this.users = result.users;
          this.users2 = result.users2;
          this.users3 = result.users3;
        }
      },
      renderNickName: function (user_name) {
        return RenderNickName(this.userInfos, user_name);
      }
    },
    mounted() {
      this.refreshExpertWallList();
    }
  }
</script>

<style scoped>

</style>
