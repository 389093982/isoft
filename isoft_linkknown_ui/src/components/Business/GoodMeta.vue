<template>
  <div>
    <div v-if="good" style="padding: 50px;">
      <div style="display: flex;">
        <div style="width: 40%;">
          <Carousel autoplay dots="outside" trigger="hover" :autoplay-speed="4000">
            <CarouselItem v-for="(good_image, index) in parseGoodImages(good.good_images)">
              <div class="carousel">
                <img :src="good_image" width="100%" height="250px"
                     style="border: 3px solid rgba(188,238,204,0.24);"/>
              </div>
            </CarouselItem>
          </Carousel>
        </div>

        <div style="width: 60%;padding: 0 20px;">
          <div>
            <p class="isoft_inline_ellipsis isoft_font16">商品名称：<span class="isoft_hover_red">{{good.good_name}}</span>
            </p>
            <div style="margin: 10px 0;">
              <span class="isoft_tag3" v-for="(tag, index) in parseTag(good.good_tag)">{{tag}}</span>
            </div>
            <p class="p3line isoft_font14">商品描述：<span class="isoft_color_grey3">{{good.good_desc}}</span></p>
            <p>商品原价：<span style="color: red;font-weight: bold;">￥{{good.good_price}}</span></p>
            <p>
              优惠价格：<span style="color: red;font-weight: bold;">￥{{good.good_price}}</span>
            </p>
            <div>商品价格：<span style="color: red;font-weight: bold;">￥{{good.good_price}}</span></div>
            <div>卖家姓名：{{good.good_seller}}</div>
            <div>卖家联系方式：{{good.seller_contact}}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {checkEmpty, strSplit} from "../../tools"

  export default {
    name: "GoodMeta",
    props: {
      good: {
        type: Object,
        default: null,
      }
    },
    methods: {
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

</style>
