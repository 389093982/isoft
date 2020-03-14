<template>
  <div>
    <div class="isoft_bg_white" style="padding: 10px 0 0 10px;text-align: center;">
      <div class="header" style="cursor:pointer;display: inline-block;"
           @click="section = 'section1'" :style="{color: section === 'section1' ? 'red':''}">公司主页
      </div>
      <div class="header" style="cursor:pointer;display: inline-block;"
           @click="section = 'section2'" :style="{color: section === 'section2' ? 'red':''}">招聘岗位
      </div>

      <div class="header" style="cursor:pointer;display: inline-block;"
           @click="$router.push({path:'/job/applyList'})">投递清单
      </div>
    </div>

    <!-- 公司主页 -->
    <div id="section1" v-if="section === 'section1'" class="isoft_top5">
      <!-- 有公司信息 -->
      <CorporateInfo v-if="corporateInfo && corporateInfo.corporate_name" :corporate-info="corporateInfo"/>
      <!-- 未填写公司主页 -->
      <div v-else class="isoft_bg_white isoft_pd20" style="min-height: 400px;">
        <Spin size="large" fix v-if="isLoading">
          <div class="isoft_loading"></div>
        </Spin>
        <div v-else class="isoft_button_green1" style="width: 500px;margin: 0 auto;"
             @click="$router.push({path:'/job/corporateEdit'})">您还未创建公司信息，前去创建
        </div>
      </div>
    </div>

    <div id="section2" v-if="section === 'section2'" class="isoft_bg_white isoft_pd20 isoft_top5"
         style="min-height: 400px;">
      <div v-if="showJobDetails.length > 0">
        <div v-for="(jobDetail,index) in showJobDetails">
          <JobItem :job-detail="jobDetail"/>
        </div>

        <div class="isoft_top10" style="text-align: center;">
          <IBeautifulLink v-if="showJobDetails.length < allJobDetails.length" @onclick="showMore">查看更多职位
          </IBeautifulLink>
        </div>
      </div>
      <div v-else>
        <Spin size="large" fix v-if="isLoading">
          <div class="isoft_loading"></div>
        </Spin>
        <span v-else>
          <div v-if="corporateInfo && corporateInfo.corporate_name">
            <div class="isoft_button_green1" style="width: 500px;margin: 0 auto;"
                 @click="$router.push({path:'/job/jobEdit', query: {corporate_id: corporateInfo.id}})">您还未创建岗位信息，前去创建</div>
          </div>
          <div v-else>
            <div class="isoft_button_green1" style="width: 500px;margin: 0 auto;"
                 @click="$router.push({path:'/job/corporateEdit'})">您还未创建公司信息，前去创建</div>
          </div>
        </span>
      </div>
    </div>
  </div>
</template>

<script>
  import {QueryCorporateDetail} from "../../api"
  import {checkEmpty, GetLoginUserName} from "../../tools";
  import JobItem from "./JobItem";
  import CorporateInfo from "./CorporateInfo";

  export default {
    name: "CorporateDetail",
    components: {CorporateInfo, JobItem},
    data() {
      return {
        isLoading: true,
        section: 'section1',
        corporateInfo: {
          id: -1,
          corporate_name: '',
          corporate_site: '',
          corporate_logo: '',
          corporate_size: '',
          job_type: '',
          job_type_detail: '',
          salary_range: '',
          corporate_desc: '',
          job_desc: '',
          corporate_welfare: '',
          corporate_address: '',
        },
        allJobDetails: [],
        showJobDetails: [],
      }
    },
    methods: {
      showMore: function () {
        this.showJobDetails = this.allJobDetails.slice(0, this.showJobDetails.length + 5);
      },
      refreshCorporateDetail: async function () {
        this.isLoading = true;
        try {
          let params = {};
          if (this.$route.query.corporate_id && this.$route.query.corporate_id > 0) {
            // 根据公司 id 去查
            params["id"] = this.$route.query.corporate_id;
          } else if (!checkEmpty(GetLoginUserName())) {
            // 根据当前登录用户名去查
            params["user_name"] = GetLoginUserName();
          } else {
            // 否则不查
            return;
          }
          const result = await QueryCorporateDetail(params);
          if (result.status === "SUCCESS" && result.corporate_detail) {
            this.corporateInfo = result.corporate_detail;
            this.allJobDetails = result.job_details;
            this.showJobDetails = this.allJobDetails.slice(0, this.showJobDetails.length + 5);
          }
        } finally {
          this.isLoading = false;
        }

      },
    },
    mounted() {
      // 设置默认切换的标签页
      this.section = this.$route.query.loc && this.$route.query.loc === 2 ? "section2" : "section1";
      this.refreshCorporateDetail();
    },
  }
</script>

<style scoped>
  .header {
    margin: 0 10px 10px 10px;
    position: relative;
    height: 40px;
    color: #111;
    font-size: 20px;
    font-weight: 400;
    line-height: 40px;
    white-space: nowrap;
  }
</style>
