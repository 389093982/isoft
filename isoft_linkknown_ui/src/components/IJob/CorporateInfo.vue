<template>
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

        <div class="isoft_button_green1" style="width: 200px;position: absolute; bottom: -10px;right: 10px;"
             v-if="isLoginUserName(corporateInfo.created_by)" @click="$router.push({path:'/job/corporate_edit'})">前去编辑
        </div>
      </Col>
    </Row>

    <div class="isoft_bg_white isoft_pd20 isoft_top5">
      <Row :gutter="10">
        <Col span="16">
          <div class="isoft_pd10 fadeToRight" style="background-color: #fff;">
            <div class="isoft_font_header" style="text-align: center;color: #00c2b3;">公司简介</div>
            {{corporateInfo.corporate_desc}}
          </div>
          <div class="isoft_pd10 fadeToTop" style="background-color: #fff;">
            <div class="isoft_font_header" style="text-align: center;color: #00c2b3;">公司福利</div>
            {{corporateInfo.corporate_welfare}}
          </div>
        </Col>
        <Col span="8">
          <div class="isoft_pd10 fadeToLeft" style="background-color: #fff;">
            <div class="isoft_font_header" style="text-align: center;color: #00c2b3;">职位简介</div>
            {{corporateInfo.job_desc}}
          </div>
        </Col>
      </Row>
    </div>
  </div>
</template>

<script>
  import {checkEmpty, GetLoginUserName, goToTargetLink, strSplit} from "../../tools";

  export default {
    name: "CorporateDetail2",
    props: {
      corporateInfo: {
        type: Object,
        default: null,
      }
    },
    methods: {
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
      isLoginUserName: function (user_name) {
        return user_name === GetLoginUserName();
      },
      goToTargetLink: function (url) {
        goToTargetLink(url);
      },
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

</style>
