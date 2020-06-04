<template>
	<div>
    <div style="background-color: white;min-height: 550px">
      <!--一行订单信息-->
      <Table border :columns="orderColumns" :data="orders"></Table>

      <div v-if="coupon" style="margin:10px 0 0 50px; display: flex ">
        <div>
          <Coupon :activity_id="coupon.activity_id"
                  :coupon_type="coupon.coupon_type"
                  :youhui_type="coupon.youhui_type"
                  :start_date="coupon.start_date"
                  :end_date="coupon.end_date"
                  :coupon_amount="coupon.coupon_amount"
                  :goods_min_amount="coupon.goods_min_amount"
                  :discount_rate="coupon.discount_rate">
          </Coupon>
        </div>
        <div>
          <!--已使用-->
          <img style="border-radius:50%;position: relative;top: 10px;left: 50px" width=80 height=80 src="../../../static/images/order/used.jpg">
        </div>
      </div>

      <!--如果是课程信息-->
      <div v-if="orders[0].goods_type==='course_theme_type'" style="border: 1px solid #f4f4f4;padding: 15px;margin-left: 25px;min-height: 500px;">
        <Row style="border-bottom: 1px solid #f4f4f4;padding: 10px;">
          <Col span="4">
            <router-link :to="{path:'/ilearning/courseDetail',query:{course_id:course.id}}">
              <h4>课程名称：{{course.course_name}}</h4>
              <img v-if="course.small_image" :src="course.small_image" height="120" width="180"/>
              <img v-else src="../../../static/images/common_img/default.png" height="120" width="180"/>
            </router-link>
          </Col>
          <Col span="10">
            <CourseMeta :course="course" :showCourseSpace="false"/>
          </Col>
        </Row>
      </div>
      <!--如果是vip会员-->
      <div v-else-if="orders[0].goods_type==='vip'" style="display: flex">
        <div style="width: 16%;background-color: white;margin: 30px 0 0 40px">
          <img src="../../../static/images/vipCenter/vip_card.jpg" width="180" height="120"/>
        </div>
        <div style="width: 35%;background-color: white;margin: 30px 0 0 0 ">
          <Row style="margin-top: 10px">vip会员</Row>
          <Row style="margin-top: 10px">{{orders[0].goods_desc}}</Row>
          <Row style="margin-top: 10px">会员到期时间: {{user.vip_expired_time}} </Row>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {GetUserDetail, queryPayOrderList, ShowCourseDetail,QueryPagePayCoupon} from "../../api/index"
  import HotUser from "../User/HotUser";
  import CourseMeta from "../ILearning/Course/CourseMeta";
  import {getLoginUserName} from "../../tools/sso";
  import Coupon from "../Common/coupon/Coupon";

	export default {
		name: "OrderDetail",
    components:{Coupon, CourseMeta, HotUser},
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
            title: '支付金额',
            key: 'paid_amount',
            width:130
          },
          {
            title: '原价格',
            key: 'goods_original_price',
            width:130
          },
          {
            title: '参与活动',
            key: 'activity_type',
            width:130
          },
          {
            title: '支付状态',
            key: 'pay_result',
          },
        ],
        orders:[],
        coupon:'',
        course:'',
        user:'',
        //本页面最多只会查到一条数据，订单详情信息
        page:{totalCount:0,currentPage:1,offset:10},
      }
    },
    methods:{
		  refreshOrder:async function () {
		    let params = {
		      'order_id':this.$route.query.order_id,
          'currentPage':1,
          'offset':10,
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
          //如果使用了优惠券
          if (this.orders[0].activity_type === 'coupon') {
            this.orders[0].activity_type = '优惠券';
            //并且展示一下优惠券
            this.queryCoupon(this.orders[0].activity_type_bind_id);
          }else if (this.orders[0].activity_type === '') {
            this.orders[0].activity_type = '无';
          }

        }
      },
      queryCoupon:async function(coupon_id){
        let params = {
          'coupon_id':coupon_id,//券号本身就是就是具有唯一性
          'currentPage':1,
          'offset':10,
        };
        const result = await QueryPagePayCoupon(params);
        if (result.status === 'SUCCESS') {
          this.coupon = result.couponDatas[0];
        }
      },
      queryCourseDetail:async function () {
        const result = await ShowCourseDetail({course_id:this.orders[0].goods_id});
        if (result.status === 'SUCCESS') {
          this.course = result.course;
        }
      },
      getUserDetail:async function () {
		    let params = {
		      'userName':this.loginUserName()
        };
        const result = await GetUserDetail(params);
        if (result.status === 'SUCCESS') {
          this.user = result.user;
          //格式化会员过期时间
          this.user.vip_expired_time = this.formatVipExpiredTime('YYYY-mm-dd HH:MM:SS',this.user.vip_expired_time);
        }
      },
      formatTransTime:function (trans_time) {
        let date = trans_time.slice(0,8);
        let time = trans_time.slice(8,14);
        let formatDate = date.slice(0,4)+"-"+date.slice(4,6)+"-"+date.slice(6,8);
        let formatTime = time.slice(0,2)+":"+time.slice(2,4)+":"+time.slice(4,6);
        return formatDate + " " +formatTime;
      },
      formatVipExpiredTime:function(fmt, date) {
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
