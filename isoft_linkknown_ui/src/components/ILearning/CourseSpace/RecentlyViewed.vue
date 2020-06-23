<template>
  <div>

    <div v-for="history in historys" style="margin-top: 10px">
      <Row>
        <Col span="10" offset = 5>
          <IBeautifulLink @onclick="$router.push({path:'/ilearning/courseDetail',query:{course_id:history.history_link}})">
            {{history.history_desc}}
          </IBeautifulLink>
        </Col>
        <Col span="6">
          <Time :time="history.last_updated_time" :interval="1"/>
        </Col>
      </Row>
    </div>
    <!--分页-->
    <div style="text-align: center;margin-top: 20px">
      <Page :total="page.totalCount" :page-size="page.offset" :current="page.currentPage" show-total show-sizer @on-change="pageChange" @on-page-size-change="pageSizeChange"/>
    </div>

  </div>
</template>

<script>
  import {ShowCourseHistory} from "../../../api"
  import IBeautifulLink from "../../Common/link/IBeautifulLink";

  export default {
    name: "RecentlyViewed",
    components: {IBeautifulLink},
    data() {
      return {
        page:{totalCount:0,currentPage:1,offset:10},
        historys: [],
      }
    },
    methods: {
      searchFunc: function (data) {
        this.$router.push({path: '/ilearning/courseSearch', query: {search: data}});
      },
      async refreshRecentlyViewed() {
        let params = {
          'offset':this.page.offset,
          'current_page':this.page.currentPage
        };
        const data = await ShowCourseHistory(params);
        if (data.status === "SUCCESS") {
          this.historys = data.historys;
          this.page.totalCount = data.paginator.totalcount;
        }
      },
      pageChange:function (page) {
        this.page.currentPage = page;
        this.refreshRecentlyViewed()
      },
      pageSizeChange:function (pageSize) {
        this.page.offset = pageSize;
        this.refreshRecentlyViewed()
      },
    },
    mounted: function () {
      this.refreshRecentlyViewed();
    }
  }
</script>

<style scoped>

</style>
