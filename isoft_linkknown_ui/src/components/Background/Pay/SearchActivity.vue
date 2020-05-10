<template>
  <div>

    <!--表格展示活动-->
    <Table width="1000" border :columns="activityColumns" :data="activityDatas" size="small"></Table>

    <!--分页-->
    <div style="text-align: center;margin-top: 10px">
      <Page :total="page.totalCount" :page-size="page.offset" :current="page.currentPage" show-total show-sizer @on-change="pageChange" @on-page-size-change="pageSizeChange"/>
    </div>

    <!--修改活动信息-->
    <Modal v-model="updateActivityModal" title="更新活动信息" @on-ok="updateActivity" @on-cancel="cancel" width="600" he>

      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100">
        <div style="display: flex">
          <FormItem label="活动ID" prop="activity_id">
            <b>{{formValidate.activity_id}}</b>
          </FormItem>
          <FormItem label="举办方" prop="organizer">
            <b>{{formValidate.organizer}}</b>
          </FormItem>
        </div>
        <FormItem label="活动描述" prop="activity_desc">
          <Input v-model.trim="formValidate.activity_desc" placeholder="例如：xxx感恩大回馈！快来领取5折！优惠券！"/>
        </FormItem>
        <div style="display: flex">
          <FormItem label="活动类型" prop="activity_type">
            <RadioGroup v-model="formValidate.activity_type">
              <Radio label='coupon'>优惠券</Radio>
              <!--<Radio label='hongbao'>红包</Radio>-->
            </RadioGroup>
          </FormItem>
          <FormItem v-if="formValidate.activity_type==='coupon'" label="优惠券数量" prop="type_entity_account">
            <b>{{formValidate.type_entity_account}}</b>
          </FormItem>
          <FormItem v-else-if="formValidate.activity_type==='hongbao'" label="红包数量" prop="type_entity_account">
            <Input type="number" v-model.trim="formValidate.type_entity_account"/>
          </FormItem>
        </div>
        <FormItem label="活动开始日期" prop="start_date">
          <DatePicker type="date" placeholder="活动开始日期" v-model="formValidate.start_date"></DatePicker>
        </FormItem>
        <FormItem label="活动结束日期" prop="end_date">
          <DatePicker type="date" placeholder="活动结束日期" v-model="formValidate.end_date"></DatePicker>
        </FormItem>
      </Form>
    </Modal>

  </div>
</template>
<script>
  import {QueryPagePayActivity,UpdateActivity} from "../../../api/index"
  import {formatDate_yyyyMMdd} from "../../../tools/index"

  export default {
    name: "SearchActivity",
    data () {
      return {
        page:{totalCount:0,currentPage:1,offset:10},
        activityColumns: [
          {
            title: '活动ID',
            key: 'activity_id',
            width: 180,
            fixed: 'left'
          },
          {
            title: '活动描述',
            key: 'activity_desc',
            width: 200,
          },
          {
            title: '活动类型',
            key: 'activity_type',
            width: 100,
          },
          {
            title: '数量',
            key: 'type_entity_account',
            width: 80,
          },
          {
            title: '已领取',
            key: 'received_entity_account',
            width: 80,
          },
          {
            title: '已使用',
            key: 'used_entity_account',
            width: 80,
          },
          {
            title: '举办方',
            key: 'organizer',
            width: 150,
          },
          {
            title: '活动开始日期',
            key: 'start_date',
            width: 120,
          },
          {
            title: '活动结束日期',
            key: 'end_date',
            width: 120,
          },
          {
            title: '最后更新人',
            key: 'last_updated_by',
            width: 200,
          },
          {
            title: '最后更新时间',
            key: 'last_updated_time',
            width: 200,
          },
          {
            title: '创建人',
            key: 'created_by',
            width: 200,
          },
          {
            title: '创建时间',
            key: 'created_time',
            width: 150,
            fixed: 'right'
          },
          {title: '操作',                     key: 'action',                   width: 80,         fixed: 'right',
            render: (h, params) => {
              return h('div', [
                h('Button', {
                  props: {type: 'button', size: 'small'},
                  on:{
                    click: () => {
                      this.updateActivityModal = true;
                      let temp = this.activityDatas[params.index];
                      this.formValidate.activity_id = temp.activity_id;
                      this.formValidate.activity_desc = temp.activity_desc;
                      this.formValidate.activity_type = temp.activity_type;
                      this.formValidate.type_entity_account = temp.type_entity_account;
                      this.formValidate.organizer = temp.organizer;
                      this.formValidate.start_date = temp.start_date;
                      this.formValidate.end_date = temp.end_date;
                    }
                  }
                }, '修改'),
              ]);
            }
          }
        ],
        activityDatas: [],
        updateActivityModal:false,
        formValidate:{
          //活动信息
          activity_id: '',
          activity_desc: '',
          activity_type: '',
          type_entity_account: '',
          organizer:'',
          start_date:'',
          end_date:'',
        },
        ruleValidate:{
          activity_desc: [
            {required: true,message:'活动描述必填', trigger: 'change blur'}
          ],
          start_date: [
            {required: true,type: 'date',message:'活动开始日期必填', trigger: 'change blur'}
          ],
          end_date: [
            {required: true,type: 'date',message:'活动结束日期必填', trigger: 'change blur'}
          ],
        },
      }
    },
    methods: {
      refreshPayActivity: async function () {
        let params = {
          'currentPage':this.page.currentPage,
          'offset':this.page.offset,
        };
        const result = await QueryPagePayActivity(params);
        if (result.status === 'SUCCESS') {
          this.page.totalCount = result.paginator.totalcount;
          this.activityDatas = result.activityDatas;
        }
      },
      pageChange:function (page) {
        this.page.currentPage = page;
        this.refreshPayActivity()
      },
      pageSizeChange:function (pageSize) {
        this.page.offset = pageSize;
        this.refreshPayActivity()
      },
      updateActivity:async function () {
        //长度不为8的都进来做格式化
        this.formValidate.start_date = formatDate_yyyyMMdd(this.formValidate.start_date);
        //长度不为8的都进来做格式化
        this.formValidate.end_date = formatDate_yyyyMMdd(this.formValidate.end_date);

        if (this.validateParams()) {
          let params = {
            'activity_id':this.formValidate.activity_id,
            'activity_desc':this.formValidate.activity_desc,
            'start_date':this.formValidate.start_date,
            'end_date':this.formValidate.end_date,
          };
          const result = await UpdateActivity(params);
          if (result.status === 'SUCCESS') {
            this.$Message.success('更新成功！');
          }else {
            this.$Message.error(result.errorMsg)
          }
        }
        this.refreshPayActivity();
      },
      validateParams:function () {
        let flag = true;
        if (this.formValidate.activity_desc === '') {
          this.$Message.error('活动描述必填！');
          flag = false;
        }
        if (this.formValidate.start_date.length !== 8) {
          this.$Message.error('开始日期必填！');
          flag = false;
        }
        if (this.formValidate.end_date.length !== 8) {
          this.$Message.error('结束日期必填！');
          flag = false;
        }
        if (this.formValidate.end_date < this.formValidate.start_date){
          this.$Message.error('活动结束日期，不能小于开始日期！');
          flag = false;
        }
        return flag;
      }

    },
    mounted:function () {
      this.refreshPayActivity();
    },
  }
</script>

<style scoped>

</style>
