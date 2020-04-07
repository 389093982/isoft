<template>
  <ElementsLoader :placement_name="placement_name" @onLoadElement="onLoadElement">
    <div class="isoft_font_header">{{placement_label}}</div>
    <div class="newsItem hoverItemClass" v-for="(element, index) in elements">
        <span style="float:right;color: #999;font-size: 12px;"><Time :time="element.created_time" type="date"/></span>
        <a class="isoft_hover_red2" target="_blank" :href="element.linked_refer">
          <img class="imgIcon" src="../../../assets/icon_b.png"/>{{element.element_label}}
          <img v-if="index < 5" src="../../../../static/images/smallicon/news.gif">
        </a>
    </div>
  </ElementsLoader>
</template>

<script>
  import ElementsLoader from "../../Background/CMS/ElementsLoader"
  import IBeautifulLink from "../../Common/link/IBeautifulLink";

  export default {
    name: "NewsNotice",
    components: {IBeautifulLink, ElementsLoader},
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
  .newsItem {
    margin: 0 20px;
    padding: 10px 10px 5px 10px;
    border-top: 1px solid #eee;
    display: block;
    position: relative;
    cursor: pointer;
  }

  li {
    padding: 3px 15px 0 15px;
    list-style: none;
    font-size: 14px;
  }

  .imgIcon {
    position: relative;
    top: 2px;
    margin-right: 5px;
  }
</style>
