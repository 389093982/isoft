<template>
  <div>

    <div v-if="showPage" class="demo-split" style="padding-top: 30px;background-color: rgba(0,0,0,0.09)">
      <Row>
        <Col span="8">&nbsp;</Col>
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
                  <a style="color: #ff6600">支付金额: <Icon type="logo-yen" /></a><a style="font-size: 20px ;color: #ff6900">{{goods_price}}</a>
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
                    <img v-else src="../../assets/default.png" width="180" height="120" @error="defImg()" @click="$router.push({path:'/ilearning/courseDetail',query:{course_id:goods_id}})"/>
                    <div class="ico_play"></div>
                  </div>
                </div>
              </Col>
            </Row>
          </div>
          <div style="margin-left: 150px">
            <div v-if="codeUrl">
              扫码支付金额：<Icon type="logo-yen" />{{goods_price}}
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
        <Col span="8">&nbsp;</Col>
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
  import {ShowCourseDetail,queryPayOrderList,addPayOrder} from "../../api/index"
  import {checkHasLogin,getLoginUserName} from "../../tools/sso"
  import {CheckHasLoginConfirmDialog2} from "../../tools/index"
  import vueQr from 'vue-qr'

  export default {
    name: "Pay",
    components: { vueQr},
    data() {
      return {
        //是否展示页面
        showPage:false,
        //商品信息
        goods_type:'',
        goods_id:'',
        goods_desc:'',
        goods_price:'',
        goods_img:'',
        //支付信息
        payType: '微信支付',
        lastTimeCodeUrl: '',
        codeUrl: '',
        imageUrl: require("../../../static/images/vip.png"),
        websocket:null,
        percent:0,
        showPayResult:false,
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
            this.goods_price = result.course.price;
            this.goods_img = result.course.small_image;
            this.showPage = true;
          }
        }
      },
      checkHasLogin:function(){
        return checkHasLogin();
      },
      getPayUrl: async function () {
        if (this.lastTimeCodeUrl !== '') {
          return false;
        }
        //检查该商品是否已经下过单
        if (this.goods_type === 'course') {
          //发送请求到订单表做个查询
          const result = await queryPayOrderList({
            'user_name':this.loginUserName,
            'goods_type':'course_theme_type',
            'goods_id':this.goods_id,
          });
          if (result.status === 'SUCCESS') {
            if (result.orders.length===1 && result.orders[0].pay_result==='SUCCESS') {
              this.$Message.warning("该课程您已购买过，无需再次购买^_^");
            }else {
              let _this = this;
              CheckHasLoginConfirmDialog2(this, async function () {
                //清理上次付款结果
                _this.showPayResult = false;
                _this.payResultDesc = '';
                _this.percent = 0;
                //准备参数
                let ProductId = _this.goods_id.toString();
                let ProductDesc = _this.goods_desc;
                let TransAmount = _this.goods_price * 100;
                let TransCurrCode = 'CNY';
                let OrderParams = {
                  'user_name':_this.loginUserName,
                  'product_id': ProductId,
                  'poduct_desc': ProductDesc,
                  'trans_amount': TransAmount,
                  'trans_curr_code': TransCurrCode
                };
                _this.initWebSocket(OrderParams);
              });
            }
          }
        }
      },
      initWebSocket:function(OrderParams) {
        const wsuri = this.GLOBAL.isoft_unifiedpay_order;
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
            this.lastTimeCodeUrl = result.code_url;
          }
          if (result.pay_result != null) {
            //支付成功，订单入pay_order表
            const res = await addPayOrder({
              'order_id':result.order_id,
              'trans_time':result.trans_time,
              'user_name':result.user_name,
              'goods_type':'course_theme_type',
              'goods_id':result.product_id,
              'goods_desc':result.product_desc,
              'goods_price':result.trans_amount,
              'goods_img':this.goods_img,
              'pay_result':result.pay_result,
            });
            //页面支付成功动态效果
            if (res.status === 'SUCCESS') {
              this.codeUrl = '';
              this.showPayResult = true;
              var handler = setInterval(this.add, 50);
              //一秒后让handler失效
              setTimeout(function () {
                clearInterval(handler);
              }, 1000);
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
  .payCenter {
    color: #ff6900;
    font-size: 25px
  }
  .demo-split {
    height: 550px;
    border: 1px solid #dcdee2;
  }
  .ico_play {
    background: url(../../assets/ico_play.png) no-repeat;
    background-size: 20px;
    position: absolute;
    top: 145px;
    left: 150px;
    width: 20px;
    height: 20px;
  }

</style>
