<template>
  <div style="padding-top: 10px; min-height: 510px" >
    <div class="isoft_font_header" v-if="isSearchFlag">博客搜索结果</div>
    <div class="isoft_font_header" v-else>{{custom_label}}</div>
    <div class="blogItem hoverItemClass isoft_inline_ellipsis"
         v-for="(blog, index) in blogs" @click="$router.push({path:'/iblog/blogArticleDetail', query:{'blog_id': blog.id}})">
      <span v-if="index === 0">
        <img :src="blog.first_img" style="width: 90px;height: 60px;"/>&nbsp;
        <strong class="isoft_hover_red2">{{blog.blog_title}}</strong>
      </span>
      <span v-else>
        <img class="imgIcon" src="../../assets/icon_b.png"/>&nbsp;
        <span class="isoft_hover_red2">{{blog.blog_title}}</span>
      </span>
    </div>

    <div v-if="isSearchFlag && !(blogs && blogs.length > 0)" style="text-align: center;border-top: 1px solid #eee;padding-top: 10px;">
      <p>未搜索到匹配的图书</p>
      <p class="isoft_hover_red2" @click="refreshBlogList">给我推荐一些</p>
      <p class="isoft_hover_red2" @click="handleReSearch">重新搜索</p>
    </div>
  </div>
</template>

<script>
  import {QueryCustomTagBlog, queryPageBlog} from "../../api"
  import IBeautifulCard from "../Common/card/IBeautifulCard"

  export default {
    name: "RankBlog",
    components: {IBeautifulCard},
    props:{
      custom_tag: {
        type: String,
        default: 'hot',
      },
      custom_label: {
        type: String,
        default: '热门博客',
      }
    },
    data() {
      return {
        blogs: [],
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 8,
        isSearchFlag: false,    // 是否是搜索模式
      }
    },
    methods: {
      handleReSearch: function (){
        this.$emit("research");
      },
      refreshCustomTagBlog: async function () {
        const result = await QueryCustomTagBlog({
          custom_tag: this.custom_tag,
          offset: 10,
          current_page: 1,
        });
        if (result.status === "SUCCESS") {
          this.blogs = result.custom_tag_blogs;
        }
      },
      search: async function (search_data) {
        this.isSearchFlag = true;
        const result = await queryPageBlog({
          search_data: search_data,
          offset: 10,
          current_page: 1,
        });
        if (result.status === "SUCCESS") {
          this.blogs = result.blogs;
          this.total = result.paginator.totalcount;
        }
      },
      refreshBlogList: function () {
        this.isSearchFlag = false;
        this.refreshCustomTagBlog();
      }
    },
    mounted() {
      this.refreshBlogList();
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
    position: relative;
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
