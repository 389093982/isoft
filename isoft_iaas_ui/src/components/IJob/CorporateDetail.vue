<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <p class="clear">
        <span>公司名称：{{formInline.corporate_name}}</span>
      </p>
      <p class="clear">
        <span>公司主页：{{formInline.corporate_site}}</span>
      </p>
      <p class="clear">
        <span>公司 logo：{{formInline.corporate_logo}}</span>
      </p>
      <p class="clear">
        <span>公司规模：{{formInline.corporate_size}}</span>
      </p>
      <p class="clear">
        <span>招聘类型：{{formInline.job_type}}</span>
      </p>
      <p class="clear">
        <span>详细类型：{{formInline.job_type_detail}}</span>
      </p>
      <p class="clear">
        <span>薪酬范围：{{formInline.salary_range}}</span>
      </p>

      <Button style="position: relative;float: right;right: 10px;bottom: 35px;" @click="$router.push({path:'/job/corporate_edit'})">编辑模式</Button>
    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_top10">
      <p class="clear">
        <span>公司简介：{{formInline.corporate_desc}}</span>
      </p>
      <p class="clear">
        <span>职位简介：{{formInline.job_desc}}</span>
      </p>
    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_top10">
      <p class="clear">
        <span>公司福利：{{formInline.corporate_welfare}}</span>
      </p>

      <p class="clear">
        <span>公司地址：{{formInline.corporate_address}}</span>
      </p>


    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_top10">
      <p>招聘岗位<a style="margin-left: 20px;">新增</a></p>
    </div>
  </div>
</template>

<script>
  import {QueryCorporateDetail} from "../../api"

  export default {
    name: "CorporateDetail",
    data(){
      return {
        formInline: {
          id:-1,
          corporate_name: '',
          corporate_site: '',
          corporate_logo: '',
          corporate_size: '',
          job_type: '',
          job_type_detail: '',
          salary_range: '',
          corporate_desc: '',
          job_desc: '',
          corporate_welfare:'',
          corporate_address: '',
        },
      }
    },
    methods:{
      refreshCorporateDetail:async function () {
        const result = await QueryCorporateDetail();
        if(result.status == "SUCCESS" && result.corporate_detail){
          this.formInline.id = result.corporate_detail.id;
          this.formInline.corporate_name = result.corporate_detail.corporate_name;
          this.formInline.corporate_site = result.corporate_detail.corporate_site;
          this.formInline.corporate_logo = result.corporate_detail.corporate_logo;
          this.formInline.corporate_size = result.corporate_detail.corporate_size;
          this.formInline.job_type = result.corporate_detail.job_type;
          this.formInline.job_type_detail = result.corporate_detail.job_type_detail;
          this.formInline.salary_range = result.corporate_detail.salary_range;
          this.formInline.corporate_desc = result.corporate_detail.corporate_desc;
          this.formInline.job_desc = result.corporate_detail.job_desc;
          this.formInline.corporate_welfare = result.corporate_detail.corporate_welfare;
          this.formInline.corporate_addres = result.corporate_detail.corporate_addres;
        }
      }
    },
    mounted(){
      this.refreshCorporateDetail();
    }
  }
</script>

<style scoped>

</style>
