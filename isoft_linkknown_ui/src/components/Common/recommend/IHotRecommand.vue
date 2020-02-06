<template>
  <ElementsLoader :placement_name="placement_name" @onLoadElement="onLoadElement">
    <div>
      <div class="isoft_auto_with title" :title="placement_label">{{placement_label}}</div>
      <div style="padding: 10px;border-top: 2px solid #edeff0;">
        <Row :gutter="50">
          <Col span="8" style="margin-top: 12px;" v-for="element in elements">
            <span style="font-size: 14px;">{{element.element_label}}</span>
            <IBeautifulButtonLink msg="点击了解详情" floatstyle="right" :hrefaddr="element.linked_refer"/>
          </Col>
        </Row>
      </div>
    </div>
  </ElementsLoader>
</template>

<script>
  import ElementsLoader from "../../Background/CMS/ElementsLoader"

  import IBeautifulButtonLink from "../../Common/link/IBeautifulButtonLink"

  export default {
    name: "IHotRecommand",
    components: {IBeautifulButtonLink, ElementsLoader},
    props: {
      placement_name: {
        type: String,
        default: '',
      }
    },
    data() {
      return {
        elements: [],
        placement_label: '',
      }
    },
    methods: {
      onLoadElement: function (placement_label, elements) {
        this.placement_label = placement_label;
        this.elements = elements;
      }
    },
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
