<template>
  <div v-if="advertisements && advertisements.length > 0" style="margin-top: 5px;">
    <div style="text-align:right;font-size: 12px;" v-if="showRightText">
      热门广告推荐 &nbsp;&nbsp;&nbsp;
      <a @click="$router.push({path:'/advertisement/manage'})">我也要发布广告</a>
    </div>
    <div class="clear">
      <div style="display:inline-block;">
        <div v-for="(advertisement, index) in advertisements_kt" style="display:inline-block;margin-right: 2px;">
          <a @click="redirectAdvertisement(advertisement.id, advertisement.linked_refer)"
             :title="advertisement.advertisement_label">
            <img :src="advertisement.linked_img" style="width: 200px;height: 80px;"/>
            <div class="advertisement_label" style="width: 200px;">{{advertisement.advertisement_label}}</div>
          </a>
        </div>
      </div>
      <div style="display: inline-block;">
        <div v-for="(advertisement, index) in advertisements" style="display:inline-block;margin-right: 2px;">
          <a @click="redirectAdvertisement(advertisement.id, advertisement.linked_refer)"
             :title="advertisement.advertisement_label">
            <img :src="advertisement.linked_img" style="width: 120px;height: 80px;"/>
            <div class="advertisement_label" style="width: 120px;">{{advertisement.advertisement_label}}</div>
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {GetRandomAdvertisement, RecordAdvstAccessLog} from "../../api"

  export default {
    name: "RandomAdmt",
    props: {
      showRightText: {
        type: Boolean,
        default: true
      },
      advertisementAmount: {
        type: Number,
        default: 4
      },
    },
    data() {
      return {
        advertisements_kt: [],
        advertisements: [],
      }
    },
    methods: {
      redirectAdvertisement: function (id, linked_refer) {
        // 广告统计
        RecordAdvstAccessLog({id: id});
        window.open(linked_refer, "about:blank")
      },
      refreshRandomAdvertisement: async function () {
        var _this = this;
        const result = await GetRandomAdvertisement(this.advertisementAmount);
        if (result.status === "SUCCESS") {
          this.advertisements = result.advertisements;
          this.advertisements_kt = result.advertisements.filter(adv => adv.size_type === 1);
          this.advertisements = result.advertisements.filter(adv => adv.size_type === 0);
        }
      },
    },
    mounted() {
      this.refreshRandomAdvertisement();
    },
  }
</script>

<style scoped>
  a:hover img {
    border: 1px solid red;
  }

  * {
    box-sizing: border-box;
  }

  a .advertisement_label {
    text-align: center;
    height: 30px;
    line-height: 30px;
    padding: 0 0 0 10px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    background-color: rgba(0, 0, 0, 0.6);
    color: white;
    position: relative;
    visibility: visible;
    top: -37px;
  }

  a:hover .advertisement_label {
    visibility: hidden;
  }
</style>
