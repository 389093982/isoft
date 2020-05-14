<template>
  <div>

    <div>
      <Row>
        <Col span="8" style="position: relative;top: -10px;">
          <Input v-model.trim="activity_id" placeholder="活动ID" style="width: 150px"/>
          <Input v-model.trim="coupon_id" placeholder="券ID" style="width: 150px"/>
          <Button type="primary" shape="circle" icon="ios-search" @click="refreshPayCoupon"></Button>
        </Col>
        <Col span="16" style="position: relative;top: -10px;">
          <!--分页-->
          <div style="text-align: center;">
            <Page :total="page.totalCount" :page-size="page.offset" :current="page.currentPage" show-total show-sizer @on-change="pageChange" @on-page-size-change="pageSizeChange"/>
          </div>
        </Col>
      </Row>
    </div>

    <!--表格展示优惠券-->
    <Table width="1000" border :columns="couponColumns" :data="couponDatas" size="small"></Table>


  </div>
</template>
<script>
  import {QueryPagePayCoupon} from "../../../api/index"

  export default {
    name: "SearchCoupon",
    data () {
      return {
        activity_id:'',
        coupon_id:'',
        page:{totalCount:0,currentPage:1,offset:10},
        couponColumns: [
          {
            title: '活动ID',
            key: 'activity_id',
            width: 180,
            fixed: 'left'
          },
          {
            title: '券ID',
            key: 'coupon_id',
            width: 200,
          },
          {
            title: '券类型',
            key: 'coupon_type',
            width: 100,
          },
          {
            title: '被使用对象',
            key: 'target_type',
            width: 150,
          },
          {
            title: '被使用对象ID',
            key: 'target_id',
            width: 150,
          },
          {
            title: '优惠方式',
            key: 'youhui_type',
            width: 150,
          },
          {
            title: '折扣率',
            key: 'discount_rate',
            width: 150,
          },
          {
            title: '券面金额',
            key: 'coupon_amount',
            width: 150,
          },
          {
            title: '商品门槛金额',
            key: 'goods_min_amount',
            width: 150,
          },
          {
            title: '活动开始日期',
            key: 'start_date',
            width: 120,
          },
          {
            title: '活动结束日期',
            key: 'end_date',
            width: 120,
          },
          {
            title: '领取者',
            key: 'coupon_owner',
            width: 200,
            fixed: 'right'
          },
          {
            title: '使用状态',
            key: 'coupon_state',
            width: 100,
            fixed: 'right'
          },
        ],
        couponDatas: [],
      }
    },
    methods: {
      refreshPayCoupon: async function () {
        let params = {
          'activity_id':this.activity_id,
          'coupon_id':this.coupon_id,
          'currentPage':this.page.currentPage,
          'offset':this.page.offset,
        };
        const result = await QueryPagePayCoupon(params);
        if (result.status === 'SUCCESS') {
          this.page.totalCount = result.paginator.totalcount;
          this.couponDatas = result.couponDatas;
          for (let i = 0; i < this.couponDatas.length; i++) {
            //格式化券类型
            if (this.couponDatas[i].coupon_type === 'general') {
              this.couponDatas[i].coupon_type = '通用券';
            }else if (this.couponDatas[i].coupon_type === 'designated') {
              this.couponDatas[i].coupon_type = '指定券';
            }
            //格式化 被使用对象
            if (this.couponDatas[i].target_type === 'course') {
              this.couponDatas[i].target_type = '课程';
            }
            //格式化 优惠方式
            if (this.couponDatas[i].youhui_type === 'reduce') {
              this.couponDatas[i].youhui_type = '减免';
            }else if (this.couponDatas[i].youhui_type === 'discount') {
              this.couponDatas[i].youhui_type = '打折';
            }
            //格式化使用状态
            if (this.couponDatas[i].coupon_state === 'used') {
              this.couponDatas[i].coupon_state = '已使用';
            }
          }
        }
      },
      pageChange:function (page) {
        this.page.currentPage = page;
        this.refreshPayCoupon()
      },
      pageSizeChange:function (pageSize) {
        this.page.offset = pageSize;
        this.refreshPayCoupon()
      },

    },
    mounted:function () {
      this.refreshPayCoupon();
    },
  }
</script>

<style scoped>

</style>
