<template>
  <div>
    <div v-if="good">
      <div style="display: flex;justify-content:space-between;">
        <div :style="{'width': good.good_images ? '40%':'0'}">
          <Carousel autoplay dots="outside" trigger="hover" :autoplay-speed="4000">
            <CarouselItem v-for="(good_image, index) in parseGoodImages(good.good_images)">
              <div class="carousel">
                <img :src="good_image" width="100%" height="250px"/>
                <div style="position: relative;">
                  <div class="carousel_label">{{getLabel()}}</div>
                </div>
              </div>
            </CarouselItem>
          </Carousel>
        </div>

        <div :style="{'width':good.good_images ? '59%' : '100%'}" style="padding: 0 20px;"
             :class="good.good_images ? 'item_border1' : ''">
          <div>
            <div class="isoft_tip_parent">
              <div class="isoft_inline_ellipsis isoft_font16">
                商品名称：<span class="isoft_hover_red"
                           @click="$router.push({path:'/business/businessDetail',query:{id:good.id}})">{{good.good_name}}</span>
              </div>
              <div v-if="!showHighlights && good.highlights" class="isoft_tip isoft_font14" style="width: 500px;">
                亮点特色：<span class="isoft_color_grey3 isoft_font12">{{good.highlights}}</span>
              </div>
            </div>

            <div style="margin: 10px 0;">
              <TagRender :tags="parseTag(good.good_tag)"/>
            </div>
            <p class="p3line isoft_font14">商品描述：<span class="isoft_color_grey3">{{good.good_desc}}</span></p>
            <p v-if="showHighlights && good.highlights" class="p3line isoft_font14">亮点特色：<span
              class="isoft_color_grey3">{{good.highlights}}</span>
            </p>
            <p>商品原价：<span style="color: red;font-weight: bold;">￥{{good.good_price}}</span></p>
            <p>
              优惠价格：<span style="color: red;font-weight: bold;">￥{{good.good_price}}</span>
              <GoodShortcut/>
            </p>
            <div>卖家姓名：{{good._nick_name}}</div>
            <div>
              卖家联系方式：{{good.seller_contact}}
              <div style="float: right;">
                <span class="isoft_button_red_small" @click="freeContact(good.good_seller, good._nick_name)">免费咨询</span>
              </div>
            </div>
          </div>

          <slot name="footer"></slot>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {checkEmpty, CheckHasLoginConfirmDialog, checkNotEmpty, strSplit} from "../../tools"
  import GoodShortcut from "./GoodShortcut"
  import TagRender from "../Common/TagRender";

  export default {
    name: "GoodMeta",
    components: {TagRender, GoodShortcut},
    props: {
      good: {
        type: Object,
        default: null,
      },
      showHighlights: {
        type: Boolean,
        default: true,
      }
    },
    methods: {
      freeContact: function (userName, nickName) {
        CheckHasLoginConfirmDialog(this, {path: '/contact/contactList', query: {userName, nickName}});
      },
      getLabel: function () {
        return "链知网，让赚钱变得更简单一些";
      },
      parseTag: function (good_tag) {
        return strSplit(good_tag, "|").filter(tag => !checkEmpty(tag));
      },
      parseGoodImages: function (good_images) {
        return checkNotEmpty(good_images) ? JSON.parse(good_images) : [];
      },
    }
  }
</script>

<style scoped>
  .carousel .carousel_label {
    display: none;
    padding: 2px 0 0 10px;
    background-color: rgba(0, 0, 0, 0.6);
    color: white;
    width: 100%;
    height: 40px;
    line-height: 40px;
    text-align: center;
    position: absolute;
    transition: all ease-in 1s;
  }

  .carousel:hover .carousel_label {
    display: block;
    bottom: 8px;
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

  .isoft_tip_parent {

  }

  .isoft_tip {
    position: absolute;
    padding: 10px;
    border: 1px solid #eee;
    border-radius: 5px;
    background-color: white;
    display: none;
  }

  .isoft_tip_parent:hover > .isoft_tip {
    display: block;
    animation: moveTop 2s infinite;
    animation-iteration-count: 1;
  }

  @keyframes moveTop {
    0% {
      border: 1px solid #cfeec9;
      margin-top: 10px;
    }
    50% {
      border: 1px solid #c0c6ee;
      margin-top: 5px;
    }
    100% {
      border: 1px solid #eee;
      margin-top: 0;
    }
  }

</style>
