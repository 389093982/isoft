<template>
  <div style="margin: 10px;">
    <div style="display: flex;margin-bottom: 10px;margin-right: 10px;">
      <div style="width: 15%;">
        <ResourceEdit ref="resource_edit" @handleSuccess="refreshResourceList"/>
      </div>
      <div style="width: 35%;">
        <span class="dsnBox" style="position: relative;">
          <span style="cursor: pointer;">查看常用 dsn 链接串示例</span>
          <div class="dsnDemo">
            <Tag>mysql</Tag>
            <span @click="handleCopy(mysqlDsnEgg)">{{mysqlDsnEgg}}</span>
          </div>
        </span>
      </div>
      <div style="width: 50%;">
        <ISimpleSearch @handleSimpleSearch="handleSearch"/>
      </div>
    </div>

    <Table border :columns="columns1" :data="resources" size="small"></Table>
    <Page :total="total" :page-size="offset" show-total show-sizer :styles="{'text-align': 'center','margin-top': '10px'}"
          @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
  </div>
</template>

<script>
  import {copyText, formatDate} from "../../../tools/index"
  import {ResourceList} from "../../../api/index"
  import {DeleteResource} from "../../../api/index"
  import {ValidateResource} from "../../../api/index"
  import ISimpleLeftRightRow from "../../Common/layout/ISimpleLeftRightRow"
  import ISimpleSearch from "../../Common/search/ISimpleSearch"
  import ResourceEdit from "./ResourceEdit"

  export default {
    name: "ResourceList",
    components:{ISimpleLeftRightRow,ISimpleSearch,ResourceEdit},
    data(){
      return {
        mysqlDsnEgg: 'root:123456@tcp(127.0.0.1:3306)/iwork_learning?allowNativePasswords=true&charset=utf8&loc=Asia%2FShanghai&parseTime=True',
        // 当前页
        current_page:1,
        // 总数
        total:0,
        // 每页记录数
        offset:10,
        // 搜索条件
        search:"",
        resources: [],
        columns1: [
          {
            title: 'resource_name',
            key: 'resource_name',
            width: 150,
          },
          {
            title: 'resource_type',
            key: 'resource_type',
            width: 120,
          },
          {
            title: 'resource_url',
            key: 'resource_url',
            width: 150,
            ellipsis: true,
            tooltip: true,
          },
          {
            title: 'resource_dsn',
            key: 'resource_dsn',
            width: 320,
            ellipsis: true,
            tooltip: true,
          },
          {
            title: 'resource_username',
            key: 'resource_username',
            width: 160,
          },
          {
            title: 'resource_password',
            key: 'resource_password',
            width: 200,
          },
          {
            title: 'last_updated_time',
            key: 'last_updated_time',
            width: 150,
            render: (h,params)=>{
              return h('div',
                formatDate(new Date(params.row.last_updated_time),'yyyy-MM-dd hh:mm')
              )
            }
          },
          {
            title: '操作',
            key: 'operate',
            width: 280,
            fixed: 'right',
            render: (h, params) => {
              return h('div', [
                h('Button', {
                  props: {
                    type: 'error',
                    size: 'small'
                  },
                  style: {
                    marginRight: '5px',
                  },
                  on: {
                    click: () => {
                      this.validateResource(this.resources[params.index]['id']);
                    }
                  }
                }, '连接测试'),
                h('Button', {
                  props: {
                    type: 'success',
                    size: 'small'
                  },
                  style: {
                    marginRight: '5px',
                  },
                  on: {
                    click: () => {
                      this.$refs.resource_edit.initData(this.resources[params.index]['id']);
                    }
                  }
                }, '编辑'),
                h('Button', {
                  props: {
                    type: 'warning',
                    size: 'small'
                  },
                  style: {
                    marginRight: '5px',
                  },
                  on: {
                    click: () => {
                      this.deleteResource(this.resources[params.index]['id']);
                    }
                  }
                }, '删除'),
              ]);
            }
          }
        ],
      }
    },
    methods:{
      handleCopy: function (text){
        var _this = this;
        copyText(text, function () {
          _this.$Message.success("复制成功！");
        });
      },
      validateResource: async function(id){
        const result = await ValidateResource(id);
        if(result.status==="SUCCESS"){
          this.$Message.success("验证通过!");

        }else{
          this.$Message.error("验证失败!" + result.errorMsg);
        }
      },
      deleteResource: async function(id){
        const result = await DeleteResource(id);
        if(result.status==="SUCCESS"){
          this.refreshResourceList();
        }
      },
      refreshResourceList:async function () {
        const result = await ResourceList(this.offset,this.current_page,this.search);
        if(result.status==="SUCCESS"){
          this.resources = result.resources;
          this.total = result.paginator.totalcount;
        }
      },
      handleChange(page){
        this.current_page = page;
        this.refreshResourceList();
      },
      handlePageSizeChange(pageSize){
        this.offset = pageSize;
        this.refreshResourceList();
      },
      handleSearch(data){
        this.offset = 10;
        this.current_page = 1;
        this.search = data;
        this.refreshResourceList();
      }
    },
    mounted: function () {
      this.refreshResourceList();
    },
  }
</script>

<style scoped>
  .dsnBox {
    padding: 10px 0;
  }
  .dsnDemo {
    display: none;
  }
  .dsnBox:hover .dsnDemo {
    position:absolute;
    height: 200px;
    width: 500px;
    bottom: -200px;
    left: 0;
    display: inline-block;
    z-index: 10;
    padding: 10px;
    background-color: white;
    cursor: pointer;
    box-shadow: 0 0 5px 1px rgba(255, 0, 0, 0.51);
  }
</style>
