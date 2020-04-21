<template>
  <div class="section01">
    <span v-for="(blog, index) in blogs" style="cursor: pointer;"
          @click="$router.push({path:'/iblog/blogArticleDetail', query:{'blog_id': blog.id}})">
      <p :style="{'background-color': (index==0 ? '#eeeeee':'')}"><img src="../../../static/images/common_img/icon_b.png"/><a>{{blog.blog_title}}</a></p>
    </span>
  </div>
</template>

<script>
  import {QueryCustomTagBlog} from "../../api"

  export default {
    name: "BlogRank2",
    data() {
      return {
        blogs: [],
      }
    },
    methods: {
      refreshCustomTagBlog: async function () {
        const result = await QueryCustomTagBlog({custom_tag: 'official'});
        if (result.status == "SUCCESS") {
          this.blogs = result.custom_tag_blogs;
        }
      }
    },
    mounted() {
      this.refreshCustomTagBlog();
    }
  }
</script>

<style scoped>
  a {
    color: black;
  }

  a:hover {
    color: red;
  }

  .section01 {
    border: 2px solid rgba(220, 220, 220, 0.28);
    margin: 10px 0 10px 0;
    padding: 15px;
    height: 420px;
  }

  img {
    position: relative;
    top: 2px;
    margin-right: 10px;
  }

  p {
    padding: 4px;
  }
</style>
