<template>
  <div>
    <Button type="success" size="small" style="margin: 10px 0;" @click="handleEditKaoshiTimu">新增题目</Button>

    <Table stripe :columns="columns1" :data="kaoshi_timus"></Table>
    <Page :total="total" :page-size="offset" show-total show-sizer :styles="{'text-align': 'center','margin-top': '10px'}"
          @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
  </div>
</template>

<script>
  import {QueryPageKaoshiTimu} from "../../api"

  export default {
    name: "KaoshiTimu",
    data (){
      return {
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 10,
        // 考试题目
        kaoshi_timus:[],
        columns1: [
          {
            title: '题目名称',
            key: 'timu_question'
          },
          {
            title: '操作',
            key: 'operate',
            width: '100',
            render: (h, params) => {
              return h('div', [
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
                      this.$router.push({path:'/background/kaoshiTimuEdit', query: {'timuId': this.kaoshi_timus[params.index].id}});
                    }
                  }
                }, '编辑')
              ]);
            }
          }
        ],
      }
    },
    methods:{
      handleEditKaoshiTimu: function () {
        this.$router.push({path: '/background/kaoshiTimuEdit'});
      },
      handleChange(page){
        this.current_page = page;
        this.refreshKaoshiTimu();
      },
      handlePageSizeChange(pageSize){
        this.offset = pageSize;
        this.refreshKaoshiTimu();
      },
      refreshKaoshiTimu: async function () {
        const result = await QueryPageKaoshiTimu({"current_page":this.current_page, "offset":this.offset});
        if (result.status === "SUCCESS"){
          this.kaoshi_timus = result.kaoshi_timus;
          this.total = result.paginator.totalcount;
        }
      },
    },
    mounted() {
      this.refreshKaoshiTimu();
    }
  }
</script>

<style scoped>

</style>
