<template>
  <div style="margin:15px;background-color: #fff;border: 1px solid #e6e6e6;border-radius: 4px;min-height: 500px;">
    <div style="display: flex;padding: 15px 150px;text-align: center;">
      <div style="width: 20%;" @click="$router.push({path:'/business/list'})">热销商品</div>
      <div style="width: 20%;" @click="showMyBusiness">我的店铺商品</div>
      <div style="width: 20%;" @click="$router.push({path:'/business/edit'})">发布服务或商品</div>
    </div>

    <div style="display: flex;">
      <div style="width: 80%;">
        <div v-for="(good, index) in goods" class="isoft_top10 isoft_pd10"
             style="margin: 10px;border: 1px solid #eee;">
          <div style="display: flex;">
            <div style="width: 40%;">
              <Carousel autoplay dots="outside" trigger="hover" :autoplay-speed="4000">
                <CarouselItem v-for="(good_image, index) in parseGoodImages(good.good_images)">
                  <div class="carousel">
                    <img :src="good_image" width="100%" height="250px"
                         style="border: 3px solid rgba(188,238,204,0.24);"/>
                    <div style="position: relative;">
                      <div class="carousel_label">{{getLabel(good, index)}}</div>
                    </div>
                  </div>
                </CarouselItem>
              </Carousel>
            </div>
            <div style="width: 60%;padding: 0 20px;" :class="index % 2 === 0 ? 'item_border1': 'item_border2'">
              <div>
                <p class="isoft_inline_ellipsis isoft_font16">
                  商品名称：<span class="isoft_hover_red"
                             @click="$router.push({path:'/business/detail',query:{id:good.id}})">{{good.good_name}}</span>
                </p>
                <div style="margin: 10px 0;">
                  <span class="isoft_tag3" v-for="(tag, index) in parseTag(good.good_tag)">{{tag}}</span>
                </div>
                <p class="p3line isoft_font14">商品描述：<span class="isoft_color_grey3">{{good.good_desc}}</span></p>
                <p>商品原价：<span style="color: red;font-weight: bold;">￥{{good.good_price}}</span></p>
                <p>
                  优惠价格：<span style="color: red;font-weight: bold;">￥{{good.good_price}}</span>

                  <span style="font-size: 12px;color: #999;float: right;">
                    <span class="pl">
                      <div style="cursor: pointer;display: inline-block;" class="amount">
                        评论数:0
                        <BusinessComment class="comment" style="position: absolute;z-index: 999;"/>
                      </div>
                    </span>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <span class="cjl">
                      <span class="amount">成交量:0</span>
                      <span class="info">专业的事找专业的人，<a @click="showExpertise">我要发挥我的专长</a></span>
                    </span>
                  </span>
                </p>
                <p>卖家姓名：{{good.good_seller}}</p>
                <p>卖家联系方式：{{good.seller_contact}}</p>
              </div>
              <div style="display: flex;margin-top: 10px;">
                <div style="width: 50%;">
                  <span class="isoft_button_red2 isoft_point_cursor" v-if="isLoginUserName(good.good_seller)"
                        @click="$router.push({path:'/business/edit',query:{id:good.id}})">
                    编辑商品
                  </span>
                  <span v-else class="isoft_button_red2 isoft_point_cursor" @click="payConfirm(good)">立即购买</span>
                </div>
                <div style="width: 50%;text-align: right;">
                  <span class="isoft_button_red_small">免费咨询</span>
                </div>
              </div>
            </div>
          </div>
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
      </div>
    </div>

    <div>
      <!-- 专长 -->
      <Expertise ref="expertise"/>
    </div>
  </div>
</template>

<script>
  import IBeautifulLink from "../Common/link/IBeautifulLink";
  import {GoodList, NewOrder} from "../../api"
  import {checkEmpty, CheckHasLoginConfirmDialog2, GetLoginUserName, strSplit} from "../../tools"
  import Expertise from "./Expertise";
  import BusinessComment from "./BusinessComment";

  export default {
    name: "GoodList",
    components: {BusinessComment, Expertise, IBeautifulLink},
    data() {
      return {
        showGoodEditModal: false,
        goods: [],
      }
    },
    methods: {
      showExpertise: function () {
        this.$refs.expertise.showModal();
      },
      getLabel: function (good, index) {
        return "链知网，让赚钱变得更简单一些";
      },
      parseTag: function (good_tag) {
        return strSplit(good_tag, "|").filter(tag => !checkEmpty(tag));
      },
      parseGoodImages: function (good_images) {
        return JSON.parse(good_images);
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
            path: '/business/pay_confirm',
            query: {"good_id": good.id, "orderCode": result.orderCode}
          });
        }
      },
      refreshGoodList: async function () {
        const result = await GoodList();
        if (result.status === "SUCCESS") {
          this.goods = result.goods;
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
  .carousel .carousel_label {
    display: none;
    margin: 4px;
    padding: 2px 0 0 10px;
    background-color: rgba(0, 0, 0, 0.6);
    color: white;
    width: 100%;
    height: 40px;
    line-height: 40px;
    text-align: center;
    position: absolute;
    top: 0px;
    transition: all ease-in 1s;
  }

  .carousel:hover .carousel_label {
    display: block;
    top: -55px;
  }

  .item_border1::before {
    content: '';
    float: left;
    margin-top: 25%;
    margin-left: -16px;
    border-left: 3px solid #53b66d;
    transition: all 300ms ease-in-out;
    height: 0;
  }

  .item_border1:hover::before {
    margin-top: 0;
    height: 100%;
  }

  .item_border2::before {
    content: '';
    float: left;
    margin-top: 25%;
    margin-left: -16px;
    border-left: 3px solid #718cbc;
    transition: all 300ms ease-in-out;
    height: 0;
  }

  .item_border2:hover::before {
    margin-top: 0;
    height: 100%;
  }

  .cjl .info {
    display: none;
  }

  .cjl:hover .info {
    display: inline-block;
  }

  .cjl:hover .amount {
    display: none;
  }

  .pl .amount .comment {
    display: none;
  }
  .pl .amount:hover .comment {
    display: block;
  }
</style>
