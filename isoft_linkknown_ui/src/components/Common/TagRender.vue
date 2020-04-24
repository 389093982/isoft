<template>
  <span>
    <span class="isoft_tag3" v-for="(tag, index) in showTags">{{tag | filterLimitFunc}}</span>
  </span>
</template>

<script>
  export default {
    name: "TagRender",
    props: {
      tags: {
        type: Array,
        default: function () {
          return ['测试1', '测试2', '测试3', '测试4', '测试5', '测试6'];
        },
      },
    },
    data() {
      return {
        showTags: [],
      }
    },
    methods: {
    },
    mounted() {
      this.showTags = this.tags.slice(0, 5);
    },
    filters: {
      // 内容超长则显示部分
      filterLimitFunc: function (value) {
        if (value && value.length > 10) {
          value = value.substring(0, 10);
        }
        return value;
      },
    }
  }
</script>

<style scoped>

</style>
