<template>
  <div style="margin: 20px 2% 0 2% ;height: 400px;">

    <!--左侧悬浮-->
    <div id="leftContentId">
      <Affix :offset-top="120">
        <left-content-show></left-content-show>
      </Affix>
    </div>

    <!--中间router-view-->
    <div id="centerContentId">
      <router-view></router-view>
    </div>

    <!--右边悬浮-->
    <div id="rightContentId">
      <Affix :offset-top="120">
        <right-content-show></right-content-show>
      </Affix>
    </div>

    <!--clear: both-->
    <div style="clear: both"></div>

  </div>
</template>

<script>
	import LeftContentShow from "../leftContentShow/leftContentShow";
  import RightContentShow from "../rightContentShow/rightContentShow";
  export default {
		name: "leftMiddleRight",
    components: {RightContentShow, LeftContentShow}
  }
</script>

<style scoped>
  #leftContentId{
    width: 10%;float: left;
  }
  #centerContentId{
    width: 78%;float: left;background-color: rgba(255, 81, 142, 0.06);margin: 0 1% 0 1% ;
  }
  #rightContentId{
    width: 10%;float: right;
  }
</style>
