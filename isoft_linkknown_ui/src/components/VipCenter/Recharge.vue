<template>
  <div>

    <div class="demo-split" style="padding-top: 30px;background-color: rgba(225,209,232,0.2)">
        <div style="text-align: center">
          <Icon type="md-ribbon" style="font-size: 40px;color: #ff6900"/>
          <span class="payCenter">支付中心</span>
        </div>
        <div class="vipDesc">
          <Row>
            <a style="color: #ff6600">当前账号: </a>{{loginUserName}}
          </Row>
          <br>
          <Row>
            <a style="color: #ff6600">开通时长: </a>{{openingTime}}&nbsp;&nbsp;&nbsp;
            <RadioGroup v-model="openingTime" type="button">
              <Radio :label="openingTime_choice_one" openingTime></Radio>
              <Radio :label="openingTime_choice_two" openingTime></Radio>
              <Radio :label="openingTime_choice_three" openingTime></Radio>
              <Radio :label="openingTime_choice_four" openingTime></Radio>
            </RadioGroup>
          </Row>
          <br>
          <Row>
            <a style="color: #ff6600">支付金额: <Icon type="logo-yen" /></a><a style="font-size: 20px ;color: #ff6900">{{openingTime.trim().split('¥')[1]}}</a>
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
        </div>
        <div v-if="codeUrl" class="qrcode">
          扫码支付金额：<Icon type="logo-yen" />{{realPay}}
          <br>
          <vue-qr :logoSrc="imageUrl" :text="codeUrl" :size="180"></vue-qr>
        </div>
        <div v-if="showPayResult" style="float: right;width: 55%">
          <i-circle :percent="percent" :stroke-color="color" :size="100">
            <Icon v-if="percent === 100" type="ios-checkmark" size="60" style="color:#5cb85c"></Icon>
            <span v-else style="font-size:24px">等待中...</span>
          </i-circle>
          <div style="margin-left: 20px">{{payResultDesc}}</div>
        </div>
      </div>

  </div>
</template>

<script>
  import {GetLoginUserName} from "../../tools"
  import {isoft_unifiedpay_order_api,addPayOrder} from "../../api/index"
  import vueQr from 'vue-qr'

  export default {
    name: "Recharge",
    components: {vueQr},
    data() {
      return {
        openingTime_choice_one:' 1个月 / ¥12 ',
        openingTime_choice_two:' 3个月 / ¥30 ',
        openingTime_choice_three:' 6个月 / ¥50 ',
        openingTime_choice_four:' 1年 / ¥80 ',
        openingTime: ' 1个月 / ¥12 ',
        tempChoice: '',
        lastTimeChoice:'',
        payType: '微信支付',
        realPay:'',
        codeUrl: '',
        imageUrl: require("../../../static/images/vipCenter/vip.png"),
        websocket:null,
        percent:0,
        showPayResult:false,
        payResultDesc:'',
      }
    },
    computed: {
      loginUserName: function () {
        return GetLoginUserName();
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
      getPayUrl: async function () {
        this.lastTimeChoice = this.tempChoice;
        this.tempChoice = this.openingTime;
        if (this.openingTime===this.lastTimeChoice) {
          // this.$Message.warning("请勿重复下单！");
          return false;
        }
        //清理上次付款结果
        this.showPayResult = false;
        this.payResultDesc = '';
        this.percent = 0;
        //准备参数
        let payMoney = this.openingTime.trim().split('¥')[1];
        this.realPay = payMoney;
        let ProductId = '待定';
        let ProductDesc = 'linkknown网站会员';
        let TransAmount = payMoney * 100;
        let TransCurrCode = 'CNY';
        let OrderParams = {
          'user_name':this.loginUserName,
          'product_id': ProductId,
          'poduct_desc': ProductDesc,
          'trans_amount': TransAmount,
          'trans_curr_code': TransCurrCode
        };
        this.initWebSocket(OrderParams);
      },
      initWebSocket:function(OrderParams) {
        const wsuri = isoft_unifiedpay_order_api;
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
            //支付成功，订单入pay_order表, 充值vip，会在流程里修改user表会员标识
            let params = {
              'order_id':result.order_id,
              'trans_time':result.trans_time,
              'user_name':result.user_name,
              'goods_type':'vip',
              'goods_id':result.product_id,
              'goods_desc':result.product_desc,
              'goods_price':result.trans_amount,
              'goods_img':'',
              'pay_result':result.pay_result,
            };
            const res = await addPayOrder(params);
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
  }
</script>

<style scoped>
  .vipDesc {
    float: right;
    width: 70%;
  }
  .payCenter {
    color: #ff6900;
    font-size: 25px
  }
  .demo-split {
    height: 550px;
    border: 1px solid #dcdee2;
  }
  .qrcode {
    float: right;
    width: 58%;
  }
  #order {
    width: 30%;
    height: 40px;
    margin: 8px 0 0 0 ;
    margin-bottom: 20px;
    display: block;
    line-height: 40px;
    font-size: 16px;
    font-weight: 800;
    cursor: pointer;
    color: #fff;
    background: #ec7f17;
    border: 0;
  }
</style>
