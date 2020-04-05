<template>
  <div>

    <div class="section02_bg" style="font-size: 28px;height: 80px;line-height: 80px;padding: 0 30px;">
      <a target="_blank" href="https://github.com/search?q=&type=" style="margin-left: 128px;cursor: pointer">
        <span style="color: white;">精选文章推荐，热门项目参考 github</span>
      </a>
    </div>

    <div class="isoft_bg_white" style="padding: 5px 0;box-shadow: 0 1px 2px 0 rgba(0,87,255,0.24);border-radius: 4px;">
      <Row>
        <Col span="22" offset="1">
          <!-- 6大热门分类 -->
          <HotCatalogItems @chooseItem="chooseItem"/>
        </Col>
        <!--搜索框-->
        <Col span="1">
          <Affix :offset-top="125">
            <ISearch @submitFunc="submitFunc" @searchDataHasChange="searchDataHasChange" style="position: relative;top:-65px;left: -170px;"></ISearch>
          </Affix>
        </Col>
      </Row>
    </div>

    <!--移动的小球-->
    <div style="width: 60%;height:0;">
      <MoveLine style="position: relative;top: -170px;"/>
    </div>

    <!--三列：左、中、右-->
    <div class="isoft_bg_white">
      <Row>
        <!--左侧分类-->
        <Col span="5">
          <div style="padding: 5px 10px 0 30px">
            <div class="_search">
              <div style="text-align: center;">
                <a @click="chooseItem(1)" :style="{color: pattern === 1 ? 'red':''}">全部分类</a></div>
              <div style="text-align: center;">
                <a @click="chooseItem(2)" :style="{color: pattern === 2 ? 'red':''}"><Icon type="md-flame" />热门博客</a></div>
              <div style="text-align: center;">
                <a @click="chooseItem(3)" :style="{color: pattern === 3 ? 'red':''}"><Icon type="ios-list-box-outline" />我的博客</a></div>
              <div style="text-align: center;">
                <a @click="blogEdit"><Icon type="ios-brush" />我也要发布</a>
              </div>
            </div>
            <div>
              <CatalogList :showAddBoder="false"></CatalogList>
            </div>
          </div>
        </Col>

        <!--中部博客-->
        <Col span="11">
          <div style="padding: 5px 10px 0 10px;">
            <!--中间大图-->
            <img style="width: 100%;height: 220px;" src="../../assets/xuexi.jpg"/>
            <!--遍历博客-->
            <ul>
              <li v-for="(searchblog,index) in searchblogs" style="list-style:none;background: #fff;border-bottom: 1px solid rgba(223,223,223,0.42);">
                <Row class="blogHangHover" style="height: 80px; " @click.native="clickThisBlock(index)" :style="{'margin-top': index===0 ? 5+'px':20+'px', backgroundColor:index===currentClickIndex?'rgba(128, 128, 128, 0.16)':''}">
                  <!--第一列 ：博客中第一张图片-->
                  <Col span="6">
                    <div style="padding: 0 5px 0 0">
                      <img style="width: 100%;height: 80px;" src="../../assets/xuexi.jpg"/>
                    </div>
                  </Col>
                  <!--第二列 ：分两行-->
                  <Col span="18">
                    <Row>
                      <!--第一行：博客标题-->
                      <span v-if="searchblog.blog_status === -1" style="float: right;color: red;">审核不通过！</span>
                      <router-link :to="{path:'/iblog/blogArticleDetail',query:{blog_id:searchblog.id}}">
                        <span class="title_hover">{{searchblog.blog_title | filterLimitFunc(25)}}</span>
                      </router-link>
                      <Tag v-if="searchblog.to_top > 0" color="rgba(254,211,145,0.59)" style="width: 40px;height: 20px;"><span style="font-size: 11px;color: grey">置顶</span></Tag>
                    </Row>
                    <Row>
                      <!--第二行：博客分类 + 作者 + 发布 + 更新时间 -->
                      <a class="type_hover" @click="chooseItem(searchblog.catalog_name)">{{searchblog.catalog_name | filterLimitFunc(5)}}</a>
                      <router-link :to="{path:'/user/userDetail',query:{username:searchblog.author}}">
                        <span style="color: #797776;border-bottom: 1px solid #797776;font-size: 12px">
                          <span v-if="renderNickName(searchblog.author)">{{renderNickName(searchblog.author) | filterLimitFunc(5)}}</span>
                          <span v-else>{{searchblog.author}}</span>
                        </span>
                      </router-link>

                      <!--如果是往年发布、更新的，只显示日期即可-->
                      <span v-if="isThisYear(searchblog.created_time)">
                        <span style="color: #adaaa8;font-size: 12px"> • 发布于:<Time :time="searchblog.created_time" :interval="1"/></span>
                      </span>
                      <span v-else>
                        <span style="color: #adaaa8;font-size: 12px"> • 发布于:<Time :time="searchblog.created_time" type="date" :interval="1"/></span>
                      </span>
                      <span v-if="isThisYear(searchblog.last_updated_time)">
                        <span style="color: #9b9896;font-size: 12px">, 更新于:<Time :time="searchblog.last_updated_time" :interval="1"/></span>
                      </span>
                      <span v-else>
                        <span style="color: #9b9896;font-size: 12px">, 更新于:<Time :time="searchblog.last_updated_time" type="date" :interval="1"/></span>
                      </span>
                    </Row>
                    <!--第三行:阅读次数、评论次数-->
                    <Row>
                      <span style="font-size: 12px">
                        <span style="color: #adaaa8;">{{searchblog.views}}</span>
                        <span style="color: #adaaa8">次阅读</span>
                      </span>
                      <span style="font-size: 12px">
                        <span style="color: #adaaa8;margin-left: 10px;">{{searchblog.comments}}</span>
                        <span style="color: #adaaa8">条评论</span>
                      </span>
                    </Row>
                  </Col>
                  <!--博客完整信息提示卡，hover后可见-->
                  <div class="hoverSeeDetail">
                    <div style="position: absolute">
                      <!--博主头像-->
                      <Row>
                        <Col span="6" offset="1">
                          <Row style="position: absolute;top: 5px;left: 5px;">
                            <router-link :to="{path:'/user/userDetail',query:{username:searchblog.author}}">
                              <HatAndFacePicture :src="renderUserIcon(searchblog.author)" :vip_level="renderVipLevel(searchblog.author)" :hat_in_use="renderHatInUse(searchblog.author)" :src_size="30" :hat_width="26" :hat_height="10" :hat_relative_left="2" :hat_relative_top="-48" ></HatAndFacePicture>
                            </router-link>
                          </Row>
                        </Col>
                        <Col span="17" style="font-size: 10px;">
                          <Row style="position: absolute;left: 38px;">
                            <div style="width: 110px;height: 15px;">
                              {{renderNickName(searchblog.author) | filterLimitFunc(5)}}
                            </div>
                          </Row>
                          <Row style="position: absolute;left: 38px;top: 18px;">
                            <div style="width: 100px;height: 15px;">
                              <span v-if="renderGender(searchblog.author)==='male'">
                                <Icon type="md-male" />
                              </span>
                              <span v-else-if="renderGender(searchblog.author)==='female'">
                                <Icon type="md-female" />
                              </span>
                              <span v-else>&nbsp;</span>
                            </div>
                          </Row>
                        </Col>
                      </Row>
                      <!--博主签名-->
                      <Row style="position: absolute;left: 5px;top: 50px;">
                        <div style="width: 150px;height: 20px;font-size: 10px;color: grey;">
                          {{renderSignature(searchblog.author) | filterLimitFunc(13)}}
                        </div>
                      </Row>
                    </div>
                  </div>

                </Row>
              </li>
            </ul>
            <!--分页-->
            <div style="padding-bottom: 10px">
              <Page :total="total" :page-size="offset" show-total show-sizer :page-size-opts="pageSizeOpts" :styles="{'text-align': 'center','margin-top': '10px'}" @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
            </div>
          </div>
        </Col>

        <!--右侧推荐-->
        <Col span="8">
          <div style="padding: 5px 50px 0 10px">
            <HotUser></HotUser>
          </div>
        </Col>
      </Row>

      <!--加载圈圈-->
      <div>
        <Spin fix size="large" v-if="isLoading">
          <div class="isoft_loading"></div>
        </Spin>
      </div>

    </div>

    <div class="isoft_bg_white isoft_pd10" style="margin-top: 8px;">
      <HorizontalLinks :placement_name="GLOBAL.placement_want_to_find"/>
    </div>

  </div>
