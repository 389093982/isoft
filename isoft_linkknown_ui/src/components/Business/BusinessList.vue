<template>
  <div style="margin:15px;background-color: #fff;border: 1px solid #e6e6e6;border-radius: 4px;min-height: 500px;">
    <!-- 内外边距：上右下左 -->
    <Row style="padding: 15px 10px 10px 25px;">
      <Col span="2">
        <IBeautifulLink @onclick="$router.push({path:'/business/list'})">热销商品</IBeautifulLink>
      </Col>
      <Col span="3">
        <IBeautifulLink @onclick="showMyBusiness">我的店铺商品
        </IBeautifulLink>
      </Col>
      <Col span="2">
        <IBeautifulLink @onclick="$router.push({path:'/business/edit'})">发布服务或商品</IBeautifulLink>
      </Col>
    </Row>

    <div v-for="(good, index) in goods" class="isoft_top10 isoft_pd10 isoft_mg10" style="border: 1px solid #eee;">
      <div style="display: flex;">
        <div style="width: 40%;">
          <Carousel autoplay dots="outside" trigger="hover" :autoplay-speed="4000">
            <CarouselItem v-for="(good_image, index) in parseGoodImages(good.good_images)">
              <div class="demo-carousel">
                <img :src="good_image" width="100%" height="250px"/>
              </div>
            </CarouselItem>
          </Carousel>
        </div>
        <div style="width: 60%;padding: 0 50px;" :class="index % 2 === 0 ? 'item_border1': 'item_border2'">
          <div>
            <p class="isoft_inline_ellipsis isoft_font16">
              商品名称：<span class="isoft_hover_red" @click="$router.push({path:'/business/detail',query:{id:good.id}})">{{good.good_name}}</span>
            </p>
            <div style="margin: 10px 0;">
              <span class="isoft_tag3" v-for="(tag, index) in parseTag(good.good_tag)">{{tag}}</span>
            </div>
            <p class="p3line isoft_font14">商品描述：<span class="isoft_color_grey3">{{good.good_desc}}</span></p>
            <p>商品原价：<span style="color: red;font-weight: bold;">￥{{good.good_price}}</span></p>
            <p>优惠价格：<span style="color: red;font-weight: bold;">￥{{good.good_price}}</span></p>
            <p>卖家姓名：{{good.good_seller}}</p>
            <p>卖家联系方式：{{good.seller_contact}}</p>
          </div>
          <div>
            <span class="isoft_button_red2 isoft_point_cursor" v-if="isLoginUserName(good.good_seller)"
                  @click="$router.push({path:'/business/edit',query:{id:good.id}})">
              编辑商品
            </span>
            <span v-else>
              <span class="isoft_button_red2 isoft_point_cursor" @click="payConfirm(good)">立即购买</span>
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import IBeautifulLink from "../Common/link/IBeautifulLink";
  import {GoodList, NewOrder} from "../../api"
  import {checkEmpty, CheckHasLoginConfirmDialog2, GetLoginUserName, strSplit} from "../../tools"

  export default {
    name: "GoodList",
    components: {IBeautifulLink},
    data() {
      return {
        showGoodEditModal: false,
        goods: [],
      }
    },
    methods: {
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
  .item_border1::before {
    content: '';
    float: left;
    margin-top: 25%;
    margin-left: -30px;
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
    margin-left: -30px;
    border-left: 3px solid #718cbc;
    transition: all 300ms ease-in-out;
    height: 0;
  }

  .item_border2:hover::before {
    margin-top: 0;
    height: 100%;
  }
</style>
