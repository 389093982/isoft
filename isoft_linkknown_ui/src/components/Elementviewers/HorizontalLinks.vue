<template>
  <ElementsLoader :placement_name="placement_name" @onLoadElement="onLoadElement">
    <div style="padding: 10px;">
      <div style="float: left;width: 92%">
        <span style="font-size: 20px;margin-right: 15px"><B><Icon type="md-search" style="font-size: 30px" />{{placement_label}}</B></span>
        <span v-for="element in filter_elements" style="margin-left: 10px">
        <IBeautifulLink @onclick="openLink(element)">{{element.element_label}}</IBeautifulLink>
      </span>
      </div>
      <div v-if="elements.length > minLen" style="float: right;width: 8%;margin-top: 3px">
        <span style="cursor: pointer;font-size: 12px;color: grey" @click="changeShowMore()">
          <span v-if="showMore"><Icon type="ios-arrow-down" /></span>
          <span v-else><Icon type="ios-arrow-up" /></span>
          查看更多
        </span>
      </div>
      <div style="clear: both"></div>

    </div>
  </ElementsLoader>
</template>

<script>
  import ElementsLoader from "../Background/CMS/ElementsLoader"
  import IBeautifulLink from "../Common/link/IBeautifulLink"

  export default {
    name: "HorizontalLinks",
    components: {IBeautifulLink, ElementsLoader},
    props: {
      placement_name: {
        type: String,
        default: '',
      }
    },
    data() {
      return {
        filter_elements: [],
        elements: [],
        minLen:10,
        placement_label: '',
        showMore:false,
      }
    },
    methods: {
      openLink: function (element) {
        window.location.href = element.linked_refer;
      },
      onLoadElement: function (placement_label, elements) {
        this.placement_label = placement_label;
        this.elements = elements;
        this.filter_elements = this.elements.slice(0,this.minLen);
      },
      changeShowMore:function () {
        this.showMore = !this.showMore;
        this.showMore ? this.filter_elements = this.elements : this.filter_elements = this.elements.slice(0,this.minLen);

      }
    },
  }
</script>

<style scoped>

</style>
