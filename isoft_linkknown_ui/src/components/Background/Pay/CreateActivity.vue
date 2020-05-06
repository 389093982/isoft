<template>
	<div style="width: 70%">
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100">
      <FormItem label="活动ID" prop="activity_id">
        <Input v-model.trim="formValidate.activity_id" readonly/>
      </FormItem>
      <FormItem label="活动类型" prop="activity_type">
        <RadioGroup v-model="formValidate.activity_type">
          <Radio label='coupon'>优惠券</Radio>
          <Radio label='hongbao'>红包</Radio>
        </RadioGroup>
      </FormItem>

      <FormItem v-if="formValidate.activity_type==='coupon'" label="优惠券数量" prop="type_entity_account">
        <Input type="number" v-model.trim="formValidate.type_entity_account"/>
      </FormItem>
      <FormItem v-else-if="formValidate.activity_type==='hongbao'" label="红包数量" prop="type_entity_account">
        <Input type="number" v-model.trim="formValidate.type_entity_account"/>
      </FormItem>

      <div v-if="formValidate.activity_type==='coupon'" style="margin-left: 100px;border: 2px rgba(248,168,38,0.9) solid;margin-bottom: 20px">
        <div style="color: rgba(248,168,38,0.9)">优惠券配置信息:</div>
        <FormItem label="券类型" prop="coupon_type">
          <RadioGroup v-model="formValidate.coupon_type">
            <Radio label='general'>通用券</Radio>
            <Radio label='designated'>指定券</Radio>
          </RadioGroup>
        </FormItem>
        <div v-if="formValidate.coupon_type==='designated'">
          <FormItem label="被使用对象" prop="target_type">
            <RadioGroup v-model="formValidate.target_type">
              <Radio label='course'>课程</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="被使用对象ID" prop="target_id">
            <Input type="number" v-model="formValidate.target_id" style="width: 100px" />
          </FormItem>
        </div>
        <FormItem label="券面金额" prop="coupon_amount">
          <Input v-model="formValidate.coupon_amount" placeholder="000.00" style="width: 100px" /><Icon type="logo-yen" />
        </FormItem>
        <FormItem label="商品门槛金额" prop="goods_min_amount">
          <Input v-model="formValidate.goods_min_amount" placeholder="000.00" style="width: 100px" /><Icon type="logo-yen" />
        </FormItem>
      </div>

      <FormItem label="举办方" prop="organizer">
        <Input v-model.trim="formValidate.organizer" placeholder=""/>
      </FormItem>
      <FormItem label="活动开始日期" prop="start_date">
        <DatePicker type="date" placeholder="活动开始日期" v-model="formValidate.start_date"></DatePicker>
      </FormItem>
      <FormItem label="活动结束日期" prop="end_date">
        <DatePicker type="date" placeholder="活动结束日期" v-model="formValidate.end_date"></DatePicker>
      </FormItem>

      <FormItem>
        <Button type="success" @click="handleSubmit('formValidate')">提交</Button>
        <Button style="margin-left: 8px" @click="handleReset('formValidate')">重置</Button>
      </FormItem>
    </Form>
  </div>
</template>

