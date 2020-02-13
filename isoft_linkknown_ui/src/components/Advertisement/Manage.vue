<template>
  <div>
    <EditAdvertisement ref="editAdvertisement" @handleSubmit="handleAdvertisementSubmit"/>

    <div class="isoft_bg_white" style="padding: 10px 10px 0 10px;">
      <IBeautifulCard title="我的广告清单">
        <div slot="content" style="padding: 10px;">
          <div v-if="advertisements && advertisements.length > 0">
            <div v-for="(advertisement,index) in advertisements" style="border-bottom: 1px solid #eee;padding: 10px 0;">
              <p>广告显示名称:
                <a @click="$router.push({path:'/advertisement/accesslog',query:{id:advertisement.id}})">
                  {{advertisement.advertisement_label}}
                </a>
              </p>
              <p>链接类型: {{advertisement.linked_type}}</p>
              <p>链接地址: {{advertisement.linked_refer}}</p>
              <p>显示图片: <span :title="advertisement.linked_img">{{advertisement.linked_img}}</span>&nbsp;</p>
              <p>联系人：{{loginUserName}}</p>
              <p>广告状态: 已发布</p>
              <p>
                <a @click="editAdvertisement(advertisement.id)">编辑</a>
                <a @click="$router.push({path:'/advertisement/accesslog',query:{id:advertisement.id}})"
                   style="color: green;">
                  查看访问记录
                </a>
              </p>
            </div>
          </div>
        </div>
      </IBeautifulCard>
    </div>
  </div>
</template>

<script>
  import {GetPersonalAdvertisement} from "../../api"
  import {GetLoginUserName} from "../../tools"
  import EditAdvertisement from "./EditAdvertisement"
  import IBeautifulCard from "../Common/card/IBeautifulCard"
  import {scrollTop} from "iview/src/utils/assist"

  export default {
    name: "Manage",
    components: {IBeautifulCard, EditAdvertisement},
    data() {
      return {
        advertisements: null,
      }
    },
    methods: {
      handleAdvertisementSubmit: function () {
        this.refreshPersonalAdvertisement();
      },
      refreshPersonalAdvertisement: async function () {
        const result = await GetPersonalAdvertisement();
        if (result.status == "SUCCESS") {
          this.advertisements = result.advertisements;
        } else {
          this.$Message.error(result.errorMsg);
        }
      },
      editAdvertisement: function (id) {
        this.$refs.editAdvertisement.initData(id);
        const sTop = document.documentElement.scrollTop || document.body.scrollTop;
        scrollTop(window, sTop, 0, 1000);
      }
    },
    computed: {
      loginUserName() {
        return GetLoginUserName();
      }
    },
    mounted() {
      this.refreshPersonalAdvertisement();
    }
  }
</script>

<style scoped>


</style>
