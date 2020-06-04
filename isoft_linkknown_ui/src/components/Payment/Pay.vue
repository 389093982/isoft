<template>
  <div>

    <div v-if="showPage" class="demo-split" style="padding-top: 30px;background-color: rgba(225,209,232,0.2)">
      <Row>
        <!--第一列：被选中的优惠券-->
        <Col span="8">&nbsp;
          <div v-if="currentSelectCoupon" style="position: relative;top: 50px;left: 30px">
            <div style="text-align: center">
              <span style="position: relative;left: -40px;">正在使用的券</span>
            </div>
            <Coupon :activity_id="currentSelectCoupon.activity_id"
                    :coupon_type="currentSelectCoupon.coupon_type"
                    :youhui_type="currentSelectCoupon.youhui_type"
                    :start_date="currentSelectCoupon.start_date"
                    :end_date="currentSelectCoupon.end_date"
                    :coupon_amount="currentSelectCoupon.coupon_amount"
                    :goods_min_amount="currentSelectCoupon.goods_min_amount"
                    :discount_rate="currentSelectCoupon.discount_rate">
            </Coupon>
            <div style="text-align: center">
              <span style="position: relative;left: -40px;top: 5px;">
                <Icon type="md-close-circle" size="25" style="cursor: pointer" @click="cancleUseCoupon()"/>
              </span>
            </div>
          </div>
        </Col>
        <!--第二列：支付中心-->
        <Col span="8">
          <div style="margin-left: 150px">
            <Icon type="md-ribbon" style="font-size: 40px;color: #ff6900"/>
            <span class="payCenter">支付中心</span>
          </div>
          <div>
            <Row>
              <Col span="12">
                <Row>
                  <a style="color: #ff6600">当前账号: </a>{{loginUserName}}
                </Row>
                <br>
                <Row>
                  <a style="color: #ff6600">商品描述: </a><a style="font-size: 20px;color: #ff6600;">{{goods_desc}}</a>
                </Row>
                <br>
                <Row>
                  <a style="color: #ff6600">支付金额: <Icon type="logo-yen" /></a><a style="font-size: 20px ;color: #ff6900">{{paid_amount}}</a>
                </Row>
                <br>
                <Row>
                  <a style="color: #ff6600">支付方式: </a>
                  <RadioGroup v-model="payType">
                    <Radio label="微信支付" border></Radio>
                  </RadioGroup>
                </Row>
                <br>
                <Row>
                  <Button type="primary" icon="md-cart" @click="getPayUrl">立&nbsp;&nbsp;即&nbsp;&nbsp;下&nbsp;&nbsp;单</Button>
                </Row>
              </Col>
              <Col span="12">
                <div style="margin-top: 50px">
                  <div style="width: 180px;height: 120px;cursor: pointer;">
                    <img v-if="goods_img" :src="goods_img" width="180" height="120" @error="defImg()" @click="$router.push({path:'/ilearning/courseDetail',query:{course_id:goods_id}})"/>
                    <img v-else src="../../../static/images/common_img/default.png" width="180" height="120" @error="defImg()" @click="$router.push({path:'/ilearning/courseDetail',query:{course_id:goods_id}})"/>
                    <div class="ico_play"></div>
                  </div>
                </div>
              </Col>
            </Row>
          </div>
          <div style="margin-left: 150px">
            <div v-if="codeUrl">
              扫码支付金额：<Icon type="logo-yen" />{{paid_amount}}
              <br>
              <vue-qr :logoSrc="imageUrl" :text="codeUrl" :size="180"></vue-qr>
            </div>
            <div v-if="showPayResult" style="margin-left: 20px;margin-top: 20px">
              <i-circle :percent="percent" :stroke-color="color" :size="100">
                <Icon v-if="percent === 100" type="ios-checkmark" size="60" style="color:#5cb85c"></Icon>
                <span v-else style="font-size:24px">等待中...</span>
              </i-circle>
              <div style="margin-left: 20px">{{payResultDesc}}</div>
            </div>
          </div>
        </Col>
        <!--第三列：展示本次可用的优惠券-->
        <Col span="8">
          <div style="display: flex">
            <!--彩色线条-->
            <div v-if="showAvailableCouponList" style="width: 2px;height: 500px;position: relative;left: -15px">
              <div style="height: 20%;background-color: rgba(255,105,0,0.56)"></div>
              <div style="height: 30%;background-color: rgba(239,105,255,0.54)"></div>
              <div style="height: 20%;background-color: rgba(51,255,20,0.48)"></div>
              <div style="height: 30%;background-color: rgba(255,37,37,0.53)"></div>
            </div>

            <div>
              <div v-if="coupons.length>0" style="margin-top: 10px;position: relative">
                <div v-if="!showAvailableCouponList">
                  <!--消息提示，组合成的-->
                  <div class="tipMessage_01 animated faster bounceInRight">
                  <span style="margin-left: 6px;">
                    <span style="color: #ff6900">点我</span>查看您本次可用的优惠券! ^_^
                  </span>
                  </div>
                  <div class="sjx animated faster bounceInRight"></div>
                </div>
                <div v-else>
                  <div class="tipMessage_02 animated faster bounceInRight">
                  <span style="margin-left: 6px;">
                    哇,您有<span style="color: #ff6900">{{coupons.length}}</span>张可用的券哦,点击即可使用！
                  </span>
                  </div>
                  <div class="sjx animated faster bounceInRight"></div>
                </div>

                <!--机器人-->
                <div>
                  <img @click="showAvailableCouponList=!showAvailableCouponList" src="../../../static/images/common_img/xiaodou.png" style="width: 35px;height: 40px;cursor: pointer;" class="isoft_hover_top3"/>
                </div>

                <div v-if="showAvailableCouponList">
                  <Scroll height="400">
                    <Row v-for="(coupon,index) in coupons" @click.native="selectThisCoupon(index)" style="margin-top: 8px">
                      <Coupon :activity_id="coupon.activity_id" class="isoft_hover_top3"
                              :coupon_type="coupon.coupon_type"
                              :youhui_type="coupon.youhui_type"
                              :start_date="coupon.start_date"
                              :end_date="coupon.end_date"
                              :coupon_amount="coupon.coupon_amount"
                              :goods_min_amount="coupon.goods_min_amount"
                              :discount_rate="coupon.discount_rate">
                      </Coupon>
                    </Row>
                  </Scroll>
                </div>
              </div>
            </div>
          </div>
        </Col>
      </Row>

    </div>
    <div v-else>
      <div style="text-align: center;margin-top: 200px;color: #ff6600;font-size: 20px">
        找不到付费商品信息...
      </div>
    </div>

  </div>
