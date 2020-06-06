<template>
  <div style="width: 100%;">

    <div style="display: flex">
      <div style="width: 68%;background-color: white">
        <Row>
          <!--左侧空出一点-->
          <Col span="4">
            &nbsp;
          </Col>
          <!--右侧展示商品-->
          <Col span="20">
            <div style="position: relative;top: 10px">
              <span class="isoft_tag5 isoft_font12 isoft_mr10 isoft_point_cursor">全部</span>
              <span style="margin-left: 20px">数量:{{page.totalCount}}</span>
            </div>
            <!--一行一个商品-->
            <Row v-for="(goods, index) in goodsData" style="margin-top: 20px">
              <Row>
                <!--商品图片-->
                <Col span="7">
                  <!--图片-->
                  <div @click="$router.push({path:'/ilearning/courseDetail',query:{course_id:goods.goods_id}})" style="cursor: pointer">
                    <img v-if="goods.small_image" :src="goods.small_image" width="180" height="120"/>
                    <img v-else src="../../../static/images/common_img/default.png" width="180" height="120"/>
                    <div class="ico_play"></div>
                  </div>
                </Col>
                <!--商品信息-->
                <Col span="10">
                  <Row v-if="goods.goods_type==='course'">
                    <div style="margin-top: 10px">课程</div>
                  </Row>
                  <Row style="margin-top: 10px">
                    {{goods.course_name}}
                  </Row>
                  <Row style="margin-top: 10px">
                    <div style="display: flex">
                      <div class="shoppingTipService" @click="$router.push({path:'/payment/pay',name:'pay',params:{type:goods.goods_type,id:goods.goods_id}})" style="margin-left: 10px">前去付款</div>
                      <div class="shoppingTipService" @click="$router.push({path:'/contact/contactList'})" style="margin-left: 10px">联系客服</div>
                      <div class="deleteTipService" @click="deleteFromShoppingCart(goods.goods_type,goods.goods_id)" style="margin-left: 10px">删除</div>
                    </div>
                  </Row>
                </Col>
                <!--支付金额-->
                <Col span="6" style="color: #ff6900;">
                  <Row>
                    <div style="margin-top: 20px">
                      <Icon type="logo-yen" style="font-size: 12px"/>
                      <span style="font-size: 20px">{{goods.price}}</span>
                    </div>
                    <Row v-if="goods.goods_price_on_add < goods.price">
                      <div style="color: #cc2517;font-size: 12px">
                        比加入时降 {{goods.price - goods.goods_price_on_add}} 元
                      </div>
                    </Row>
                    <Row style="margin-top: 5px">
                      <code style="color: grey">{{formatAddTime(goods.add_time)}}</code>
                    </Row>
                  </Row>
                </Col>
                <Col span="1">
                  <div style="width: 0;height: 0">
                    <img style="position: relative;top: -20px;left: -100px;z-index:0" src="../../../static/images/vipCenter/shopping_cart.png" height="80" width="100"/>
                  </div>
                </Col>
              </Row>
              <!--彩色分底线-->
              <SepLine></SepLine>
            </Row>

            <!--分页-->
            <div style="text-align: center;margin-top: 10px;margin-bottom: 10px">
              <Page :total="page.totalCount" :page-size="page.offset" :current="page.currentPage" show-total show-sizer @on-change="pageChange" @on-page-size-change="pageSizeChange"/>
            </div>

          </Col>
        </Row>
      </div>
      <div style="width: 27.5%;margin-left: 5px;background-color: white">
        <div>
          <img src="../../../static/images/common_img/linkknown_to_lovely_you.jpg" height="590" width=100%/>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
  import {queryPayShoppingCartList,deleteFromShoppingCart} from "../../api/index"
  import {formatUTCtime} from "../../tools/index"
  import SepLine from "../Common/SepLine";

  export default {
    name: "ShoppingCart",
    components:{SepLine},
    data () {
      return {
        goodsData: [],
        page:{totalCount:0,currentPage:1,offset:10},
      }
    },
    methods: {
      refreshShoppingCart:async function(){
        let params = {
          'current_page':this.page.currentPage,
          'offset':this.page.offset,
        };
        const result = await queryPayShoppingCartList(params);
        if (result.status === 'SUCCESS') {
          this.goodsData = result.goodsData;
          this.page.totalCount = result.paginator.totalcount;
        }

      },
      deleteFromShoppingCart:async function(goods_type,goods_id){
        let params = {
          'goods_type':goods_type,
          'goods_id':goods_id,
        };
        const result = await deleteFromShoppingCart(params);
        if (result.status === 'SUCCESS') {
          this.$Message.success("删除成功");
          this.refreshShoppingCart();
        }
      },
      pageChange:function (page) {
        this.page.currentPage = page;
        this.refreshShoppingCart()
      },
      pageSizeChange:function (pageSize) {
        this.page.offset = pageSize;
        this.refreshShoppingCart()
      },
      formatAddTime:function (addTime) {
        return formatUTCtime(addTime,'yyyy-MM-dd HH:mm:ss')
      }
    },
    mounted:function () {
      this.refreshShoppingCart();
    }
  }
</script>

<style scoped>
  .ico_play {
    background: url(../../../static/images/common_img/ico_play.png) no-repeat;
    position: absolute;
    top: 35px;
    left: 55px;
    width: 60px;
    height: 60px;
  }
  .shoppingTipService{
    cursor: pointer;
    padding: 2px 0 0 8px ;
    height: 30px;
    width: 80px;
    background-color: rgba(220, 220, 220, 0.39);
    border-radius: 20px;
    border: 2px orange solid;
  }
  .shoppingTipService:hover{
    color: rgba(255, 105, 0, 0.65);
    background-color: rgba(214, 214, 214, 0.99);
  }

  .deleteTipService{
    cursor: pointer;
    padding: 2px 0 0 8px ;
    height: 30px;
    width: 48px;
    background-color: rgba(220, 220, 220, 0.39);
    border-radius: 20px;
    border: 2px orange solid;
  }
  .deleteTipService:hover{
    color: rgba(255, 105, 0, 0.65);
    background-color: rgba(214, 214, 214, 0.99);
  }

</style>
