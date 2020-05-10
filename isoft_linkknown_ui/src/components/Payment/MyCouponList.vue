<template>
  <div style="width: 100%;">

  <div style="display: flex">
    <div style="width: 68%;background-color: white">
      <Row>
        <!--左侧空出一点-->
        <Col span="4">
          &nbsp;
        </Col>
        <!--右侧展示商品-->
        <Col span="20">
          <!--一行一个商品-->
          <Row v-for="(goods, index) in orderData" style="margin-top: 20px">
            <Row>
              <!--商品图片-->
              <Col span="7">
                <!--如果是课程-->
                <div v-if="goods.goods_type==='course_theme_type'" @click="$router.push({path:'/ilearning/courseDetail',query:{course_id:goods.goods_id}})" style="cursor: pointer">
                  <img v-if="goods.goods_img" :src="goods.goods_img" width="180" height="120"/>
                  <img v-else src="../../../static/images/common_img/default.png" width="180" height="120"/>
                  <div class="ico_play"></div>
                </div>
                <!--如果是vip-->
                <div v-if="goods.goods_type==='vip'">
                  <img src="../../../static/images/vipCenter/vip_card.jpg" width="180" height="120"/>
                </div>
              </Col>
              <!--商品信息-->
              <Col span="10">
                <Row style="margin-top: 10px">
                  <code v-if="goods.goods_type==='course_theme_type'">课程</code>
                  <code v-else-if="goods.goods_type==='vip'">会员</code>
                </Row>
                <Row style="margin-top: 10px">
                  {{goods.goods_desc}}
                </Row>
                <Row style="margin-top: 10px">
                  <div style="display: flex">
                    <div class="orderTipService" @click="$router.push({path:'/payment/orderDetail',query:{order_id:goods.order_id}})">查看详情</div>
                    <div class="orderTipService" style="margin-left: 10px">查看发票</div>
                    <div class="orderTipService" @click="$router.push({path:'/contact/contactList'})" style="margin-left: 10px">联系客服</div>
                  </div>
                </Row>
              </Col>
              <!--商品价格-->
              <Col span="5" style="color: #ff6900;">
                <Row style="margin-top: 10px">&nbsp;</Row>
                <Row>
                  <div style="margin-top: 10px">
                    <Icon type="logo-yen" style="font-size: 12px"/>
                    <span style="font-size: 20px">{{formatAmount(goods.goods_price)}}</span>
                  </div>
                </Row>
                <Row style="margin-top: 10px">
                  <code style="color: grey">{{formatTransTime(goods.trans_time)}}</code>
                </Row>
              </Col>
              <!--交易完成认证，圆形印章图片-->
              <Col span="2">
                <img v-if="goods.pay_result==='SUCCESS'" style="border-radius:50%;position: relative;top: -20px;left: -50px" width=80 height=80 src="../../../static/images/order/transaction_success.png">
              </Col>
            </Row>
            <!--彩色分底线-->
            <SepLine></SepLine>
          </Row>

          <!--分页-->
          <div style="text-align: center;margin-top: 10px">
            <Page :total="page.totalCount" :page-size="page.offset" :current="page.currentPage" show-total show-sizer @on-change="pageChange" @on-page-size-change="pageSizeChange"/>
          </div>

        </Col>
      </Row>
    </div>
    <div style="width: 27%;margin-left: 5px;background-color: white">
      <div>
        <img src="../../../static/images/common_img/linkknown_to_lovely_you.jpg" height="590" width=100%/>
      </div>
    </div>
  </div>

  </div>
</template>

<script>
  import {GetLoginUserName} from "../../tools";
  import {queryPayOrderList} from "../../api/index"
  import SepLine from "../Common/SepLine";

  export default {
    name: "MyCouponList",
    components:{SepLine, GetLoginUserName},
    data () {
      return {
        orderData: [],
        page:{totalCount:0,currentPage:1,offset:10},
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
          'pay_result':this.pay_result,
          'currentPage':this.page.currentPage,
          'offset':this.page.offset,
        };
        const result = await queryPayOrderList(params);
        if (result.status === 'SUCCESS') {
          this.orderData = result.orders;
          this.page.totalCount = result.paginator.totalcount;
        }

      },
      formatAmount:function (amount) {
        let newAmount = (amount/100).toString();
        if (newAmount.indexOf('.')<0) {
          newAmount = newAmount+".00"
        }
        return newAmount;
      },
      formatTransTime:function (trans_time) {
        let date = trans_time.slice(0,8);
        let time = trans_time.slice(8,14);
        let formatDate = date.slice(0,4)+"-"+date.slice(4,6)+"-"+date.slice(6,8);
        let formatTime = time.slice(0,2)+":"+time.slice(2,4)+":"+time.slice(4,6);
        return formatDate + " " +formatTime;
      },
      pageChange:function (page) {
        this.page.currentPage = page;
        this.refreshOrderList()
      },
      pageSizeChange:function (pageSize) {
        this.page.offset = pageSize;
        this.refreshOrderList()
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
  .ico_play {
    background: url(../../../static/images/common_img/ico_play.png) no-repeat;
    position: absolute;
    top: 35px;
    left: 55px;
    width: 60px;
    height: 60px;
  }
  .orderTipService{
    cursor: pointer;
    padding: 2px 0 0 8px ;
    height: 30px;
    width: 80px;
    background-color: rgba(220, 220, 220, 0.39);
    border-radius: 20px;
    border: 2px orange solid;
  }
  .orderTipService:hover{
    color: rgba(255, 105, 0, 0.65);
    background-color: rgba(214, 214, 214, 0.99);
  }
</style>
