<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <Form ref="formInline" :model="formInline" :rules="ruleValidate" :label-width="100">
        <FormItem label="公司名称" prop="corporate_name">
          <Input v-model.trim="formInline.corporate_name" placeholder="请您输入公司名称"></Input>
        </FormItem>
        <FormItem label="公司主页" prop="corporate_site">
          <Input v-model.trim="formInline.corporate_site" placeholder="请您输入公司主页"></Input>
        </FormItem>
        <FormItem label="公司 logo" prop="corporate_logo">
          <Input v-model.trim="formInline.corporate_logo" placeholder="请您上传公司 logo"></Input>
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
          <Input v-model.trim="formInline.job_type_detail" placeholder="请您输入招聘岗位详细类型"></Input>
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
  import {EditCorporateDetail, QueryCorporateDetail} from "../../api"
  import {checkEmpty, strSplit} from "../../tools"
  import IAreaChooser from "../Common/IAreaChooser";

  export default {
    name: "EditCorporate",
    components: {IAreaChooser},
    data(){
      return {
        salaryRanges: this.GLOBAL.salaryRanges,
        corporateSizes: this.GLOBAL.corporateSizes,
        jobTypes: this.GLOBAL.jobTypes,
        showModal:false,
        modalTitle:'',
        modalItemName:'',
        modalChoices:[],
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
        ruleValidate: {}
      }
    },
    methods:{
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
      handleReturn:function(){
        this.$router.push({path:'/job/corporate_detail'});
      },
      handleSubmit: function (name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            const result = await EditCorporateDetail(
              this.formInline.id,
              this.formInline.corporate_name,
              this.formInline.corporate_site,
              this.formInline.corporate_logo,
              this.formInline.corporate_size,
              this.formInline.job_type,
              this.formInline.job_type_detail,
              this.formInline.salary_range,
              this.formInline.corporate_desc,
              this.formInline.job_desc,
              this.formInline.corporate_welfare,
              this.formInline.corporate_address
            );
            if (result.status == "SUCCESS") {
              this.$Message.success("保存成功！");
              this.$router.push({path: '/job/corporate_detail'});
            } else {
              this.$Message.error(result.errorMsg);
            }
          }
        })
      },
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
