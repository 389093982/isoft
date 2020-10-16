<template>
  <div>

    <div>
      <Row>
        <Col span="8" style="position: relative;top: -10px;">
          <Input v-model.trim="access_ip" placeholder="IP" style="width: 150px"/>
          <Input v-model.trim="user_name" placeholder="用户名" style="width: 150px"/>
          <Button type="primary" shape="circle" icon="ios-search" @click="refreshUserSiteRecords"></Button>
        </Col>
        <Col span="16" style="position: relative;top: -10px;">
          <!--分页-->
          <div style="text-align: center;">
            <Page :total="page.totalCount" :page-size="page.offset" :current="page.currentPage" show-total show-sizer @on-change="pageChange" @on-page-size-change="pageSizeChange"/>
          </div>
        </Col>
      </Row>
    </div>


    <Table border :columns="userSiteRcordsColumns" :data="userSiteRecords" size="small"></Table>

  </div>
</template>

<script>
  import {QueryUserSiteRecords} from "../../api"
  import {formatUTCtime} from "../../tools/index"

  export default {
    name: "VisitSearch",
    data() {
      return {
        access_ip:'',
        user_name:'',
        page:{totalCount:0,currentPage:1,offset:10},
        userSiteRecords: [],
        userSiteRcordsColumns: [
          {
            title: 'IP',
            key: 'access_ip',
            width: 200,
          },
          {
            title: '用户名',
            key: 'user_name',
            width: 200,
          },
          {
            title: '调用接口次数',
            key: 'access_count',
            width: 200,
          },
          {
            title: '最后访问时间',
            key: 'last_updated_time',
          }
        ],
      }
    },
    methods: {
      pageChange(page) {
        this.page.current_page = page;
        this.refreshUsersList();
      },
      pageSizeChange(pageSize) {
        this.page.offset = pageSize;
        this.refreshUsersList();
      },
      refreshUserSiteRecords: async function () {
        let params = {
          'access_ip':this.access_ip,
          'user_name':this.user_name,
          'offset':this.page.offset,
          'current_page':this.page.currentPage,
        };
        const result = await QueryUserSiteRecords(params);
        if (result.status === "SUCCESS") {
          this.userSiteRecords = result.userSiteRecords;
          for (let i = 0; i < this.userSiteRecords.length; i++) {
            // 格式化 last_updated_time
            this.userSiteRecords[i].last_updated_time = this.formatLastUpdateTime(this.userSiteRecords[i].last_updated_time);
          }
          this.page.totalCount = result.paginator.totalcount;
        }
      },
      formatLastUpdateTime:function(last_update_time) {
        return formatUTCtime(last_update_time,'yyyy-MM-dd HH:mm:ss')
      },
    },
    mounted() {
      this.refreshUserSiteRecords();
    }
  }
</script>

<style scoped>

</style>
