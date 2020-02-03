<template>
  <div style="margin:15px;background-color: #fff;border: 1px solid #e6e6e6;border-radius: 4px;min-height: 500px;">
    <!-- 内外边距：上右下左 -->
    <Row style="padding: 15px 10px 10px 25px;">
      <Col span="2">
        <IBeautifulLink @onclick="$router.push({path:'/igood/good_list'})">热销商品</IBeautifulLink>
      </Col>
      <Col span="3">
        <IBeautifulLink @onclick="$router.push({path:'/igood/mine/good_list',query:{type:'mine'}})">我的店铺商品
        </IBeautifulLink>
      </Col>
      <Col span="2">
        <IBeautifulLink @onclick="$router.push({path:'/igood/mine/good_edit'})">发布商品</IBeautifulLink>
      </Col>
    </Row>

    <div style="margin: 0 20px;">
      <Row :gutter="10">
        <Col span="12" v-for="good in goods" class="isoft_top10 isoft_pd10">
          <Row style="margin-bottom: 10px;">
            <Col span="10" style="text-align: center;">
              <router-link :to="{path:'/igood/good_detail',query:{id:good.id}}">
                <!-- 长度大于 2 排除空数组 [] -->
                <img v-if="good.good_images.length > 2" :src="good.good_images | filterFirst" width="160px"
                     height="180px"/>
                <img v-else src="../../assets/default.png" width="180px" height="180px"/>
              </router-link>
            </Col>
            <Col span="14" style="height: 200px;">
              <div style="width: 100%;word-wrap:break-word;word-break:break-all;overflow: hidden;">
                <p class="p1line">商品名称：{{good.good_name}}</p>
                <p class="p3line isoft_font12">商品描述：{{good.good_desc}}</p>
                <p>商品价格：<span style="color: red;font-weight: bold;">￥{{good.good_price}}</span></p>
                <p>卖家姓名：{{good.good_seller}}</p>
                <p>卖家联系方式：{{good.seller_contact}}</p>
              </div>
              <div style="position: absolute;right: 0;bottom: 0;">
                <Button v-if="editable(good)" @click="$router.push({path:'/igood/mine/good_edit',query:{id:good.id}})">
                  编辑商品
                </Button>
                <span v-else>
                  <Button size="small" @click="payConfirm(good)">立即购买</Button>
                </span>
              </div>
            </Col>
          </Row>
        </Col>
      </Row>
    </div>
  </div>
</template>

<script>
  import IBeautifulLink from "../Common/link/IBeautifulLink";
  import {GoodList, NewOrder} from "../../api"
  import {CheckHasLogin, GetLoginUserName} from "../../tools"

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
      payConfirm: async function (good) {
        const result = await NewOrder(good.id);
        if (result.status == "SUCCESS") {
          this.$router.push({path: '/igood/pay_confirm', query: {"good_id": good.id, "orderCode": result.orderCode}});
        }
      },
      refreshGoodList: async function () {
        const result = await GoodList();
        if (result.status == "SUCCESS") {
          this.goods = result.goods;
        }
      },
      editable: function (good) {
        return this.$route.query.type == 'mine' && CheckHasLogin() && GetLoginUserName() == good.good_seller;
      }
    },
    filters: {
      filterFirst: function (good_images) {
        let arr = JSON.parse(good_images);
        return arr[0];
      }
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
