<template>
  <div style="border: 1px solid #eee;margin: 50px;padding: 50px;">
    <div style="display: flex;">
      <div style="width: 60%;">
        <span v-if="decorates" v-for="(_decorate, index) in decorates" @click="decorate = _decorate">{{_decorate.decorate_name}}</span>
      </div>
      <div style="width: 40%;text-align: right;" class="isoft_hover_red" @click="editDecorate">
        <Icon type="md-add"/>
        快速新建装修位
      </div>
    </div>
    <DecorateItems ref="decorateItems" v-if="decorate" :decorate="decorate"/>
  </div>
</template>

<script>
  import {EditDecorate, LoadAllDecorates} from "../../api"
  import DecorateItems from "./DecorateItems";

  export default {
    name: "Decorate",
    components: {DecorateItems},
    props: {
      referer_type: {
        type: String,
        default: '',
      },
      referer_id: {
        type: String,
        default: '',
      }
    },
    data() {
      return {
        decorates: null,  // 所有装修位
        decorate: null,    // 当前正在编辑的装修位
      }
    },
    methods: {
      editDecorate: async function () {
        let params = {
          'referer_type': this.referer_type,
          'referer_id': this.referer_id,
          'decorate_name': '默认装修位',
          'decorate_icon': '',
        }
        const result = await EditDecorate(params);
        if (result.status === "SUCCESS") {
          this.refreshLoadAllDecorates();
        }
      },
      refreshLoadAllDecorates: async function () {
        const result = await LoadAllDecorates({referer_type: this.referer_type, referer_id: this.referer_id});
        if (result.status === "SUCCESS") {
          this.decorates = result.decorates;
        }
      },
    },
    mounted() {
      this.refreshLoadAllDecorates();
    }
  }
</script>

<style scoped>

</style>