<script>
  import {validatePatternForString,copyObj,GetTodayTime_yyyyMMddhhmmss,GetDate_yyyyMMdd_byDate,MakeCouponIdArrayStr} from "../../../tools/index"
  import {AddPayActivity} from "../../../api/index"

	export default {
		name: "CreateActivity",
    data(){
      const checkActivityId = (rule,value,callback) => {
        if (value === '') {
          callback(new Error('活动ID不能为空！'));
        }else if(value.length!==20){
          callback(new Error('活动ID长度必须是20位，请重新进入此页面'));
        }else {
          callback();
        }
      };
      const checkCouponAmount = (rule,value,callback) => {
        let patrn = /^[0-9]{1,7}(.[0-9]{1,2})?$/;
        if (value === '') {
          callback(new Error('金额不能为空'));
        }else if(!validatePatternForString(patrn,value)){
          callback(new Error('金额格式不正确'));
        }else if(value<=0){
          callback(new Error('金额必须大于0'));
        }else {
          callback();
        }
      };
      const checkGoodsMinAmount = (rule,value,callback) => {
        let patrn = /^[0-9]{1,7}(.[0-9]{1,2})?$/;
        if (value === '') {
          callback(new Error('金额不能为空'));
        }else if(!validatePatternForString(patrn,value)){
          callback(new Error('金额格式不正确'));
        }else if(value<=0){
          callback(new Error('金额必须大于0'));
        }else if (parseInt(value) <= parseInt(this.formValidate.coupon_amount)) {
          callback(new Error('商品门槛金额必须大于券面金额！'));
        }else {
          callback();
        }
      };
      const checkTypeEntityAccount = (rule,value,callback) => {
        if (value === '') {
          callback(new Error('数量必填！'));
        }else if(value.length>6){
          callback(new Error('暂定:数量长度建议不要超过6位数！'));
        }else {
          callback();
        }
      };
		  return{
        formValidate: {
          //活动信息
          activity_id: '',
          activity_type: '',
          type_entity_account: '',
          organizer:'',
          start_date:'',
          end_date:'',
          //优惠信息
          coupon_type:'general',
          target_type:'course',
          target_id:'',
          coupon_amount:'',
          goods_min_amount:'',
        },
        ruleValidate: {
          //活动校验
          activity_id: [
            {required: true,validator:checkActivityId, trigger: 'change blur'}
          ],
          activity_type: [
            {required: true,message:'活动类型必填', trigger: 'change blur'}
          ],
          type_entity_account: [
            {required: true,validator:checkTypeEntityAccount, trigger: 'change blur'}
          ],
          organizer: [
            {required: true,message:'举办方必填', trigger: 'change blur'}
          ],
          start_date: [
            {required: true,type: 'date',message:'活动开始日期必填', trigger: 'change blur'}
          ],
          end_date: [
            {required: true,type: 'date',message:'活动结束日期必填', trigger: 'change blur'}
          ],
          //优惠校验
          coupon_type: [
            {required: true,message:'券类型必填', trigger: 'change blur'}
          ],
          target_type: [
            {required: true,message:'对象类型必填', trigger: 'change blur'}
          ],
          target_id: [
            {required: true,message:'对象ID必填', trigger: 'change blur'}
          ],
          coupon_amount: [
            {required: true,validator:checkCouponAmount, trigger: 'change blur'}
          ],
          goods_min_amount: [
            {required: true,validator:checkGoodsMinAmount, trigger: 'change blur'}
          ],
        },
      }
    },
    methods:{
		  randomActivityId:function(){
        //随机生成活动ID
        let random = Math.random().toString().slice(-6);
        this.formValidate.activity_id = GetTodayTime_yyyyMMddhhmmss() +''+ random;
      },
      handleSubmit(name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            if (this.formValidate.end_date < this.formValidate.start_date){
              this.$Message.error('活动结束日期，不能小于开始日期！');
              return false;
            }
            this.addPayActivity();
          }
        });
      },
      addPayActivity:async function(){
		    //如果是优惠券，走下面这个分支 -- 界面和后台流程都做个分支判断，因为不同活动类型，逻辑不一样
		    if (this.formValidate.activity_type === 'coupon') {
          let params = copyObj(this.formValidate);
          params.start_date = GetDate_yyyyMMdd_byDate(params.start_date);
          params.end_date = GetDate_yyyyMMdd_byDate(params.end_date);
          //如果是通用券，那么这里设置target_type、target_id 为空值，保险起见。
          if (params.coupon_type === 'general') {
            params.target_type = '';
            params.target_id = '';
          }
          //生成券号
          params.couponIdArrayStr = MakeCouponIdArrayStr(parseInt(params.type_entity_account));
          const result = await AddPayActivity(params);
          if (result.status === 'SUCCESS') {
            this.$Message.success('活动举办成功！');
            //举办成功后，重置界面
            this.handleReset('formValidate');
          }else{
            this.$Message.error('活动举办失败！');
            this.randomActivityId();
          }
        }else{
		      this.$Message.info('暂指支持优惠券！');
        }
      },
      handleReset(name) {
        this.$refs[name].resetFields();
        this.randomActivityId();
      },

    },
    mounted:function () {
		  this.randomActivityId();
    },
	}
</script>

<style scoped>

</style>
