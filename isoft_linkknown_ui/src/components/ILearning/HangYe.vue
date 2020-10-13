<template>
  <div class="hangye_box">
    <div class="header">
      <img class="icon_border" src="../../../static/images/index/icon_border.png"/>
      <a href="https://www.tiobe.com/tiobe-index/">TIOBE</a>
      <img class="icon_border" src="../../../static/images/index/icon_border.png"/>
    </div>
    <div class="content">
      <div class="left">
        <div class="header">
          <select style="float: right;" v-model="l_index">
            <option value="0">趋势图</option>
            <option value="1">最新排名</option>
            <option value="2">历年排名</option>
            <option value="3">年度语言</option>
          </select>
        </div>
        <div class="img_box">
          <img v-if="l_index == 0" src="../../../static/images/index/biancheng_1.png" title="历年编程语言占比趋势图" @click="showBig($event)"/>
          <img v-if="l_index == 1" src="../../../static/images/index/biancheng_2.png" title="2020 年编程语言最新排名" @click="showBig($event)"/>
          <img v-if="l_index == 2" src="../../../static/images/index/biancheng_3.png" title="历年编程语言排行榜" @click="showBig($event)"/>
          <img v-if="l_index == 3" src="../../../static/images/index/biancheng_4.png" title="历年年度编程语言" @click="showBig($event)"/>

          <!-- 图片放大图 -->
          <div id="enlarge_images" v-if="showBigImg">
            <div class="modalShade"></div>
            <div class="modalClose" @click="hideBig($event)">X</div>
            <img class="modalContent" :src="bigImg" >
          </div>
        </div>
      </div>
      <div class="right">
        <div class="header">
          <div class="label">精选文章</div>
          <ul style="display: inline-block;">
            <li :class="{showIndex: r_index === 0}" @click="r_index = 0">热点新闻</li>
            <li :class="{showIndex: r_index === 1}" @click="r_index = 1">行业现状</li>
            <li :class="{showIndex: r_index === 2}" @click="r_index = 2">薪资水平</li>
            <li :class="{showIndex: r_index === 3}" @click="r_index = 3">热门城市</li>

          </ul>
        </div>
        <div class="content">
          <ul>
            <li v-for="(article, index) in Array.prototype.reverse.call(articles[r_index])" @click="goToTargetLink(article.article_link)" :title="article.article_desc">
              <span v-if="index < 4" class="new">新</span> {{article.article_desc}}
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
    import {goToTargetLink} from "../../tools/index"

    export default {
      name: "HangYe",
      data(){
        return {
          l_index: 0,  // 左侧图片索引
          showBigImg: false,
          bigImg: '',

          r_index: 0,
          articles: [
            // 热点新闻
            [
              {
                'article_desc': 'php是世界上最好的语言是什么梗',
                'article_link': 'https://www.php.cn/php-weizijiaocheng-228061.html',
              },
              {
                'article_desc': '动态语言和静态语言的区别',
                'article_link': 'https://www.cnblogs.com/fishshadow/p/10920290.html',
              },
              {
                'article_desc': 'Java的发展史',
                'article_link': 'https://www.cnblogs.com/lukelook/p/13585062.html',
              },
              {
                'article_desc': '高司令 老骥伏枥，志在千里的 Java 之父',
                'article_link': 'https://www.cnblogs.com/lukelook/p/13584933.html',
              },
              {
                'article_desc': 'Sun公司的发展史',
                'article_link': 'https://blog.csdn.net/weixin_43392489/article/details/102366806',
              },
              {
                'article_desc': '拉里·埃里森在他 32 岁创办甲骨文之前的时候是什么样的一个人？性格如何？',
                'article_link': 'https://www.zhihu.com/question/20118236',
              },
              {
                'article_desc': '巴基斯坦宣布禁用TikTok 巴铁正经历我们曾经的争议',
                'article_link': 'http://news.zol.com.cn/753/7538039.html',
              },
              {
                'article_desc': '搜索引擎选择： Elasticsearch与Solr',
                'article_link': 'https://www.cnblogs.com/fosilzhou/articles/4629220.html',
              },
              {
                'article_desc': '为什么JAVA这么受欢迎？',
                'article_link': 'https://zhuanlan.zhihu.com/p/98496029',
              },
              {
                'article_desc': '容器和 VM 之间的差异',
                'article_link': 'https://www.cnblogs.com/leoshi/p/13549033.html',
              },
              {
                'article_desc': '2020年预测：为“数据时代的下一个十年”铺平道路',
                'article_link': 'https://tech.huanqiu.com/article/3wE48icGfaI',
              }
            ],
            // 行业现状
            [
              {
                'article_desc': '为什么程序员都不愿意升级 Java 8？ ',
                'article_link': 'https://www.sohu.com/a/312982064_115128',
              },
              {
                'article_desc': '甲骨文要改变 Java 发布频率，将每半年发布一个版本',
                'article_link': 'https://blog.csdn.net/redditnote/article/details/102590340',
              },
              {
                'article_desc': 'Java 技术论坛 | Java 中文开发者社区',
                'article_link': 'https://learnku.com/java',
              }
            ],
            // 薪资水平
            [
              {
                'article_desc': '2019年10月全国程序员工资统计，一半以上的职位5个月没招到人。',
                'article_link': 'https://blog.csdn.net/juwikuang/article/details/102326317',
              },
              {
                'article_desc': '2020 年 9 月程序员工资统计，工资中位数16500元！',
                'article_link': 'https://blog.csdn.net/weixin_35681869/article/details/108989040',
              },
            ],
            // 热门城市
            [
              {
                'article_desc': '实地探访：月薪 12K 的北京程序员是怎么工作生活的？',
                'article_link': 'https://www.cnblogs.com/gdjk/p/10754597.html',
              },
            ],
          ]
        }
      },
      methods: {
        goToTargetLink:function(url){
          goToTargetLink(url);
        },

        // 显示和隐藏放大图
        showBig: function (event) {
          this.showBigImg = true;
          this.bigImg= event.currentTarget.src;
          document.getElementsByTagName("body")[0].style.overflow = 'hidden';
        },
        hideBig: function (event) {
          this.showBigImg = false;
          document.getElementsByTagName("body")[0].style.overflow = 'visible';
        },
      },
    }
