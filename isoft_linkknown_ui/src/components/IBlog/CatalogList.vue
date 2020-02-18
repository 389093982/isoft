<template>
  <IBeautifulCard title="我的博客分类">
    <div slot="content" style="padding: 10px;">
      <span v-if="hasLogin">
        <BlogCatalogEdit v-if="showBlogCatalogEdit" @handleSuccess="handleSuccess"/>
        <Row>
          <Col span="18">我的博客分类
            <span style="cursor: pointer;color: red;" @click="showBlogCatalogEdit = !showBlogCatalogEdit"><Icon
              type="md-add"/>添加</span>
          </Col>
          <Col span="6">创建时间</Col>
        </Row>
        <Row v-for="(catalog,index) in catalogs" style="line-height: 28px;height: 28px;">
          <Col span="18" class="isoft_inline_ellipsis">{{ catalog.catalog_name }}</Col>
          <Col span="6" style="font-size: 12px;"><Time :time="catalog.created_time" type="date"/></Col>
        </Row>
      </span>
      <span v-else>
        <ForwardLogin/>
      </span>
    </div>
  </IBeautifulCard>
</template>

<script>
  import {GetMyCatalogs} from "../../api"
  import BlogCatalogEdit from "./BlogCatalogEdit"
  import {CheckHasLogin} from "../../tools"
  import IBeautifulCard from "../../components/Common/card/IBeautifulCard"
  import ForwardLogin from "../SSO/ForwardLogin"

  export default {
    name: "CatalogList",
    components: {ForwardLogin, IBeautifulCard, BlogCatalogEdit},
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
    }
  }
</script>

<style scoped>

</style>
