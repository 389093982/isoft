<template>
  <div>
    <div v-if="good">
      <div style="display: flex;">
        <div style="width: 40%;">
          <Carousel autoplay dots="outside" trigger="hover" :autoplay-speed="4000">
            <CarouselItem v-for="(good_image, index) in parseGoodImages(good.good_images)">
              <div class="carousel">
                <img :src="good_image" width="100%" height="250px"
                     style="border: 3px solid rgba(188,238,204,0.24);"/>
                <div style="position: relative;">
                  <div class="carousel_label">{{getLabel()}}</div>
                </div>
              </div>
            </CarouselItem>
          </Carousel>
        </div>

        <div style="width: 60%;padding: 0 20px;" class="item_border1">
          <div>
            <p class="isoft_inline_ellipsis isoft_font16">商品名称：
              <span class="isoft_hover_red" @click="$router.push({path:'/business/detail',query:{id:good.id}})">{{good.good_name}}</span>
            </p>

            <div style="margin: 10px 0;">
              <span class="isoft_tag3" v-for="(tag, index) in parseTag(good.good_tag)">{{tag}}</span>
            </div>
            <p class="p3line isoft_font14">商品描述：<span class="isoft_color_grey3">{{good.good_desc}}</span></p>
            <p>商品原价：<span style="color: red;font-weight: bold;">￥{{good.good_price}}</span></p>
            <p>
              优惠价格：<span style="color: red;font-weight: bold;">￥{{good.good_price}}</span>
              <GoodShortcut/>
            </p>
            <div>商品价格：<span style="color: red;font-weight: bold;">￥{{good.good_price}}</span></div>
            <div>卖家姓名：{{good.good_seller}}</div>
            <div>卖家联系方式：{{good.seller_contact}}</div>
          </div>

          <slot name="footer"></slot>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {checkEmpty, strSplit} from "../../tools"
  import GoodShortcut from "./GoodShortcut"

  export default {
    name: "GoodMeta",
    components: {GoodShortcut},
    props: {
      good: {
        type: Object,
        default: null,
      }
    },
    methods: {
      getLabel: function () {
        return "链知网，让赚钱变得更简单一些";
      },
      parseTag: function (good_tag) {
        return strSplit(good_tag, "|").filter(tag => !checkEmpty(tag));
      },
      parseGoodImages: function (good_images) {
        return JSON.parse(good_images);
      },
    }
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
</style>
