<template>
  <div style="padding: 50px;">
    <Button @click="$router.push({path:'/oa/requireEdit'})">新增需求</Button>

    <Row style="padding: 15px 0px;border-bottom: 1px solid #d7dde4;">
      <Col span="4">需求名称</Col>
      <Col span="4">需求详情</Col>
      <Col span="4">需求备注</Col>
      <Col span="4">需求时间</Col>
      <Col span="2">需求状态</Col>
      <Col span="2">需求 owner</Col>
      <Col span="2">需求分配人员</Col>
      <Col span="2">操作</Col>
    </Row>
    <Row v-for="(require, index) in requires" style="padding: 15px 0px;border-bottom: 1px solid #d7dde4;">
      <Col span="4" class="isoft_inline_ellipsis">{{require.require_name}}</Col>
      <Col span="4" class="isoft_inline_ellipsis">{{require.require_detail}}</Col>
      <Col span="4" class="isoft_inline_ellipsis">{{require.require_mark}}</Col>
      <Col span="4">{{require.require_start_time}} - {{require.require_end_time}}</Col>
      <Col span="2">{{require.require_status}}</Col>
      <Col span="2">{{require.require_owner}}</Col>
      <Col span="2">{{require.require_user}}</Col>
      <Col span="2">
        <Button type="success" size="small" @click="$router.push({path:'/oa/requireEdit', query:{id:require.id}})">编辑
        </Button>
      </Col>
    </Row>

    <Page :total="total" :page-size="offset" show-total show-sizer
          :styles="{'text-align': 'center','margin-top': '10px'}"
          @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
  </div>
</template>

<script>
  import {FilterPageRequireList} from "../../api"

  export default {
    name: "RequireList",
    data() {
      return {
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 10,
        requires: []
      }
    },
    methods: {
      handleChange(page) {
        this.current_page = page;
        this.refreshRequireList();
      },
      handlePageSizeChange(pageSize) {
        this.offset = pageSize;
        this.refreshRequireList();
      },
      refreshRequireList: async function () {
        const result = await FilterPageRequireList({offset: this.offset, current_page: this.current_page});
        if (result.status == "SUCCESS") {
          this.requires = result.requires;
          this.total = result.paginator.totalcount;
        }
      }
    },
    mounted() {
      this.refreshRequireList();
    }
  }
</script>

<style scoped>
  .isoft_inline_ellipsis {
    word-wrap: break-word;
    word-break: break-all;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
</style>
