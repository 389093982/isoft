<template>
  <div>
    <div class="isoft_bg_white">
      <div class="bg01" style="width: 100%;height: 80px;overflow: hidden;padding: 20px;">
        <Row>
          <Col span="16">
            <p class="isoft_font_white" style="font-size: 24px;font-weight: 300;">温馨提示：</p>
            <p class="isoft_font_white" style="margin-top: 5px;">开通会员，全站资源免费下载</p>
          </Col>
          <Col span="8">
            <Button type="default" ghost style="cursor:pointer;margin-top: 5px" @click="openVip">
              <Icon type="md-arrow-round-forward" />立即开通vip
            </Button>
          </Col>
        </Row>
      </div>
    </div>

    <div style="display: flex">
      <div style="width: 70%;background-color: white;min-height: 500px">
        <div v-if="resource" class="isoft_bg_white isoft_pd20" style="margin: 20px 0 0 40px ">
          <Row>
            <Col span="4">
              <p style="font-size: 15px;color: #777;">您下载的文件是：</p>
            </Col>
            <Col span="10">
              <h4>{{resource.resource_name}}</h4>
            </Col>
            <Col span="10">
              <Button type="default" style="color: rgba(255,115,14,0.61)" @click="downloadResource(resource)">
                <Icon type="md-cloud-download" style="font-size: 20px"/>&nbsp;立刻下载
              </Button>
              <Button type="default" style="color: rgba(255,115,14,0.61)" @click="$router.push({path:'/resource/resourceList'})">
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
      <div style="width: 25%;margin-left: 5px;margin-top: 5px;background-color: white">
        <img src="../../../static/images/common_img/linkknown_tip_share.jpg" height="480" width=100%/>
      </div>
    </div>

  </div>
</template>

<script>
  import {DownloadResourceFile, GetResourceInfo} from "../../api"
  import {CheckHasLoginConfirmDialog} from "../../tools/index"

  export default {
    name: "ResourceDownload",
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
      background-image: url("../../../static/images/resourceList/banner1.jpg");
    }
    25% {
      background-image: url("../../../static/images/resourceList/banner2.jpg");
    }
    50% {
      background-image: url("../../../static/images/resourceList/banner3.jpg");
    }
    75% {
      background-image: url("../../../static/images/resourceList/banner2.jpg");
    }
    100% {
      background-image: url("../../../static/images/resourceList/banner1.jpg");
    }
  }
</style>
