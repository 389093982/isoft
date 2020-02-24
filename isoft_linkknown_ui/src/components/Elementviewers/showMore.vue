<template>
  <div v-if="elements.length > minLen">
    <span style="cursor: pointer;font-size: 12px;color: grey" @click="changeShowMore()">
      <span v-if="showMore"><Icon type="ios-arrow-down" /></span>
      <span v-else><Icon type="ios-arrow-up" /></span>
      查看更多
    </span>
  </div>
</template>

<script>
	export default {
	  props:{
      filter_elements:[],
      elements:[],
      minLen:10,
    },
		name: "showMore",
    data(){
		  return{
        showMore:false,
      }
    },
    methods:{
      changeShowMore:function () {
        this.showMore = !this.showMore;
        this.showMore ? this.filter_elements = this.elements : this.filter_elements = this.elements.slice(0,this.minLen);
      }
    }
	}
</script>

<style scoped>

</style>
