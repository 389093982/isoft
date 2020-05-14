<template>
  <div>

    <div>
      <Row>
        <Col span="8" style="position: relative;top: -10px;">
          <Input v-model.trim="file_name" placeholder="文件名" style="width: 150px"/>
          <Input v-model.trim="created_by" placeholder="上传人" style="width: 150px"/>
          <Button type="primary" shape="circle" icon="ios-search" @click="refreshFileUploadLog"></Button>
        </Col>
        <Col span="16" style="position: relative;top: -10px;">
          <!--分页-->
          <div style="text-align: center;">
            <Page :total="page.totalCount" :page-size="page.offset" :current="page.currentPage" show-total show-sizer @on-change="pageChange" @on-page-size-change="pageSizeChange"/>
          </div>
        </Col>
      </Row>
    </div>

    <Table width="1000" border :columns="fileUploadLogColumns" :data="fileUploadLogDatas" size="small"></Table>


  </div>
</template>
<script>
  import {QueryPageFileUploadLog} from "../../api/index"

  export default {
    name: "FileUploadMonitor",
    data () {
      return {
        file_name:'',
        created_by:'',
        page:{totalCount:0,currentPage:1,offset:10},
        fileUploadLogColumns: [
          {
            title: '文件名',
            key: 'file_name',
            width: 260,
            fixed: 'left'
          },
          {
            title: '上传人',
            key: 'created_by',
            width: 200,
          },
          {
            title: '上传时间',
            key: 'created_time',
            width: 200,
          },
          {
            title: '文件路径',
            key: 'file_addr',
            width: 700,
          },
          {
            title: '目标表名',
            key: 'table_name',
            width: 150,
          },
          {
            title: '表字段',
            key: 'table_field',
            width: 150,
          },
          {
            title: '文件大小',
            key: 'file_size',
            width: 100,
            fixed: 'right'
          },

        ],
        fileUploadLogDatas: [],
      }
    },
    methods: {
      refreshFileUploadLog: async function () {
        let params = {
          'file_name':this.file_name,
          'created_by':this.created_by,
          'currentPage':this.page.currentPage,
          'offset':this.page.offset,
        };
        const result = await QueryPageFileUploadLog(params);
        if (result.status === 'SUCCESS') {
          this.page.totalCount = result.paginator.totalcount;
          this.fileUploadLogDatas = result.fileUploadLogDatas;
          for (let i = 0; i < this.fileUploadLogDatas.length; i++) {
            //格式化上传时间
            this.fileUploadLogDatas[i].created_time = this.formatCreatedTime('YYYY-mm-dd HH:MM:SS',this.fileUploadLogDatas[i].created_time);
          }
        }
      },
      pageChange:function (page) {
        this.page.currentPage = page;
        this.refreshFileUploadLog()
      },
      pageSizeChange:function (pageSize) {
        this.page.offset = pageSize;
        this.refreshFileUploadLog()
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
    mounted:function () {
      this.refreshFileUploadLog();
    },
  }
</script>

<style scoped>

</style>
