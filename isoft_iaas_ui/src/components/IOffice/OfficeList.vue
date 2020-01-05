<template>
  <div>
    <div class="isoft_bg_white">
      <div style="width: 100%;height: 100px;overflow: hidden;background-color: rgba(0,21,228,0.5);padding: 20px;">
        <p class="isoft_font_white" style="font-size: 24px;font-weight: 300;">简历模板</p>
        <p class="isoft_font_white" style="margin-top: 5px;">
          2万精品简历模板内容，每日更新10+涵盖科技、简约、商务等风格精品简历模板，为互联网、行政、设计等领域从业者打造。
        </p>
        <p class="isoft_font_white" style="text-align: right;">上传资源赚钱</p>
      </div>

      <div class="isoft_pd10">
        <p class="isoft_top10">场景：全部贴图材质</p>
        <p class="isoft_top10">行业：全部电商微商IT互联网地产家居政府公益教育培训文化娱乐餐饮业酒店旅游医疗医药生活服务金融理财美容健身</p>
        <p class="isoft_border_bottom isoft_top10">格式：全部SKPJPGMAXAIPSDOBJC4D3DMCDREPS3DSDWGSTP</p>
        <p class="isoft_border_bottom isoft_top10">类型：全部免费专区精选精选基础基础办公办公字体字体</p>
      </div>
    </div>

    <div class="isoft_bg_white isoft_top10">
      <Row>
        <Col class="box" span="8" v-for="(data,index) in officesData" style="padding: 5px;">
          <div
            style="min-height: 180px;background: linear-gradient(to right, rgba(0,243,47,0.2), rgba(0,243,47,0.05));padding: 10px;">
            <div style="display:flex;">
              <div class="box_img">
                <img v-if="data.icon" :src="data.icon" height="160px" width="120px"/>
                <img v-else src="../../assets/default.png" height="160px" width="120px"/>
              </div>
              <div class="box_info" style="word-wrap: break-word;overflow:hidden;margin-left: 5px;">
                <p style="font-size: 16px;font-weight: 400;color: #666;">{{data.title}}</p>
                <p class="hovered hvr-grow hoverLinkColor" style="font-size: 12px;font-weight: 400;">{{data.desc}}</p>
              </div>
            </div>
            <div style="text-align: right;">
              <Icon type="md-download"/>
            </div>
          </div>
        </Col>
      </Row>
    </div>
  </div>
</template>

<script>
  export default {
    name: "OfficeList",
    data() {
      return {
        officesData: [
          {
            'title': '药生活服务金融理财美容健身',
            'icon': '',
            'desc': '药生活服务金融理财美容健身',
          }, {
            'title': '药生活服务金融理财美容健身',
            'icon': '',
            'desc': '药生活服务金融理财美容健身',
          }, {
            'title': '药生活服务金融理财美容健身',
            'icon': '',
            'desc': '药生活服务金融理财美容健身',
          }, {
            'title': '药生活服务金融理财美容健身',
            'icon': '药生活服务金融理财美容健身',
            'desc': '药生活服务金融理财美容健身',
          }, {
            'title': '药生活服务金融理财美容健身',
            'icon': '药生活服务金融理财美容健身',
            'desc': '药生活服务金融理财美容健身',
          }, {
            'title': '药生活服务金融理财美容健身',
            'icon': '药生活服务金融理财美容健身',
            'desc': '药生活服务金融理财美容健身',
          }, {
            'title': '药生活服务金融理财美容健身',
            'icon': '药生活服务金融理财美容健身',
            'desc': '药生活服务金融理财美容健身',
          }
        ]
      }
    }
  }
</script>

<style scoped>
  @keyframes isoft_flash {
    0% {
      opacity: 0.3;
    }
    30% {
      opacity: 0.4;
    }
    50% {
      opacity: 0.5;
    }
    100% {
      opacity: 1;
    }
  }

  .box:hover .box_img {
    animation: isoft_flash 1s linear 1;
  }
</style>
