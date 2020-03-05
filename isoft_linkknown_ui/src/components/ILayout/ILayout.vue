<template>
  <div>
    <!--<Layout :style="{minHeight: '550px'}" style="background: linear-gradient(to right, rgba(0,7,255,0.04), rgba(255,0,249,0.04));">-->
    <Layout :style="{minHeight: '550px'}" style="background-color: #e5e5e5">
      <Affix :offset-top="0">
        <Header></Header>
      </Affix>
      <Content
        :style="{margin: '0px 0px 18px 0px', paddingTop: '5px', minHeight: '550px', lineHeight: '24px', fontSize: '14px'}">
        <router-view/>
      </Content>
      <Footer></Footer>
    </Layout>

    <RightSuspensionMenu/>
  </div>
</template>

<script>

  import Header from '../Header/Header'
  import Footer from '../Footer/Footer'
  import RightSuspensionMenu from "./RightSuspensionMenu"

  export default {
    name: 'ILayout',
    components: {
      RightSuspensionMenu,
      Header,
      Footer
    },
  }
</script>

<style scoped>

</style>
