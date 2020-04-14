<template>
  <div>
    <!-- isoft_markdown 样式主要用于调整 iview 框架导致的缩进问题 -->
    <div class="markdown isoft_markdown isoft_word_break" v-html="compiledMarkdown"></div>
  </div>
</template>

<script>
  let marked = require('marked');
  let hljs = require('highlight.js');
  import 'highlight.js/styles/default.css';

  marked.setOptions({
    renderer: new marked.Renderer(),
    gfm: true,
    tables: true,
    breaks: false,
    pedantic: false,
    sanitize: false,
    smartLists: true,
    smartypants: false,
    highlight: function (code, lang) {
      let copyBtnHtml = `<div class="isoft_copy">复制代码块</div>`;
      var preCode = "";
      if (lang && hljs.getLanguage(lang)) {
        // highlight.js 高亮代码
        preCode = hljs.highlight(lang, code, true).value;
      } else {
        preCode = hljs.highlightAuto(code).value;
      }
      return `<div style="position: relative;">${copyBtnHtml}${preCode}</div>`;
    }
  });

  export default {
    name: "ShowMarkdown",
    props: {
      content: {
        type: String
      }
    },
    methods: {
      renderImgSize: function (htmlStr) {
        var _htmlStr = htmlStr;
        //匹配图片（g表示匹配所有结果i表示区分大小写）
        var imgReg = /<img.*?(?:>|\/>)/gi;
        //匹配src属性
        var srcReg = /src=[\'\"]?([^\'\"]*)[\'\"]?/i;
        var arr = htmlStr.match(imgReg);
        if (arr !== null && arr.length>0) {
          for (var i = 0; i < arr.length; i++) {
            var src = arr[i].match(srcReg);
            // src[0] src="http://xxxxx.jpg?width=100px&height=200px"
            // src[1] http://xxxxx.jpg?width=100px&height=200px
            // 提取图片链接地址中的尺寸
            let imgSize = this.parseImgSize(src[1]);
            if (imgSize.width && imgSize.height) {
              let replaceUrl = src[0] + "' width='" + imgSize.width + "' height='" + imgSize.height + "'";
              _htmlStr = _htmlStr.replace(src[0], replaceUrl);
            }
          }
        }
        return _htmlStr;
      },
      parseImgSize: function (url) {
        var imgSize = {};
        if (url.indexOf("?") !== -1) {
          var str = url.substring(url.indexOf("?") + 1);
          let strs = str.split("&");
          for (var i = 0; i < strs.length; i++) {
            var kv = strs[i].split("=");
            imgSize[kv[0]] = (kv[1]);
          }
        }
        return imgSize;
      }
    },
    computed: {
      compiledMarkdown() {
        var htmlStr = marked(this.content || '', {
          sanitize: true
        });
        htmlStr = this.renderImgSize(htmlStr);
        return htmlStr;
      }
    },
  }
</script>

<style scoped>
  @import "../../../assets/css/vue-markdown2.css";

  .isoft_markdown>>>li {
    margin-left: 24px;
  }

  .isoft_markdown >>> .isoft_copy {
    position: absolute;
    right: 4px;
    top: 4px;
    font-size: 12px;
    color: #4d4d4d;
    background-color: white;
    padding: 2px 8px;
    margin: 4px;
    border-radius: 4px;
    cursor: pointer;
    box-shadow: 0 2px 4px rgba(0,0,0,0.05), 0 2px 4px rgba(0,0,0,0.05);
  }
</style>
