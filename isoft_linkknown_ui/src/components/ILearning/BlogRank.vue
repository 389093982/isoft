<template>
  <IBeautifulCard title="热门博客"  :minHeight="600">
    <ul slot="content">
      <li v-for="(blog, index) in blogs" @click="$router.push({path:'/iblog/blogArticleDetail', query:{'blog_id': blog.id}})">
        <img class="imgIcon" src="../../assets/icon_b.png"/>&nbsp;
        <IBeautifulLink>{{blog.blog_title | filterLimitFunc(22)}}</IBeautifulLink>
      </li>
    </ul>
  </IBeautifulCard>
</template>

<script>
  import {QueryCustomTagBlog} from "../../api"
  import IBeautifulCard from "../Common/card/IBeautifulCard"

  export default {
    name: "BlogRank",
    components: {IBeautifulCard},
    data() {
      return {
        blogs: [],
      }
    },
    methods: {
      refreshCustomTagBlog: async function () {
        const result = await QueryCustomTagBlog({custom_tag: 'hot'});
        if (result.status == "SUCCESS") {
          this.blogs = result.custom_tag_blogs;
        }
      }
    },
    mounted() {
      this.refreshCustomTagBlog();
    },
    filters: {
      // 内容超长则显示部分
      filterLimitFunc:function (value,limitLenth) {
        if (value.length > limitLenth) {
          return value.slice(0,limitLenth) + ' · · ·'
        }else {
          return value
        }
      }
    },
  }
</script>

<style scoped>
  a {
    color: black;
  }

  a:hover {
    color: red;
  }

  li {
    padding: 3px 15px 0 15px;
    list-style: none;
    font-size: 14px;
  }

  .imgIcon {
    position: relative;
    top: 2px;
    margin-right: 5px;
  }
</style>
