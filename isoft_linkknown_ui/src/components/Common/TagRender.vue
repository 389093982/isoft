<template>
  <span @mouseleave="showMore = false">
    <span class="isoft_tag3" v-for="(tag, index) in showTags">{{tag | filterLimitFunc}}</span>
    <span class="isoft_tag3" v-for="(tag, index) in hideTags" :style="{display: showMore ? 'inline-block': 'none'}">{{tag}}</span>
    <span class="isoft_tag3" v-if="hasMore && !showMore" @mouseenter="showMoreLazy">...</span>
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
      showMaxLen: {
        type: Number,
        default: 6,
      }
    },
    data() {
      return {
        showTags: [],
        hideTags: [],
        hasMore: false,
        showMore: false,
      }
    },
    methods: {
      showMoreLazy: function () {
        setTimeout(() => {
          this.showMore = true;
        }, 300);
      }
    },
    mounted() {
      this.hasMore = this.tags.length > 3;
      this.showTags = this.tags.slice(0, 3);
      this.hideTags = this.tags.slice(3, this.showMaxLen);
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
