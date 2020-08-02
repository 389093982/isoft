<template>
  <div>
    <div>
      注意：<br/>
      app 显示版本必须满足正则 /^[0-9]{1,2}\.[0-9]{1,2}.[0-9]{1,2}$/ <br/>
      app 比较版本必须满足正则 /^[0-9]{2}\.[0-9]{2}.[0-9]{2}$/       <br/>
    </div>

    <Button type="success" size="small" @click="addNewVersion" style="margin-bottom: 10px;">升级版本</Button>

    <ISimpleConfirmModal ref="appVersionEditModal" modal-title="新增/编辑 app版本" :modal-width="600" :footer-hide="true">
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100">
        <FormItem label="app 显示版本" prop="app_show_version">
          <Input v-model.trim="formValidate.app_show_version" placeholder="app 显示版本"></Input>
        </FormItem>
        <FormItem label="app 比较版本" prop="app_padding_version">
          <Input v-model.trim="formValidate.app_padding_version" placeholder="app 比较版本"></Input>
        </FormItem>
        <FormItem label="安卓下载地址" prop="android_download_url">
          <Input v-model.trim="formValidate.android_download_url" placeholder="安卓下载地址"></Input>
        </FormItem>
        <FormItem label="ios下载地址" prop="ios_download_url">
          <Input v-model.trim="formValidate.ios_download_url" placeholder="ios下载地址"></Input>
        </FormItem>
        <FormItem>
          <Button type="success" @click="handleSubmit('formValidate')" style="margin-right: 6px">提交</Button>
          <Button type="warning" @click="handleReset('formValidate')" style="margin-right: 6px">重置</Button>
        </FormItem>
      </Form>
    </ISimpleConfirmModal>


    <Table border :columns="columns1" :data="appVersions" size="small"></Table>
    <Page :total="total" :page-size="offset" show-total show-sizer :page-size-opts="[20,30,50]"
          :styles="{'text-align': 'center','margin-top': '10px'}"
          @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
  </div>
</template>

<script>
  import {QueryPageAppVersion,AddAppVersion} from "../../api"
  import ISimpleConfirmModal from "../../components/Common/modal/ISimpleConfirmModal";
  import {validatePatternForString} from "../../tools";

  export default {
    name: "AppVersionList",
    components: {ISimpleConfirmModal},
    data() {
      return {
        showModal: false,
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 20,
        // 搜索条件
        search: "",
        appVersions: [],
        columns1: [
          {
            title: 'app 显示版本',
            key: 'app_show_version',
            width: 200
          },
          {
            title: 'app 比较版本',
            key: 'app_padding_version',
            width: 200
          },
          {
            title: '安卓下载地址',
            key: 'android_download_url',
            width: 200
          },
          {
            title: 'ios下载地址',
            key: 'ios_download_url',
            width: 200
          },
          {
            title: '强制更新',
            key: 'force_update',
            width: 200
          },
          {
            title: '创建时间',
            key: 'created_time',
            width: 200
          },
        ],
        formValidate: {
          id: -1,
          app_show_version:'',
          app_padding_version:'',
          android_download_url:'',
          ios_download_url:'',
          force_update:'Y',
        },
        ruleValidate: {
          app_show_version: [
            {required: true, message: 'app显示版本不能为空!', trigger: 'blur'},
          ],
          app_padding_version: [
            {required: true, message: 'app比较版本不能为空!', trigger: 'blur'},
          ],
          android_download_url: [
            {required: true, message: 'android下载地址不能为空!', trigger: 'blur'},
          ],
          ios_download_url: [
            {required: true, message: 'ios下载地址不能为空!', trigger: 'blur'},
          ],
        },
      }
    },
    methods: {
      handleSubmit(name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            if (!validatePatternForString(/^[0-9]{1,2}\.[0-9]{1,2}.[0-9]{1,2}$/, this.formValidate.app_show_version)) {
              this.$Message.error("app 显示版本号不正确");
              return;
            }
            if (!validatePatternForString(/^[0-9]{2}\.[0-9]{2}.[0-9]{2}$/, this.formValidate.app_padding_version)) {
              this.$Message.error("app 比较版本号不正确");
              return;
            }

            //修改名称
            const result = await AddAppVersion(this.formValidate);
            if (result.status === "SUCCESS") {
              this.$Message.success("编辑成功!");
              this.$refs.appVersionEditModal.hideModal();
              this.refreshAppVersions();
              this.handleReset('formValidate');
            } else {
              this.$Message.error("编辑失败!");
            }
          }
        })
      },
      handleReset(name) {
        this.$refs[name].resetFields();
      },
      addNewVersion: function () {
        this.$refs.appVersionEditModal.showModal();
      },
      handleChange(page) {
        this.current_page = page;
        this.refreshAppVersions();
      },
      handlePageSizeChange(pageSize) {
        this.offset = pageSize;
        this.refreshAppVersions();
      },
      refreshAppVersions: async function () {
        const result = await QueryPageAppVersion({current_page: this.current_page, offset: this.offset});
        if (result.status === "SUCCESS") {
          this.appVersions = result.appVersions;
          this.total = result.paginator.totalcount;
        }
      }
    },
    mounted() {
      this.refreshAppVersions();
    }
  }
</script>

<style scoped>

</style>
