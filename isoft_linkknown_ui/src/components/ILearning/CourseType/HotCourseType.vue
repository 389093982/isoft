<template>
  <ElementsLoader :placement_name="placement_name" @onLoadElement="onLoadElement">
    <div style="padding: 10px 0px;">
      <Row>
        <Col span="2">
          <span style="font-weight: bold;">课程大类：</span>
        </Col>
        <Col span="22">
          <div v-for="(element,index) in levelOneElements" style="display: inline-block;margin: 0 5px 5px 5px;">
            <span class="isoft_font12 isoft_hover_bottom isoft_hover_red" @click="currentElement=element">{{element.element_label}}</span>
          </div>
        </Col>
      </Row>
      <Row>
        <Col span="2">
          <span style="font-weight: bold;">详细分类：</span>
        </Col>
        <Col span="22">
          <div v-for="(element,index) in levelTwoElements" style="display: inline-block;margin: 0 5px 5px 5px;"
               v-if="currentElement != null && element.navigation_parent_id === currentElement.id">
            <span class="isoft_font12 isoft_hover_bottom isoft_hover_red"
                  @click="chooseCourseType(currentElement.element_label, element.element_label)">
              {{element.element_label}}
            </span>
          </div>
        </Col>
      </Row>
    </div>
  </ElementsLoader>
</template>

<script>
  import ElementsLoader from "../../Background/CMS/ElementsLoader"

  export default {
    name: "HotCourseType",
    components: {ElementsLoader},
    props: {
      placement_name: {
        type: String,
        default: '',
      }
    },
    data() {
      return {
        currentElement: null,
        placement_label: '',
        levelOneElements: [],
        levelTwoElements: [],
      }
    },
    methods: {
      chooseCourseType: function (course_type, course_sub_type) {
        this.$emit("chooseCourseType", course_type, course_sub_type);
      },
      onLoadElement: function (placement_label, elements) {
        this.placement_label = placement_label;
        this.levelOneElements = elements.filter(element => element.navigation_level === 0);
        this.levelTwoElements = elements.filter(element => element.navigation_level === 1);
        // 进行排序, 二级子分类元素多的放在前面
        this.levelOneElements = this.levelOneElements.sort((e1, e2) => {
          return this.levelTwoElements.filter(e => e.navigation_parent_id === e2.id).length -
            this.levelTwoElements.filter(e => e.navigation_parent_id === e1.id).length;
        });
        this.currentElement = this.levelOneElements[0];
      }
    },
  }
</script>

<style scoped>

</style>
