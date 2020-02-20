<template>
  <div>
    <Row>
      <Col span="12">
        <div class="isoft_bg_white isoft_pd10 mr5">
          <EditAdvertisement ref="editAdvertisement" @handleSubmit="handleAdvertisementSubmit"/>
        </div>
      </Col>
      <Col span="12">
        <div class="isoft_bg_white isoft_pd10">
          <IBeautifulCard title="我的广告清单">
            <div slot="content" style="padding: 10px;">
              <div v-if="advertisements && advertisements.length > 0">
                <div v-for="(advertisement,index) in advertisements"
                     style="border-bottom: 1px solid #eee;padding: 10px 0;">
                  <p>广告显示名称:
                    <a @click="$router.push({path:'/advertisement/accesslog',query:{id:advertisement.id}})">
                      {{advertisement.advertisement_label}}
                    </a>
                  </p>
                  <p>链接类型: {{advertisement.linked_type}}</p>
                  <p>链接地址: {{advertisement.linked_refer}}</p>
                  <p>显示图片:
                    <img :src="advertisement.linked_img" style="width: 150px;height: 100px;"/>
                  </p>
                  <p>联系人：{{loginUserName}}</p>
                  <p>广告状态:
                    <span v-if="advertisement.is_valid === 1">已生效</span>
                    <span v-else>等待审核</span>
                  </p>
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
      </Col>
    </Row>
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
          this.$Message.error(result.insensitiveErrorMsg);
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
