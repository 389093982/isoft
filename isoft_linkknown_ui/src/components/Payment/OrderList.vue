<template>
	<div style="width: 100%;background-color: white">

  <Row>
    <Col span="22" offset="1">
      <Table border :columns="columns" :data="orderData">
        <template slot-scope="{ row }" slot="name">
          <strong>{{ row.name }}</strong>
        </template>
        <template slot-scope="{ row, index }" slot="action">
          <Button type="primary" size="small" style="margin-right: 5px" @click="show(index)">View</Button>
          <Button type="error" size="small" @click="remove(index)">Delete</Button>
        </template>
      </Table>
    </Col>
  </Row>

  </div>
</template>

<script>
  import {GetLoginUserName} from "../../tools";
  import {queryPayOrderList} from "../../api/index"

  export default {
    name: "OrderList",
    components:{GetLoginUserName},
    data () {
      return {
        columns: [
          {
            title: '订单号',
            slot: 'order_id'
          },
          {
            title: '交易时间',
            key: 'trans_time'
          },
          {
            title: '商品类型',
            key: 'goods_type'
          },
          {
            title: '商品ID',
            key: 'goods_id'
          },
          {
            title: '商品描述',
            key: 'goods_desc'
          },
          {
            title: '商品价格',
            key: 'goods_price'
          },
          {
            title: '商品图片',
            key: 'goods_img'
          },
          {
            title: '支付状态',
            key: 'pay_result'
          },
          {
            title: '操作',
            slot: 'action',
            width: 150,
            align: 'center'
          }
        ],
        orderData: [],
        //查询条件
        order_id:'',
        trans_date_start:'',
        trans_date_end:'',
        goods_type:'',
        goods_id:'',
        goods_desc:'',
        goods_price:'',
        pay_result:'SUCCESS',
      }
    },
    methods: {
      refreshOrderList:async function(){
        let params = {
          'order_id':this.order_id,
          'trans_date_start':this.trans_date_start,
          'trans_date_end':this.trans_date_end,
          'user_name':this.loginUserName,
          'goods_type':this.goods_type,
          'goods_id':this.goods_id,
          'goods_desc':this.goods_desc,
          'goods_price':this.goods_price,
          'pay_result':this.pay_result
        };
        const result = await queryPayOrderList(params)


      },
      show (index) {
        this.$Modal.info({
          title: '订单详情',
          content: `Name：${this.orderData[index].name}<br>Age：${this.orderData[index].age}<br>Address：${this.orderData[index].address}`
        })
      },
    },
    computed:{
      loginUserName: function () {
        return GetLoginUserName();
      }
    },
    mounted:function () {
      this.refreshOrderList();
    }
  }
</script>

<style scoped>

</style>
