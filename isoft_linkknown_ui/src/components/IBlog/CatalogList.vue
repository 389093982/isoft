<template>
  <div>
    <div class="isoft_font_header isoft_border_bottom">我的博客分类</div>
    <span v-if="hasLogin">
        <BlogCatalogEdit v-if="showBlogCatalogEdit" @handleSuccess="handleSuccess"/>
        <SepLine v-if="showBlogCatalogEdit" style="margin: 10px 0 30px 0;"/>
        <Row>
          <Col span="17">
            我的博客分类
            <span v-if="showAddBoder" class="catalogOperate" @click="showBlogCatalogEdit = !showBlogCatalogEdit">
              <span v-if="!showBlogCatalogEdit"><Icon type="md-add"/>添加分类</span>
              <span v-else><Icon type="ios-remove" />收起添加</span>
            </span>
          </Col>
          <Col span="7">创建时间</Col>
        </Row>
        <Row v-for="(catalog,index) in catalogs" style="line-height: 28px;height: 28px;">
          <Col span="17" class="isoft_inline_ellipsis">{{ catalog.catalog_name | filterLimitFunc(10)}}</Col>
          <Col span="7" style="font-size: 12px;"><Time :time="catalog.created_time" type="date"/></Col>
        </Row>
      </span>
    <span v-else>
      <ForwardLogin/>
    </span>
  </div>
</template>

<script>
  import {GetMyCatalogs} from "../../api"
  import BlogCatalogEdit from "./BlogCatalogEdit"
  import {CheckHasLogin} from "../../tools"
  import IBeautifulCard from "../../components/Common/card/IBeautifulCard"
  import ForwardLogin from "../SSO/ForwardLogin"
  import SepLine from "../Common/SepLine";

  export default {
    props:{
      showAddBoder:{
        type:Boolean,
        default:true,
      }
    },
    name: "CatalogList",
    components: {SepLine, ForwardLogin, IBeautifulCard, BlogCatalogEdit},
    data() {
      return {
        showBlogCatalogEdit: false,
        catalogs: [],
      }
    },
    methods: {
      handleSuccess: function () {
        this.showBlogCatalogEdit = false;
        this.refreshMyCatalogList();
      },
      refreshMyCatalogList: async function () {
        const result = await GetMyCatalogs();
        if (result.status == "SUCCESS") {
          this.catalogs = result.catalogs;
        }
      },
    },
    mounted() {
      if (CheckHasLogin()) {
        this.refreshMyCatalogList();
      }
    },
    computed: {
      hasLogin: function () {
        return CheckHasLogin();
      }
    },
    filters: {
      // 内容超长则显示部分
      filterLimitFunc:function (value,limitLenth) {
        if (value.length > limitLenth) {
          return value.slice(0,limitLenth) + ' · · ·'
        }else {
          return value
        }
      }
    },
  }
</script>

<style scoped>
  .catalogOperate {
    font-size: 12px;
    line-height: 20px;
    padding: 5px 10px;
    cursor: pointer;
    color: white;
    background-color: rgba(255, 138, 0, 0.67);
  }
  .catalogOperate:hover {
    background-color: darkorange;
  }
</style>
