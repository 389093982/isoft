<template>
  <div v-if="advertisements && advertisements.length > 0" style="margin-top: 5px;">
    <div style="text-align:right;font-size: 12px;" v-if="showRightText">
      热门广告推荐 &nbsp;&nbsp;&nbsp;
      <a @click="$router.push({path:'/advertisement/manage'})">我也要发布广告</a>
    </div>
    <div class="clear">
      <div v-for="(advertisement, index) in advertisements"
           style="width: 120px;height: 120px;float: left;margin-right: 2px;">
        <a @click="redirectAdvertisement(advertisement.id, advertisement.linked_refer)"
           :title="advertisement.advertisement_label">
          <img :src="advertisement.linked_img" :style="styles"/>
          <div class="advertisement_label">{{advertisement.advertisement_label}}</div>
        </a>
      </div>
    </div>
  </div>
</template>

<script>
  import {GetRandomAdvertisement, RecordAdvstAccessLog} from "../../api"
  import {oneOf} from "../../tools"

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
      size: {
        type: String,
        default: 'default',
        validator(value) {
          return oneOf(value, ['small', 'large', 'default']);
        },
      }
    },
    data() {
      return {
        advertisements: null,
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
        if (result.status == "SUCCESS") {
          this.advertisements = result.advertisements;
        }
      },
    },
    mounted() {
      this.refreshRandomAdvertisement();
    },
    computed: {
      styles() {
        let style = {};
        if (this.size === "large") {
          style.width = '120px';
          style.height = '80px';
        } else {
          style.width = '120px';
          style.height = '80px';
        }
        return style;
      }
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
    padding-left: 10px;
    background-color: rgba(0, 0, 0, 0.6);
    color: white;
    width: 120px;
    height: 30px;
    position: relative;
    transition: all ease-in 1s;
    display: block;
    top: -37px;
  }

  a:hover .advertisement_label {
    display: none;
    top: 0px;

  }
</style>
