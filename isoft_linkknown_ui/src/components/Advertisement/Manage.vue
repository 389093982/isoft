<template>
  <div>
    <div v-if="!isVip" style="text-align: center;margin-top: 50px;">
      <OpenVipTip tip="无法发布广告"/>
    </div>

    <div v-else>
      <Row>
        <Col span="12">
          <div class="isoft_bg_white isoft_pd10 isoft_mr10" style="min-height: 600px;">
            <IBeautifulCard title="我的广告清单">
              <div slot="content" style="padding: 10px;">
                <div v-if="advertisements && advertisements.length > 0">
                  <div v-for="(advertisement,index) in advertisements"
                       style="padding: 10px;border-bottom: 2px solid #eee;">
                    <h4>{{index + 1}} 号广告位</h4>
                    <p>广告显示名称:{{advertisement.advertisement_label}}</p>
                    <p>链接类型: {{advertisement.linked_type}}</p>
                    <p>链接地址: {{advertisement.linked_refer}}</p>
                    <p>显示图片:
                      <img :src="advertisement.linked_img" style="width: 150px;height: 100px;"/>
                    </p>
                    <p>联系人：{{loginUserName}}</p>
                    <p>广告状态:
                      <span v-if="advertisement.is_valid === 1">
                      <span v-if="checkDateDiff(advertisement.created_time)"
                            class="hovered hvr-grow isoft_hover_red2">已过期</span>
                      <span v-else class="hovered hvr-grow isoft_hover_red2">已生效</span>
                    </span>
                      <span v-else class="hovered hvr-grow isoft_hover_red2">等待审核</span>
                    </p>
                    <p style="text-align: right;">
                      <IBeautifulLink class="mr5" @onclick="editAdvertisement(advertisement.id)">编辑</IBeautifulLink>
                      <IBeautifulLink class="mr5" @onclick="showAdvAccesslog(index)">访问记录</IBeautifulLink>
                    </p>
                  </div>
                </div>

                <div v-else style="text-align: center;margin-top: 50px;">
                  您还没有发布过广告,赶紧发布一条吧
                </div>
              </div>
            </IBeautifulCard>
          </div>
        </Col>
        <Col span="12">
          <div class="isoft_bg_white isoft_pd10">
            <div style="background-color: #eee;padding: 5px 20px;text-align: center;margin: 10px 0;">
              开通会员可免费发布三条广告，可实时查看广告访问记录
            </div>

            <EditAdvertisement ref="editAdvertisement" @handleSubmit="handleAdvertisementSubmit"/>
          </div>

          <div class="isoft_bg_white isoft_pd10 isoft_top10">
            <div style="background-color: #eee;padding: 5px 20px;text-align: center;margin: 10px 0;">
              精准广告或商业合作请邮件联系 <a href="mailto:389093982@qq.com">链知网管理员 389093982@qq.com</a>
            </div>

            <div class="isoft_auto_with title">广告访问记录</div>
            <div style="padding: 10px;border-top: 2px solid #edeff0;">
              <AccessLog ref="adv_accesslog"/>
            </div>
          </div>
        </Col>
      </Row>
    </div>
  </div>
</template>

<script>
  import {GetPersonalAdvertisement} from "../../api"
  import {GetLoginUserName} from "../../tools"
  import {IsVip} from "../../tools/vip"
  import EditAdvertisement from "./EditAdvertisement"
  import IBeautifulCard from "../Common/card/IBeautifulCard"
  import {scrollTop} from "iview/src/utils/assist"
  import AccessLog from "./AccessLog";
  import OpenVipTip from "../VipCenter/OpenVipTip";

  export default {
    name: "Manage",
    components: {OpenVipTip, AccessLog, IBeautifulCard, EditAdvertisement},
    data() {
      return {
        advertisements: null,
        isVip: false,
      }
    },
    methods: {
      checkDateDiff: function (dateStart) {
        dateStart = (dateStart).replace(/-/g, "/");      // 一般得到的时间的格式都是：yyyy-MM-dd hh24:mi:ss
        var dateStart = new Date(dateStart);                         // 将字符串转化为时间
        var difDay = (new Date() - dateStart) / (1000 * 60 * 60 * 24);
        return difDay > 30;
      },
      showAdvAccesslog: function (index) {
        if (this.advertisements && this.advertisements.length > 0) {
          this.$refs.adv_accesslog.refreshAdvstAccessLog(this.advertisements[index].id);
        }
      },
      handleAdvertisementSubmit: function () {
        this.refreshPersonalAdvertisement();
      },
      refreshPersonalAdvertisement: async function () {
        const result = await GetPersonalAdvertisement();
        if (result.status === "SUCCESS") {
          this.advertisements = result.advertisements.slice(0, 10);
          this.showAdvAccesslog(0);
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
      this.isVip = IsVip();
      this.refreshPersonalAdvertisement();
    }
  }
</script>

<style scoped>
  .title {
    font-size: 18px;
    font-weight: normal;
    height: 35px;
    line-height: 35px;
    font-family: "微软雅黑";
  }

  .title::after {
    content: "";
    display: block;
    height: 3px;
    border-bottom: 3px solid red;
  }
</style>
