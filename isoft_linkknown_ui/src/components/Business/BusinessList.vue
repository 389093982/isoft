<template>
  <div style="margin:0 15px;border: 1px solid #e6e6e6;border-radius: 4px;min-height: 500px;">
    <div class="isoft_bg_white"
         style="display: flex;padding: 20px 150px 20px 150px;margin-bottom:5px;text-align: center;">
      <div style="width: 20%;" @click="$router.push({path:'/business/businessList'})">热销商品</div>
      <div style="width: 20%;" @click="showMyBusiness">我的店铺商品</div>
      <show-more default-desc="搜索" @changeShowMore="showSearch = !showSearch"></show-more>
      <div v-show="showSearch">
        <span v-for="(goodType, index) in goodTypes" style="margin: 0 10px;">{{goodType}}</span>
      </div>
      <div style="position: absolute;right: 50px;margin-top: -10px;" class="isoft_point_cursor isoft_button_red2"
           @click="publishBusiness">我要发布
      </div>
    </div>
    <div style="display: flex;justify-content:space-between;">
      <div class="isoft_bg_white" style="width: 74.5%;padding-bottom: 10px;">
        <div v-for="(good, index) in goods" class="isoft_top10 isoft_pd20"
             style="margin: 10px;border: 1px solid #eee;">

          <GoodMeta :good="good" :show-highlights="false">
            <div slot="footer" style="display: flex;margin-top: 10px;">
              <div style="width: 50%;">
                  <span class="isoft_button_red2 isoft_point_cursor" v-if="isLoginUserName(good.good_seller)"
                        @click="$router.push({path:'/business/businessEdit',query:{id:good.id}})">
                    编辑商品
                  </span>
                <span v-else class="isoft_button_red2 isoft_point_cursor" @click="payConfirm(good)">立即购买</span>
              </div>
            </div>
          </GoodMeta>
        </div>

        <Page :total="total" :page-size="offset" show-total show-sizer
              :styles="{'text-align': 'center','margin-top': '10px'}"
              @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
      </div>
      <div class="isoft_bg_white" style="width: 25%;padding: 0 10px;">
        <div style="margin-top: 10px;" class="isoft_info_tip">
          <span class="hovered hvr-grow isoft_hover_red2 isoft_point_cursor">上链知网，总有心发现</span>
        </div>

        <div style="margin-top: 10px;padding: 25px 5px;" class="isoft_info_tip2">
          <p style="font-weight: bold;color: #ff633b;">没有找到满意的服务或商品？</p>
          <p style="font-weight: bold;">是否对我们感到不满意？</p>
          <p style="font-weight: bold;padding: 10px 20px;">
            <span class="isoft_button_red1" @click="$router.push({path:'/ilearning/advise'})">我要好好提提意见></span>
          </p>
        </div>

        <div style="margin-top: 10px;padding: 0 3px;" class="isoft_info_tip3">
          <span style="color: red;"><span>规则</span>&nbsp;|&nbsp;<span>入驻</span></span><span>入驻链知网，成为创业合作伙伴</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import IBeautifulLink from "../Common/link/IBeautifulLink";
  import {GoodsList} from "../../api"
  import {CheckHasLoginConfirmDialog2, FillUserNickNameInfoByNames, GetLoginUserName} from "../../tools"
  import GoodMeta from "./GoodMeta";
  import ShowMore from "../Elementviewers/showMore";

  export default {
    name: "BusinessList",
    components: {ShowMore, GoodMeta, IBeautifulLink},
    data() {
      return {
        showSearch: false,                  // 显示搜索
        goodTypes: this.GLOBAL.goodTypes,   // 商品类型
        showGoodEditModal: false,
        goods: [],
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 10,
      }
    },
    methods: {
      publishBusiness: function () {
        var _this = this;
        CheckHasLoginConfirmDialog2(this, function () {
          _this.$router.push({path: '/business/businessEdit'});
        });
      },
      showMyBusiness: function () {
        var _this = this;
        CheckHasLoginConfirmDialog2(this, function () {
          _this.$router.push({path: '/business/businessList'});
        });
      },
      payConfirm: async function (good) {

          this.$router.push({
            path: '/business/payConfirm',
            query: {"good_id": good.id, "orderCode": ''}
          });

      },
      refreshGoodsList: async function () {
        const result = await GoodsList({offset: this.offset, current_page: this.current_page});
        if (result.status === "SUCCESS") {
          this.goods = await FillUserNickNameInfoByNames(result.goods, "good_seller");
          this.total = result.paginator.totalcount;
        }
      },
      isLoginUserName: function (user_name) {
        return user_name === GetLoginUserName();
      },
      handleChange(page) {
        this.current_page = page;
        this.refreshGoodsList();
      },
      handlePageSizeChange(pageSize) {
        this.offset = pageSize;
        this.refreshGoodsList();
      },
    },
    mounted() {
      this.refreshGoodsList();
    },
    watch: {
      '$route': 'refreshGoodsList',
    },
  }
</script>

<style scoped>

</style>
