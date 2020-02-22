<template>
  <div>
    <div v-for="(book_collect, index) in book_collects">
      {{book_collect}}
    </div>
  </div>
</template>

<script>
  import {GetUserFavoriteList, QueryBookListByIds} from "../../api"
  import {checkEmpty} from "../../tools"

  export default {
    name: "UserFavorite",
    props: {
      userName: {
        type: String,
        default: ''
      },
    },
    data() {
      return {
        book_collects: [],
      }
    },
    methods: {
      refreshBookList: async function () {
        let book_ids = this.book_collects.map(bc => bc.id).join(",");
        const result = await QueryBookListByIds({book_ids});
        alert(JSON.stringify(result));
      },
      refreshUserFavoriteList: async function () {
        const result = await GetUserFavoriteList({favorite_type: 'book_collect', user_name: this.userName});
        if (result.status === "SUCCESS") {
          this.book_collects = result.favorites;
          this.refreshBookList();
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
