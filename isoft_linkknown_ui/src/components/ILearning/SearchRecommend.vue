<template>
  <div>
    <!--为您精选、热门、特色、高评-->
    <div id="rankCourse" style="display: flex;justify-content: center;" class="isoft_top5">
      <div style="width: 33%;">
        <RankCourse ref="rankCourse1" class="isoft_bg_white isoft_pd10" custom_tag="views" custom_label="精选课程" @research="handleReSearch"/>
      </div>
      <div style="width: 33%;margin: 0 5px;">
        <RankCourse class="isoft_bg_white isoft_pd10"  custom_tag="recommend" custom_label="推荐课程"/>
      </div>
      <div style="width: 33%;">
        <RankCourse class="isoft_bg_white isoft_pd10" custom_tag="hot" custom_label="热门课程"/>
      </div>
    </div>

    <!--热门博客-->
    <div style="display:flex;justify-content: center;" class="isoft_top5">
      <div style="width: 33%;">
        <RankBlog ref="rankBlog1" class="isoft_bg_white isoft_pd10" custom_tag="views" custom_label="精选博客" @research="handleReSearch"/>
      </div>
      <div style="width: 33%;margin: 0 5px;">
        <RankBlog class="isoft_bg_white isoft_pd10" custom_tag="recommend" custom_label="推荐博客"/>
      </div>
      <div style="width: 33%;">
        <RankBlog class="isoft_bg_white isoft_pd10" custom_tag="hot" custom_label="热门博客"/>
      </div>
    </div>

    <!--热门书单-->
    <div style="display: flex;justify-content: center;" class="isoft_top5">
      <div style="width: 33%;">
        <RankBook ref="rankBook1" class="isoft_bg_white isoft_pd10" custom_tag="views" custom_label="精选图书" @research="handleReSearch"/>
      </div>
      <div style="width: 33%;margin: 0 5px;">
        <RankBook class="isoft_bg_white isoft_pd10" custom_tag="recommend" custom_label="推荐图书"/>
      </div>
      <div style="width: 33%;">
        <RankBook class="isoft_bg_white isoft_pd10" custom_tag="hot" custom_label="热门图书"/>
      </div>
    </div>
  </div>
</template>

<script>
  const RankCourse = () => import("@/components/ILearning/RankCourse");
  const RankBook = () => import("@/components/ILearning/RankBook");
  const RankBlog = () => import("@/components/ILearning/RankBlog");

  export default {
    name: "SearchRecommend",
    components:{RankCourse,RankBook,RankBlog},
    data (){
      return {

      }
    },
    methods:{
      handleReSearch: function (){
        this.$emit("research");
      },
      search: function (search_data) {
        // 滚动到指定位置
        document.querySelector("#rankCourse").scrollIntoView({block: 'end', behavior: 'smooth'});

        this.$refs.rankCourse1.search(search_data);
        this.$refs.rankBook1.search(search_data);
        this.$refs.rankBlog1.search(search_data);
      }
    }
  }
</script>

<style scoped>

</style>
