<template>
  <div>
    请选择数据源：
    <Select v-model="taskDetail.resource_name" style="width:300px;margin: 10px 0;">
      <Option v-for="(resource,index) in resources" :value="resource.resource_name" :key="resource.resource_name">
        {{ resource.resource_name }} ~ {{ resource.resource_dsn }}
      </Option>
    </Select>

    <br/>

    <div style="display: flex;margin-top: 20px;padding:5px;border-top: 2px solid #eee;">
      <div style="width: 80%;">
        <Tabs type="card" :animated="false" name="tab_level_2" :value="tabValue">
        <span v-if="taskDetail.query_cases && taskDetail.query_cases.length > 0">
          <TabPane v-for="(item, index) in taskDetail.query_cases" :name="'tabValue_' + index"
                   :label="item.case_name ? item.case_name : '场景 ' + (index + 1)" tab="tab_level_2">
            场景名称: <span style="color: #00ce00;">参考案例：生效、失效、审核通过、内容不合法等中文或英文</span>
                   <Button type="error" size="small" @click="handleRemove(index)">删除</Button>
                   <Icon type="md-arrow-back" size="20" @click="moveLocation(index, -1)"/> <Icon type="md-arrow-forward" size="20" @click="moveLocation(index, 1)"/>

            <Input type="text" v-model="item.case_name" placeholder="请输入场景名称" style="margin: 5px 0;"></Input>
            场景查询sql: <span style="color: #00ce00;">参考案例：select * from blog where status = 1</span>
            <Input type="textarea" :rows="6" v-model="item.query_sql" placeholder="请输入 query_sql"
                   style="margin: 5px 0;"></Input>
            场景描述: <span style="color: #00ce00;">参考案例：查询所有 status = 1 的用户</span>
            <Input type="textarea" :rows="6" v-model="item.query_desc" placeholder="请输入描述" style="margin: 5px 0;"></Input>
          </TabPane>
        </span>
          <span v-else>暂无场景</span>
        </Tabs>
      </div>
      <div style="width: 20%;padding-left: 10px;">
        <Button type="success" size="small" @click="editAuditTaskSource">保存提交</Button>
        <Button type="success" size="small" @click="handleAdd">新增场景</Button>

        <br/><br/>
        <Tag v-for="(item, index) in taskDetail.query_cases"><span @click="tabValue = 'tabValue_' + index">{{item.case_name}}</span></Tag>
      </div>
    </div>

    <h3 style="margin-top: 20px;">字段显示类型：</h3>
    <p v-for="(col_name, index) in taskDetail.col_names">
      <Tag>{{col_name}}</Tag>
    </p>
  </div>
</template>

<script>
  import {GetAllResource, EditAuditTaskSource,QueryTaskDetail} from "../../../api"
  import {swapArray} from "../../../tools";

  export default {
    name: "AuditDetailSourceEdit",
    components:{},
    data(){
      return {
        tabValue: "tabValue_0",            // 当前激活 tab 面板的 name
        resources:[],
        taskDetail:{
          resource_name:'',
          col_names:[],
          query_cases:[
              {
              case_name:'全部',
              query_sql: '',
              query_desc:'',
            }
          ]
        },
      }
    },
    methods:{
      moveLocation: function (index, step){
          swapArray(this.taskDetail.query_cases, index, index + step);
          if (step > 0) {
            this.tabValue = "tabValue_" + (index < this.taskDetail.query_cases.length - 1 ? index + 1 : index);
          } else if (step < 0) {
            this.tabValue = "tabValue_" + (index > 0 ? index - 1 : index);
          }
      },
      handleRemove (index) {
        // 删除一个数组元素
        this.taskDetail.query_cases.splice(index, 1);
      },
      handleAdd: function (){
        this.taskDetail.query_cases.push({
          case_name:'新增场景' + this.taskDetail.query_cases.length,
          query_sql: '',
          query_desc:'',
        });
      },
      editAuditTaskSource:async function(){
        const result = await EditAuditTaskSource({
            task_name:this.$route.query.task_name,
            resource_name:this.taskDetail.resource_name,
            query_cases:JSON.stringify(this.taskDetail.query_cases),
        });
        if(result.status === "SUCCESS"){
          this.$Message.success("保存成功！");
          this.$router.go(0);     // 强制刷新页面
        }else{
          this.$Message.error("配置错误!");
        }
      },
      refreshAllResource:async function(){
        const result = await GetAllResource("db");
        if(result.status === "SUCCESS"){
          this.resources = result.resources;
        }
      },
      refreshAuditDetail:async function () {
        const result = await QueryTaskDetail(this.$route.query.task_name);
        if(result.status === "SUCCESS"){
          this.taskDetail = result.taskDetail;
          this.taskDetail.query_cases = result.taskDetail.query_cases || [];
          this.taskDetail.col_names = JSON.parse(result.taskDetail.col_names);
        }
      }
    },
    mounted(){
      this.refreshAllResource();
      this.refreshAuditDetail();
    }
  }
</script>

<style scoped>

</style>
