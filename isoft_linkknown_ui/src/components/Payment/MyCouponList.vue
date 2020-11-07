<template>
  <div style="width: 100%;">

  <div style="display: flex;">
    <div style="width: 68%;background-color: white">
      <Row>
        <!--左侧空出一点-->
        <Col span="2">
          &nbsp;
        </Col>
        <!--右侧展示券信息-->
        <Col span="22">
          <div style="position: relative;top: 10px">
            <span @click="click2RefreshCouponList(0)" :class="pattern === 0 ? 'isoft_tag5': 'isoft_tag2'" class="isoft_font12 isoft_mr10 isoft_point_cursor">已领取</span>
            <span @click="click2RefreshCouponList(1)" :class="pattern === 1 ? 'isoft_tag5': 'isoft_tag2'" class="isoft_font12 isoft_mr10 isoft_point_cursor">已使用</span>
            <span @click="click2RefreshCouponList(2)" :class="pattern === 2 ? 'isoft_tag5': 'isoft_tag2'" class="isoft_font12 isoft_mr10 isoft_point_cursor">已过期</span>
            <span style="margin-left: 20px">数量:{{page.totalCount}}</span>
          </div>
          <Row v-for="(coupon, index) in couponDatas" style="margin-top: 20px">
            <Row>
              <!--优惠券-->
              <Col span="11">
                <Coupon :activity_id="coupon.activity_id"
                        :coupon_type="coupon.coupon_type"
                        :youhui_type="coupon.youhui_type"
                        :start_date="coupon.start_date"
                        :end_date="coupon.end_date"
                        :coupon_amount="coupon.coupon_amount"
                        :goods_min_amount="coupon.goods_min_amount"
                        :discount_rate="coupon.discount_rate">
                </Coupon>
              </Col>
              <Col span="13">
                <div>
                  <Row>
                    <Col span="14">
                      <span v-if="coupon.coupon_type==='general'">
                        通用券: 适用于 <span style="color: #cc0000">所有付费课程</span>
                      </span>
                      <span v-else-if="coupon.coupon_type==='designated'">
                        指定券: 适用于
                        <span v-if="coupon.target_type==='course'">
                          <span @click="$router.push({path:'/ilearning/courseDetail',query:{course_id:coupon.target_id}})">
                            <span style="cursor: pointer;color: blue">{{getCourseNameById(coupon.target_id)}}</span>
                          </span>
                        </span>
                      </span>

                      <div>
                        距离活动结束天数:<span class="isoft_tag3">{{remainDays(coupon.end_date)}}</span>
                      </div>

                      <div>
                        活动描述:<span>{{coupon.activity_desc}}</span>
                      </div>
                    </Col>
                    <Col span="10">
                      <div v-if="coupon.coupon_state==='used'">
                        <!--已使用-->
                        <img style="border-radius:50%;position: relative;left: -40px;" width=80 height=80 src="../../../static/images/order/used.jpg">
                      </div>
                      <div v-else>
                        <!--未使用-->
                        <span v-if="isOverdue(coupon.start_date,coupon.end_date)">
                          <!--未使用 过期的-->
                          <img style="border-radius:50%;position: relative;left: -40px" width=80 height=80 src="../../../static/images/order/overdue.jpg">
                        </span>
                        <span v-else>
                          <!--未使用 未过期-->
                          <span v-if="coupon.coupon_type==='general'">
                            通用券:这个地方可以跳到新界面，展示所有付费商品
                          </span>
                          <div v-else="coupon.coupon_type==='designated'">
                            <!--指定券-->
                            <div v-if="coupon.target_type==='course'">
                                <router-link :to="{path:'/ilearning/courseDetail',query:{course_id:coupon.target_id}}" style="position: relative">
                                  <img v-if="getCourseImgById(coupon.target_id)" :src="getCourseImgById(coupon.target_id)" height="100" width="155"/>
                                  <img v-else src="../../../static/images/common_img/default.png" height="100" width="155"/>
                                  <!-- 播放图标 -->
                                  <div v-if="true" class="ico_play" style="position: absolute;top: -65px;left: 50px;"></div>
                                </router-link>
                            </div>
                          </div>
                        </span>
                      </div>
                    </Col>
                  </Row>



                </div>
              </Col>
            </Row>
            <!--彩色分底线-->
            <SepLine></SepLine>
          </Row>

          <!--分页-->
          <div style="text-align: center;margin-top: 10px;margin-bottom: 10px">
            <Page :total="page.totalCount" :page-size="page.offset" :current="page.currentPage" show-total show-sizer @on-change="pageChange" @on-page-size-change="pageSizeChange"/>
          </div>

        </Col>
      </Row>
    </div>
  </div>

  </div>
