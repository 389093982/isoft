<template>
  <div>

    <div>
      <Row>
        <Col span="6">
          <Input v-model.trim="user_name" placeholder="user_name" style="width: 200px"/>
          <Button type="primary" shape="circle" icon="ios-search" @click="refreshLoginRecordList"></Button>
        </Col>
        <Col span="18" style="position: relative;top: -10px;">
          <!--分页-->
          <div style="text-align: center;">
            <Page :total="total" :page-size="offset" show-total show-sizer :styles="{'text-align': 'center','margin-top': '10px'}" @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
          </div>
        </Col>
      </Row>
    </div>

    <Table width="1000" border :columns="loginRecordColumn" :data="loginRecordDatas" size="small"></Table>

  </div>
</template>

<script>
  import {formatDate} from "../../tools"
  import {LoginRecordList} from "../../api"
  import ISimpleLeftRightRow from "../Common/layout/ISimpleLeftRightRow"
  import ISimpleSearch from "../Common/search/ISimpleSearch"

  export default {
    name: "LoginRecord",
    components: {ISimpleLeftRightRow, ISimpleSearch},
    data() {
      return {
        current_page: 1,
        total: 0,
        offset: 10,
        user_name: "",
        loginRecordDatas: [],
        loginRecordColumn: [
          {
            title: '登录用户',
            key: 'user_name',
            width: 190,
            fixed: 'left'
          },
          {
            title: '用户昵称',
            key: 'nick_name',
            width: 160
          },
          {
            title: '角色',
            key: 'role_name',
            width: 90
          },
          {
            title: '登录时间',
            key: 'created_time',
            render: (h, params) => {
              return h('div', formatDate(new Date(params.row.created_time), 'yyyy-MM-dd hh:mm:ss'))
            },
            width: 150
          },
          {
            title: '登录状态',
            key: 'login_status',
            width: 95
          },
          {
            title: '登录IP',
            key: 'login_ip',
            width: 140
          },
          {
            title: 'origin',
            key: 'origin',
            width: 180
          },
          {
            title: 'referer',
            key: 'referer',
            width: 800,
          },
          {
            title: '客户端类型',
            key: 'client_type',
            width: 100,
            fixed: 'right'
          },
        ],
      }
    },
    methods: {
      refreshLoginRecordList: async function () {
        let params = {
          'offset':this.offset,
          'current_page':this.current_page,
          'user_name':this.user_name,
        };
        const result = await LoginRecordList(params);
        if (result.status === "SUCCESS") {
          this.loginRecordDatas = result.loginRecords;
          this.total = result.paginator.totalcount;
        }
      },
      handleChange(page) {
        this.current_page = page;
        this.refreshLoginRecordList();
      },
      handlePageSizeChange(pageSize) {
        this.offset = pageSize;
        this.refreshLoginRecordList();
      },
    },
    mounted: function () {
      this.refreshLoginRecordList();
    },
  }
</script>

<style scoped>

</style>