</script>

<style scoped>
  .hangye_box {
    width: 90%;
    margin: 50px auto;
    background-color: #fafafa;
    min-height: 200px;
  }
  .hangye_box > .header {
    text-align: center;
    height: 100px;
    line-height: 100px;
  }
  .hangye_box > .header .icon_border:first-child {
    width: 20px;
    height: 60px;
    position: relative;
    top: 15px;
    margin-right: 10px;
  }
  .hangye_box > .header .icon_border:last-child {
    width: 20px;
    height: 60px;
    position: relative;
    top: 15px;
    margin-left: 10px;
    transform: rotateY(180deg);
  }

  .hangye_box > .header > a {
    font-size: 40px;
    color: green;
  }
  .hangye_box > .content .left {
    width: 48%;
    height: 360px;
    display: inline-block;
    margin-left: 1%;
  }
  .hangye_box > .content .left .header {
    height: 40px;
    line-height: 40px;
    border-bottom: 2px solid rgba(0, 128, 0, 0.5);
  }
  .hangye_box > .content .left .img_box {
    height: 320px;
    overflow: hidden;
  }
  .hangye_box > .content .left img {
    width: 100%;
  }
  .hangye_box > .content .right {
    width: 48%;
    height: 360px;
    display: inline-block;
    vertical-align: top;
    background-color: white;
    margin-left: 1%;
  }
  .hangye_box > .content .right .header{
    height: 40px;
    line-height: 40px;
    background-color: #ececec;
    border-bottom: 2px solid rgba(0, 128, 0, 0.5);
  }

  .hangye_box > .content .right .header .label {
    line-height: 40px;
    width: 100px;
    display: inline-block;
    vertical-align: top;
    padding-left: 20px;
    font-weight: bold;
  }
  .hangye_box > .content .right .header ul li {
    float: left;
    list-style: none;
    width: 80px;
    text-align: center;
    cursor: pointer;
    font-size: 14px;
  }
  .hangye_box > .content .right .header ul li.showIndex {
    background-color: white;
    border: 2px solid rgba(0, 128, 0, 0.5);
    border-top: 3px solid rgba(0, 128, 0, 0.5);
    border-bottom-color: transparent;
    margin-top: -4px;
    color: green;
  }
  .hangye_box > .content .right .content {
    border-left: 2px solid rgba(0, 128, 0, 0.5);
    height: 320px;
  }
  .hangye_box > .content .right .content li {
    list-style: none;
    color: black;
    float: left;
    width: 50%;
    height: 30px;
    line-height: 30px;
    padding: 0 10px 0 30px;
    margin-top: 2px;
    position: relative;

    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .hangye_box > .content .right .content li .new {
    position: absolute;
    left: 5px;
    top: 5px;
    color: white;
    padding: 0 2px;
    background-color: green;
    border-radius: 3px;
    height: 20px;
    line-height: 20px;
  }

  .hangye_box > .content .right .content li:hover {
    cursor: pointer;
    color: red;
  }

  /* 图片放大区域 */
  #enlarge_images .modalShade {
    position: fixed;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    width: 100%;
    height: 100%;
    background-color: black;
    opacity: 0.3;
    z-index: 100;
  }
  #enlarge_images .modalContent {
    width: 80%;
    height: 80%;
    position: fixed;
    left: 50%;
    top: 50%;
    transform: translateX(-50%) translateY(-50%);
    z-index: 101;
  }
  #enlarge_images .modalClose {
    position: fixed;
    right: 10%;
    top: 10%;
    transform: translateX(-20px) translateY(20px);
    color: #979797;
    font-size: 30px;
    cursor: pointer;
    z-index: 102;
    transition: transform 0.2s ease-in-out;
  }
  #enlarge_images .modalClose:hover {
    transform: translateX(-20px) translateY(20px) rotateZ(180deg);
  }
</style>
