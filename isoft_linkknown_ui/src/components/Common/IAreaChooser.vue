<template>
<span>
  <Modal
    v-model="areaChooserModal"
    width="500"
    :title="title"
    :mask-closable="false"
    :styles="{top: '20px'}"
    @on-ok="handleOk">
    <div>
      <Scroll height="350">
        <VDistpicker type="mobile" @selected='selected'/>
      </Scroll>
    </div>
  </Modal>
</span>
</template>

<script>
  import VDistpicker from "v-distpicker"

  export default {
    name: "IAreaChooser",
    components: {VDistpicker},
    props: {
      title: {
        type: String,
        default: 'title',
      }
    },
    data() {
      return {
        province: '',
        city: '',
        area: '',
        areaChooserModal: false,
      }
    },
    methods: {
      selected: function (data) {
        this.province = data.province.value;
        this.city = data.city.value;
        this.area = data.area.value;
      },
      handleOk: function () {
        this.$emit('handleSubmit', this.province, this.city, this.area);
      },
      showModal() {
        this.areaChooserModal = true;
      },
      hideModal() {
        this.areaChooserModal = false;
      }
    }
  }
</script>

<style scoped>

</style>
