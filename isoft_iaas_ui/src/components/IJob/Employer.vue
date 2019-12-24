<template>
  <div>
    <div class="isoft_bg_white isoft_pd10">
      <p class="clear">
        <label for="corporate_name">公司名称：</label>
        <input id="corporate_name" class="input" v-if="editMode" v-model="formInline.corporate_name" placeholder="请您输入公司名称"/>
        <span v-else>{{formInline.corporate_name}}</span>
      </p>
      <p class="clear">
        <label for="corporate_site">公司主页：</label>
        <input id="corporate_site" class="input" v-if="editMode" v-model="formInline.corporate_site" placeholder="请您输入公司主页"/>
        <span v-else>{{formInline.corporate_site}}</span>
      </p>
      <p class="clear">
        <label for="corporate_logo">公司 logo：</label>
        <input id="corporate_logo" readonly="readonly"
               class="input" v-if="editMode" v-model="formInline.corporate_logo" placeholder="请您上传公司 logo"/>
        <span v-else>{{formInline.corporate_logo}}</span>
      </p>
      <p class="clear">
        <label for="corporate_size">公司规模：</label>
        <input id="corporate_size" readonly="readonly"
               class="input" v-if="editMode" v-model="formInline.corporate_size" placeholder="请您输入公司规模"/>
        <span v-else>{{formInline.corporate_size}}</span>
      </p>
      <p class="clear">
        <label for="job_type">招聘类型：</label>
        <input id="job_type" readonly="readonly"
               class="input" v-if="editMode" v-model="formInline.job_type" placeholder="请您选择招聘类型"/>
        <span v-else>{{formInline.job_type}}</span>
      </p>
      <p class="clear">
        <label for="salary_range">薪酬范围：</label>
        <input id="salary_range" readonly="readonly"
               class="input" v-if="editMode" v-model="formInline.salary_range"  placeholder="请您选择薪酬范围"/>
        <span v-else>{{formInline.salary_range}}</span>
      </p>

      <Button style="position: relative;float: right;right: 10px;bottom: 35px;" @click="editMode = !editMode">编辑模式</Button>
    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_top10">
      <p class="clear">
        <label for="corporate_desc">公司简介：</label>
        <textarea rows="5" id="corporate_desc"
               class="input" v-if="editMode" v-model="formInline.corporate_desc" placeholder="请您输入公司简介"/>
        <span v-else>{{formInline.corporate_desc}}</span>
      </p>
      <p class="clear">
        <label for="job_desc">职位简介：</label>
        <textarea rows="5" id="job_desc"
                  class="input" v-if="editMode" v-model="formInline.job_desc" placeholder="请您输入职位简介"/>
        <span v-else>{{formInline.job_desc}}</span>
      </p>
    </div>

    <div class="isoft_bg_white isoft_pd10 isoft_top10">
      <p class="clear">
        <label for="corporate_address">公司地址：</label>
        <input id="corporate_address" class="input" v-if="editMode" v-model="formInline.corporate_address" placeholder="请您选择公司地址"/>
        <span v-else>{{formInline.corporate_address}}</span>
      </p>
    </div>
  </div>
</template>

<script>
  export default {
    name: "Employer",
    data(){
      return {
        editMode:false,
        formInline: {
          corporate_name: null,
          corporate_site: null,
          corporate_logo: null,
          corporate_size: null,
          salary_range: null,
          job_type: null,
          corporate_desc: null,
          job_desc: null,
          corporate_address: null,
        },
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
    width: 450px;
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
