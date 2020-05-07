<template>
  <div>

    <!--表格展示活动-->
    <Table width="1000" border :columns="activityColumns" :data="activityDatas"></Table>

    <!--分页-->
    <div style="text-align: center;margin-top: 10px">
      <Page :total="page.totalCount" :page-size="page.offset" :current="page.currentPage" show-total show-sizer @on-change="pageChange" @on-page-size-change="pageSizeChange"/>
    </div>

  </div>
</template>
<script>
  import {QueryPagePayActivity} from "../../../api/index"

  export default {
    name: "SearchActivity",
    data () {
      return {
        page:{totalCount:0,currentPage:1,offset:10},
        activityColumns: [
          {
            title: '活动ID',
            key: 'activity_id',
            width: 180,
            fixed: 'left'
          },
          {
            title: '活动描述',
            key: 'activity_desc',
            width: 200,
          },
          {
            title: '活动类型',
            key: 'activity_type',
            width: 100,
          },
          {
            title: '数量',
            key: 'type_entity_account',
            width: 80,
          },
          {
            title: '已领取',
            key: 'received_entity_account',
            width: 80,
          },
          {
            title: '已使用',
            key: 'used_entity_account',
            width: 80,
          },
          {
            title: '举办方',
            key: 'organizer',
            width: 150,
          },
          {
            title: '活动开始日期',
            key: 'start_date',
            width: 120,
          },
          {
            title: '活动结束日期',
            key: 'end_date',
            width: 120,
          },
          {
            title: '创建人',
            key: 'created_by',
            width: 200,
          },
          {
            title: '创建时间',
            key: 'created_time',
            width: 150,
            fixed: 'right'
          },
        ],
        activityDatas: []
      }
    },
    methods: {
      refreshPayActivity: async function () {
        let params = {
          'currentPage':this.page.currentPage,
          'offset':this.page.offset,
        };
        const result = await QueryPagePayActivity(params);
        if (result.status === 'SUCCESS') {
          this.page.totalCount = result.paginator.totalcount;
          this.activityDatas = result.activityDatas;
        }
      },
      pageChange:function (page) {
        this.page.currentPage = page;
        this.refreshPayActivity()
      },
      pageSizeChange:function (pageSize) {
        this.page.offset = pageSize;
        this.refreshPayActivity()
      },

    },
    mounted:function () {
      this.refreshPayActivity();
    },
  }
</script>

<style scoped>

</style>
