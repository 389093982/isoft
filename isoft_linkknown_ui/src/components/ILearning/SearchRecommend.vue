<template>
  <div>
    <!--为您精选、热门、特色、高评-->
    <div id="rankCourse" style="display: flex;justify-content: center;" class="isoft_top5">
      <div style="width: 31%;">
        <RankCourse ref="rankCourse1" class="isoft_bg_white isoft_pd10"/>
      </div>
      <div style="width: 31%;margin: 0 5px;">
        <RankCourse class="isoft_bg_white isoft_pd10"/>
      </div>
      <div style="width: 31%;">
        <RankCourse class="isoft_bg_white isoft_pd10"/>
      </div>
    </div>

    <div style="display: flex;justify-content: center;" class="isoft_top5">
      <!--热门书单-->
      <div style="width: 31%;">
        <RankBook ref="rankBook1" class="isoft_bg_white isoft_pd10"/>
      </div>
      <div style="width: 31%;margin: 0 5px;">
        <RankBook class="isoft_bg_white isoft_pd10"/>
      </div>
      <div style="width: 31%;">
        <RankBook class="isoft_bg_white isoft_pd10"/>
      </div>
    </div>

    <!--热门博客-->
    <div style="display:flex;justify-content: center;" class="isoft_top5">
      <div style="width: 31%;">
        <RankBlog ref="rankBlog1" class="isoft_bg_white isoft_pd10"/>
      </div>
      <div style="width: 31%;margin: 0 5px;">
        <RankBlog class="isoft_bg_white isoft_pd10"/>
      </div>
      <div style="width: 31%;">
        <RankBlog class="isoft_bg_white isoft_pd10"/>
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
