<template>
  <div style="overflow-x: hidden;position: relative">
    <div class="course_title_bg" style="height: 80px;padding: 0 30px;display: flex">
      <span style="color: white;font-size: 28px;margin-left: 128px;width: 50%;position: relative">
        <span class="animated faster bounceInRight" style="cursor: pointer;position: absolute;top: 27px;">Stay hungry，Stay foolish</span>
      </span>
      <!--搜索框-->
      <span style="width: 50%;position: relative;">
        <Affix :offset-top="38">
          <ISearch @submitFunc="submitFunc" style="position: absolute;top: 20px;right: 140px"></ISearch>
        </Affix>
      </span>
    </div>

    <!--课程分类-->
    <CourseType></CourseType>

    <!--优惠券信息-->
    <div style="position: absolute;top: 190px;left: 1040px;cursor: pointer">
      <img class="animated zoomIn slower infinite" src="../../../static/images/common_img/coupon_center.png" height="120" width="120" @click="$router.push('/payment/couponCenter')"/>
    </div>

    <!--热门课程推荐-->
    <div class="isoft_bg_white isoft_pd10">
      <HotRecommend :show-display-icon="true"></HotRecommend>
    </div>
  </div>
</template>

<script>
  import CourseType from "./CourseType/CourseType"
  import ISearch from "../Common/search/ISearch"
  import HotRecommend from "./Course/HotRecommend"
  import ToolBox from "../Background/CMS/ToolBox"
  import HorizontalLinks from "../Elementviewers/HorizontalLinks"
  import IHotRecommand from "../Common/recommend/IHotRecommand"
  import IndexCarousel from "./IndexCarousel";
  import ShowModulars from "./ShowModulars";
  import Coupon from "../Common/coupon/Coupon";
  import {GetToday_yyyyMMdd} from "../../tools/index"
  import {QueryGeneralCoupon,ReceiveCoupon} from "../../api/index"

  export default {
    name: "JingpinCourse",
    components: {
      Coupon,
      CourseType, ShowModulars, IndexCarousel, IHotRecommand, HorizontalLinks, HotRecommend, ToolBox, ISearch,
    },
    data() {
      return {
        general_reduce_coupon:'',
        general_discount_coupon:'',
      }
    },
    methods:{
      submitFunc: function (search_data) {
        if (search_data.length === 0) {
          return false;
        }
        this.$router.push({path: '/ilearning/courseSearch', query: {search:search_data}});
      },
      chooseCourseType: function (course_type, course_sub_type) {
        // params是路由的一部分
        // query是拼接在url后面的参数
        // 由于动态路由也是传递params的,所以在 this.$router.push() 方法中path不能和params一起使用,否则params将无效.需要用name来指定页面
        this.$router.push({path: '/ilearning/courseSearch', query: {search: course_sub_type}});
      },
      toggle: function (data) {
        alert(data);
      },
      //刷新优惠券
      refreshCoupon:async function () {
        // 查指定券
        let params = {
          'today':GetToday_yyyyMMdd(),
        };
        const generalResult = await QueryGeneralCoupon(params);
        if (generalResult.status === 'SUCCESS') {
          this.general_reduce_coupon = generalResult.general_reduce_coupon;
          this.general_discount_coupon = generalResult.general_discount_coupon;
        }
      },
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
        this.refreshCoupon();
      }
    },
    mounted:function () {
      this.refreshCoupon();
    }
  }
</script>

<style scoped>
  .course_title_bg {
    height: 140px;
    background: url(../../../static/images/common_img/course_title_bg.jpg) no-repeat;
    background-size: 100%;
  }
</style>
