<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <IBeautifulCard title="专家墙">
        <div slot="content">
          <div v-for="(user,index) in users2">
            <span v-if="renderNickName(user.user_name)">{{renderNickName(user.user_name)}}</span>
            <span v-else>{{user.user_name}}</span>
            累计好评次数：{{user.good_numbers}}
          </div>
        </div>
      </IBeautifulCard>
    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_top5">
      <IBeautifulCard title="答疑勤兵">
        <div slot="content">
          <div v-for="(user,index) in users">
            <span v-if="renderNickName(user.user_name)">{{renderNickName(user.user_name)}}</span>
            <span v-else>{{user.user_name}}</span>
            累计解答次数：{{user.count}}
          </div>
        </div>
      </IBeautifulCard>
    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_top5">
      <IBeautifulCard title="提问达人">
        <div slot="content">
          <div v-for="(user,index) in users3">
            <span v-if="renderNickName(user.user_name)">{{renderNickName(user.user_name)}}</span>
            <span v-else>{{user.user_name}}</span>
            累计提问次数：{{user.question_numbers}}
          </div>
        </div>
      </IBeautifulCard>
    </div>
  </div>
</template>

<script>
  import IBeautifulCard from "../Common/card/IBeautifulCard"
  import {QueryExpertWallList} from "../../api"
  import {RenderNickName, renderUserInfoByNames} from "../../tools";

  export default {
    name: "ExpertWall",
    components: {IBeautifulCard},
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
          this.userInfos = await renderUserInfoByNames(result.users.concat(result.users2).concat(result.users3), "user_name");
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
