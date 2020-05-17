<template>
  <div>

    <div>
      <Row>
        <Col span="8" style="position: relative;top: -10px;">
          <Input v-model.trim="user_name" placeholder="用户名" style="width: 150px"/>
          <Input v-model.trim="nick_name" placeholder="昵称" style="width: 150px"/>
          <Button type="primary" shape="circle" icon="ios-search" @click="refreshUsersList"></Button>
        </Col>
        <Col span="16" style="position: relative;top: -10px;">
          <!--分页-->
          <div style="text-align: center;">
            <Page :total="page.totalCount" :page-size="page.offset" :current="page.currentPage" show-total show-sizer @on-change="pageChange" @on-page-size-change="pageSizeChange"/>
          </div>
        </Col>
      </Row>
    </div>


    <Table border :columns="userColumns" :data="users" size="small"></Table>

  </div>
</template>

<script>
  import {QueryUsers} from "../../api"

  export default {
    name: "UserSearch",
    data() {
      return {
        user_name:'',
        nick_name:'',
        page:{totalCount:0,currentPage:1,offset:10},
        users: [],
        userColumns: [
          {
            title: '用户名',
            key: 'user_name',
            width: 190,
          },
          {
            title: '昵称',
            key: 'nick_name',
            width: 190,
          },
          {
            title: '性别',
            key: 'gender',
            width: 90,
          },
          {
            title: 'vip等级',
            key: 'vip_level',
            width: 90,
          },
          {
            title: '角色',
            key: 'role_name',
            width: 90,
          },
          {
            title: '积分',
            key: 'user_points',
            width: 90,
          },
          {
            title: '来源',
            key: 'third_user_type',
            width: 90,
          },
          {
            title: '创建时间',
            key: 'created_time',
          },
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
      refreshUsersList: async function () {
        let params = {
          'user_name':this.user_name,
          'nick_name':this.nick_name,
          'offset':this.page.offset,
          'current_page':this.page.current_page,
        };
        const result = await QueryUsers(params);
        if (result.status === "SUCCESS") {
          this.users = result.users;
          for (let i = 0; i < this.users.length; i++) {
            // 格式化 created_time
            this.users[i].created_time = this.formatCreatedTime('YYYY-mm-dd HH:MM:SS',this.users[i].created_time);
            // 格式化 gender
            if (this.users[i].gender==='male'){
              this.users[i].gender = '男'
            }else if (this.users[i].gender==='female') {
              this.users[i].gender = '女'
            }
          }
          this.page.totalCount = result.paginator.totalcount;
        }
      },
      formatCreatedTime:function(fmt, date) {
        let ret="";
        date=new Date(date);
        const opt = {
          'Y+': date.getFullYear().toString(), // 年
          'm+': (date.getMonth() + 1).toString(), // 月
          'd+': date.getDate().toString(), // 日
          'H+': date.getHours().toString(), // 时
          'M+': date.getMinutes().toString(), // 分
          'S+': date.getSeconds().toString() // 秒
          // 有其他格式化字符需求可以继续添加，必须转化成字符串
        };
        for (let k in opt) {
          ret = new RegExp('(' + k + ')').exec(fmt);
          if (ret) {
            fmt = fmt.replace(
              ret[1],
              ret[1].length === 1 ? opt[k] : opt[k].padStart(ret[1].length, '0')
            )
          }
        }
        return fmt
      },
    },
    mounted() {
      this.refreshUsersList();
    }
  }
</script>

<style scoped>

</style>
