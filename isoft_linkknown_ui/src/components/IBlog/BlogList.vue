<template>
  <div>
    <div class="isoft_bg_white"
         style="padding: 10px 0;box-shadow: 0px 1px 2px 0px rgba(0,87,255,0.24);border-radius: 4px;">
      <!-- 热门分类 -->
      <HotCatalogItems @chooseItem="chooseItem"/>
    </div>

    <div class="isoft_top5">
      <Row>
        <Col span="17" style="padding: 0 8px 0 0;">
          <div class="isoft_bg_white isoft_pd10">
            <Row class="_search" style="border-bottom: 1px solid #e6e6e6;padding: 20px;height: 62px;">
              <Col span="5" style="font-size: 20px;color: #333;">
                <!-- 占据内容 -->
                <span style="width: 1px;height: 1px;display: inline-block;"></span>
                <span>{{showLabel}}</span>
              </Col>
              <Col span="3" offset="4" style="text-align: center;">
                <a @click="chooseItem(1)" :style="{color: pattern === 1 ? 'red':''}">全部分类</a></Col>
              <Col span="3" style="text-align: center;">
                <a @click="chooseItem(2)" :style="{color: pattern === 2 ? 'red':''}">热门博客</a></Col>
              <Col span="3" style="text-align: center;">
                <a @click="chooseItem(3)" :style="{color: pattern === 3 ? 'red':''}">我的博客</a></Col>
              <Col span="3" style="text-align: center;">
                <a @click="blog_edit">我也要发布</a>
              </Col>
              <Col span="3" style="text-align: center;">
                <a @click="$router.push({path:'/expert/ask_expert'})"><span style="color: red;">(荐)</span>求问专家</a>
              </Col>
              <MoveLine/>
            </Row>

            <!--下面展示一篇博客具体格式，按照三列，中间一列分两行-->
            <ul>
              <li v-for="searchblog in searchblogs" style="list-style:none;padding: 10px 10px;background: #fff;border-bottom: 1px solid #f4f4f4;">
                <Row style="margin-top: 15px">
                  <Col span="2">
                    <!--第一列 ：头像-->
                    <router-link to="" style="float: left;">
                      <Avatar size="large" v-if="renderUserIcon(searchblog.author)" :src="renderUserIcon(searchblog.author)" />
                      <Avatar size="large" v-else src="../../../static/images/404.jpg"/>
                    </router-link>
                  </Col>
                  <Col span="15" style="position: relative;left: -12px;top: -3px">
                    <!--第二列 ：分两行-->
                    <Row>
                      <!--第一行：所属分类 + 博客标题-->
                      <a class="type_hover" @click="chooseItem(searchblog.catalog_name)">{{searchblog.catalog_name }}</a>
                      <span v-if="searchblog.blog_status == -1" style="float: right;color: red;">审核不通过！</span>
                      <span>&nbsp;</span>
                      <router-link :to="{path:'/iblog/blog_detail',query:{blog_id:searchblog.id}}">
                        <span class="title_hover">{{searchblog.blog_title}}</span>
                      </router-link>
                      <Tag v-if="searchblog.to_top > 0" color="rgba(254,211,145,0.59)" style="width: 40px;height: 20px;"><span style="font-size: 11px;color: grey">置顶</span></Tag>
                    </Row>
                    <Row>
                      <!--第二行：作者 + 发布 + 更新时间 -->
                      <router-link to="">
                        <span style="color: #797776;">
                          <span v-if="renderNickName(searchblog.author)">{{renderNickName(searchblog.author)}}</span>
                          <span v-else>{{searchblog.author}}</span>
                        </span>
                      </router-link>
                      <span style="color: #adaaa8"> • 发布于:<Time :time="searchblog.created_time"/></span>
                      <span style="color: #9b9896">, 更新于:<Time :time="searchblog.last_updated_time"/></span>
                    </Row>
                  </Col>
                  <Col span="7">
                    <!--第三列：-->
                    <router-link :to="{path:'/iblog/blog_detail',query:{blog_id:searchblog.id}}">
                      <span class="isoft_font12"><span style="color: rgba(255,0,0,0.65);margin-left: 20px">{{searchblog.views}}</span> 次阅读</span>
                    </router-link>
                    <router-link :to="{path:'/iblog/blog_detail',query:{blog_id:searchblog.id}}">
                      <span class="isoft_font12"><span style="color: rgba(255,0,0,0.65);margin-left: 10px">0</span> 条评论</span>
                    </router-link>
                    <a @click="blog_edit" class="isoft_font12" style="margin-left: 10px">我也要发布</a>
                  </Col>
                </Row>
              </li>
            </ul>

            <Page :total="total" :page-size="offset" show-total show-sizer
                  :styles="{'text-align': 'center','margin-top': '10px'}"
                  @on-change="handleChange" @on-page-size-change="handlePageSizeChange"/>
          </div>
        </Col>
        <Col span="7">
          <div class="isoft_bg_white isoft_pd10">
            <HotUser/>
            <CatalogList/>
          </div>

          <div class="isoft_bg_white isoft_pd10 isoft_top10">
            <RandomAdmt/>
            <RandomAdmt/>
          </div>
        </Col>
      </Row>
    </div>

    <div class="isoft_bg_white isoft_pd10" style="margin-top: 8px;">
      <HorizontalLinks :placement_name="GLOBAL.placement_want_to_find"/>
    </div>

  </div>
</template>

<script>
  import HotCatalogItems from "./HotCatalogItems"
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

  export default {
    name: "BlogList",
    components: {MoveLine, RandomAdmt, IBeautifulLink, HorizontalLinks, CatalogList, HotCatalogItems, HotUser},
    data() {
      return {
        // 当前页
        current_page: 1,
        // 总数
        total: 0,
        // 每页记录数
        offset: 20,
        searchblogs: [],
        search_type: '_all',
        showLabel: '全部分类',
        search_user_name: '',
        userInfos: [],
        pattern: 1,           // 按钮选中的模式
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
      handleChange(page) {
        this.current_page = page;
        this.refreshBlogList();
      },
      handlePageSizeChange(pageSize) {
        this.offset = pageSize;
        this.refreshBlogList();
      },
      refreshBlogList: async function () {
        var search_type = this.search_type;
        const result = await queryPageBlog({
          offset: this.offset,
          current_page: this.current_page,
          search_type: search_type,
          search_user_name: this.search_user_name,
        });
        if (result.status === "SUCCESS") {
          this.userInfos = await RenderUserInfoByNames(result.blogs, 'author');
          this.searchblogs = result.blogs;
          this.total = result.paginator.totalcount;
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
      filterLimitFunc: function (value) {
        if (value && value.length > 100) {
          value = value.substring(0, 100) + '...';
        }
        return value;
      },
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
