<template>
  <div>
    <Table border :columns="columns1" :data="advises" size="small"></Table>

    <Page :total="total" :page-size="offset" show-total show-sizer
          :styles="{'text-align': 'center','margin-top': '10px'}"
          @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
  </div>
</template>

<script>
  import {queryPageAdvise} from "../../api"

  export default {
    name: "AdviseList",
    data() {
      return {
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 20,
        advises: [],
        columns1: [
          {
            title: '类型',
            key: 'advise_type',
            width: 80,
          },
          {
            title: '内容',
            key: 'advise',
            width: 580,
          },
          {
            title: '提出人',
            key: 'created_by',
            width: 200,
          },
          {
            title: '提出时间',
            key: 'created_time',
          },
        ],
      }
    },
    methods: {
      handleChange(page) {
        this.current_page = page;
        this.refreshAdviseList();
      },
      handlePageSizeChange(pageSize) {
        this.offset = pageSize;
        this.refreshAdviseList();
      },
      refreshAdviseList: async function () {
        let params = {
          'offset':this.offset,
          'current_page':this.current_page
        };
        const result = await queryPageAdvise(params);
        if (result.status === "SUCCESS") {
          this.advises = result.advises;
          for (let i = 0; i < this.advises.length; i++) {
            // 格式化created_time
            this.advises[i].created_time = this.formatCreatedTime('YYYY-mm-dd HH:MM:SS',this.advises[i].created_time);
            if (this.advises[i].advise_type==='advise'){
              this.advises[i].advise_type = '意见';
            } else if (this.advises[i].advise_type==='complaints') {
              this.advises[i].advise_type = '吐槽';
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
      this.refreshAdviseList();
    }
  }
</script>

<style scoped>

</style>
