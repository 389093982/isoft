<template>
  <div>
    <ISimpleConfirmModal ref="eModal" modal-title="发现更好的自己" :modal-width="800" :footer-hide="true">
      <div style="display: flex;">
        <div style="width: 80%;padding: 0 20px;">
          <Form ref="formInline" :model="formInline" :rules="ruleValidate" :label-width="100">
            <FormItem label="我擅长" prop="expertise">
              <Input type="textarea" :rows="5" v-model.trim="formInline.expertise"
                     placeholder="如果您有充足的业余时间，如果您有充足的资源，如果你想通过副业赚钱，请写下您的专长"></Input>
            </FormItem>
            <div style="width: 83%;float: right;">
              <span class="isoft_button_red3" @click="$router.push({path:'/business/edit'})">免费发布服务</span>
            </div>
          </Form>
        </div>
        <div style="width: 20%;">
          <span>购买 订单推送服务</span>
          <span>购买 专家咨询服务</span>
        </div>
      </div>
    </ISimpleConfirmModal>
  </div>
</template>

<script>
  import ISimpleConfirmModal from "../Common/modal/ISimpleConfirmModal";

  export default {
    name: "Expertise",
    components: {ISimpleConfirmModal},
    data() {
      return {
        formInline: {
          expertise: '',
        },
        ruleValidate: {
          expertise: [
            {required: true, message: '填写的内容不能为空!', trigger: 'blur'},
          ],
        }
      }
    },
    methods: {
      showModal: function () {
        this.$refs.eModal.showModal();
      }
    }
  }
</script>

<style scoped>

</style>
