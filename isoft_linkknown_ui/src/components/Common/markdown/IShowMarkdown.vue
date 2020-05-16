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
  import Clipboard from 'clipboard';    // 引入剪切板
  import $ from "jquery"

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
      // 复制功能主要使用的是 clipboard.js
      let copyBtnHtml = `<div class="isoft_copy_blog_code" data-clipboard-text="${code}">复制代码块</div>`;
      var preCode = "";
      if (lang && hljs.getLanguage(lang)) {
        // highlight.js 高亮代码
        preCode = hljs.highlight(lang, code, true).value;
      } else {
        preCode = hljs.highlightAuto(code).value;
      }
      return `<div style="position: relative;">${copyBtnHtml}<span class="isoft_code">${preCode}</span></div>`;
    }
  });

  export default {
    name: "ShowMarkdown",
    props: {
      content: {
        type: String
      }
    },
    data (){
      return {
        clipboard: '',
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
    mounted () {
      this.$nextTick(() => {
        this.clipboard = new Clipboard('.isoft_copy_blog_code');
        // 复制成功失败的提示
        this.clipboard.on('success', (e) => {
          this.$Message.success('复制成功!')
        });
        this.clipboard.on('error', (e) => {
          this.$Message.error('复制失败!')
        });

        //添加行号
        $('.isoft_code').each(function (index, item) {
          var num = item.innerHTML.split('\n').length;//通过统计换行获取总行数
          var ol = $('<ol class="lineNumberOl"></ol>');
          var n = 1;
          while (n <= num) {
            ol.append($('<li class="lineNumberLi"></li>').text(n));
            n++;
          }
          $(this).before(ol);
          // 设置行号宽度
          $(ol).css({"width": num > 99 ? "36px" : "26px"});
        })
      })
    },
    destroyed () {
      this.clipboard.destroy();
    }
  }
</script>

<style scoped>
  @import "../../../assets/css/vue-markdown2.css";

  .isoft_markdown>>>li {
    margin-left: 24px;
  }

  .isoft_markdown >>> .isoft_copy_blog_code {
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

  .isoft_markdown >>> .lineNumberOl {
    position: absolute;
    top: -6px;
    bottom: -6px;
    /*width: 36px; 由 js 控制 */
    padding-right: 6px;
    padding-top: 6px;
    margin-left: -40px;
    text-align: right;
    background: #e5eaf1;
    color: #666;
  }

  .isoft_markdown >>> .lineNumberLi {
    list-style: none;
    height: 23.33px;
    margin-left: 0;
    font: 12px/22px Consolas, Menlo, Monaco, "Courier New", monospace !important;
  }
</style>
