<template>
	<div>

    <div class="coupon_bg" style="position: relative;cursor: pointer">
      <span v-if="youhui_type === 'reduce'">
        <span class="amount_icon">￥</span>
        <span class="coupon_amount">
          {{(coupon_amount*1).toFixed(2)}}
          <span class="yuan">元</span>
        </span>
      </span>
      <span v-else-if="youhui_type === 'discount'">
        <span class="discount_rate">
          {{(discount_rate*10).toFixed(2)}}
          <span class="zhe">折</span>
        </span>
      </span>


      <span v-if="coupon_type === 'general'" class="coupon_type">通用券</span>
      <span v-else-if="coupon_type === 'designated'" class="coupon_type">指定券</span>

      <span v-if="youhui_type === 'reduce'" class="youhui_type">减免</span>
      <span v-else-if="youhui_type === 'discount'" class="youhui_type">打折</span>

      <span class="activity_date">活动日期: {{start_date}} - {{end_date}}</span>
    </div>

  </div>
</template>

<script>
	export default {
		name: "Coupon",
    props:{
		  coupon_type:String,
      youhui_type:String,
      start_date:String,
      end_date:String,

      coupon_amount:String,
      discount_rate:String,

    },
    methods:{
      GetDate_yyyyMMdd_byDate:function(date0){
        let date = new Date(date0);
        let year = date.getFullYear();
        let month = date.getMonth()+1;
        if (parseInt(month)<10) {
          month =  '0'+month;
        }
        let today = date.getDate();
        if (parseInt(today)<10) {
          today =  '0'+today;
        }
        return year + '' + month + '' + today;
      }
    },
	}
</script>

<style scoped>
  .coupon_bg {
    height: 100px;
    background: url(../../../../static/images/order/coupon.jpg) no-repeat;
    background-size: 350px 100px; /*长宽比例 3.5 :1 */
  }

  /*竖线左侧*/
  .amount_icon{
    color: rgb(255, 215, 116);font-size: 30px;position: absolute;top: 40px;left: 12px;
  }

  .coupon_amount{
    color: rgb(255, 215, 116);font-size: 25px;position: absolute;top: 40px;left: 40px;
  }

  .discount_rate{
    color: rgb(255, 215, 116);font-size: 25px;position: absolute;top: 40px;left: 40px;
  }

  .yuan{
    color: rgb(255, 215, 116);font-size: 12px;position: relative;top: 0px;left: 0px;
  }

  .zhe{
    color: rgb(255, 215, 116);font-size: 12px;position: relative;top: 0px;left: 0px;
  }

/*竖线右侧*/
  .coupon_type{
    color: rgb(255, 215, 116);font-size: 30px;position: absolute;top: 40px;left: 200px;
  }

  .youhui_type{
    color: rgb(255, 215, 116);font-size: 13px;position: absolute;top: 10px;left: 290px;
  }

  .activity_date{
    color: rgb(255, 215, 116);font-size: 10px;position: absolute;top: 70px;left: 180px;
  }

</style>
