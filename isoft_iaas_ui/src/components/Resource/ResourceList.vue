<template>
  <div>
    <div class="isoft_bg_white">
      <div style="width: 100%;height: 100px;overflow: hidden;background-color: rgba(0,21,228,0.5);padding: 20px;">
        <p class="isoft_font_white" style="font-size: 24px;font-weight: 300;">共享资源</p>
        <p class="isoft_font_white" style="margin-top: 5px;">
          许多精品资源文件汇集，涵盖it、科技、办公等全部资源，为互联网、行政、设计等领域从业者打造。
        </p>
        <p class="isoft_font_white" style="cursor:pointer;text-align: right;"
           @click="uploadResource">
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

      <p class="isoft_font14" style="padding:15px;font-weight: 600;">
        大家都在寻找适合自己的作品，我们为您
        <span style="color: red;">&nbsp;&nbsp;"精选"&nbsp;&nbsp;"推荐"&nbsp;&nbsp;</span>
        以下作品：
      </p>
    </div>

    <div class="isoft_bg_white isoft_top10 isoft_pd20">
      <div v-for="(resource,index) in resources" style="padding: 10px;border-bottom: 1px solid #eee;">
        <h4>{{resource.resource_name}}</h4>
        <p>介绍：{{resource.resource_desc}}</p>
        <p>
          <Row class="isoft_font12">
            <Col span="4"><span>下载所需积分：<span style="color:red;">{{resource.points}}</span> </span></Col>
            <Col span="4">上传时间：<span style="color:red;"><Time :time="resource.last_updated_time" :interval="1"/></span>
            </Col>
            <Col span="4"><span>已下载：<span style="color:red;">{{resource.downloads}}</span> 次 </span></Col>
            <Col span="4"><a @click="downloadResource(resource)">立刻下载</a></Col>
            <Col span="8">
              <IBeautifulLink>内容真实 (10)</IBeautifulLink>&nbsp;&nbsp;&nbsp;
              <IBeautifulLink>内容不真实 (10)</IBeautifulLink>
            </Col>
          </Row>
        </p>
      </div>
    </div>
  </div>
</template>

<script>
  import {FilterPageResourceList} from "../../api"
  import {CheckHasLoginConfirmDialog} from "../../tools";

  export default {
    name: "ResourceList",
    data() {
      return {
        resources: null,
        resource_fl: ["热门分类1", "热门分类2", "热门分类3", "热门分类4"],
        resource_hy: ["热门行业1", "热门行业2", "热门行业3", "热门行业4"],
        resource_gs: ["doc", "docx", "xls", "xlsx", "ppt", "pptx", "zip"],
      }
    },
    methods: {
      uploadResource: function () {
        CheckHasLoginConfirmDialog(this, {path: '/resource/uploadResource'});
      },
      downloadResource: function (resource) {
        CheckHasLoginConfirmDialog(this, {path: '/resource/downloadResource', query: {id: resource.id}});
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
