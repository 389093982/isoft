<template>
  <div>
    <Layout :style="{minHeight: '550px'}">
      <Header>
        <Header/>
      </Header>
      <Layout>
        <Sider hide-trigger :style="{background: '#fff'}">
          <Menu active-name="1-1" theme="light" width="auto" :open-names="['1']">
            <Submenu name="2">
              <template slot="title">
                <Icon type="ios-barcode"></Icon>
                广告位管理
              </template>
            </Submenu>
            <Submenu name="3">
              <template slot="title">
                <Icon type="ios-barcode"></Icon>
                评论/建议管理
              </template>
              <MenuItem name="3-1">
                <router-link to="/background/adviseList">意见和吐槽列表</router-link>
              </MenuItem>
            </Submenu>
            <Submenu name="4">
              <template slot="title">
                <Icon type="ios-barcode"></Icon>
                流量计费统计
              </template>
              <MenuItem name="4-1">
                <router-link to="#">外部链接跳转上报信息统计</router-link>
              </MenuItem>
            </Submenu>
            <Submenu name="5">
              <template slot="title">
                <Icon type="ios-barcode"></Icon>
                用户管理
              </template>
              <MenuItem name="5-1">
                <router-link to="/background/sso/loginRecord">登录记录</router-link>
              </MenuItem>
              <MenuItem name="5-2">
                <router-link to="/background/userSearch">用户查询</router-link>
              </MenuItem>
              <MenuItem name="5-3">权限管理</MenuItem>
            </Submenu>
            <Submenu name="6">
              <template slot="title">
                <Icon type="ios-barcode"></Icon>
                内容发布
              </template>
              <MenuItem name="6-1">
                <router-link to="/background/placementList">占位符管理</router-link>
              </MenuItem>
            </Submenu>
            <Submenu name="7">
              <template slot="title">
                <Icon type="ios-barcode"></Icon>
                活动与优惠
              </template>
              <MenuItem name="7-1">
                <router-link to="/background/createActivity">举办活动</router-link>
              </MenuItem>
              <MenuItem name="7-2">
                <router-link to="/background/searchActivity">活动查询</router-link>
              </MenuItem>
              <MenuItem name="7-3">
                <router-link to="/background/searchCoupon">优惠券管理</router-link>
              </MenuItem>
            </Submenu>
            <Submenu name="8">
              <template slot="title">
                <Icon type="ios-barcode"></Icon>
                风险控制
              </template>
              <MenuItem name="8-1">
                <router-link to="/background/fileUploadMonitor">文件上传监控</router-link>
              </MenuItem>
            </Submenu>
          </Menu>
        </Sider>
        <Content
          :style="{padding: '18px', minHeight: '550px', background: '#f5f6f7', lineHeight: '24px', fontSize: '14px'}">
          <router-view/>
        </Content>
      </Layout>
      <Footer>
        <Footer/>
      </Footer>
    </Layout>
  </div>
</template>

<script>
  import Header from '../Header/Header'

  export default {
    name: "ICMSLayout",
    components: {Header},
  }
</script>

<style scoped>

</style>
