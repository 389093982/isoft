<template>
  <div>
    <Modal
      v-model="showModal"
      :width="500"
      :title="modalTitle"
      :footer-hide="true"
      :transfer="false"
      :mask-closable="false"
      :styles="{top: '50px'}">
      <div>
        <Tag v-for="modalChoice in modalChoices">
          <span @click="choiceItem(modalChoice)">{{modalChoice}}</span>
        </Tag>
      </div>
    </Modal>

    <div class="isoft_bg_white isoft_pd10">
      <p class="clear">
        <label for="corporate_name">公司名称：</label>
        <input id="corporate_name" class="input" v-model="formInline.corporate_name" placeholder="请您输入公司名称"/>
      </p>
      <p class="clear">
        <label for="corporate_site">公司主页：</label>
        <input id="corporate_site" class="input" v-model="formInline.corporate_site" placeholder="请您输入公司主页"/>
      </p>
      <p class="clear">
        <label for="corporate_logo">公司 logo：</label>
        <input id="corporate_logo" readonly="readonly" @focus="handleFocus('corporate_logo')"
               class="input" v-model="formInline.corporate_logo" placeholder="请您上传公司 logo"/>
      </p>
      <p class="clear">
        <label for="corporate_size">公司规模：</label>
        <input id="corporate_size" readonly="readonly" @focus="handleFocus('corporate_size')"
               class="input" v-model="formInline.corporate_size" placeholder="请您输入公司规模"/>
      </p>
      <p class="clear">
        <label for="job_type">招聘类型：</label>
        <input id="job_type" readonly="readonly" @focus="handleFocus('job_type')"
               class="input" v-model="formInline.job_type" placeholder="请您选择招聘类型"/>
      </p>
      <p class="clear">
        <label for="job_type_detail">详细类型：</label>
        <input id="job_type_detail"
               class="input" v-model="formInline.job_type_detail" placeholder="请您输入招聘岗位详细类型"/>
      </p>
      <p class="clear">
        <label for="salary_range">薪酬范围：</label>
        <input id="salary_range" readonly="readonly" @focus="handleFocus('salary_range')"
               class="input" v-model="formInline.salary_range"  placeholder="请您选择薪酬范围"/>
      </p>
    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_top10">
      <p class="clear">
        <label for="corporate_desc">公司简介：</label>
        <textarea rows="8" id="corporate_desc"
                  class="input" v-model="formInline.corporate_desc" placeholder="请您输入公司简介"></textarea>
      </p>
      <p class="clear">
        <label for="job_desc">职位简介：</label>
        <textarea rows="8" id="job_desc"
                  class="input" v-model="formInline.job_desc" placeholder="请您输入职位简介"></textarea>
      </p>
    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_top10">
      <p class="clear">
        <label for="corporate_welfare">公司福利：</label>
        <input id="corporate_welfare" class="input" v-model="formInline.corporate_welfare" placeholder="请您输入公司福利"/>
      </p>

      <p class="clear">
        <label for="corporate_address">公司地址：</label>
        <input id="corporate_address" class="input" v-model="formInline.corporate_address" placeholder="请您选择公司地址"/>
      </p>

      <p class="isoft_top10" style="text-align: center;">
        <Button type="success" @click="handleSubmit">提交</Button>
        <Button type="success" @click="handleReturn">返回</Button>
      </p>
    </div>
  </div>
</template>

<script>
  import {EditCorporateDetail, QueryCorporateDetail} from "../../api"
  import {checkEmpty} from "../../tools"

  export default {
    name: "EditCorporate",
    data(){
      return {
        options:{
          'corporate_size':{
            modalTitle:'选择公司规模',
            modalChoices:['1-10人','10-20人','20-50人','50-100人','100-500人','500-2000人','2000以上'],
          },
          'job_type':{
            modalTitle:'选择招聘类型',
            modalChoices:['前端工程师','后端工程师','研发经理','软件工程师','安卓开发工程师','架构负责人'],
          },
          'salary_range':{
            modalTitle:'薪酬范围',
            modalChoices:['1k-5k','5k-10k','10k-15k','15k-20k','20k-50k','50k-100k','100k+'],
          }
        },
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
      }
    },
    methods:{
      noRepeatAppend(str, s){
        if(checkEmpty(str)){
          return s;
        } else if (str.indexOf(s) >= 0){
          return str;
        }else {
          return str + "," + s;
        }

      },
      handleFocus:function (name) {
        this.showModal = true;
        this.modalItemName = name;
        this.modalTitle = this.options[name].modalTitle;
        this.modalChoices = this.options[name].modalChoices;
      },
      choiceItem:function (item) {
        if (this.modalItemName === 'corporate_size') {
          this.formInline.corporate_size = item;
        } else if(this.modalItemName === 'job_type') {
          this.formInline.job_type = this.noRepeatAppend(this.formInline.job_type, item);
        } else if(this.modalItemName === 'salary_range') {
          this.formInline.salary_range = this.noRepeatAppend(this.formInline.salary_range, item);
        }
        this.showModal = false;
      },
      handleReturn:function(){
        this.$router.push({path:'/job/corporate_detail'});
      },
      handleSubmit:async function () {
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
        if(result.status == "SUCCESS"){
          this.$Message.success("保存成功！");
          this.$router.push({path:'/job/corporate_detail'});
        }else{
          this.$Message.error(result.errorMsg);
        }
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
  p{
    margin-top: 5px;
  }
  label{
    width: 100px;
    float: left;
  }
  input,textarea{
    outline-style: none;
    border: 1px solid #ccc;
    border-radius: 3px;
    padding: 3px 3px;
    width: 850px;
    font-size: 14px;
    font-family: 'Microsoft Yahei', 'PingFangSC', sans-serif;
  }
  /* 设置输入框点击发光效果 */
  input:focus,textarea:focus{
    border-color: #66afe9;
    outline: 0;
    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(102,175,233,.6);
    box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(102,175,233,.6);
  }
</style>
