<template>
  <div class="isoft_bg_white isoft_pd10">
    <div class="vipCenterUp">
      <div style="font-size: 30px;padding: 70px 20px 30px 38%">
        <span style="color: #ff6600">会员尊享，学而不止</span>
      </div>
      <div style="font-size: 30px;padding: 10px 54% 30px 46%">
        <Button shape="circle" size="large" @click="toRecharge()" style="color: #ff6600;font-size: 20px;">
          <div id="openVip" class="hvr-grow">开通会员</div>
        </Button>
      </div>
    </div>
    <div>
      <Table stripe :columns="columns1" :data="data1"></Table>
    </div>
  </div>
</template>
<script>
  import {CheckHasLoginConfirmDialog} from "../../tools"

  export default {
    name: "VipIntroduction",
    data() {
      return {
        split1: 0.5,
        columns1: [
          {
            title: '普通用户',
            key: 'notVip'
          },
          {
            title: '会员',
            key: 'isVip'
          },
          {
            title: '尊享特权',
            key: 'privilege'
          }
        ],
        data1: [
          {
            notVip: '学习&发布教学视频',
            isVip: '学习时间不限',
            privilege: '随时随地都可以登录学习',
          },
          {
            notVip: '视频数量20，长度30分钟',
            isVip: '视频数量200，长度120分钟',
            privilege: '上传视频自己开课',
          },
          {
            notVip: '视频数量20，长度30分钟',
            isVip: '视频数量200，长度120分钟',
            privilege: '上传视频自己开课',
          },

        ]
      }
    },
    methods: {
      toRecharge: function () {
        CheckHasLoginConfirmDialog(this, {path: "/vipcenter/recharge/"});
      }
    },
  }
</script>
<style>
  #openVip{
    text-align: center;
    font-size: 20px;
    color: #ff6900;
  }
  #openVip:hover{
    text-align: center;
    font-size: 21px;
    color: red;
  }
  .vipCenterUp {
    width: 100%;
    height: 246px;
    background-image: url("../../../static/images/vipCenter/vipCenterImg.png");
    background-position-x: center;
    background-position-y: center;
    margin-bottom: 10px;
  }

  .vipCenterDown {
    width: 100%;
    height: 500px;
    background-position-x: center;
    background-position-y: center;
  }

  .demo-split {
    height: 500px;
    border: 1px solid #dcdee2;
  }

  .demo-split-pane {
    padding: 10px;
  }
</style>
