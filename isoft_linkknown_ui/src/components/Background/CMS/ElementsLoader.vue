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
            result = await FilterElementByPlacement(this.placement_name);
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
        alert(result.status +""+result.placement);
        if (result == null || result.status !== "SUCCESS") {
          console.log("call filterElementByPlacement method error!");
        } else if (result.status === "SUCESS" && result.placement === null) {
          console.log("please config placement for " + this.placement_name);
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
