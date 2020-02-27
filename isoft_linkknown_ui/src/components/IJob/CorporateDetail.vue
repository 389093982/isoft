<template>
  <div>
    <div class="isoft_bg_white" style="padding: 10px 0 0 10px;">
      <div class="header" style="cursor:pointer;display: inline-block;"
           @click="section = 'section1'" :style="{color: section === 'section1' ? 'red':''}">公司主页
      </div>
      <div class="header" style="cursor:pointer;display: inline-block;"
           @click="section = 'section2'" :style="{color: section === 'section2' ? 'red':''}">招聘岗位
      </div>

      <div class="header" style="cursor:pointer;display: inline-block;"
           @click="$router.push({path:'/job/apply_list'})">投递清单
      </div>
    </div>

    <!-- 公司主页 -->
    <div id="section1" v-if="section === 'section1'" class="isoft_top5">
      <!-- 有公司信息 -->
      <div v-if="corporateInfo && corporateInfo.corporate_name">
        <Row class="isoft_bg_white isoft_pd10">
          <Col span="5">
            <div class="bookImg">
              <a>
                <img v-if="corporateInfo.corporate_logo" :src="corporateInfo.corporate_logo" height="160px"
                     width="180px"/>
                <img v-else src="../../assets/default.png" height="160px" width="140px"/>
              </a>
            </div>
          </Col>
          <Col span="19">
            <p>
              <span><span class="isoft_color_grey">公司名称：</span><span class="hovered hvr-grow isoft_hover_red"
                                                                     @click="goToTargetLink(corporateInfo.corporate_site)">{{corporateInfo.corporate_name}}</span></span>
            </p>
            <p>
              <span><span class="isoft_color_grey">公司规模：</span>{{corporateInfo.corporate_size}}</span>
            </p>
            <p>
            <span><span class="isoft_color_grey">招聘类型：</span>
              <Tag v-for="(jobTypeTag, index) in jobTypeTags">{{jobTypeTag}}</Tag>
            </span>
            </p>
            <p>
            <span><span class="isoft_color_grey">详细类型：</span>
              <Tag v-for="(jobTypeDetail, index) in jobTypeDetails">{{jobTypeDetail}}</Tag>
            </span>
            </p>
            <p>
            <span><span class="isoft_color_grey">薪酬范围：</span>
               <Tag v-for="(salaryRange, index) in salaryRanges">{{salaryRange}}</Tag>
            </span>
            </p>
            <p><span class="isoft_color_grey">公司地址:</span>
              {{corporateInfo.corporate_address}}
            </p>
          </Col>
          <Button style="position: relative;float: right;right: 10px;bottom: 35px;"
                  v-if="isLoginUserName(corporateInfo.created_by)" @click="$router.push({path:'/job/corporate_edit'})">
            前去编辑
          </Button>
        </Row>

        <div class="isoft_bg_white isoft_pd20 isoft_top5">
          <Row :gutter="10">
            <Col span="16">
              <div class="isoft_pd10 fadeToRight" style="background-color: #fff;">
                <div class="header" style="text-align: center;color: #00c2b3;">公司简介</div>
                {{corporateInfo.corporate_desc}}
              </div>
              <div class="isoft_pd10 fadeToTop" style="background-color: #fff;">
                <div class="header" style="text-align: center;color: #00c2b3;">公司福利</div>
                {{corporateInfo.corporate_welfare}}
              </div>
            </Col>
            <Col span="8">
              <div class="isoft_pd10 fadeToLeft" style="background-color: #fff;">
                <div class="header" style="text-align: center;color: #00c2b3;">职位简介</div>
                {{corporateInfo.job_desc}}
              </div>
            </Col>
          </Row>
        </div>
      </div>
      <!-- 未填写公司主页 -->
      <div v-else class="isoft_bg_white isoft_pd10">
        您还未发布公司信息，
        <Button @click="$router.push({path:'/job/corporate_edit'})">前去填写</Button>
      </div>
    </div>

    <div id="section2" v-if="section === 'section2'" class="isoft_bg_white isoft_pd20 isoft_top5">
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
        您还创建岗位信息，
        <Button size="small" @click="$router.push({path:'/job/job_edit', query: {corporate_id: corporateInfo.id}})">
          前去创建
        </Button>
      </div>
    </div>
  </div>
</template>

<script>
  import {QueryCorporateDetail} from "../../api"
  import {checkEmpty, GetLoginUserName, goToTargetLink, strSplit} from "../../tools";
  import JobItem from "./JobItem";

  export default {
    name: "CorporateDetail",
    components: {JobItem},
    data() {
      return {
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
      isLoginUserName: function (user_name) {
        return user_name === GetLoginUserName();
      },
      goToTargetLink: function (url) {
        goToTargetLink(url);
      },
      showMore: function () {
        this.showJobDetails = this.allJobDetails.slice(0, this.showJobDetails.length + 5);
      },
      refreshCorporateDetail: async function () {
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
      },
      getSplitArray(str, defaultVal) {
        return this.getSplitArray2(str, ",", defaultVal)
      },
      getSplitArray2(str, sep, defaultVal) {
        if (!checkEmpty(str)) {
          return strSplit(str, sep);
        } else {
          return defaultVal ? [defaultVal] : [];
        }
      },
    },
    mounted() {
      this.refreshCorporateDetail();
    },
    computed: {
      jobTypeTags: function () {
        return this.getSplitArray(this.corporateInfo.job_type, '暂无分类');
      },
      jobTypeDetails: function () {
        return this.getSplitArray(this.corporateInfo.job_type_detail, '暂无详细分类');
      },
      salaryRanges: function () {
        return this.getSplitArray(this.corporateInfo.salary_range, '暂无范围');
      },
    }
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
