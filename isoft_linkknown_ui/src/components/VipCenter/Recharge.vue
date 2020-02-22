<template>
  <div>

    <div class="demo-split" style="padding-top: 30px;background-color: rgba(0,0,0,0.09)">
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
              <Radio label=" 1个月 / ¥60 " openingTime></Radio>
              <Radio label=" 3个月 / ¥150 " openingTime></Radio>
              <Radio label=" 6个月 / ¥240 " openingTime></Radio>
              <Radio label=" 1年 / ¥360 " openingTime></Radio>
            </RadioGroup>
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
            <a style="color: #ff6600">支付金额: <Icon type="logo-yen" /></a><a style="font-size: 20px ;color: #ff6900">{{openingTime.trim().split('¥')[1]}}</a>
          </Row>
          <br>
          <Row>
            <Button type="primary" icon="md-cart" @click="getPayUrl">立&nbsp;&nbsp;即&nbsp;&nbsp;下&nbsp;&nbsp;单</Button>
          </Row>
        </div>
        <div v-if="payUrl" class="qrcode">
          扫码支付金额：<Icon type="logo-yen" />{{realPay}}
          <br>
          <vue-qr :logoSrc="imageUrl" :text="payUrl" :size="180"></vue-qr>
        </div>
      </div>

  </div>
</template>

<script>
  import {GetLoginUserName} from "../../tools"
  import isoft_unifiedpay_order from "../GlobalData/index"
  import vueQr from 'vue-qr'

  export default {
    name: "Recharge",
    components: {vueQr},
    data() {
      return {
        openingTime: ' 1个月 / ¥60 ',
        tempChoice: '',
        lastTimeChoice:'',
        payType: '微信支付',
        realPay:'',
        payUrl: '',
        imageUrl: require("../../../static/images/vip.png"),
        websocket:null,
      }
    },
    computed: {
      loginUserName: function () {
        return GetLoginUserName();
      },
    },
    methods: {
      getPayUrl: async function () {
        this.lastTimeChoice = this.tempChoice;
        this.tempChoice = this.openingTime;
        if (this.openingTime===this.lastTimeChoice) {
          this.$Message.warning("请勿重复下单！");
          return false;
        }
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
      websocketonmessage(e){
        console.log("WebSocket 数据接收: " + JSON.stringify(e.data));
        let result = JSON.parse(e.data);
        if (result.user_name === this.loginUserName) {
          this.payUrl = result.code_url;
        }
      },
      websocketclose(e){
        console.log("WebSocket 连接关闭 (" + e.code + ")");
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
