<template>
  <div style="margin:15px;background-color: #fff;border: 1px solid #e6e6e6;border-radius: 4px;min-height: 500px;">
    <div v-if="good" style="padding: 50px;">
      <div style="display: flex;">
        <div style="width: 40%;">
          <img :src="good_images[index]" width="100%" height="400px"/>
          <p style="text-align: center;">
            <Icon style="float: left;margin-top: 10px;" size="50" type="ios-arrow-back" @click="showPrefix"/>
            <a v-for="show_index in show_indexs" src="javascript:;" @click="showBigImg(show_index)">
              <img :src="good_images[show_index]" width="70px" height="70px"/>
            </a>
            <Icon style="float: right;margin-top: 10px;" size="50" type="ios-arrow-forward" @click="showNext"/>
          </p>
        </div>
        <div style="width: 60%;">
          <div>
            <p class="isoft_inline_ellipsis isoft_font16">商品名称：<span class="isoft_hover_red">{{good.good_name}}</span>
            </p>
            <p class="p3line isoft_font14">商品描述：<span class="isoft_color_grey3">{{good.good_desc}}</span></p>
            <div>商品价格：<span style="color: red;font-weight: bold;">￥{{good.good_price}}</span></div>
            <div>卖家姓名：{{good.good_seller}}</div>
            <div>卖家联系方式：{{good.seller_contact}}</div>
          </div>
          <div style="text-align: right;">
            <Button>立即购买</Button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {GetGoodDetail} from "../../api"
  import IBeautifulLink from "../Common/link/IBeautifulLink";

  export default {
    name: "BusinessDetail",
    components: {IBeautifulLink},
    data() {
      return {
        good: null,
        good_images: [],   // 商品图片
        show_indexs: [],   // 轮播商品图片索引
        index: 0,          // 当前默认展示第几张图片
      }
    },
    methods: {
      showPrefix: function () {
        alert("暂未实现");
      },
      showNext: function () {
        alert("暂未实现");
      },
      showBigImg: function (index) {
        this.index = index;
      },
      refreshGoodDetail: async function (good_id) {
        const result = await GetGoodDetail(good_id);
        if (result.status == "SUCCESS") {
          this.good = result.good;
          this.good_images = JSON.parse(result.good.good_images);
          for (var i = 0; i < this.good_images.length; i++) {
            if (i < 5) {
              this.show_indexs.push(i);
            }
          }
        }
      }
    },
    mounted() {
      if (this.$route.query.id != undefined && this.$route.query.id != null) {
        this.refreshGoodDetail(this.$route.query.id);
      }
    }
  }
</script>

<style scoped>

</style>
