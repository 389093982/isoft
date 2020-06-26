<template>
  <div class="isoft_bg_white" style="min-height: 600px;">
    <div v-if="coupons.length>0">
      <Row>
        <Col span="8" v-for="(coupon, index) in coupons" style="margin:20px 20px 20px 20px ">
          <!--通用券-减免-->
          <Coupon v-if="coupon.coupon_type === 'general'"
                  :activity_id="coupon.activity_id"
                  :coupon_type="coupon.coupon_type"
                  :youhui_type="coupon.youhui_type"
                  :start_date="coupon.start_date"
                  :end_date="coupon.end_date"
                  :coupon_amount="coupon.coupon_amount"
                  :goods_min_amount="coupon.goods_min_amount"
                  :discount_rate="coupon.discount_rate"
                  @receiveCoupon="receiveCoupon">
          </Coupon>
          <!--通用券-打折-->
          <Coupon v-else
                  :activity_id="coupon.activity_id"
                  :coupon_type="coupon.coupon_type"
                  :youhui_type="coupon.youhui_type"
                  :start_date="coupon.start_date"
                  :end_date="coupon.end_date"
                  :coupon_amount="coupon.coupon_amount"
                  :goods_min_amount="coupon.goods_min_amount"
                  :discount_rate="coupon.discount_rate"
                  @receiveCoupon="receiveCoupon">
          </Coupon>
        </Col>
      </Row>
      <Page :total="total" :page-size="offset" show-total show-sizer
            :styles="{'text-align': 'center','margin-top': '10px'}"
            @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
    </div>
    <div v-else>
      <div style="text-align: center;">
        <div style="position: relative;top: 200px;color: #ff6900;font-size: 20px">
          暂时没有活动哦...
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {QueryCouponCenterList,ReceiveCoupon} from "../../api"
  import Coupon from "../Common/coupon/Coupon"

  export default {
    name: "CouponCenter",
    components:{Coupon},
    data() {
        return {
          coupons:[],
          // 当前页
          current_page: 1,
          // 总数
          total: 0,
          // 每页记录数
          offset: 10,
        }
    },
    methods:{
      //领券
      receiveCoupon:async function (activity_id) {
        let params = {
          'activity_id':activity_id
        };
        const result = await ReceiveCoupon(params);
        if (result.status === 'SUCCESS') {
          this.$Message.info('领券成功！');
        }else{
          this.$Message.error(result.errorMsg);
        }
        this.refreshCouponCenterList();
      },
      handleChange(page) {
        this.current_page = page;
        this.refreshCouponCenterList();
      },
      handlePageSizeChange(pageSize) {
        this.offset = pageSize;
        this.refreshCouponCenterList();
      },
      refreshCouponCenterList: async function (){
        const result = await QueryCouponCenterList({current_page: this.current_page, offset: this.offset});
        if (result.status === "SUCCESS") {
          this.coupons = result.coupons;
          this.total = result.paginator.totalcount;
        }
      }
    },
    mounted(){
      this.refreshCouponCenterList();
    }
  }
</script>

<style scoped>

</style>
