<template>
  <div>
    <!--<Layout :style="{minHeight: '550px'}" style="background: linear-gradient(to right, rgba(0,7,255,0.04), rgba(255,0,249,0.04));">-->
    <Layout :style="{minHeight: '550px'}" style="background-color: #e5e5e5">
      <div class="headerBox">
        <Header :class="{fixedHeaderCls: fixedHeader}"></Header>
      </div>
      <Content
        :style="{margin: '0px 0px 18px 0px', paddingTop: '3px', minHeight: '550px', lineHeight: '24px', fontSize: '14px'}">
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
    data(){
      return {
        // 是否固定头部
        fixedHeader: false,
      }
    },
    methods:{
      scrollEvent: function () {
        var _this = this;
        window.addEventListener('scroll', function () {
          // 为了保证兼容性，这里取两个值，哪个有值取哪一个
          // scrollTop就是触发滚轮事件时滚轮的高度
          var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
          _this.fixedHeader = scrollTop > 60;
          console.log(_this.fixedHeader);
        });
      }
    },
    mounted(){
      this.scrollEvent();
    }
  }
</script>

<style scoped>
  .headerBox {
    height: 62px;
  }
  .fixedHeaderCls {
    position: fixed;
    z-index: 100;
    top: 0;
    animation: moveFixedHeader 0.2s linear;
  }
  @keyframes moveFixedHeader {
    0% {
      top: -62px;
    }
    100%{
      top: 0;
    }
  }
</style>
