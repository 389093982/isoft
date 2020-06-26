<template>
	<div style="display: flex;">

    <div style="width: 60%">
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="120">
        <FormItem label="活动ID" prop="activity_id">
          <Input v-model.trim="formValidate.activity_id" readonly/>
        </FormItem>
        <FormItem label="活动描述" prop="activity_desc">
          <Input v-model.trim="formValidate.activity_desc" placeholder="例如：xxx感恩大回馈！快来领取5折！优惠券！"/>
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


        <!--优惠券配置信息-->
        <div v-if="formValidate.activity_type==='coupon'" style="margin-left: 100px;border: 2px rgba(248,168,38,0.9) solid;margin-bottom: 20px">
          <div style="color: rgba(242,163,37,0.99)">优惠券配置信息:</div>
          <FormItem label="券类型" prop="coupon_type">
            <RadioGroup v-model="formValidate.coupon_type">
              <Radio label='general'>通用券</Radio>
              <Radio label='designated'>指定券</Radio>
            </RadioGroup>
          </FormItem>
          <div v-if="formValidate.coupon_type==='designated'">
            <FormItem label="被使用对象类型" prop="target_type">
              <RadioGroup v-model="formValidate.target_type">
                <Radio label='course'>课程</Radio>
              </RadioGroup>
            </FormItem>
            <FormItem label="被使用对象ID" prop="target_id">
              <Input type="number" v-model="formValidate.target_id" style="width: 100px" />
              <Button type="primary" shape="circle" icon="ios-search" @click="queryComfirm()">查询确认</Button>
            </FormItem>
            <FormItem label="被使用对象名称" prop="target_name">
              <RadioGroup v-model="formValidate.target_name">
                <Input v-model="formValidate.target_name" readonly style="width: 200px" />
              </RadioGroup>
            </FormItem>

          </div>

          <div style="width: 100%;height: 2px;background-color: rgba(255,105,0,0.34)"></div>
          <!--以下是优惠具体方式-->

          <FormItem label="优惠方式" prop="youhui_type">
            <RadioGroup v-model="formValidate.youhui_type">
              <Radio label='reduce'>减免</Radio>
              <Radio label='discount'>打折</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem v-if="formValidate.youhui_type==='discount'" label="折扣率" prop="discount_rate">
            <Input v-model="formValidate.discount_rate" placeholder="0.68" style="width: 100px" />
            <span v-if="formValidate.discount_rate" style="color: grey">
              {{formartDiscount(formValidate.discount_rate)}} 折 【表示需付款金额为：商品价格 * {{formValidate.discount_rate}}】
            </span>
          </FormItem>
          <div v-if="formValidate.youhui_type==='reduce'">
            <FormItem label="券面金额" prop="coupon_amount">
              <Input v-model="formValidate.coupon_amount" placeholder="000.00" style="width: 100px" /><Icon type="logo-yen" />
            </FormItem>
            <FormItem label="商品门槛金额" prop="goods_min_amount">
              <Input v-model="formValidate.goods_min_amount" placeholder="000.00" style="width: 100px" /><Icon type="logo-yen" />
            </FormItem>
          </div>
        </div>



        <FormItem label="举办方" prop="organizer">
          <Input v-model.trim="formValidate.organizer" placeholder="例:链知网官方 or 具体合作伙伴"/>
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
    <div style="width: 40%">
      <div v-if="formValidate.activity_type==='coupon'" style="margin: 150px 0 0 30px ">
        <div style="font-size: 20px;color: #ff6900;text-align: center">效果图展示:</div><br>
        <Coupon :coupon_type="formValidate.coupon_type"
                 :youhui_type="formValidate.youhui_type"
                 :start_date="formatCouponComponentDate(formValidate.start_date)"
                 :end_date="formatCouponComponentDate(formValidate.end_date)"
                 :coupon_amount="formValidate.coupon_amount"
                 :goods_min_amount="formValidate.goods_min_amount"
                 :discount_rate="formValidate.discount_rate">
        </Coupon>
      </div>

      <div v-if="formValidate.target_name!=''">
        <div style="margin-top: 30px;margin-left: 30px">
          <!--图片-->
          <img v-if="formValidate.target_name" :src="course_img" width="180" height="120"/>
          <img v-else src="../../../../static/images/common_img/default.png" width="180" height="120"/>
          <div>{{formValidate.target_name}}</div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
  import {validatePatternForString,copyObj,GetTodayTime_yyyyMMddhhmmss,formatDate_yyyyMMdd,MakeCouponIdArrayStr} from "../../../tools/index"
  import {AddPayActivity,ShowCourseDetail} from "../../../api/index"
  import Coupon from "../../Common/coupon/Coupon";

	export default {
		name: "CreateActivity",
    components: {Coupon},
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
      const checkDiscountRate = (rule,value,callback) => {
        let patrn = /^[0-9]{1,7}(.[0-9]{1,2})?$/;
        if (value === '') {
          callback(new Error('折扣率不能为空'));
        }else if(!validatePatternForString(patrn,value)){
          callback(new Error('格式不正确'));
        }else if(value<=0){
          callback(new Error('折扣率必须大于0'));
        }else if(value>=1){
          callback(new Error('折扣率必须小于1'));
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
        }else if(parseInt(value)>5000){
          callback(new Error('暂定:数量建议不要大于5000！'));
        }else {
          callback();
        }
      };
		  return{
        formValidate: {
          //活动信息
          activity_id: '',
          activity_desc: '',
          activity_type: '',
          type_entity_account: '',
          organizer:'',
          start_date:'',
          end_date:'',
          //优惠信息
          coupon_type:'general',
          target_type:'course',
          target_id:'',
          target_name:'',
          youhui_type:'reduce',
          discount_rate:'',
          coupon_amount:'',
          goods_min_amount:'',
        },
        course_img:'',
        ruleValidate: {
          //活动校验
          activity_id: [
            {required: true,validator:checkActivityId, trigger: 'change blur'}
          ],
          activity_desc: [
            {required: true,message:'活动描述必填', trigger: 'change blur'}
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
          target_name:[
            {required: true,message:'对象名称必填', trigger: 'change blur'}
          ],
          youhui_type: [
            {required: true,message:'优惠方式必填', trigger: 'change blur'}
          ],
          discount_rate: [
            {required: true,validator:checkDiscountRate, trigger: 'change blur'}
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
      queryComfirm:async function(){
        if (this.formValidate.target_id === "") {
          this.$Message.error("请填写目标ID");
          return false;
        }
        if (this.formValidate.target_type === 'course') {
          //查课程
          let params = {
            'course_id':this.formValidate.target_id
          };
          const result = await ShowCourseDetail(params);
          if (result.status === 'SUCCESS') {
            this.formValidate.target_name = result.course.course_name;
            this.course_img = result.course.small_image;
          }else{
            this.$Message.error(result.errorMsg);
          }
        }

      },
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
          // 格式化活动开始和结束日期
          params.start_date = formatDate_yyyyMMdd(params.start_date);
          params.end_date = formatDate_yyyyMMdd(params.end_date);
          if (params.coupon_type === 'general') {
            // 通用券不设置 target_type 、 target_id
            params.target_type = '';
            params.target_id = '';
          }
          if (params.youhui_type === 'discount') {
            // 如果打折，不用设置 coupon_amount 、 goods_min_amount
            params.coupon_amount = '';
            params.goods_min_amount = '';
          }else if (params.youhui_type === 'reduce') {
            // 如果减免，不用设置 discount_rate
            params.discount_rate = '';
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
      // 传入coupon组件里的date需要的是8位的字符串日期
      formatCouponComponentDate:function (date0) {
		    if (date0 === '') {
		      return 'yyyyMMdd'
        }else {
          let date = JSON.parse(JSON.stringify(date0));
          return formatDate_yyyyMMdd(date);
        }
      },
      formartDiscount:function (discount_rate) {      //例如：0.89
        let discount = (discount_rate*10).toFixed(2); //例如：8.90折
        if (discount.lastIndexOf(0) === 3) {
          discount = discount.substring(0,3);
          if (discount.lastIndexOf(0) === 2) {
            discount = discount.substring(0,1)
          }
        }
        return discount;
      },

    },
    mounted:function () {
		  this.randomActivityId();
    },
	}
</script>

<style scoped>

</style>
