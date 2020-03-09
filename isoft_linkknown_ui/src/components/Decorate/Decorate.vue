<template>
  <div style="border: 1px solid #eee;margin: 50px;">
    <div style="border-bottom: 1px solid #e5e5e5;background: #f9f9f9;">
      <ul class="clear">
        <li v-if="decorates" v-for="(_decorate, index) in decorates"
            :class="decorate && decorate.id === _decorate.id ? 'active': ''" @click="decorate = _decorate">
          {{_decorate.decorate_name}}
        </li>
        <li style="float: right;" @click="editDecorate">
          <Icon type="md-add"/>
          快速新建装修位
        </li>
      </ul>
    </div>

    <div style="padding: 20px 100px 20px 20px;">
      <DecorateItems ref="decorateItems" v-if="decorate" :decorate="decorate"/>
    </div>
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
        decMaxLen: 5,  // 装修位最大数量
        decorates: null,   // 所有装修位
        decorate: null,    // 当前正在编辑的装修位
      }
    },
    methods: {
      editDecorate: async function () {
        if (this.decorates && this.decorates.length >= this.decMaxLen) {
          this.$Message.error("装修位超过最大数量!");
          return;
        }
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
          // 初始选中第一个
          if (this.decorate === null && this.decorates && this.decorates.length > 0) {
            this.decorate = result.decorates[0];
          }
        }
      },
    },
    mounted() {
      this.refreshLoadAllDecorates();
    }
  }
</script>

<style scoped>
  ul li {
    padding: 10px 50px;
    list-style: none;
    display: inline-block;
    cursor: pointer;
  }

  .active {
    background-color: #fff;
    border-top: 3px solid #ff9d4b;
    border-left: 1px solid #eee;
    border-right: 1px solid #eee;
  }
</style>
