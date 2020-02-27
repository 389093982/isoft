<template>
    <span style="cursor: pointer;font-size: 12px;color: grey" @click="changeShowMore()">
      <span v-if="showMore"><Icon type="ios-arrow-down" /></span>
      <span v-else><Icon type="ios-arrow-up" /></span>
      {{defaultDesc}}
    </span>
</template>

<script>
	export default {
	  props:{
	    showMore:{
	      type:Boolean,
        default:false,
      },
      defaultDesc:{
	      type:String,
        default:'查看更多'
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
