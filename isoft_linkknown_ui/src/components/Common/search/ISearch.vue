<template>
  <div style="position: relative;height: 45px;">
    <div v-click-outside="onClickOutside">
      <!-- lazy 防止输入单个字符就触发 computed 计算 -->
      <input class="search_input" :class="searchInputClass" title="请输入搜索内容" :placeholder="placeholder" v-model.lazy.trim="search_data" maxlength="25" @keyup.enter="submitFunc" @focus="handleFocus">
      <input class="submit" type="submit" title="提交" @click="submitFunc">

      <div v-if="showRecently" class="recentlySearchBox">
        <div class="recentlySearch">
          <div class="isoft_font12" style="border-bottom: 1px solid #ffa2b9;padding-bottom: 5px;">
            <span class="isoft_point_cursor" @click="searchPattern = 1" style="font-weight: bold"
                  :style="{color: searchPattern === 1 ? 'red' : ''}">最近搜索</span>
            &nbsp;|&nbsp;
            <span class="isoft_point_cursor" @click="searchPattern = 2" style="font-weight: bold"
                  :style="{color: searchPattern === 2 ? 'red' : ''}">热搜榜</span>
          </div>
          <div>
            <span class="searchTag isoft_point_cursor isoft_inline_ellipsis" :style="searchTagColor(index)"
                  v-if="searchPattern === 1" v-for="(searchItem, index) in searchItems1()" :key="index"
                  @click="handleSearchItem(searchItem)">{{searchItem}}</span>
            <span class="searchTag isoft_point_cursor isoft_inline_ellipsis" :style="searchTagColor(index)"
                  v-if="searchPattern === 2" v-for="(searchItem, index) in searchItems2" :key="index"
                  @click="handleSearchItem(searchItem)">{{searchItem}}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {checkNotEmpty, GenerateRandom} from "../../../tools";

  export default {
    name: "Search",
    props:{
      longer: {
        type: Boolean,
        default: false,
      },
      searchType:{
        type: String,
        default:'default',    // 搜索类型,主要用于存储不同模块的搜索关键词
      }
    },
    data() {
      return {
        search_data: "",
        showRecently: false,
        searchPattern: 1,  // 默认 1、最近搜索 2、热搜榜
        searchItems2:['前端','mysql','java','python','golang','vue'],
        placeholderArr: ["请输入搜索内容...", "予人玫瑰，手有余香", "书山有路勤为径，学海无涯苦作舟", "如果您有好的素材，也可以分享给我们哦~"],
        placeholder: "请输入搜索内容...",
        changePlaceholderTimer: null,
      }
    },
    methods: {
      onClickOutside(event) {
        this.showRecently = false;
      },
      searchItems1: function (){
        return JSON.parse(localStorage.getItem(this.searchType)) || [];
      },
      handleSearchItem: function (search_data){
        this.search_data = search_data;
        this.showRecently = false;
        this.submitFunc();
      },
      submitFunc: function () {
        if (checkNotEmpty(this.search_data)) {
          // 缓存搜索关键词
          if (!localStorage.getItem(this.searchType)) {
            localStorage.setItem(this.searchType, JSON.stringify([]));
          }
          let cacheArr = Array.from(new Set([this.search_data].concat(JSON.parse(localStorage.getItem(this.searchType)))));
          localStorage.setItem(this.searchType, JSON.stringify(cacheArr.slice(0, 16)));

          // 通知父组件
          this.$emit("submitFunc", this.search_data);

          // 300 ms 后隐藏推荐
          setTimeout(() => {
            this.showRecently = false;
          }, 300);
        }
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
      },
      searchInputClass: function () {
        return [this.longer ? 'search_input_len_longer' : 'search_input_len_default'];
      }
    },
    mounted(){
      var _this = this;
      _this.changePlaceholderTimer = setInterval(function () {
        _this.placeholder = GenerateRandom(_this.placeholderArr);
      }, 3000);
    },
    beforeDestroy() {
      if (this.changePlaceholderTimer != null){
        clearInterval(this.changePlaceholderTimer);
      }
    }
  }
</script>

<style scoped>
  /* 设置 input 或 textarea 的 placeholder 样式 */
  input::-webkit-input-placeholder {
    /* placeholder颜色  */
    color: #aab2bd;
    /* placeholder字体大小  */
    font-size: 12px;
  }

  .search_input {
    padding-left: 16px;
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
    box-shadow: 0 0 3px rgba(142, 142, 142, 0.22);
  }

  .search_input_len_default {
    width: 350px;
  }

  .search_input_len_default:focus {
    width: 450px;
  }

  .search_input_len_longer {
    width: 500px;
  }

  .submit {
    background: url('../../../../static/images/common_img/search.png') 50% 50% no-repeat;
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
    width: 100%;
    min-height: 200px;
    max-height: 250px;
    box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.16);
    padding: 12px;
    background: #FFFFFF;
    border-radius: 4px;
  }
  .recentlySearchBox {
    width: 100%;
    position: absolute;
    bottom: -2px;
    transform: translateY(100%);
    z-index: 9999;
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
    padding: 0 5px;
    text-align: center;
    font-size: 12px;
  }
</style>
