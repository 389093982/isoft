<template>
  <div>
    <div class="isoft_bg_white">
      <div style="width: 100%;height: 100px;overflow: hidden;background-color: rgba(0,21,228,0.5);padding: 20px;">
        <p class="isoft_font_white" style="font-size: 24px;font-weight: 300;">简历模板</p>
        <p class="isoft_font_white" style="margin-top: 5px;">
          2万精品简历模板内容，每日更新10+涵盖科技、简约、商务等风格精品简历模板，为互联网、行政、设计等领域从业者打造。
        </p>
        <p class="isoft_font_white" style="cursor:pointer;text-align: right;"
           @click="$router.push({path:'/office/uploadResource'})">
          上传资源赚钱
        </p>
      </div>

      <div class="isoft_pd10">
        <p class="isoft_top10">
          分类：
          <Tag>全部</Tag>
          <Tag v-for="(fl, index) in resource_fl">{{fl}}</Tag>
        </p>
        <p class="isoft_top10">
          行业：
          <Tag>全部</Tag>
          <Tag v-for="(hy, index) in resource_hy">{{hy}}</Tag>
        </p>
        <p class="isoft_border_bottom isoft_top10" style="padding-bottom: 10px;">
          格式：
          <Tag>全部</Tag>
          <Tag v-for="(gs, index) in resource_gs">{{gs}}</Tag>
        </p>
      </div>

      <p class="isoft_font12" style="padding:15px;font-weight: 600;">
        大家都在寻找适合自己的作品，我们为您
        <span style="color: red;">&nbsp;&nbsp;"精选"&nbsp;&nbsp;"推荐"&nbsp;&nbsp;</span>
        以下作品：
      </p>
    </div>

    <div class="isoft_bg_white isoft_top10">
      <Row>
        <Col class="box" span="8" v-for="(resource,index) in resources" style="padding: 5px;">
          <div
            style="min-height: 180px;background: linear-gradient(to right, rgba(0,243,47,0.2), rgba(0,243,47,0.05));padding: 10px;">
            <div style="display:flex;">
              <div class="box_img">
                <img v-if="resource.resource_path" :src="resource.resource_path" height="160px" width="120px"/>
                <img v-else src="../../assets/default.png" height="160px" width="120px"/>
              </div>
              <div class="box_info" style="word-wrap: break-word;overflow:hidden;margin-left: 5px;">
                <p style="font-size: 16px;font-weight: 400;color: #666;">{{resource.resource_name}}</p>
                <p class="hovered hvr-grow hoverLinkColor" style="font-size: 12px;font-weight: 400;">
                  {{resource.resource_desc}}</p>
              </div>
            </div>
            <div style="text-align: right;">
              <Icon style="color: #ff7e00;cursor: pointer;" :size="20" type="md-download"
                    @click="downloadResource(resource)"/>
            </div>
          </div>
        </Col>
      </Row>
    </div>
  </div>
</template>

<script>
  import {FilterPageResourceList} from "../../api"
  import {CheckHasLoginConfirmDialog} from "../../tools";

  export default {
    name: "OfficeList",
    data() {
      return {
        resources: null,
        resource_fl: ["热门分类1", "热门分类2", "热门分类3", "热门分类4"],
        resource_hy: ["热门行业1", "热门行业2", "热门行业3", "热门行业4"],
        resource_gs: ["doc", "docx", "xls", "xlsx", "ppt", "pptx", "zip"],
      }
    },
    methods: {
      downloadResource: function (resource) {
        CheckHasLoginConfirmDialog(this, {path: '/office/downloadResource', query: {id: resource.id}});
      },
      refreshResourceList: async function () {
        const result = await FilterPageResourceList();
        if (result.status == "SUCCESS") {
          this.resources = result.resources;
        }
      }
    },
    mounted() {
      this.refreshResourceList();
    }
  }
</script>

<style scoped>

</style>
