<template>
  <ElementsLoader :placement_name="placement_name" @onLoadElement="onLoadElement" style="margin-left: 60px;margin-right: 60px;">
    <Row>
      <Col span="4" v-for="element in elements">
        <a href="javascript:;" style="color: #999;" @click="chooseItem(element.linked_refer)">
          <div class="item" style="padding:10px; height: 80px;">
            <Row :gutter="10">
              <Col span="6">
                <img :src="element.img_path" :alt="element.element_label" width="40px" height="40px" @error="defImg()"/>
              </Col>
              <Col span="17" offset="1" style="padding-left: 5px;">
                <p class="share_catalog_name">{{element.element_label}}</p>
                <p style="font-size: 12px;">{{element.content}}</p>
              </Col>
            </Row>
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
        placement_name: this.GLOBAL.placement_host_recommend_blog_tpyes,
        // 热门分享类型
        elements: [],
        // 当前页
        defaultImg: require('../../assets/default.png'),
      }
    },
    methods: {
      defImg() {
        let img = event.srcElement;
        img.src = this.defaultImg;
        img.onerror = null; //防止闪图
      },
      chooseItem: function (share_name) {
        this.$emit('chooseItem', share_name);
      },
      onLoadElement: function (placement_label, elements) {
        this.elements = elements;
      }
    },
  }
</script>

<style scoped>
  * {
    font-family: 'Helvetica Neue', 'STHeiti', '微软雅黑', 'Microsoft YaHei', Helvetica, Arial, sans-serif;
  }

  .item:hover {
    background: #ebebeb;
    border-radius: 10px;
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
