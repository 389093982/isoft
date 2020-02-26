<template>
  <div>
    <div style="padding: 10px 0px;">
      <Row>
        <Col span="2">
          <span style="font-weight: bold;">课程大类：</span>
        </Col>
        <Col span="22">
          <div v-for="course_type in course_types" style="display: inline-block;margin: 0 5px 5px 5px;">
            <span class="isoft_font12 isoft_hover_bottom isoft_hover_red" @click="loadSubCourseType(course_type)">{{course_type.course_type}}</span>
          </div>
        </Col>
      </Row>
      <Row>
        <Col span="2">
          <span style="font-weight: bold;">详细分类：</span>
        </Col>
        <Col span="22">
          <div v-for="sub_course_type in sub_course_types" style="display: inline-block;margin: 0 5px 5px 5px;">
            <span class="isoft_font12 isoft_hover_bottom isoft_hover_red"
                  @click="chooseCourseType(current_course_type, sub_course_type.course_sub_type)">{{sub_course_type.course_sub_type}}</span>
          </div>
        </Col>
      </Row>
    </div>
  </div>
</template>

<script>
  import {GetAllCourseSubType, GetAllCourseType} from "../../../api"

  export default {
    name: "TotalCourseType",
    components: {},
    data() {
      return {
        course_types: [],
        current_course_type: '',
        sub_course_types: [],
      }
    },
    methods: {
      refreshCourseType: async function () {
        const result = await GetAllCourseType();
        if (result.status == "SUCCESS") {
          this.course_types = result.course_types;
          this.loadSubCourseType(result.course_types[0]);
        }
      },
      loadSubCourseType: async function (course_type) {
        const result = await GetAllCourseSubType(course_type.course_type);
        if (result.status == "SUCCESS") {
          this.current_course_type = course_type;
          this.sub_course_types = result.sub_course_types;
        }
      },
      chooseCourseType: function (course_type, course_sub_type) {
        this.$emit("chooseCourseType", course_type, course_sub_type);
      }
    },
    mounted: function () {
      this.refreshCourseType();
    }
  }
</script>

<style scoped>

</style>