</template>

<script>
  import {QueryPersonalCouponList,GetCourseListByIds} from "../../api/index"
  import SepLine from "../Common/SepLine";
  import Coupon from "../Common/coupon/Coupon";
  import {GetLoginUserName,CheckHasLoginConfirmDialog,checkFastClick,GetToday_yyyyMMdd} from "../../tools/index";
  import {checkHasLogin} from "../../tools/sso"

  export default {
    name: "MyCouponList",
    components:{Coupon, SepLine},
    data () {
      return {
        couponDatas: [],
        courses:[],
        page:{totalCount:0,currentPage:1,offset:10},
        pattern: 0,     // 搜索模式
      }
    },
    methods: {
      click2RefreshCouponList:function(pattern){
        if (checkFastClick()) {
          this.$Message.error("点击过快,请稍后重试!");
          return false;
        }else {
          this.pattern = pattern;
          this.refreshCouponList();
        }
      },
      refreshCouponList:async function(){
        if (checkHasLogin()) {
          var isExpired = "";
          var isUsed = "";
          if (this.pattern === 0) {
            // 已领取状态
            isExpired = "false";
            isUsed = "false";
          } else if (this.pattern === 1) {
            // 已使用状态
            isUsed = "true";
          } else if (this.pattern === 2) {
            // 已过期状态(没使用)
            isExpired = "true";
            isUsed = "false";
          }
          let params = {
            'isExpired': isExpired,
            'isUsed': isUsed,
            'currentPage':this.page.currentPage,
            'offset':this.page.offset,
          };
          const result = await QueryPersonalCouponList(params);
          if (result.status === 'SUCCESS') {
            this.couponDatas = result.coupons;
            this.page.totalCount = result.paginator.totalcount;

            //这里再发一次请求，根据课程id 集合查询 课程
            let ids = "";
            for (let i = 0;i<result.coupons.length;i++){
              if (result.coupons[i].target_id != null && result.coupons[i].target_id != "") {
                ids += result.coupons[i].target_id + ","
              }
            }
            if (ids.trim() === "") {
              return false;
            }
            //去掉最后一个逗号
            ids = ids.substring(0,ids.length-1);
            params = {
              'ids':ids
            };
            const res = await GetCourseListByIds(params);
            if (res.status === 'SUCCESS') {
              this.courses = res.courses;
            }

          }
        }else {
          CheckHasLoginConfirmDialog(this, {path: "/payment/myCouponList"});
        }
      },
      getCourseNameById:function(course_id){
        for (let i = 0; i < this.courses.length; i++) {
          if (this.courses[i].id == course_id) {
            return this.courses[i].course_name;
          }
        }
      },
      getCourseImgById:function(course_id){
        for (let i = 0; i < this.courses.length; i++) {
          if (this.courses[i].id == course_id) {
            return this.courses[i].small_image;
          }
        }
      },
      pageChange:function (page) {
        this.page.currentPage = page;
        this.refreshCouponList()
      },
      pageSizeChange:function (pageSize) {
        this.page.offset = pageSize;
        this.refreshCouponList()
      },
      remainDays:function (end_date) {
        var endDate = new Date(end_date.substring(0,4)+"/"+end_date.substring(4,6)+"/"+end_date.substring(6,8)); // 通过日期字符串，初始化第一个日期对象
        let today = GetToday_yyyyMMdd();
        var startDate = new Date(today.substring(0,4)+"/"+today.substring(4,6)+"/"+today.substring(6,8)); // 同上，初始化第二个日期对象
        var oneDay = 24 * 60 * 60 * 1000; // 完整一天有24小时，将小时换算成毫秒时间戳
        var diffDay = (endDate - startDate) / oneDay; // 计算间隔天数（注意日期隐式转换）
        if (diffDay >= 0) {
          return diffDay;
        }else{
          return "活动已结束";
        }
      },
      isOverdue:function (start_date,end_date) {
        if (Number(start_date.toString()) > Number(GetToday_yyyyMMdd().toString()) || Number(end_date.toString()) < Number(GetToday_yyyyMMdd().toString())) {
          return true;
        }else{
          return false;
        }
      }
    },
    computed:{
      loginUserName: function () {
        return GetLoginUserName();
      }
    },
    mounted:function () {
      this.refreshCouponList();
    }
  }
</script>

<style scoped>
  .ico_play {
    background: url(../../../static/images/common_img/ico_play.png) no-repeat;
    position: absolute;
    top: 35px;
    left: 55px;
    width: 60px;
    height: 60px;
  }
</style>
