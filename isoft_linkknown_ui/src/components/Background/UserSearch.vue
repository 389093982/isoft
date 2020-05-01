<template>
  <div>
    <Table border :columns="userColumns" :data="users" size="small"></Table>

    <Page :total="total" :page-size="offset" show-total show-sizer :styles="{'text-align': 'center','margin-top': '10px'}" @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
  </div>
</template>

<script>
  import {QueryAllUsers} from "../../api"

  export default {
    name: "UserSearch",
    data() {
      return {
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 20,
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
      handleChange(page) {
        this.current_page = page;
        this.refreshAllUsersList();
      },
      handlePageSizeChange(pageSize) {
        this.offset = pageSize;
        this.refreshAllUsersList();
      },
      refreshAllUsersList: async function () {
        const result = await QueryAllUsers(this.offset, this.current_page);
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
          this.total = result.paginator.totalcount;
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
          ret = new RegExp('(' + k + ')').exec(fmt)
          if (ret) {
            fmt = fmt.replace(
              ret[1],
              ret[1].length == 1 ? opt[k] : opt[k].padStart(ret[1].length, '0')
            )
          }
        }
        return fmt
      },
    },
    mounted() {
      this.refreshAllUsersList();
    }
  }
</script>

<style scoped>

</style>
