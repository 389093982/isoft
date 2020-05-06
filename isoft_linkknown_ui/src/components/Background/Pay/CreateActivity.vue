<template>
	<div style="width: 70%">
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100">
      <FormItem label="活动ID" prop="activity_id">
        <Input v-model.trim="formValidate.activity_id" readonly="true"/>
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
  import {validatePatternForString} from "../../../tools/index"
  import {GetToday_yyyyMMdd} from "../../../tools";

	export default {
		name: "CreateActivity",
    data(){
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
            {required: true,message:'活动ID必填', trigger: 'change blur'}
          ],
          activity_type: [
            {required: true,message:'活动类型必填', trigger: 'change blur'}
          ],
          type_entity_account: [
            {required: true,message:'数量必填', trigger: 'change blur'}
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
      handleSubmit(name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            this.$Message.info('校验通过！');
          }
        });
      },
      handleReset(name) {
        this.$refs[name].resetFields();
      },

    },
    mounted:function () {
		  //随机生成活动ID
      let random = Math.random().toString().slice(-8);
      this.formValidate.activity_id = GetToday_yyyyMMdd() + random;
    },
	}
</script>

<style scoped>

</style>
