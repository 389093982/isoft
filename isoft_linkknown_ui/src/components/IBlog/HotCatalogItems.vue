<template>
  <ElementsLoader :placement_name="placement_name" @onLoadElement="onLoadElement" style="margin-left: 60px;margin-right: 60px;">
    <Row>
      <Col span="4" v-for="(element,index) in elements">
        <a href="javascript:;" style="color: #999;" @click="chooseItem(index,element.linked_refer)">
          <div class="item" style="height: 145px;text-align: center;padding: 8px 8px 0 8px " :style="{backgroundColor:index===currentClickIndex?'rgba(172,168,167,0.2)':''}">
            <img :src="element.img_path" :alt="element.element_label" width="100%" height="80px" @error="defImg()"/>
            <div class="share_catalog_name" style="text-align: center;" :style="{color:index===currentClickIndex?'red':''}">
              <p>{{element.element_label}}</p>
              <p style="font-size: 12px;">{{element.content}}</p>
            </div>
          </div>
        </a>
      </Col>
    </Row>
  </ElementsLoader>
</template>

<script>
  import ElementsLoader from "../Background/CMS/ElementsLoader";

  export default {
    name: "HotCatalogItems",
    components: {ElementsLoader},
    data() {
      return {
        currentClickIndex:'',
        placement_name: this.GLOBAL.placement_host_recommend_blog_tpyes,
        // 热门分享类型
        elements: [],
        // 当前页
        defaultImg: require('../../../static/images/common_img/default.png'),
      }
    },
    methods: {
      defImg() {
        let img = event.srcElement;
        img.src = this.defaultImg;
        img.onerror = null; //防止闪图
      },
      chooseItem: function (index,share_name) {
        this.currentClickIndex = index;
        this.$emit('chooseItem', share_name);
      },
      onLoadElement: function (placement_label, elements) {
        this.elements = elements;
      }
    },
  }
</script>

<style scoped>
  .item:hover {
    background: rgba(176, 173, 171, 0.15);
  }

  .item:hover img {
    transform: rotateY(360deg)
  }

  .item img {
    transition: transform 1s ease-in
  }

  .share_catalog_name {
    color: green;
  }

  .share_catalog_name:hover {
    color: red;
  }
</style>
