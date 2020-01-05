<template>
  <div>
    <div class="isoft_bg_white">
      <div style="width: 100%;height: 100px;overflow: hidden;background-color: rgba(180,0,255,0.8);padding: 20px;">
        <p class="isoft_font_white" style="font-size: 24px;font-weight: 300;">温馨提示：</p>
        <p class="isoft_font_white" style="margin-top: 5px;">
          开通会员，全站资源免费下载
        </p>
        <p class="isoft_font_white" style="cursor:pointer;text-align: right;"
           @click="">
          立即开通
        </p>
      </div>
    </div>

    <div v-if="resource" class="isoft_bg_white isoft_top10 isoft_pd20">
      <p style="font-size: 24px;font-weight: 300;">您查找的文件为：{{resource.resource_name}}</p>
      <p class="isoft_top10">{{resource.resource_desc}}</p>
      <p style="cursor:pointer;text-align: right;"
         @click="downloadResource(resource)">
        立即下载
      </p>
    </div>
  </div>
</template>

<script>
  import {DownloadResourceFile, GetResourceInfo} from "../../api"

  export default {
    name: "DownloadResource",
    data() {
      return {
        resource: null,
      }
    },
    methods: {
      downloadResource: function (resource) {
        DownloadResourceFile({id: resource.id});
      },
      refreshResourceInfo: async function (id) {
        const result = await GetResourceInfo({id: id});
        if (result.status == "SUCCESS") {
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

</style>
