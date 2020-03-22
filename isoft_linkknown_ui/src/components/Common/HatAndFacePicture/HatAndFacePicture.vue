<template>
	<div>

    <div v-if="src">
      <img class="isoft_hover_red" style="cursor: pointer;border: 1px solid grey;border-radius:50%;" :width=src_size :height=src_size :src=src @error="defImg()">
    </div>
    <div v-else>
      <img class="isoft_hover_red" style="cursor: pointer;border: 1px solid grey;border-radius:50%;" :width=src_size :height=src_size src="../../../assets/default.png" @error="defImg()">
    </div>

    <!--帽子 & 头像-->
    <div v-if="vip_level>0 && hat_in_use==='Y'" :style="{position: 'relative',width:0,height:0,left:hat_relative_left+'px',top:hat_relative_top+'px'}">
      <img src="../../../../static/images/vipHat/hat01.png" :width=hat_width :height=hat_height @error="defImg()" style="cursor: pointer"/>
    </div>
    <div v-else :style="{position: 'relative',width:0,height:0,left:hat_relative_left+'px',top:hat_relative_top+'px'}">
    <img src="../../../../static/images/vipHat/emptyHat.png" :width=hat_width :height=hat_height @error="defImg()"/>
    </div>

  </div>
</template>

<script>
	export default {
		name: "HatAndFacePicture",
    props:{
		  // vip等级
      vip_level:{
        type: [Number, String],
        default:0
      },
      //头像size
		  src_size:{
		    type:Number,
        default:90,
      },
      //头像图片
		  src:{
		    type:String,
        default:''
      },
      //是否戴帽子
      hat_in_use:{
        type:String,
        default:'N'
      },
      //帽子宽度
      hat_width:{
        type:Number,
        default:80,
      },
      //帽子高度
      hat_height:{
        type:Number,
        default:20,
      },
      //帽子自左移动
      hat_relative_left:{
        type:Number,
        default:5
      },
      //帽子自上移动
      hat_relative_top:{
        type:Number,
        default:-100
      },

    },
    data(){
		  return{

      }
    }
	}
</script>

<style scoped>

</style>
