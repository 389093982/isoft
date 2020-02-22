<template>
  <div>
    SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
  </div>
</template>

<script>
  import {GetUserFavoriteList} from "../../api"
  import {checkEmpty} from "../../tools"

  export default {
    name: "UserFavorite",
    props: {
      userName: {
        type: String,
        default: ''
      },
    },
    methods: {
      refreshUserFavoriteList: async function () {
        const result = await GetUserFavoriteList({favorite_type: 'book_collect'});
        if (result.status === "SUCCESS") {
          alert(JSON.stringify(result));
        }
      }
    },
    mounted() {
      if (checkEmpty(this.user_name)) {
        this.refreshUserFavoriteList();
      }
    }
  }
</script>

<style scoped>

</style>
