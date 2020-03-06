<template>
  <!--删除模块弹框-->
  <Modal
    v-model="isShow"
    :title="title"
    @on-ok="ok"
    @on-cancel="cancel">
    <p>{{content}}</p>
  </Modal>
</template>

<script>
	export default {
	  props:{
      title:{
        type:String,
        default:'提示:'
      },
      content:{
        type:String,
        default:'确认删除?'
      },
    },
		name: "IsComfirmDelete",
    data(){
		  return{
        isShow:false,
      }
    },
    methods:{
      showModal(){
        this.isShow = true;
      },
      ok () {
        // this.isShow = false;
        // this.$emit("confirmDelete")
      },
      cancel(){
        // this.isShow = false;
      }
    }
	}
</script>

<style scoped>

</style>
