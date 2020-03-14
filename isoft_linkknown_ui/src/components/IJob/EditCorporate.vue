<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <Form ref="formInline" :model="formInline" :rules="ruleValidate" :label-width="100">
        <FormItem label="公司名称" prop="corporate_name">
          <Input v-model.trim="formInline.corporate_name" :maxlength="50" show-word-limit
                 placeholder="请您输入公司名称"></Input>
        </FormItem>
        <FormItem label="公司主页" prop="corporate_site">
          <Input v-model.trim="formInline.corporate_site" :maxlength="200" show-word-limit
                 placeholder="请您输入公司主页"></Input>
        </FormItem>
        <FormItem label="公司 logo" prop="corporate_logo">
          <Input v-model.trim="formInline.corporate_logo" placeholder="请您上传公司 logo"
                 @on-focus="editCorporateLogo"></Input>
          <IFileUpload ref="fileUpload" :show-button="false" :auto-hide-modal="true" :multiple="false"
                       :format="['jpg','jpeg','png','gif']" @uploadComplete="uploadComplete" :action="fileUploadUrl"
                       uploadLabel="上传公司 logo"/>
        </FormItem>
        <FormItem label="公司规模" prop="corporate_size">
          <Select v-model="formInline.corporate_size">
            <Option v-for="(corporateSize, index) in corporateSizes" :value="corporateSize" :key="corporateSize">
              {{corporateSize}}
            </Option>
          </Select>
        </FormItem>
        <FormItem label="招聘类型" prop="job_type">
          <Select v-model="formInline.job_type">
            <Option v-for="(jobType, index) in jobTypes" :value="jobType" :key="jobType">
              {{jobType}}
            </Option>
          </Select>
        </FormItem>
        <FormItem label="详细类型" prop="job_type_detail">
          <Input v-model.trim="formInline.job_type_detail" :maxlength="100" show-word-limit
                 placeholder="请您输入招聘岗位详细类型"></Input>
        </FormItem>
        <FormItem label="薪酬范围" prop="salary_range">
          <Select v-model="formInline.salary_range">
            <Option v-for="(salaryRange, index) in salaryRanges" :value="salaryRange" :key="salaryRange">
              {{salaryRange}}
            </Option>
          </Select>
        </FormItem>
        <FormItem label="公司简介" prop="corporate_desc">
          <Input type="textarea" :rows="8" v-model.trim="formInline.corporate_desc" placeholder="请您输入公司简介"></Input>
        </FormItem>
        <FormItem label="职位简介" prop="job_desc">
          <Input type="textarea" :rows="8" v-model.trim="formInline.job_desc" placeholder="请您输入职位简介"></Input>
        </FormItem>
        <FormItem label="公司福利" prop="corporate_welfare">
          <Input v-model.trim="formInline.corporate_welfare" placeholder="请您输入公司福利"></Input>
        </FormItem>
        <FormItem label="公司地址" prop="corporate_address">
          <Input readonly="readonly" v-model.trim="formInline.corporate_address" placeholder="请您选择公司地址"
                 @on-focus="handleFocus('areaChooser')"></Input>
          <IAreaChooser ref="areaChooser" title="地区选择" @handleSubmit="handleAreaSubmit"/>
        </FormItem>
        <FormItem>
          <Button type="success" @click="handleSubmit('formInline')" style="margin-right: 6px">提交</Button>
          <Button type="success" @click="handleReturn" style="margin-right: 6px">返回</Button>
        </FormItem>
      </Form>
    </div>
  </div>
</template>

<script>
  import {EditCorporateDetail, fileUploadUrl, QueryCorporateDetail} from "../../api"
  import {checkEmpty, GetLoginUserName, handleSpecial, strSplit} from "../../tools"
  import IAreaChooser from "../Common/IAreaChooser";
  import IFileUpload from "../Common/file/IFileUpload";

  export default {
    name: "EditCorporate",
    components: {IFileUpload, IAreaChooser},
    data() {
      return {
        fileUploadUrl: fileUploadUrl + "?table_name=corporate_detail&table_field=corporate_logo",
        salaryRanges: this.GLOBAL.salaryRanges,
        corporateSizes: this.GLOBAL.corporateSizes,
        jobTypes: this.GLOBAL.jobTypes,
        showModal: false,
        modalTitle: '',
        modalItemName: '',
        modalChoices: [],
        formInline: {
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
        ruleValidate: {
          corporate_name: [
            {required: true, message: '公司名称不能为空!', trigger: 'blur'},
          ],
          corporate_desc: [
            {required: true, message: '公司简介不能为空!', trigger: 'blur'},
          ],
          job_desc: [
            {required: true, message: '职位简介不能为空!', trigger: 'blur'},
          ],
        }
      }
    },
    methods: {
      editCorporateLogo: function () {
        this.$refs.fileUpload.showModal();
      },
      uploadComplete: function (result) {
        if (result.status == "SUCCESS") {
          this.formInline.corporate_logo = handleSpecial(result.fileServerPath);
        }
      },
      handleAreaSubmit: function (province, city, area) {
        if (checkEmpty(city)) {
          this.formInline.corporate_address = province;
        } else if (checkEmpty(area)) {
          this.formInline.corporate_address = province + '-' + city;
        } else {
          this.formInline.corporate_address = province + '-' + city + '-' + area;
        }
      },
      handleFocus: function (name) {
        if (name === "areaChooser") {
          let arr = strSplit(this.formInline.corporate_address, "-");
          // 暂时不做回显
          this.$refs.areaChooser.showModal();
        }
      },
      handleReturn: function () {
        showModal
        this.$router.push({path: '/job/corporateDetail'});
      },
      handleSubmit: function (name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            const result = await EditCorporateDetail(this.formInline);
            if (result.status == "SUCCESS") {
              this.$Message.success("保存成功！");
              this.$router.push({path: '/job/corporateDetail'});
            } else {
              this.$Message.error(result.errorMsg);
            }
          } else {
            this.$Message.error("校验失败!");
          }
        })
      },
      refreshCorporateDetail: async function () {
        let params = {};
        if (!checkEmpty(GetLoginUserName())) {
          // 根据当前登录用户名去查
          params["user_name"] = GetLoginUserName();
        } else {
          // 否则不查
          return;
        }
        const result = await QueryCorporateDetail(params);
        if (result.status === "SUCCESS" && result.corporate_detail) {
          this.formInline = result.corporate_detail;
        }
      }
    },
    mounted() {
      this.refreshCorporateDetail();
    }
  }
</script>

<style scoped>

</style>
