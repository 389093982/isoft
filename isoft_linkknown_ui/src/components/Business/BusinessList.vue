<template>
  <div style="margin:0 15px;background-color: #fff;border: 1px solid #e6e6e6;border-radius: 4px;min-height: 500px;">
    <div style="display: flex;padding: 20px 150px 5px 150px;text-align: center;">
      <div style="width: 20%;" @click="$router.push({path:'/business/list'})">热销商品</div>
      <div style="width: 20%;" @click="showMyBusiness">我的店铺商品</div>
      <show-more default-desc="搜索" @changeShowMore="showSearch = !showSearch"></show-more>
      <div v-show="showSearch">
        <span v-for="(goodType, index) in goodTypes" style="margin: 0 10px;">{{goodType}}</span>
      </div>
      <div style="position: absolute;right: 50px;margin-top: -10px;" class="isoft_point_cursor isoft_button_red2"
           @click="publishBusiness">我要发布
      </div>
    </div>

    <div style="display: flex;">
      <div style="width: 80%;">
        <div v-for="(good, index) in goods" class="isoft_top10 isoft_pd20"
             style="margin: 10px;border: 1px solid #eee;">

          <GoodMeta :good="good">
            <div slot="footer" style="display: flex;margin-top: 10px;">
              <div style="width: 50%;">
                  <span class="isoft_button_red2 isoft_point_cursor" v-if="isLoginUserName(good.good_seller)"
                        @click="$router.push({path:'/business/edit',query:{id:good.id}})">
                    编辑商品
                  </span>
                <span v-else class="isoft_button_red2 isoft_point_cursor" @click="payConfirm(good)">立即购买</span>
              </div>
            </div>
          </GoodMeta>
        </div>
      </div>
      <div style="width: 20%;padding: 0 10px 0 0;">
        <div style="margin-top: 10px;" class="isoft_info_tip">
          <span class="hovered hvr-grow hoverLinkColor isoft_point_cursor">上链知网，总有心发现</span>
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
  import {GoodList, NewOrder} from "../../api"
  import {CheckHasLoginConfirmDialog2, FillUserNickNameInfoByNames, GetLoginUserName} from "../../tools"
  import GoodMeta from "./GoodMeta";
  import ShowMore from "../Elementviewers/showMore";

  export default {
    name: "GoodList",
    components: {ShowMore, GoodMeta, IBeautifulLink},
    data() {
      return {
        showSearch: false,                  // 显示搜索
        goodTypes: this.GLOBAL.goodTypes,   // 商品类型
        showGoodEditModal: false,
        goods: [],
      }
    },
    methods: {
      publishBusiness: function () {
        var _this = this;
        CheckHasLoginConfirmDialog2(this, function () {
          _this.$router.push({path: '/business/edit'})
        });
      },
      showMyBusiness: function () {
        var _this = this;
        CheckHasLoginConfirmDialog2(this, function () {
          _this.$router.push({path: '/business/list'});
        });
      },
      payConfirm: async function (good) {
        const result = await NewOrder(good.id);
        if (result.status === "SUCCESS") {
          this.$router.push({
            path: '/business/payConfirm',
            query: {"good_id": good.id, "orderCode": result.orderCode}
          });
        }
      },
      refreshGoodList: async function () {
        const result = await GoodList();
        if (result.status === "SUCCESS") {
          this.goods = await FillUserNickNameInfoByNames(result.goods, "good_seller");
        }
      },
      isLoginUserName: function (user_name) {
        return user_name === GetLoginUserName();
      },
    },
    mounted() {
      this.refreshGoodList();
    },
    watch: {
      '$route': 'refreshGoodList'
    },
  }
</script>

<style scoped>

</style>
