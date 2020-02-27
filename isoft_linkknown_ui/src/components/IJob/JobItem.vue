<template>
  <div v-if="jobDetail && jobDetail">
    <div style="margin: 5px 0 10px 0;border: 1px solid #f0f0f0;">
      <div style="display: flex;padding: 15px 25px;">
        <div style="width: 50%;height: 50px;line-height: 25px;">
          <p style="color: #00c2b3;font-size: 18px;">{{jobDetail.job_name}} [ {{jobDetail.job_age}} ]</p>
          <p><span style="color: #fc703e;">{{jobDetail.salary_range}}</span> &nbsp;|&nbsp;
            <span v-if="jobDetail.job_education">{{jobDetail.job_education}}</span><span v-else>不限</span>
            &nbsp;|&nbsp;{{jobDetail.corporate_size}}
          </p>
        </div>
        <div style="width: 50%;height: 50px;line-height: 25px;">
          <Row>
            <Col span="18">
              <p style="color: #00c2b3;font-size: 18px;">{{jobDetail.corporate_name}}&nbsp;<span
                style="color: red;font-weight: bold;font-size: 12px;">保</span></p>
              <p>
                    <span v-for="(job_tag,index) in filterJobTags(jobDetail.job_tags)">
                      <span v-if="index > 0">&nbsp;|&nbsp;</span>{{job_tag}}
                    </span>
              </p>
            </Col>
            <Col span="6">
              <span v-if="editable == 'true'">
                <Button size="small"
                        @click="$router.push({path:'/job/job_edit', query: {job_id: jobDetail.corporate_id}})">编辑</Button>
                <Button size="small"
                        @click="$router.push({path:'/job/job_edit', query: {corporate_id: jobDetail.corporate_id}})">新增</Button>
              </span>
              <span v-else>
                <Button size="small" @click="applyJob(jobDetail.id)">我要应聘</Button>
              </span>
            </Col>
          </Row>
        </div>
      </div>
      <div style="background-color: rgba(200,194,255,0.15);padding: 7px 25px;display: flex;">
        <div style="width: 50%;">
          {{jobDetail.job_address}}
          <Time :time="jobDetail.last_updated_time" :interval="1"/>
        </div>
        <div style="width: 50%;" class="isoft_inline_ellipsis">
          福利待遇：{{jobDetail.corporate_welfare}}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {checkEmpty, strSplit} from "../../tools"

  export default {
    name: "JobItem",
    props: {
      jobDetail: {
        type: Object,
        default: null,
      }
    },
    data() {
      return {
        editable: true,
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
      filterJobTags: function (tags) {
        return this.getSplitArray2(tags, "|", null).slice(0, 4);
      }
    }
  }
</script>

<style scoped>

</style>
