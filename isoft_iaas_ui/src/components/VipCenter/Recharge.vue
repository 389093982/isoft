<template>
  <div class="isoft_bg_white isoft_pd10">
    <div>
      <a @click="prePage()" style="color: #ff6600"><Icon type="ios-undo-outline"  style="font-size: 40px"/>会员中心</a>
    </div>

    <div class="demo-split">
      <div class="demo-split-pane">
        <div >
          <img class="vipLogo" src="../../../static/images/vipCenter/vipLogo.png" height="41" width="41"/>
          <span class="payCenter">支付中心</span>
        </div>
        <div class="vipDesc">
          <Row>
            <a style="color: #ff6600">当前账号:  </a>{{loginUserName}}
          </Row><br>
          <Row>
            <a style="color: #ff6600">开通时长:  </a>{{openingTime}}&nbsp;&nbsp;&nbsp;
            <RadioGroup v-model="openingTime" type="button">
              <Radio label=" 1个月 / ¥60 " openingTime></Radio>
              <Radio label=" 3个月 / ¥150 " openingTime></Radio>
              <Radio label=" 6个月 / ¥240 " openingTime></Radio>
              <Radio label=" 1年 / ¥360 " openingTime></Radio>
            </RadioGroup>
          </Row><br>
          <Row>
            <a style="color: #ff6600">支付方式:  </a>
            <RadioGroup v-model="payType">
              <Radio label="微信支付" border></Radio>
            </RadioGroup>
          </Row><br>
          <Row>
            <a style="color: #ff6600">支付金额:  </a><a style="font-size: 20px ;color: #ff6900">{{openingTime.trim().split('¥')[1]}}</a>元
          </Row><br>
        </div>
        <div class="qrcode">
          <vue-qr :logoSrc="imageUrl" :text="getPayUrl" :size="180"></vue-qr>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {GetLoginUserName} from "../../tools"
  import {pay} from '../../api'
  import vueQr from 'vue-qr'
	export default {
		name: "Recharge",
    components: {vueQr},
    data(){
      return {
        openingTime: ' 1个月 / ¥60 ',
        payType:'微信支付',
        payUrl:'test',
        imageUrl: require("../../../static/images/vip.png"),
      }
    },
    created:{
		  initPayUrl:async function () {
		    let payMoney = this.openingTime.trim().split('¥')[1];
        let ProductId = '待定';
        let ProductDesc = '学习网站会员';
        let TransAmount = payMoney*100;
        let TransCurrCode = 'CNY';
        let code_url = await pay(ProductId,ProductDesc,TransAmount,TransCurrCode);
        this.payUrl = code_url;
      }
    },
    computed:{
		  loginUserName:function () {
        return GetLoginUserName();
      },
      getPayUrl:async function () {
        let payMoney = this.openingTime.trim().split('¥')[1];
        let ProductId = '待定';
        let ProductDesc = '学习网站会员';
        let TransAmount = payMoney*100;
        let TransCurrCode = 'CNY';
        let code_url = await pay(ProductId,ProductDesc,TransAmount,TransCurrCode);
        return code_url;
      }
    },
    methods:{
      prePage:function () {
        window.location.href = "#/vipcenter/vipIntroduction/";
      }
    },
	}
</script>

<style scoped>
  .vipLogo{
    background-position-x: center;
    background-position-y: center;
  }
  .vipDesc{
    float: right;width: 70%;
  }
  .payCenter{
    float: right;width: 55%; color: #ff6900;font-size: 25px
  }
  .qrcode{
    float: right;
    width: 58%;
  }
</style>
