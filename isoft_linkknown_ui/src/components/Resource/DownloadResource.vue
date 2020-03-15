<template>
  <div>
    <div class="isoft_bg_white">
      <div class="bg01" style="width: 100%;height: 150px;overflow: hidden;padding: 20px;">
        <p class="isoft_font_white" style="font-size: 24px;font-weight: 300;">温馨提示：</p>
        <p class="isoft_font_white" style="margin-top: 5px;">
          开通会员，全站资源免费下载
        </p>
        <Button type="default" ghost style="cursor:pointer;text-align: center;margin-left: 860px" @click="openVip">
          <Icon type="md-arrow-round-forward" />立即开通vip
        </Button>
      </div>
    </div>

    <div v-if="resource" class="isoft_bg_white isoft_top10 isoft_pd20">
      <Row>
        <Col span="3">
          <p style="font-size: 15px;color: #777;">您下载的文件是：</p>
        </Col>
        <Col span="10">
          <h4>{{resource.resource_name}}</h4>
        </Col>
        <Col span="11">
          <Button type="default" style="color: rgba(255,115,14,0.61)" @click="downloadResource(resource)">
            <Icon type="md-cloud-download" style="font-size: 20px"/>&nbsp;立刻下载
          </Button>
          <Button type="default" style="color: rgba(255,115,14,0.61)" @click="$router.push({path:'/resource/list'})">
            <Icon type="ios-undo" style="font-size: 20px"/>&nbsp;返回
          </Button>
        </Col>
      </Row>
      <Row>
        <Col span="3">
          <p style="font-size: 15px;color: #777;">具体描述:</p>
        </Col>
        <Col span="21">
          <div style="width: 400px">
            {{resource.resource_desc}}
          </div>
        </Col>
      </Row>
    </div>
  </div>
</template>

<script>
  import {DownloadResourceFile, GetResourceInfo} from "../../api"
  import {CheckHasLoginConfirmDialog} from "../../tools/index"

  export default {
    name: "DownloadResource",
    data() {
      return {
        resource: null,
      }
    },
    methods: {
      openVip:function(){
        CheckHasLoginConfirmDialog(this, {path: '/vipcenter/vipIntroduction'});
      },
      downloadResource: function (resource) {
        DownloadResourceFile({id: resource.id});
      },
      refreshResourceInfo: async function (id) {
        const result = await GetResourceInfo({id: id});
        if (result.status === "SUCCESS") {
          this.resource = result.resource;
        } else {
          this.$Message.error(result.errorMsg);
        }
      }
    },
    mounted() {
      this.refreshResourceInfo(this.$route.query.id);
    }
  }
</script>

<style scoped>
  .bg01 {
    animation: toggle_bg 30s infinite;
  }

  @keyframes toggle_bg {
    0% {
      background-image: url("../../../static/images/banner1.jpg");
    }
    25% {
      background-image: url("../../../static/images/banner2.jpg");
    }
    50% {
      background-image: url("../../../static/images/banner3.jpg");
    }
    75% {
      background-image: url("../../../static/images/banner2.jpg");
    }
    100% {
      background-image: url("../../../static/images/banner1.jpg");
    }
  }
</style>