</template>

<script>
  import {isoft_unifiedpay_order_api} from "../../api/index"
  import {ShowCourseDetail,queryPayOrderList,addPayOrder,SearchCouponForPay,UpdateCouponIsUsed} from "../../api/index"
  import {checkHasLogin,getLoginUserName} from "../../tools/sso"
  import {CheckHasLoginConfirmDialog2,GetToday_yyyyMMdd} from "../../tools/index"
  import vueQr from 'vue-qr'
  import Coupon from "../Common/coupon/Coupon";

  export default {
    name: "Pay",
    components: {Coupon, vueQr},
    data() {
      return {
        //是否展示页面
        showPage:false,
        //商品信息
        goods_type:'',
        goods_id:'',
        goods_desc:'',
        paid_amount:'',
        paid_amount_backup:'',
        goods_img:'',
        //可用优惠券
        coupons:'',
        currentSelectCoupon:'',
        showAvailableCouponList:false,
        //支付信息
        payType: '微信支付',
        codeUrl: '',
        imageUrl: require("../../../static/images/vipCenter/vip.png"),
        websocket:null,
        percent:0,
        showPayResult:false,
        paySuccess:false, //true :支付成功， false:支付失败
        payResultDesc:'',
      }
    },
    computed: {
      loginUserName: function () {
        return getLoginUserName();
      },
      color () {
        let color = 'grey';
        if (this.percent === 100) {
          color = '#5cb85c';
        }
        return color;
      }
    },
    methods: {
      refreshCourseDetail: async function () {
        let course_id = this.goods_id;
        let user_name;
        if (this.checkHasLogin) {
          user_name = getLoginUserName();
        }else {
          user_name = null;
        }
        let params = {
          'course_id':course_id,
          'user_name':user_name,
        };
        const result = await ShowCourseDetail(params);
        if (result.status === "SUCCESS") {
          if (result.course.isCharge === 'charge') {
            this.goods_desc = result.course.course_name;
            this.paid_amount = result.course.price;
            this.paid_amount_backup = result.course.price; //原价格做个备份
            this.goods_img = result.course.small_image;
            this.showPage = true;
            this.searchCourseAvailableCoupon();
          }
        }
      },
      //查询可用优惠券
      searchCourseAvailableCoupon:async function(){
        if (checkHasLogin()) {
          let params = {
            'userName':this.loginUserName,
            'target_type':'course',
            'target_id':this.goods_id,
            'paid_amount':this.paid_amount,
            'today':GetToday_yyyyMMdd(),
          };
          const result = await SearchCouponForPay(params);
          if (result.status === 'SUCCESS') {
            this.coupons = result.coupons;
          }
        }
      },
      selectThisCoupon:function(index){
        if (this.codeUrl === '') {
          if (this.paySuccess) {
            this.$Message.info('已付款');
          }else{
            this.currentSelectCoupon = this.coupons[index];
            this.$Message.info('已选择');
            // 计算金额前先将金额置为初始值
            this.paid_amount = this.paid_amount_backup;
            //金额计算...
            if (this.currentSelectCoupon.youhui_type === 'reduce') {
              this.paid_amount = (this.paid_amount - this.currentSelectCoupon.coupon_amount).toFixed(2);
            }else if (this.currentSelectCoupon.youhui_type === 'discount') {
              this.paid_amount = (this.paid_amount * this.currentSelectCoupon.discount_rate).toFixed(2);
            }
          }
        }else{
          this.$Message.info('已下单，请尽快支付');
        }

      },
      cancleUseCoupon:function(){
        if (this.codeUrl === '') {
          if (this.paySuccess) {
            this.$Message.info('已付款');
          }else{
            this.currentSelectCoupon = '';
            this.$Message.info('已取消');
            // 取消就将金额置为初始值
            this.paid_amount = this.paid_amount_backup;
          }
        }else{
          this.$Message.info('已下单，请尽快支付');
        }

      },
      updateCouponIsUsed:async function(userName,coupon_id){
        let params = {
          'userName':userName,
          'coupon_id':coupon_id
        };
        const result = await UpdateCouponIsUsed(params);
        if (result.status === 'SUCCESS') {
          //刷新优惠券
          this.currentSelectCoupon = '';
          this.searchCourseAvailableCoupon();
        }
      },
      checkHasLogin:function(){
        return checkHasLogin();
      },
      getPayUrl: async function () {
        let _this = this;
        if (_this.codeUrl!=='') {
          this.$Message.info('已下单，请及时付款');
          return false;
        }
        if (_this.paySuccess) {
          this.$Message.info('已付款');
          return false;
        }
        CheckHasLoginConfirmDialog2(this, async function () {
          //检查该商品是否已经下过单
          if (_this.goods_type === 'course') {
            //发送请求到订单表做个查询
            const result = await queryPayOrderList({
              'user_name':_this.loginUserName,
              'goods_type':'course_theme_type',
              'goods_id':_this.goods_id,
              'currentPage':1,
              'offset':10,
            });
            if (result.status === 'SUCCESS') {
              if (result.orders.length===1 && result.orders[0].pay_result==='SUCCESS') {
                _this.$Message.warning("该课程您已购买过，无需再次购买^_^");
              }else {
                //清理上次付款结果
                _this.showPayResult = false;
                _this.payResultDesc = '';
                _this.percent = 0;
                //准备参数
                let ProductId = _this.goods_id.toString();
                let ProductDesc = _this.goods_desc;
                //对接微信支付，要求是分为单位，这个地方用的是int才行
                let TransAmount = parseInt((_this.paid_amount * 100).toFixed(0));
                let TransCurrCode = 'CNY';
                let OrderParams = {
                  'user_name':_this.loginUserName,
                  'product_id': ProductId,
                  'poduct_desc': ProductDesc,
                  'trans_amount': TransAmount,
                  'trans_curr_code': TransCurrCode
                };
                _this.initWebSocket(OrderParams);
              }
            }
          }
        });
      },
      initWebSocket:function(OrderParams) {
        const wsuri = isoft_unifiedpay_order_api; //这里就是下单接口
        this.websocket = new WebSocket(wsuri);
        this.websocket.onopen = this.websocketonopen;
        this.websocket.onmessage = this.websocketonmessage;
        this.websocket.onerror = this.websocketonerror;
        this.websocket.onclose = this.websocketclose;
        var _this = this;
        setTimeout(function () {
          console.log("WebSocket 发送数据: " + JSON.stringify(OrderParams));
          _this.websocket.send(JSON.stringify(OrderParams));
        }, 1000);
      },
      websocketonopen:function() {
        console.log("WebSocket 连接成功");
      },
      websocketonerror:function(e) {
        console.log("WebSocket 连接发生错误");
      },
      websocketonmessage:async function(e){
        console.log("WebSocket 数据接收: " + JSON.stringify(e.data));
        let result = JSON.parse(e.data);
        if (result.user_name === this.loginUserName) {
          if (result.code_url != null) {
            this.codeUrl = result.code_url;
          }
          if (result.pay_result != null) {
            //不论成功和失败，先入订单 pay_order 表
            const res = await addPayOrder({
              'order_id':result.order_id,
              'trans_time':result.trans_time,
              'user_name':result.user_name,
              'goods_type':'course_theme_type',
              'goods_id':result.product_id,
              'goods_desc':result.product_desc,
              'paid_amount':(result.trans_amount/100).toFixed(2), //接收再将分转为元，入库
              'goods_original_price':this.paid_amount_backup,
              'activity_type':this.currentSelectCoupon===''?'':'coupon',
              'activity_type_bind_id':this.currentSelectCoupon===''?'':this.currentSelectCoupon.coupon_id,
              'goods_img':this.goods_img,
              'pay_result':result.pay_result,
            });
            //如果支付成功，页面展示支付成功动态效果
            if (res.status === 'SUCCESS' && result.pay_result === 'SUCCESS') {
              this.codeUrl = '';
              this.showPayResult = true;
              this.paySuccess = true;
              var handler = setInterval(this.add, 50);
              //一秒后让handler失效
              setTimeout(function () {
                clearInterval(handler);
              }, 1000);

              // 更新优惠券
              if (this.currentSelectCoupon) {
                this.updateCouponIsUsed(result.user_name,this.currentSelectCoupon.coupon_id);
              }

            }
          }
        }
      },
      websocketclose(e){
        console.log("WebSocket 连接关闭 (" + e.code + ")");
      },

      //支付成功，来个进度环显示下动态效果
      add:function() {
        if (this.percent >= 100) {
          this.payResultDesc="支付成功！";
          return false;
        }
        this.percent += 10;
      },
  },
  mounted:function () {
    this.goods_type = this.$route.query.type;
    this.goods_id = this.$route.query.id;
    if (this.goods_type === 'course') {
      this.refreshCourseDetail();
    }
  }
  }
</script>

<style scoped>
  .tipMessage_01{
    position:absolute;left: 50px;
    width: 200px;height: 25px;
    background-color:white;
    font-size: 12px;
    border-radius: 5px;
  }

  .tipMessage_02{
    position:absolute;left: 50px;
    width: 220px;height: 25px;
    background-color:white;
    font-size: 12px;
    border-radius: 5px;
  }

  .sjx {
    position:absolute;left: 41px;top: 5px;
    width: 0;
    height: 0;
    border-width: 5px;
    border-style: solid;
    border-color:transparent white transparent transparent;
  }

  .payCenter {
    color: #ff6900;
    font-size: 25px
  }
  .demo-split {
    height: 550px;
    border: 1px solid #dcdee2;
  }
  .ico_play {
    background: url(../../../static/images/common_img/ico_play.png) no-repeat;
    background-size: 20px;
    position: absolute;
    top: 145px;
    left: 150px;
    width: 20px;
    height: 20px;
  }

</style>
