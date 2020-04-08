<template>
  <div>
    <div>
      <Tag v-for="(query_case, index) in query_cases">
        <span @click="filterAuditData(query_case.case_name)"
              :style="{color: case_name === query_case.case_name ? 'red' : ''}">{{query_case.case_name}}</span>
      </Tag>
    </div>

    <div v-for="rowData in rowDatas" style="border:1px solid rgba(199,199,199,0.4);margin: 5px;padding: 10px;">
      <Row>
        <Scroll height="200">
          <Row>
            <Col span="12" v-for="(colName, index) in colNames">
              <Tag>字段名：{{colName}}</Tag> {{rowData[colName]}}
            </Col>
          </Row>
        </Scroll>
        <div>
          <span v-if="update_cases" v-for="update_case in update_cases">
            <span v-if="update_case.case_name" class="operate"
                  @click="handleOperate(update_case, rowData)">{{update_case.case_name}}</span>
          </span>
        </div>
      </Row>
    </div>

    <Page :total="total" :page-size="offset" show-total show-sizer :styles="{'text-align': 'center','margin-top': '10px'}"
          @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
  </div>
</template>

<script>
  import {ExecuteAuditTask, GetAuditHandleData, QueryTaskDetail} from "../../../api"
  import {getMatchArrForString, startsWith} from "../../../tools"

  export default {
    name: "AuditDetailHandle",
    data(){
      return {
        rowDatas:[],
        colNames:[],
        // 当前页
        current_page:1,
        // 总数
        total:0,
        // 每页记录数
        offset:10,
        search:"",
        query_cases:[],
        update_cases:[],
        case_name: '',
      }
    },
    methods:{
      filterAuditData: function (case_name) {
        this.case_name = case_name;
        this.refreshHandleData();
      },
      handleChange(page){
        this.current_page = page;
        this.refreshHandleData();
      },
      handlePageSizeChange(pageSize){
        this.offset = pageSize;
        this.refreshHandleData();
      },
      refreshHandleData:async function () {
        const result = await GetAuditHandleData({
          task_name: this.$route.query.task_name,
          current_page: this.current_page,
          offset: this.offset,
          case_name: this.case_name,
        });
        if(result.status == "SUCCESS"){
          this.rowDatas = result.rowDatas;
          this.colNames = JSON.parse(result.colNames);
          this.total = result.totalcount;
        }else{
          this.$Message.error("数据加载失败！" + result.errorMsg);
        }
      },
      refreshAuditDetail:async function () {
        const result = await QueryTaskDetail(this.$route.query.task_name);
        if(result.status == "SUCCESS"){
          this.query_cases = result.taskDetail.query_cases;
          this.update_cases = result.taskDetail.update_cases;
        }
      },
      handleOperate:async function(update_case, rowData){
        var sqlStr = update_case.update_sql;
        // 所有占位符变量
        let replaces = getMatchArrForString(sqlStr, /:[a-zA-Z0-9_]+/g);
        for(var i=0; i<replaces.length; i++){
          var replace = replaces[i];
          if(startsWith(replace, ":")){
            sqlStr = sqlStr.replace(replace, rowData[replace.slice(1)]);
          }
        }
        const result = await ExecuteAuditTask(this.$route.query.task_name, sqlStr);
        if(result.status == "SUCCESS"){
          this.$Message.success("操作成功！");
          this.refreshHandleData();
        }else{
          this.$Message.error("操作失败！" + result.errorMsg);
        }
      }
    },
    mounted(){
      this.refreshHandleData();
      this.refreshAuditDetail();
    }
  }
</script>

<style scoped>
  .operate{
    font-size: 14px;
    cursor: pointer;
    margin: 2px 10px;
    color: green;
  }
  .operate:hover{
    border-bottom: 1px solid red;
  }
</style>
