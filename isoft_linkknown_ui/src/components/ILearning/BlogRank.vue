<template>
  <div style="padding-top: 10px; min-height: 400px" >
    <div class="isoft_font_header">热门博客</div>
    <div class="blogItem isoft_inline_ellipsis" v-for="(blog, index) in blogs" @click="$router.push({path:'/iblog/blogArticleDetail', query:{'blog_id': blog.id}})">
      <span v-if="index === 0">
        <img :src="blog.first_img" style="width: 90px;height: 60px;"/>&nbsp;
        <strong class="isoft_hover_red2">{{blog.blog_title}}</strong>
      </span>
      <span v-else>
        <img class="imgIcon" src="../../assets/icon_b.png"/>&nbsp;
        <span class="isoft_hover_red2">{{blog.blog_title}}</span>
      </span>

    </div>
  </div>
</template>

<script>
  import {QueryCustomTagBlog} from "../../api"
  import IBeautifulCard from "../Common/card/IBeautifulCard"
  import {checkNotEmpty} from "../../tools";

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
        if (result.status === "SUCCESS") {
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
  .blogItem {
    margin: 0 20px;
    padding: 10px 10px 5px 10px;
    border-top: 1px solid #eee;
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
