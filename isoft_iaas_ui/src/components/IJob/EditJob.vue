<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <p class="clear">
        <label for="job_name">职位名称：</label>
        <input id="job_name" class="input" v-model="formInline.job_name" placeholder="请您输入职位名称"/>
      </p>
      <p class="clear">
        <label for="job_age">工作年限：</label>
        <input id="job_age" class="input" v-model="formInline.job_age" placeholder="请您输入工作年限"/>
      </p>
      <p class="clear">
        <label for="job_address">工作地点：</label>
        <input id="job_address" class="input" v-model="formInline.job_address" placeholder="请您输入工作地点"/>
      </p>
      <p class="clear">
        <label for="salary_range">薪酬范围：</label>
        <input id="salary_range" class="input" v-model="formInline.salary_range" placeholder="请您输入薪酬范围"/>
      </p>

      <p class="isoft_top10" style="text-align: center;">
        <Button type="success" @click="handleSubmit">提交</Button>
        <Button type="success" @click="handleReturn">返回</Button>
      </p>
    </div>
  </div>
</template>

<script>
  import {EditJobDetail,QueryJobById} from "../../api"

  export default {
    name: "EditJob",
    data(){
      return {
        formInline: {
          id:-1,
          corporate_id:-1,
          job_name: '',
          job_age: '',
          job_address: '',
          salary_range: '',
        },
      }
    },
    methods:{
      handleReturn:function(){
        this.$router.push({path:'/job/corporate_detail'});
      },
      handleSubmit:async function () {
        const result = await EditJobDetail(
          this.formInline.id,
          this.formInline.corporate_id,
          this.formInline.job_name,
          this.formInline.job_age,
          this.formInline.job_address,
          this.formInline.salary_range
        );
        if(result.status == "SUCCESS"){
          this.$Message.success("保存成功！");
          this.$router.push({path:'/job/corporate_detail'});
        }else{
          this.$Message.error(result.errorMsg);
        }
      },
      refreshJobDetail:async function (id) {
        const result = await QueryJobById(id);
        if(result.status == "SUCCESS"){
          this.formInline.id = result.jobDetail.id;
          this.formInline.corporate_id = result.jobDetail.corporate_id;
          this.formInline.job_name = result.jobDetail.job_name;
          this.formInline.job_age = result.jobDetail.job_age;
          this.formInline.job_address = result.jobDetail.job_address;
          this.formInline.salary_range = result.jobDetail.salary_range;
        }
      }
    },
    mounted(){
      if(this.$route.query.job_id != null){
        this.refreshJobDetail(this.$route.query.job_id);
      }
      if(this.$route.query.corporate_id != null){
        this.formInline.corporate_id = this.$route.query.corporate_id;
      }
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
