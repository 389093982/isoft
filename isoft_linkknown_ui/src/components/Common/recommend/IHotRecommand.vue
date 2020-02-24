<template>
  <ElementsLoader :placement_name="placement_name" @onLoadElement="onLoadElement">
    <div>
      <div style="width: 92%">
        <div style="clear: both;"></div>
        <div class="isoft_auto_with title" :title="placement_label">{{placement_label}}</div>
        <div style="padding: 10px;border-top: 2px solid #edeff0;">
          <IBeautifulLink v-for="element in filter_elements" @onclick="toTarget(element.linked_refer)" style="margin: 2px 20px 2px 0;font-size: 14px;">{{element.element_label}}</IBeautifulLink>
        </div>
      </div>
      <div v-if="elements.length > minLen">
        <show-more @changeShowMore="changeShowMore" style="position: absolute;top: 60px;right: 45px"></show-more>
      </div>
    </div>
  </ElementsLoader>
</template>

<script>
  import ElementsLoader from "../../Background/CMS/ElementsLoader"
  import ShowMore from "../../Elementviewers/showMore";

  export default {
    name: "IHotRecommand",
    components: {ShowMore, ElementsLoader},
    props: {
      placement_name: {
        type: String,
        default: '',
      }
    },
    data() {
      return {
        elements: [],
        filter_elements:[],
        minLen:10,
        placement_label: '',
      }
    },
    methods: {
      toTarget: function (url) {
        window.location.href = url;
      },
      onLoadElement: function (placement_label, elements) {
        this.placement_label = placement_label;
        this.elements = elements;
        this.filter_elements = this.elements.slice(0,this.minLen);
      },
      changeShowMore:function (showMore) {
        showMore ? this.filter_elements = this.elements : this.filter_elements = this.elements.slice(0,this.minLen);
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
