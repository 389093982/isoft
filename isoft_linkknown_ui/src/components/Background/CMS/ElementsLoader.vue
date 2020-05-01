<template>
  <div>
    <slot></slot>
  </div>
</template>

<script>
  import {expires, FilterElementByPlacement} from "../../../api"
  import {_store, checkEmpty} from "../../../tools"

  export default {
    name: "ElementsLoader",
    props: {
      placement_name: {
        type: String,
        default: '',
      }
    },
    methods: {
      refreshElement: async function () {
        var result = null;
        if (_store.getItem(this.placement_name)) {
          result = JSON.parse(_store.getItem(this.placement_name));
        } else {
          if (!checkEmpty(this.placement_name)) {
            let params = {
              'placement':this.placement_name
            };
            result = await FilterElementByPlacement(params);
            if (result.status === "SUCCESS") {
              _store.setItem({
                name: this.placement_name,
                value: JSON.stringify(result),
                expires: expires,
              });
            }
          }
        }
        this.checkElementsResult(result);
        if (result != null && result.status === "SUCCESS") {
          this.$emit("onLoadElement", result.placement.placement_label, result.elements);
        }
      },
      checkElementsResult: function (result) {
        if (result.placement == null || result.elements == null) {
          console.log("placement not found ,placement_name:"+this.placement_name);
        }
      }
    },
    mounted() {
      this.refreshElement();
    }
  }
</script>

<style scoped>

</style>
