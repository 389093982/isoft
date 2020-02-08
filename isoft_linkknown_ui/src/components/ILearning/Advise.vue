<template>
  <div style="min-height: 500px;margin: 0 15px;padding: 50px; background-color: #fff;border: 1px solid #e6e6e6;border-radius: 4px;">

    <Row>
      <Col span="12">
        <h3>您的意见或建议是我们前进的动力</h3>
      </Col>
      <!--<Col span="12" style="text-align: right;">-->
        <!--<span>邀请好友赢取 VIP</span>&nbsp;&nbsp;&nbsp;<span>提出意见建议吐槽赢取 VIP</span>-->
      <!--</Col>-->
    </Row>
    <Tabs size="small">
      <TabPane label="意见">
        <div style="text-align: right;">
          <Input v-model.trim="advise" type="textarea" :rows="8" placeholder="请输入您的意见我们也会虚心接受奥！"></Input>
          <Button type="success" style="width: 100px;margin: 10px 0 0 0;" @click="submitAdvise('advise')">提交意见</Button>
        </div>
      </TabPane>
      <TabPane label="吐槽">
        <div style="text-align: right;">
          <Input v-model.trim="complaints" type="textarea" :rows="8" placeholder="请输入您的吐槽内容..."></Input>
          <Button type="warning" style="width: 100px;margin: 10px 0 0 0;" @click="submitAdvise('complaints')">提交吐槽</Button>
        </div>
      </TabPane>
    </Tabs>

    <GussYouLike style="margin-top: 50px;"/>
  </div>
</template>

<script>
  import {InsertAdvise} from "../../api"

  const GussYouLike = () => import("@/components/ILearning/GussYouLike");

  export default {
    name: "Advise",
    components: {GussYouLike},
    data() {
      return {
        advise: '',
        complaints:'',
      }
    },
    methods: {
      submitAdvise: async function (type) {
        let result = '';
        if ('advise' === type) {
          if (this.advise.trim().length === 0) {
            this.$Message.warning('请填写意见内容！');
            return false;
          }
          result = await InsertAdvise({'advise_type':type,'advise':this.advise});
        }else if ('complaints' === type) {
          if (this.complaints.trim().length === 0) {
            this.$Message.warning('请填写吐槽内容！');
            return false;
          }
          result = await InsertAdvise({'advise_type':type,'advise':this.complaints});
        }

        if (result.status == "SUCCESS") {
          this.$Message.success("提交成功!感谢您的反馈 ^_^");
        }
      },
    }
  }
</script>

<style scoped>

</style>
