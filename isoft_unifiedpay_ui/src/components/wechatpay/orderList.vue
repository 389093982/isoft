<template>
  <div>

    <!--查询-->
    <div style="margin-top: 5px">
      <Input v-model="OrderId" placeholder="订单号" style="width: 250px"/>
      <Select v-model="TransType" placeholder="交易类型" style="width:100px">
        <Option v-for="item in TransTypeList" :value="item.value" :key="item.value">{{ item.label }}</Option>
      </Select>
      <Input v-model="ProductDesc" placeholder="商品描述" style="width: 150px"/>
      <DatePicker v-model="TransTime" type="date" placeholder="交易日期" style="width: 140px"></DatePicker>
      <Input v-model="TransAmount" placeholder="交易金额:00.00" style="width: 100px"/>
      <Button type="primary" shape="circle" icon="ios-search" @click="clickQuery"></Button>
    </div>

    <!--列表展示-->
    <div>
      <Table width="1020" height="auto" border :columns="columns" :data="orders" size="small"></Table>
    </div>

    <!--分页-->
    <div style="text-align: center;margin-top: 10px">
      <Page :total="page.totalCount" :page-size="page.pageSize" :current="page.currentPage" show-total show-sizer @on-change="pageChange" @on-page-size-change="pageSizeChange"/>
    </div>

    <!--订单详情-->
    <Modal title="订单详情" v-model="orderDetailModal" :mask-closable="false" width="1200">
      <orderDetail :Order=orderDetailModalData></orderDetail>
    </Modal>

  </div>
</template>
<script>
  import {QueryOrder,ShowLastedOrders} from '../../api/api'
  import orderDetail from './orderDetail'
  export default {
    name: "orderList",
    components:{orderDetail,},
    data () {
      return {
        OrderId:'',
        TransType:'',
        TransTypeList:[{value:'SALE',label:'SALE'},{value:'REFUND',label:'REFUND'}],
        ProductDesc:'',
        TransTime:'',
        TransAmount:'',
        orderDetailModal: false,
        columns: [
          {title: '订单号',
            key: 'OrderId',
            width: 250,
            fixed: 'left'
          },
          {title: '交易类型',
            key: 'TransType',
            width: 100
          },
          {title: '商品描述',
            key: 'ProductDesc',
            width: 150
          },
          {title: '交易时间',
            key: 'TransTime',
            width: 150
          },
          {title: '交易金额',
            key: 'TransAmount',
            width: 100
          },
          {title: '下单结果描述',
            key: 'OrderResultDesc',
            width: 140
          },
          {title: '付款业务结果描述',
            key: 'PayResultDesc',
            width: 150
          },
          {title: '退款请求业务结果描述',
            key: 'RefundReqResultDesc',
            width: 160
          },
          {title: '退款状态',
            key: 'RefundStatus',
            width: 100
          },
          {title: '已退金额',
            key: 'RefundedAmount',
            width: 100
          },
          {title: '操作',
            key: 'action',
            width: 120,
            fixed: 'right',
            render: (h, params) => {
              return h('div', [
                h('Button', {
                  props: {type: 'text', size: 'small'},
                  on:{
                    click: () => {
                      this.orderDetailModal = true;
                      for (let i = 0; i < this.orders.length; i++) {
                        if (this.orders[i].OrderId===this.orders[params.index].OrderId){
                          this.orderDetailModalData = this.orders[i];
                        }
                      }
                    }
                  }
                }, '查看'),
              ]);
            }
          }
        ],
        orders: [],
        orderDetailModalData:{},
        page:{totalCount:0,currentPage:1,pageSize:10},
      }
    },
    mounted (){
      //初始化界面，展示几条订单
      this.queryOrder()
    },
    methods:{
      clickQuery:function(){
        this.page.pageSize=10;
        this.page.currentPage=1;
        this.queryOrder()
      },
      queryOrder:async function () {
        let params = {
          'OrderId':this.OrderId,
          'TransType':this.TransType,
          'ProductDesc':this.ProductDesc,
          'TransTime':new Date(this.TransTime).format("yyyyMMdd"),
          'TransAmount':(this.TransAmount*1).toFixed(2),
          'currentPage':this.page.currentPage,
          'pageSize':this.page.pageSize,
        };
        let result = await QueryOrder(params);
        let orders = result.orders;
        this.page.totalCount = result.totalCount;
        let ordersObj = JSON.parse(orders);
        if (ordersObj.length === 0) {
          this.$Message.warning("查不到数据！")
        }else{
          this.$Message.success("查询成功！");
          this.orders = ordersObj;
          for (let i = 0; i < this.orders.length; i++) {
            this.orders[i].TransAmount = (this.orders[i].TransAmount*1).toFixed(2);
            this.orders[i].RefundedAmount = (this.orders[i].RefundedAmount*1).toFixed(2);
          }
        }
      },
      pageChange:function (page) {
        this.page.currentPage = page;
        this.queryOrder()
      },
      pageSizeChange:function (pageSize) {
        this.page.pageSize = pageSize;
        this.queryOrder()
      },
    }
  }
</script>

<style scoped>

</style>