</template>

<script>
  import HotCatalogItems from "./HotCatalogItems"
  import ISearch from "../Common/search/ISearch"
  import {queryPageBlog} from "../../api"
  import CatalogList from "./CatalogList"
  import HotUser from "../User/HotUser"
  import HorizontalLinks from "../Elementviewers/HorizontalLinks";
  import IBeautifulLink from "../Common/link/IBeautifulLink";
  import RandomAdmt from "../Advertisement/RandomAdmt";
  import {
    CheckHasLoginConfirmDialog2,
    GetLoginUserName,
    RenderNickName,
    RenderUserIcon,
    RenderVipLevel,
    RenderHatInUse,
    RenderUserInfoByNames,
    RenderSignature,
    RenderGender
  } from "../../tools";
  import MoveLine from "../../components/Common/decorate/MoveLine";
  import HatAndFacePicture from "../Common/HatAndFacePicture/HatAndFacePicture";

  export default {
    name: "BlogList",
    components: {
      HatAndFacePicture,
      MoveLine, RandomAdmt, IBeautifulLink, HorizontalLinks, CatalogList, HotCatalogItems, HotUser,ISearch},
    data() {
      return {
        isLoading: true,
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 20,
        pageSizeOpts:[20, 30, 40, 50],
        searchblogs: [],
        search_type: '_all',
        showLabel: '全部分类',
        search_user_name: '',
        userInfos: [],
        pattern: 1,           // 按钮选中的模式
        search_data:'',
        currentClickIndex:'',
      }
    },
    methods: {
      blogEdit: function () {
        var _this = this;
        CheckHasLoginConfirmDialog2(this, function () {
          _this.$router.push({path: '/iblog/blogArticleEdit'});
        });
      },
      chooseItem: function (pattern) {
        // 置空搜索条件
        this.search_user_name = '';
        this.search_type = '';
        // 设置搜索的模式
        this.pattern = pattern;
        this.current_page = 1;

        if (pattern === 1) {
          this.search_type = "_all";
          this.showLabel = "全部分类";
          this.refreshBlogList();
        } else if (pattern === 2) {
          this.search_type = "_hot";
          this.showLabel = "热门博客";
          this.refreshBlogList();
        } else if (pattern === 3) {
          var _this = this;
          CheckHasLoginConfirmDialog2(this, function () {
            _this.showLabel = "我的博客";
            _this.search_user_name = GetLoginUserName();
            _this.refreshBlogList();
          });
        } else {
          this.showLabel = pattern;
          this.search_type = pattern;
          this.refreshBlogList();
        }
      },
      searchDataHasChange:function(search_data){
        this.search_data = search_data;
      },
      submitFunc:async function (search_data) {
        this.search_data = search_data;
        if (search_data.length === 0) {
          return false;
        }
        this.refreshBlogList();
      },
      handleChange(page) {
        this.current_page = page;
        this.refreshBlogList();
      },
      handlePageSizeChange(pageSize) {
        this.offset = pageSize;
        this.refreshBlogList();
      },
      refreshBlogList: async function () {
        this.isLoading = true;
        try {
          var search_type = this.search_type;
          const result = await queryPageBlog({
            offset: this.offset,
            current_page: this.current_page,
            search_type: search_type,
            search_user_name: this.search_user_name,
            search_data:this.search_data,
          });
          if (result.status === "SUCCESS") {

            this.userInfos = await RenderUserInfoByNames(result.blogs, 'author');
            this.searchblogs = result.blogs;
            this.total = result.paginator.totalcount;
          }
        } finally {
          this.isLoading = false;
        }
      },
      renderNickName: function (user_name) {
        return RenderNickName(this.userInfos, user_name);
      },
      renderUserIcon: function (user_name) {
        return RenderUserIcon(this.userInfos, user_name);
      },
      renderVipLevel: function (user_name) {
        return RenderVipLevel(this.userInfos, user_name);
      },
      renderHatInUse: function (user_name) {
        return RenderHatInUse(this.userInfos, user_name);
      },
      renderSignature:function(user_name){
        return RenderSignature(this.userInfos,user_name);
      },
      renderGender:function(user_name){
        return RenderGender(this.userInfos,user_name);
      },
      //点击博客，变灰色背景
      clickThisBlock:function(index){
        this.currentClickIndex = index;
      },
      // 判断博客是不是今年发布、更新的
      isThisYear:function (value) {
        let thisYear = new Date().getFullYear();
        return parseInt(value.substring(0,4)) === parseInt(thisYear);
      }
    },
    mounted: function () {
      this.refreshBlogList();
    },
    filters: {
      // 内容超长则显示部分
      filterLimitFunc:function (value,limitLenth) {
        if (value.length > limitLenth) {
          return value.slice(0,limitLenth) + ' · · ·'
        }else {
          return value
        }
      }
    },
  }
