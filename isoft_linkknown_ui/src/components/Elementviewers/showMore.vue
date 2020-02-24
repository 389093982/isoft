<template>
    <span style="cursor: pointer;font-size: 12px;color: grey" @click="changeShowMore()">
      <span v-if="showMore"><Icon type="ios-arrow-down" /></span>
      <span v-else><Icon type="ios-arrow-up" /></span>
      查看更多
    </span>
</template>

<script>
	export default {
	  props:{
	    showMore:{
	      type:Boolean,
        default:false,
      },
    },
		name: "showMore",
    data(){
		  return{

      }
    },
    methods:{
      changeShowMore:function () {
        this.showMore = !this.showMore;
        this.$emit("changeShowMore",this.showMore);
      }
    }
	}
</script>

<style scoped>

</style>
