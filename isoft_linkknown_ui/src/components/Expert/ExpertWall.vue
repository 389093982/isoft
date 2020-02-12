<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <IBeautifulCard title="专家墙">
        <div slot="content">
          <div v-for="(user,index) in users2">
            {{user.user_name}}
            累计好评次数：{{user.good_numbers}}
          </div>
        </div>
      </IBeautifulCard>
    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_top5">
      <IBeautifulCard title="答疑勤兵">
        <div slot="content">
          <div v-for="(user,index) in users">
            {{user.user_name}}
            累计解答次数：{{user.count}}
          </div>
        </div>
      </IBeautifulCard>
    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_top5">
      <IBeautifulCard title="提问达人">
        <div slot="content">
          <div v-for="(user,index) in users3">
            {{user.user_name}}
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

  export default {
    name: "ExpertWall",
    components: {IBeautifulCard},
    data() {
      return {
        users: [],        // 回答总数排行榜
        users2: [],       // 好评排行榜
        users3: [],       // 提问达人排行榜
      }
    },
    methods: {
      refreshExpertWallList: async function () {
        const result = await QueryExpertWallList({});
        if (result.status == "SUCCESS") {
          this.users = result.users;
          this.users2 = result.users2;
          this.users3 = result.users3;
        }
      }
    },
    mounted() {
      this.refreshExpertWallList();
    }
  }
</script>

<style scoped>

</style>
