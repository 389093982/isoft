<template>
  <div>
    <div class="isoft_bg_white isoft_pd20">
      <Row>
        <Col span="4">
          <div class="bookImg">
            <a>
              <img v-if="formInline.corporate_logo" :src="formInline.corporate_logo" height="160px" width="180px"/>
              <img v-else src="../../assets/default.png" height="160px" width="140px"/>
            </a>
          </div>
        </Col>
        <Col span="20">
          <p>
            <span>公司名称：{{formInline.corporate_name}}</span>
          </p>
          <p>
            <span>公司主页：
              <a :href="formInline.corporate_site" target="_blank">{{formInline.corporate_site}}</a>
            </span>
          </p>
          <p>
            <span>公司规模：{{formInline.corporate_size}}</span>
          </p>
          <p>
            <span>招聘类型：
              <Tag v-for="(jobTypeTag, index) in jobTypeTags">{{jobTypeTag}}</Tag>
            </span>
          </p>
          <p>
            <span>详细类型：
              <Tag v-for="(jobTypeDetail, index) in jobTypeDetails">{{jobTypeDetail}}</Tag>
            </span>
          </p>
          <p>
            <span>薪酬范围：
               <Tag v-for="(salaryRange, index) in salaryRanges">{{salaryRange}}</Tag>
            </span>
          </p>
        </Col>
      </Row>
      <Button style="position: relative;float: right;right: 10px;bottom: 35px;"
              v-if="editable == 'true'" @click="$router.push({path:'/job/corporate_edit'})">前去编辑
      </Button>
    </div>

    <div class="isoft_bg_white isoft_pd20 isoft_top10">
      <div class="isoft_pd10" style="border-top: 2px solid rgba(255,8,0,0.3);">
        公司简介：{{formInline.corporate_desc}}
      </div>
      <div class="isoft_pd10" style="border-top: 2px solid rgba(255,8,0,0.3);">
        职位简介：{{formInline.job_desc}}
      </div>
    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_top10">
      <div>
        公司福利：{{formInline.corporate_welfare}}
      </div>
      <div>
        公司地址：{{formInline.corporate_address}}
      </div>
    </div>

    <div class="isoft_bg_white isoft_pd20 isoft_top10">
      <p style="border-bottom: 1px solid #f0f0f0;">招聘岗位</p>
      <div v-if="jobDetails.length > 0">
        <Row style="padding: 15px 0px;border-bottom: 1px solid #f0f0f0;">
          <Col span="4">工作名称</Col>
          <Col span="4">工作年限</Col>
          <Col span="6">工作地点</Col>
          <Col span="4">薪酬范围</Col>
          <Col span="4">操作</Col>
        </Row>
        <Row v-for="(jobDetail,index) in jobDetails" style="padding: 15px 0px;border-bottom: 1px solid #f0f0f0;">
          <Col span="4" style="font-size: 16px;color: #656565;">{{jobDetail.job_name}}</Col>
          <Col span="4" style="font-size: 16px;color: #656565;">{{jobDetail.job_age}}</Col>
          <Col span="6">{{jobDetail.job_address}}</Col>
          <Col span="4">
            <span style="font-size: 16px;color: #393;">{{jobDetail.salary_range}}</span>
          </Col>
          <Col span="4">
            <span v-if="editable == 'true'">
              <Button size="small"
                      @click="$router.push({path:'/job/job_edit', query: {job_id: jobDetail.id}})">编辑</Button>
              <Button size="small" @click="$router.push({path:'/job/job_edit', query: {corporate_id: formInline.id}})">新增</Button>
            </span>
          </Col>
        </Row>

        <div class="isoft_top10" style="text-align: center;">
          <IBeautifulLink>查看更多职位</IBeautifulLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {QueryCorporateDetail} from "../../api"
  import {checkEmpty, strSplit} from "../../tools";

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
        jobDetails:[],
        editable: 'false',
      }
    },
    methods:{
      refreshCorporateDetail:async function () {
        let id = this.$route.query.corporate_id ? this.$route.query.corporate_id : -1;
        const result = await QueryCorporateDetail({'id': id});
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

          this.jobDetails = result.job_details;
          this.editable = result.editable;
        }
      },
      getSplitArray(str, defaultVal){
        if(!checkEmpty(str)){
          return strSplit(str, ",");
        }else{
          return [defaultVal];
        }
      }
    },
    mounted(){
      this.refreshCorporateDetail();
    },
    computed:{
      jobTypeTags:function () {
        return this.getSplitArray(this.formInline.job_type, '暂无分类');
      },
      jobTypeDetails:function () {
        return this.getSplitArray(this.formInline.job_type_detail, '暂无详细分类');
      },
      salaryRanges:function () {
        return this.getSplitArray(this.formInline.salary_range, '暂无范围');
      },
    }
  }
</script>

<style scoped>

</style>
