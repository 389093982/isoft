<template>
  <div v-if="jobDetail && jobDetail">

    <div style="margin: 5px 0 10px 0;border: 1px solid #f0f0f0;">
      <div style="display: flex;padding: 15px 25px;">
        <!--java开发工程师 [ 3-5年 ]-->
        <!--15k-20k  |  本科 | 1-10人-->
        <div style="width: 50%;height: 50px;line-height: 25px;">
          <p style="color: #00c2b3;font-size: 18px;">{{jobDetail.job_name}} [ {{jobDetail.job_age}} ]</p>
          <p>
            <span style="color: #fc703e;">{{jobDetail.salary_range}}</span> &nbsp;|&nbsp;
            <span v-if="jobDetail.job_education">{{jobDetail.job_education}}</span><span v-else>不限</span>&nbsp;|&nbsp;{{jobDetail.corporate_size}}
          </p>
        </div>

        <div style="width: 50%;height: 50px;line-height: 25px;">
          <Row>
            <Col span="18" style="display: flex;">
              <!--头像-->
              <div style="width: 15%">
                <img :src="jobDetail.corporate_logo" style="width: 45px;height: 45px;border-radius: 10px;"/>
              </div>
              <div style="width: 85%;">
                <!--南京市linkknown网络有限责任公司-->
                <p style="color: #00c2b3;font-size: 18px;">
                  <span class="isoft_point_cursor" @click="$router.push({path:'/job/corporateDetail', query:{corporate_id: jobDetail.corporate_id}})">{{jobDetail.corporate_name}}</span>&nbsp;
                  <span v-if="hrVipLevel>=1" style="color: red;font-weight: bold;font-size: 12px;">荐</span></p>
                <p>
                <!--不加班  | 双休-->
                <span v-for="(job_tag,index) in filterJobTags(jobDetail.job_tags)">
                  <span v-if="index > 0">&nbsp;|&nbsp;</span>{{job_tag}}
                </span>
                </p>
              </div>
            </Col>
            <Col span="6">
              <span v-if="isLoginUserName(jobDetail.created_by)">
                <Button size="small" @click="$router.push({path:'/job/jobEdit', query: {job_id: jobDetail.corporate_id}})">编辑岗位</Button>
                <Button size="small" @click="$router.push({path:'/job/jobEdit', query: {corporate_id: jobDetail.corporate_id}})">新增岗位</Button>
              </span>
              <span v-else>
                <Button size="small" v-if="!isLoginUserName(jobDetail.created_by)" @click="applyJob(jobDetail.id)">我要应聘</Button>
              </span>
            </Col>
          </Row>
        </div>
      </div>

      <!--北京市-北京城区-朝阳区03-05 16:58                                      福利待遇：五险一金-->
      <div style="background-color: rgba(200,194,255,0.15);padding: 7px 25px;display: flex;">
        <div style="width: 50%;">
          {{jobDetail.job_address}}<Time :time="jobDetail.last_updated_time" :interval="1"/>
        </div>
        <div style="width: 50%;" class="isoft_inline_ellipsis">
          福利待遇：{{jobDetail.corporate_welfare}}
        </div>
      </div>
    </div>

  </div>
</template>

<script>
  import {checkEmpty, CheckHasLoginConfirmDialog2, GetLoginUserName, strSplit} from "../../tools"
  import {ApplyJob} from "../../api";

  export default {
    name: "JobItem",
    props: {
      hrVipLevel:{
        type:[String,Number],
        default:false
      },
      jobDetail: {
        type: Object,
        default: null,
      }
    },
    data() {
      return {

      }
    },
    methods: {
      isLoginUserName: function (user_name) {
        return user_name === GetLoginUserName();
      },
      applyJob: function (job_id) {
        var _this = this;
        CheckHasLoginConfirmDialog2(this, async function () {
          const result = await ApplyJob({job_id: job_id});
          if (result.status === "SUCCESS") {
            _this.$Message.success("投递成功!");
            // 投递成功 1s 后进入投递详情页面
            setTimeout(function () {
              _this.$router.push({path: '/job/jobApplyList'});
            }, 1000);
          } else {
            _this.$Message.error(result.errorMsg);
          }
        });
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
      filterJobTags: function (tags) {
        return this.getSplitArray2(tags, "|", null).slice(0, 4);
      }
    }
  }
</script>

<style scoped>

</style>
