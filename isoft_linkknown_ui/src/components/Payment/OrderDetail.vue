<template>
	<div>

    <div style="display: flex">
      <div style="width: 70%;background-color: white;min-height: 550px">
        <!--一行订单信息-->
        <Table border :columns="orderColumns" :data="orders"></Table>
        <!--如果是课程信息-->
        <div v-if="orders[0].goods_type==='course_theme_type'" style="border: 1px solid #f4f4f4;padding: 15px;margin-left: 5px;min-height: 500px;">
          <Row style="border-bottom: 1px solid #f4f4f4;padding: 10px;">
            <Col span="8">
              <router-link :to="{path:'/ilearning/courseDetail',query:{course_id:course.id}}">
                <h4>课程名称：{{course.course_name}}</h4>
                <img v-if="course.small_image" :src="course.small_image" height="120" width="180"/>
                <img v-else src="../../assets/default.png" height="120" width="180"/>
              </router-link>
            </Col>
            <Col span="16">
              <CourseMeta :course="course"/>
            </Col>
          </Row>
        </div>
        <!--如果是vip会员-->
        <div v-else-if="orders[0].goods_type==='vip'" style="display: flex">
          <div style="width: 22%;background-color: white;margin: 30px 0 0 30px">
            <img src="../../../static/images/vipCenter/vip_card.jpg" width="180" height="120"/>
          </div>
          <div style="width: 35%;background-color: white;margin: 30px 0 0 0 ">
            <Row style="margin-top: 10px">vip会员</Row>
            <Row style="margin-top: 10px">{{orders[0].goods_desc}}</Row>
            <Row style="margin-top: 10px">会员到期时间: {{user.vip_expired_time}} </Row>
          </div>
        </div>
      </div>
      <div style="width: 25%;margin: 0 0 0 5px ;background-color: white">
        <HotUser></HotUser>
      </div>
    </div>

  </div>
</template>

<script>
  import {GetUserDetail, queryPayOrderList, ShowCourseDetail} from "../../api/index"
  import HotUser from "../User/HotUser";
  import CourseMeta from "../ILearning/Course/CourseMeta";
  import {getLoginUserName} from "../../tools/sso";

	export default {
		name: "OrderDetail",
    components:{CourseMeta, HotUser},
    data(){
		  return{
        orderColumns: [
          {
            title: '订单号',
            key: 'order_id',
            width:245
          },
          {
            title: '交易时间',
            key: 'trans_time',
            width:160
          },
          {
            title: '描述',
            key: 'goods_desc',
            width:220
          },
          {
            title: '价格',
            key: 'goods_price',
            width:130
          },
          {
            title: '支付状态',
            key: 'pay_result',
            width:120
          },
        ],
        orders:[],
        course:'',
        user:'',
      }
    },
    methods:{
		  refreshOrder:async function () {
		    let params = {
		      'order_id':this.$route.query.order_id,
        };
        const result = await queryPayOrderList(params);
        if (result.status === 'SUCCESS' && result.orders.length===1) {
          this.orders = result.orders;
          //给金额和支付状态格式化
          if (this.orders[0].pay_result === 'SUCCESS') {
            this.orders[0].pay_result = '支付成功'
          }else {
            this.orders[0].pay_result = '支付失败'
          }
          //格式化金额
          this.orders[0].goods_price = this.formatAmount(this.orders[0].goods_price);
          // 格式化交易时间
          this.orders[0].trans_time = this.formatTransTime(this.orders[0].trans_time);

          //如果是课程，则刷新课程信息
          if (this.orders[0].goods_type === 'course_theme_type') {
            this.queryCourseDetail();
          }
          //如果是vip，增加展示到期日期
          if (this.orders[0].goods_type === 'vip') {
            this.getUserDetail();
          }
        }
      },
      queryCourseDetail:async function () {
        const result = await ShowCourseDetail({course_id:this.orders[0].goods_id});
        if (result.status === 'SUCCESS') {
          this.course = result.course;
        }
      },
      getUserDetail:async function () {
        const result = await GetUserDetail(this.loginUserName());
        if (result.status === 'SUCCESS') {
          this.user = result.user;
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
    },
    computed:{
		  loginUserName:function () {
        return getLoginUserName;
      }
    },
    mounted:function () {
      this.refreshOrder();
    },
	}
</script>

<style scoped>

</style>
