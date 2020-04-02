<template>
  <div style="height: 45px;">
    <div style="position: relative;float: right;">
      <div>
        <input class="search_input" title="请输入搜索内容" placeholder="请输入搜索内容..." v-model.trim="search_data" @keyup.enter="submitFunc"
          @focus="handleFocus" @blur="showRecently = false">
        <input class="submit" type="submit" title="提交" @click="submitFunc">

        <div v-if="showRecently" class="recentlySearchBox" style="position: relative; top: 44px;background-color: green;">
          <div class="recentlySearch">
            <div class="isoft_font12" style="border-bottom: 1px solid #ffa2b9;padding-bottom: 5px;">
              <span @mouseenter="searchPattern = 1">最近搜索</span>
              &nbsp;|&nbsp;
              <span @mouseenter="searchPattern = 2">热搜榜</span>
            </div>
            <div>
              <span class="searchTag isoft_point_cursor" :style="searchTagColor(index)"
                    v-if="searchPattern === 1" v-for="(searchItem, index) in searchItems1" :key="index">{{searchItem}}</span>
              <span class="searchTag isoft_point_cursor" :style="searchTagColor(index)"
                    v-if="searchPattern === 2" v-for="(searchItem, index) in searchItems2" :key="index">{{searchItem}}</span>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
  import {GenerateRandom} from "../../../tools";

  export default {
    name: "Search",
    data() {
      return {
        search_data: "",
        showRecently: false,
        searchPattern: 1,  // 默认 1、最近搜索 2、热搜榜
        searchItems1:['清明节1','清明节1','清明节1','清明节','清明节','清明节','清明节','清明节'],
        searchItems2:['清明节2','清明节2','清明节2','清明节','清明节','清明节','清明节','清明节'],
      }
    },
    methods: {
      submitFunc: function () {
        this.$emit("submitFunc", this.search_data);
      },
      handleFocus: function () {
        setTimeout(() => {
          this.showRecently = true;
        }, 500);      // 延迟 500 ms 执行和 transition 保持一致
      }
    },
    watch:{
      search_data:function () {
        this.$emit("searchDataHasChange", this.search_data);
      }
    },
    computed: {
      searchTagColor(index) {
        return function(index){
          let style = {};
          let arr = ['#D4D1FD', '#C9DEFF', '#DBFFD3', '#FFCFC9', '#FFF2CE', '#FFD0E5'];
          style.background = GenerateRandom(arr);
          return style;
        }
      }
    },
  }
</script>

<style scoped>
  .search_input {
    padding-left: 16px;
    float: left;
    width: 350px;
    background: #efefef;
    vertical-align: middle;
    height: 40px;
    box-shadow: none;
    -webkit-appearance: none;
    border: none;
    outline: 0;
    color: #333;
    font-size: 14px;
    transition: all ease-in 500ms;
  }

  .search_input:focus {
    width: 450px;
    box-shadow: 0 0 3px rgba(142, 142, 142, 0.22);
  }

  .submit {
    background: url('../../../assets/search.png') 50% 50% no-repeat;
    background-size: 24px;
    position: absolute;
    right: 10px;
    top: 15px;
    margin-top: -7px;
    width: 24px;
    height: 24px;
    border: none;
    color: transparent;
    box-sizing: initial;
    cursor: pointer;
    outline: 0;
  }

  .recentlySearch {
    position: absolute;
    width: 100%;
    min-height: 200px;
    max-height: 250px;
    box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.16);
    padding: 12px;
    background: #FFFFFF;
    border-radius: 4px;
    z-index: 10;
  }

  .searchTag {
    width: 90px;
    height: 28px;
    line-height: 28px;
    background: rgba(201, 222, 255, 1);
    border-radius: 4px;
    float: left;
    margin-right: 16px;
    margin-top: 12px;
    text-align: center;
    font-size: 12px;
  }
</style>
