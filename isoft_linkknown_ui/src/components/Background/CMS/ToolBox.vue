<template>
  <ElementsLoader :placement_name="placement_name" @onLoadElement="onLoadElement">
    <div class="isoft_font_header">{{placement_label}}</div>
    <ul v-if="elements.length > 0">
      <li v-for="element in elements" class="li" :title="element.element_label">
        <a :href="element.linked_refer" target="_blank">
          <img :src="element.img_path" width="30px" height="30px" @error="defImg()"/>
          <p style="font-size: 12px;" class="isoft_inline_ellipsis">{{element.element_label}}</p>
        </a>
      </li>
    </ul>
    <p v-else style="padding: 15px;">
      敬请期待
    </p>
    <div style="clear: both;"></div>
  </ElementsLoader>
</template>

<script>
  import ElementsLoader from "./ElementsLoader"

  export default {
    name: "ToolBox",
    components: {ElementsLoader},
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
        defaultImg: require('../../../../static/images/common_img/default.png'),
      }
    },
    methods: {
      defImg() {
        let img = event.srcElement;
        img.src = this.defaultImg;
        img.onerror = null; //防止闪图
      },
      onLoadElement: function (placement_label, elements) {
        this.placement_label = placement_label;
        this.elements = elements;
      }
    }
  }
</script>

<style scoped>
  a {
    color: black;
  }

  .li {
    list-style: none;
    float: left;
    width: 70px;
    height: 70px;
    padding: 10px 10px;
    text-align: center;
    border: 1px solid transparent;
    cursor: pointer;
  }

  li:hover {
    background-color: #f4f4f4;
    border: 1px solid #d0cdd2;
  }

  li:hover a {
    color: red;
  }
</style>
