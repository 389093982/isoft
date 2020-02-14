<template>
  <span>
    <a href="javascript:;" @click="showCourseType = true">&nbsp;选择推荐分类</a>
    <Modal
      v-model="showCourseType"
      title="热门课程分类"
      width="850"
      :footer-hide="true"
      :styles="{top: '30px'}"
      :mask-closable="false">
      <div style="height: 400px;">
        <Scroll height="400">
          <HotCourseType :placement_name="GLOBAL.placement_host_course_type_carousel"
                         @chooseCourseType="chooseCourseType"/>
        </Scroll>
      </div>
    </Modal>
  </span>
</template>

<script>
  import HotCourseType from "./HotCourseType"

  export default {
    name: "ChooseHotCourseType",
    components: {HotCourseType},
    data() {
      return {
        showCourseType: false,
      }
    },
    methods: {
      chooseCourseType: function (course_type, course_sub_type) {
        this.$emit("chooseCourseType", course_type, course_sub_type);
        this.showCourseType = false;
      }
    }
  }
</script>

<style scoped>

</style>
