<template>
	<div>

    <!--第一行-->
    <div style="float: left;width:100%;height:800px;">
      <!--左边内容-->
      <div style="float: left;width: 80%;background-color: wheat">
        <div id="basicInformation">
          基础信息
        </div>
        <div id="personalizedSignature">
          个性签名
        </div>
        <div id="publicAlbum">
          公开相册
        </div>
        <div id="professionalSkills">
          专业技能
        </div>
        <div id="habitsAndCustoms">
          生活习惯
        </div>
        <div id="dynamics">
          Ta的动态
        </div>
        <div id="attention">
          Ta的关注
        </div>
        <div id="fans">
          Ta的粉丝
        </div>
      </div>

      <!--右边锚点-->
      <div style="float: right;width: 15%;">
        <Anchor show-ink offset-top="100">
          <AnchorLink href="#basicInformation" title="基础信息" />
          <AnchorLink href="#personalizedSignature" title="个性签名" />
          <AnchorLink href="#publicAlbum" title="公开相册" />
          <AnchorLink href="#professionalSkills" title="专业技能" />
          <AnchorLink href="#habitsAndCustoms" title="生活习惯" />
          <AnchorLink href="#dynamics " title="Ta的动态" />
          <AnchorLink href="#attention" title="Ta的关注" />
          <AnchorLink href="#fans" title="Ta的粉丝" />
        </Anchor>
      </div>
    </div>
    <div style="clear: both"></div>

  </div>
</template>

<script>
	export default {
		name: "usercentralIndex"
	}
</script>

<style scoped>

</style>
