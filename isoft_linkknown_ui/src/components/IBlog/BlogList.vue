<template>
  <div>
    <div class="isoft_bg_white" style="padding: 5px 0;box-shadow: 0px 1px 2px 0px rgba(0,87,255,0.24);border-radius: 4px;">
      <!-- 热门分类 -->
      <HotCatalogItems @chooseItem="chooseItem"/>
    </div>

    <div class="isoft_top5">
      <div class="isoft_bg_white">

        <!--全部分类、热门博客、我的博客、我也要发布-->
        <Row class="_search" style="padding: 12px;height: 50px; position: relative;left: -20px">
          <Col span="3" offset="3" style="font-size: 20px;color: #333;">
            <span >{{showLabel}}</span>
          </Col>
          <Col span="2"  style="text-align: center;">
            <a @click="chooseItem(1)" :style="{color: pattern === 1 ? 'red':''}">全部分类</a></Col>
          <Col span="2" style="text-align: center;">
            <a @click="chooseItem(2)" :style="{color: pattern === 2 ? 'red':''}"><Icon type="md-flame" />热门博客</a></Col>
          <Col span="2" style="text-align: center;">
            <a @click="chooseItem(3)" :style="{color: pattern === 3 ? 'red':''}"><Icon type="ios-list-box-outline" />我的博客</a></Col>
          <Col span="2" style="text-align: center;">
            <a @click="blog_edit"><Icon type="ios-brush" />我也要发布</a>
          </Col>
          <Col span="5" offset="2">
            <Affix :offset-top="68">
              <ISearch @submitFunc="submitFunc" @searchDataHasChange="searchDataHasChange" style="position: relative;top: -8px;"></ISearch>
            </Affix>
          </Col>
        </Row>

        <!--移动的小球-->
        <div style="width: 60%;height:1px;border-bottom: 1px solid #e6e6e6;">
          <MoveLine style="position: relative;top: -25px;"/>
        </div>

        <!--加载圈圈-->
        <div>
          <Spin fix size="large" v-if="isLoading">
            <div class="isoft_loading"></div>
          </Spin>
        </div>

        <!--下面是三列-->
        <Row style="padding: 12px;">
          <Col span="2">
            <!--博客左侧预留空间-->
            &nbsp;
          </Col>
          <Col span="16">
            <!--下面展示一篇博客具体格式，按照三列，中间一列分两行-->
            <ul>
              <li v-for="searchblog in searchblogs" style="list-style:none;padding: 10px 10px;background: #fff;border-bottom: 1px solid #f4f4f4;">
                <Row style="margin-top: 10px">
                  <Col span="2" offset="1">
                    <!--第一列 ：头像-->
                    <router-link :to="{path:'/user/detail',query:{username:searchblog.author}}" style="float: left;">
                      <Avatar size="large" v-if="renderUserIcon(searchblog.author)" :src="renderUserIcon(searchblog.author)" style="border: 1px solid grey;" />
                      <Avatar size="large" v-else src="../../../static/images/404.jpg" style="border: 1px solid grey;"/>
                    </router-link>
                  </Col>
                  <Col span="16" style="position: relative;top: -3px;left: -18px">
                    <!--第二列 ：分两行-->
                    <Row>
                      <!--第一行：所属分类 + 博客标题-->
                      <a class="type_hover" @click="chooseItem(searchblog.catalog_name)">{{searchblog.catalog_name}}</a>
                      <span v-if="searchblog.blog_status === -1" style="float: right;color: red;">审核不通过！</span>
                      <span>&nbsp;</span>
                      <router-link :to="{path:'/iblog/blog_detail',query:{blog_id:searchblog.id}}">
                        <span class="title_hover">{{searchblog.blog_title | filterLimitFunc(27)}}</span>
                      </router-link>
                      <Tag v-if="searchblog.to_top > 0" color="rgba(254,211,145,0.59)" style="width: 40px;height: 20px;"><span style="font-size: 11px;color: grey">置顶</span></Tag>
                    </Row>
                    <Row>
                      <!--第二行：作者 + 发布 + 更新时间 -->
                      <router-link :to="{path:'/user/detail',query:{username:searchblog.author}}">
                    <span style="color: #797776;border-bottom: 1px solid #797776;">
                      <span v-if="renderNickName(searchblog.author)">{{renderNickName(searchblog.author)}}</span>
                      <span v-else>{{searchblog.author}}</span>
                    </span>
                      </router-link>
                      <span style="color: #adaaa8"> • 发布于:<Time :time="searchblog.created_time" :interval="1"/></span>
                      <span style="color: #9b9896">, 更新于:<Time :time="searchblog.last_updated_time" :interval="1"/></span>
                    </Row>
                  </Col>
                  <Col span="5">
                    <!--第三列：-->
                    <router-link :to="{path:'/iblog/blog_detail',query:{blog_id:searchblog.id}}">
                      <span class="isoft_font12"><span style="color: rgba(255,0,0,0.65);margin-left: 20px">{{searchblog.views}}</span> 次阅读</span>
                    </router-link>
                    <router-link :to="{path:'/iblog/blog_detail',query:{blog_id:searchblog.id}}">
                      <span class="isoft_font12"><span style="color: rgba(255,0,0,0.65);margin-left: 10px">{{searchblog.comments}}</span> 条评论</span>
                    </router-link>
                  </Col>
                </Row>
              </li>
            </ul>
            &nbsp;
          </Col>
          <Col span="6">
            <!--博客右侧-->
            <HotUser></HotUser>
            <CatalogList></CatalogList>
          </Col>
        </Row>

        <!--分页-->
        <div style="padding-bottom: 10px">
          <Page :total="total" :page-size="offset" show-total show-sizer :page-size-opts="pageSizeOpts" :styles="{'text-align': 'center','margin-top': '10px'}" @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
        </div>

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
    RenderUserInfoByNames
  } from "../../tools";
  import MoveLine from "../../components/Common/decorate/MoveLine";
  import ISimpleSearch from "../../../../isoft_iwork_ui/src/components/Common/search/ISimpleSearch";

  export default {
    name: "BlogList",
    components: {MoveLine, RandomAdmt, IBeautifulLink, HorizontalLinks, CatalogList, HotCatalogItems, HotUser,ISearch},
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
      }
    },
    methods: {
      blog_edit: function () {
        var _this = this;
        CheckHasLoginConfirmDialog2(this, function () {
          _this.$router.push({path: '/iblog/blog_edit'});
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

  .type_hover{
    font-size: 15px;color: #777;
  }
  .type_hover:hover{
    font-size: 15px;color: rgba(119, 119, 119, 0.62);
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
</style>
