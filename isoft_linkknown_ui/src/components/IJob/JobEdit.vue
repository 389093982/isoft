<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <Row :gutter="10">
        <Col span="16">
          <Form ref="formInline" :model="formInline" :rules="ruleValidate" :label-width="100">
            <FormItem label="职位名称" prop="job_name">
              <Input v-model.trim="formInline.job_name" placeholder="请您输入职位名称"></Input>
            </FormItem>
            <FormItem label="工作年限" prop="job_age">
              <Select v-model="formInline.job_age">
                <Option v-for="(jobAge, index) in jobAges" :value="jobAge" :key="index">{{jobAge}}</Option>
              </Select>
            </FormItem>
            <FormItem label="工作地点" prop="job_address">
              <Input readonly="readonly" v-model.trim="formInline.job_address" placeholder="请您选择工作地点"
                     @on-focus="handleFocus('areaChooser')"></Input>
              <IAreaChooser ref="areaChooser" title="地区选择" @handleSubmit="handleAreaSubmit"/>
            </FormItem>
            <FormItem label="薪酬范围" prop="salary_range">
              <Select v-model="formInline.salary_range">
                <Option v-for="(salaryRange, index) in salaryRanges" :value="salaryRange" :key="salaryRange">
                  {{salaryRange}}
                </Option>
              </Select>
            </FormItem>
            <FormItem label="学历" prop="job_education">
              <Select v-model="formInline.job_education">
                <Option v-for="(education, index) in job_educations" :value="education" :key="education">
                  {{education}}
                </Option>
              </Select>
            </FormItem>
            <FormItem label="岗位标签" prop="job_tags">
              <Input v-model.trim="formInline.job_tags" placeholder="多个标签用 | 分割,示例：软件开发|人工智能|已上市"></Input>
            </FormItem>
            <FormItem>
              <Button type="success" @click="handleSubmit('formInline')" style="margin-right: 6px">提交</Button>
              <Button type="success" @click="handleReturn" style="margin-right: 6px">返回</Button>
            </FormItem>
          </Form>
        </Col>
        <Col span="8">
          热门岗位推荐 xxxxx
        </Col>
      </Row>

    </div>
  </div>
</template>

<script>
  import {EditJobDetail, QueryJobById} from "../../api"
  import {checkEmpty, strSplit} from "../../tools";
  import IAreaChooser from "../Common/IAreaChooser";

  export default {
    name: "JobEdit",
    components: {IAreaChooser},
    data() {
      const _validateJobName = (rule, value, callback) => {
        if (value.length < 5) {
          callback(new Error('职位名称太短!'));
        } else {
          callback();
        }
      };
      return {
        jobAges: this.GLOBAL.jobAges,
        salaryRanges: this.GLOBAL.salaryRanges,
        job_educations: this.GLOBAL.job_educations,
        formInline: {
          id: -1,
          corporate_id: -1,
          job_name: '',
          job_age: '',
          job_address: '',
          salary_range: '',
          job_education: '',
          job_tags: '',
        },
        ruleValidate: {
          job_name: [
            {required: true, message: '职位名称不能为空!', trigger: 'blur'},
            {validator: _validateJobName, trigger: 'blur'}
          ],
          job_age: [
            {required: true, message: '工作年限不能为空!', trigger: 'blur'},
          ],
          job_address: [
            {required: true, message: '工作地点不能为空!', trigger: 'blur'},
          ],
          salary_range: [
            {required: true, message: '薪酬范围不能为空!', trigger: 'blur'},
          ],
        }
      }
    },
    methods: {
      handleAreaSubmit: function (province, city, area) {
        if (checkEmpty(city)) {
          this.formInline.job_address = province;
        } else if (checkEmpty(area)) {
          this.formInline.job_address = province + '-' + city;
        } else {
          this.formInline.job_address = province + '-' + city + '-' + area;
        }
      },
      handleFocus: function (name) {
        if (name === "areaChooser") {
          let arr = strSplit(this.formInline.job_address, "-");
          // 暂时不做回显
          this.$refs.areaChooser.showModal();
        }
      },
      handleReturn: function () {
        this.$router.push({path: '/job/corporateDetail'});
      },
      handleSubmit: async function (name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            const result = await EditJobDetail(this.formInline);
            if (result.status === "SUCCESS") {
              this.$Message.success("保存成功！");
              this.$router.push({path: '/job/corporateDetail', query: {loc: 2}});
            } else {
              this.$Message.error(result.errorMsg);
            }
          }
        })
      },
      refreshJobDetail: async function (id) {
        let params = {
          'id':id
        };
        const result = await QueryJobById(params);
        if (result.status === "SUCCESS") {
          this.formInline = result.jobDetail;
        }
      }
    },
    mounted() {
      if (this.$route.query.job_id != null) {
        this.refreshJobDetail(this.$route.query.job_id);
      }
      if (this.$route.query.corporate_id != null) {
        this.formInline.corporate_id = this.$route.query.corporate_id;
      }
    }
  }
</script>

<style scoped>

</style>
