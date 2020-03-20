<template>
  <div>

    <div class="isoft_bg_white isoft_pd10">
      <IBeautifulCard title="等你来答">
        <div slot="content" style="padding: 10px;">
          <Row v-for="(waitYourAnswer,index) in waitYourAnswerList" :gutter="10">
            <Row>
              <Col span="14">{{waitYourAnswer.question}}</Col>
              <Col span="10"><Icon type="md-add-circle" @click="IWantAskAlso()"/>我也想问</Col>
            </Row>
            <Row>
              <Col span="3">
                <!--随机头像-->
              </Col>
              <Col span="11" style="position: relative;left: -5px">
                等1991人想问
              </Col>
              <Col span="10">
                <Icon type="ios-create-outline" :size="20" style="color: rgba(136,255,72,0.81)"/>我来答
              </Col>
            </Row>
          </Row>
        </div>
      </IBeautifulCard>
    </div>

  </div>
</template>

<script>
  import IBeautifulCard from "../Common/card/IBeautifulCard"
  import {QueryWaitYourAnswerList} from "../../api"
  import HatAndFacePicture from "../Common/HatAndFacePicture/HatAndFacePicture";

  export default {
    name: "WaitYourAnswer",
    components: {HatAndFacePicture, IBeautifulCard},
    data() {
      return {
        waitYourAnswerList: [],       // 等你来答
      }
    },
    methods: {
      refreshWaitYourAnswerList: async function () {
        const result = await QueryWaitYourAnswerList({});
        if (result.status === "SUCCESS") {
          this.waitYourAnswerList = result.waitYourAnswerList;
        }
      },
      IWantAskAlso:async function () {
        const result = await IWantAskAlso();
        if (rsult.status === "SUCCESS") {
          this.$Message.success("提问成功！")
        }
      }
    },
    mounted() {
      this.refreshWaitYourAnswerList();
    }
  }
</script>

<style scoped>

</style>