</script>

<style scoped>
  .section02_bg {
    height: 140px;
    background: url(../../assets/bg2.jpg) no-repeat;
    background-size: 100%;
  }
  .type_hover{
    font-size: 12px;color: #777;
  }
  .type_hover:hover{
    font-size: 12px;color: rgba(119, 119, 119, 0.62);
  }

  .title_hover {
    font-size: 15px;
    color: #555;
    font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  }
  .title_hover:hover {
    font-size: 15px;
    color: red;
    font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  }

  a {
    color: #3d3d3d;
  }

  a:hover {
    color: red;
  }

  ._search a {
    color: #155faa;
  }

  ._search a:hover {
    color: #6cb0ca;
  }

  .blogHangHover:hover{
    background-color: rgba(128, 128, 128, 0.16);
  }

  .hoverSeeDetail {
    width: 170px;
    height: 80px;
    border-radius: 10px;
    background-color: rgba(58, 103, 29, 0.19);
    position: relative;
    left:-170px;
    top: 0px;
    display: none;
    animation: moveToRight 0.2s infinite;
    animation-iteration-count: 1;
  }
  .blogHangHover:hover > .hoverSeeDetail {
    display: block;
  }
  @keyframes moveToRight {
    0%   { left: -200px;}
    100% { left: -170px;}
  }

</style>
