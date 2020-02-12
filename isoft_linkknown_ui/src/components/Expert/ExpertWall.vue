<template>
  <div class="isoft_bg_white isoft_pd10">
    <IBeautifulCard title="专家墙">
      <div slot="content">
        <div v-for="(user,index) in users">
          {{user.user_name}}
          累计解答次数：{{user.count}}
        </div>
      </div>
    </IBeautifulCard>
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
        users: [],
      }
    },
    methods: {
      refreshExpertWallList: async function () {
        const result = await QueryExpertWallList({});
        if (result.status == "SUCCESS") {
          this.users = result.users;
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
