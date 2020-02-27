<template>
  <ElementsLoader :placement_name="placement_name" @onLoadElement="onLoadElement">
    <IBeautifulCardOrange :title="placement_label">
      <ul slot="content">
        <li v-for="(element, index) in elements">
            <span style="float:right;color: #999;font-size: 12px;"><Time :time="element.created_time" type="date"/></span>
            <a target="_blank" :href="element.linked_refer">
              <img class="imgIcon" src="../../../assets/icon_b.png"/>{{element.element_label}}
              <img v-if="index < 5" src="../../../../static/images/smallicon/news.gif">
            </a>
        </li>
      </ul>
      <span slot="header_right">
    </span>
    </IBeautifulCardOrange>
  </ElementsLoader>
</template>

<script>
  import IBeautifulCardOrange from "../../Common/card/IBeautifulCardOrange"
  import ElementsLoader from "../../Background/CMS/ElementsLoader"
  import IBeautifulLink from "../../Common/link/IBeautifulLink";

  export default {
    name: "NewsNotice",
    components: {IBeautifulLink, IBeautifulCardOrange, ElementsLoader},
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
  a {
    color: black;
  }

  a:hover {
    color: red;
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
