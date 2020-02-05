<template>
  <div>

    <!--左页眉、右页眉-->
    <head-line></head-line>

    <!--logo和搜索 浮动-->
    <Affix :offset-top="0">
      <logo-and-search></logo-and-search>
    </Affix>

    <!--横向菜单栏-->
    <pink-menu-title></pink-menu-title>

    <!--左/中/右 router-view -->
    <left-middle-right></left-middle-right>

    <!--其他公共内容-->
    <middle-pub-content></middle-pub-content>

    <!--页脚-->
    <page-footer></page-footer>

    <!--反回顶端-->
    <back-top></back-top>
  </div>
</template>

<script>
  import LogoAndSearch from "./logoAndSearch/logoAndSearch";
  import HeadLine from "./headLine/headLine";
  import PinkMenuTitle from "./pinkMenuTitle/pinkMenuTitle";
  import LeftContentShow from "./leftContentShow/leftContentShow";
  import RightContentShow from "./rightContentShow/rightContentShow";
  import PageFooter from "./pageFooter/pageFooter";
  import MiddlePubContent from "./middlePubContent/middlePubContent";
  import BackTop from "./backTop/backTop";
  import LeftMiddleRight from "./leftMiddleRight/leftMiddleRight";
  export default {
    name: "indexLayout",
    components: {
      LeftMiddleRight,
      BackTop,
      MiddlePubContent, PageFooter, RightContentShow, LeftContentShow, PinkMenuTitle, HeadLine, LogoAndSearch,},
  }
</script>

<style scoped>

</style>
