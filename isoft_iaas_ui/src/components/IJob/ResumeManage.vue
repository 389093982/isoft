<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <IBeautifulLink>简历管理</IBeautifulLink>
      <IBeautifulLink>个人特色</IBeautifulLink>
      <IBeautifulLink>简历下载</IBeautifulLink>
      <IBeautifulLink style="float: right;" @onclick="$router.push({path:'/job/resume_edit'})">编辑简历</IBeautifulLink>
    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_top10">
      <p>头像</p>
      <p>性别</p>
      <p>参加工作时间</p>
      <p>联系方式：QQ 微信xxx </p>
      <p>邮箱xxx</p>
      <p>出生年月</p>
      <p>个人技能</p>
      <p>项目经验</p>
      <p>其它优势</p>
      <p>学历</p>
      <p>就业状态</p>
      <p>毕业学校</p>
      <p>期望薪资</p>
      <p>工作地区</p>
      <p>目前状况</p>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ResumeManage"
  }
</script>

<style scoped>

</style>
