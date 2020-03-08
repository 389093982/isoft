<template>
  <div class="tab-container">
    <ul class="tab-title">
      <li :class="cur === index ? 'active' : ''" v-for="index of tabCounts" @click="cur=index" :key="index">
        <slot :name="'titlePane' + index">title - {{index}}</slot>
      </li>
    </ul>
    <div class="tab-content">
      <div v-for="index of tabCounts" v-show="cur==index" :key="index">
        <slot :name="'tabPane' + index">tabPane - {{index}}</slot>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "Tab",
    props: {
      tabCounts: {
        type: Number,
        default: 1,
      }
    },
    data() {
      return {
        cur: 1 //默认选中第一个tab
      }
    }
  }
</script>

<style scoped>
  ul li {
    padding: 10px 50px;
    list-style: none;
    display: inline-block;
    cursor: pointer;
  }

  .tab-container {
    border: 1px solid #e5e5e5;
  }

  .tab-title {
    background: #f9f9f9;
    border-bottom: 1px solid #e5e5e5;
  }

  .tab-title .active {
    background-color: #fff;
    border-top: 3px solid #ff9d4b;
    border-left: 1px solid #eee;
    border-right: 1px solid #eee;
  }

  .tab-content div {
    padding: 5px 8px;
    min-height: 100px;
  }
</style>
